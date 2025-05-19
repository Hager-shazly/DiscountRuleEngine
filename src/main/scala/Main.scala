import java.sql.Connection
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.io.Source
import LoggerUtil.logger
import DBLogger._

object Main extends App {

  //Logs startup for tracking execution.
  logger.info("=== Starting Discount Calculation Process ===")

  val formatter = DateTimeFormatter.ISO_DATE_TIME

  //Reads CSV file skipping the header row and establishes DB connection for storing results.
  val data = Source.fromFile("TRX1000.csv").getLines().drop(1).toList
  val conn: Connection = DBUtil.getConnection()

  //Splits CSV rows into order objects
  val orders = data.map { line => val cols = line.split(",").map(_.trim)
    try {
      val timestamp     = LocalDate.parse(cols(0).take(10))
      val productName   = cols(1)
      val expiryDate    = LocalDate.parse(cols(2))
      val quantity      = cols(3).toInt
      val price         = cols(4).toDouble
      val paymentMethod = cols(5)
      val salesChannel  = cols(6)

  // Logs parsing status (success/failure).
      logger.info(s"Parsed order for product: $productName, Qty: $quantity, Channel: $salesChannel, Payment: $paymentMethod")

      Order(Product(productName, expiryDate), quantity, timestamp, price, paymentMethod, salesChannel)
    } catch {
      case e: Exception =>
        logger.warning(s"Failed to parse line: $line. Reason: ${e.getMessage}")
        throw e
    }
  }

  // Updated header to match new column order
  println(
    "%-25s %-10s %-10s %-10s %-10s %-10s %-10s".format(
      "Product", "Discount%", "FinalPrice", "Reason1", "Reason2", "Channel", "Payment"
    )
  )
  println("=" * 85)
  // Applies discount rules and calculates discounted price
  orders.foreach { order =>
    val (discount, reasons) = DiscountEngine.calculateDiscount(order)
    val totalPrice = order.quantity * order.pricePerUnit
    val finalPrice = totalPrice * (1 - discount / 100)

    val r1 = reasons.headOption.map(_.reason).getOrElse("-")  // Primary discount reason
    val r2 = reasons.drop(1).headOption.map(_.reason).getOrElse("-")  // Secondary reason (if any)

    //DB record
    val finalOrder = FinalOrder(
      order.product.name,
      order.salesChannel,
      order.paymentMethod,
      discount,
      finalPrice,
      r1,
      r2
    )

    insertFinalOrder(conn, finalOrder)

    // Prints formatted results (matches DB column order)
    println(
      f"${order.product.name.take(25)}%-25s " +
        f"${discount}%-10.2f " +
        f"${finalPrice}%-10.2f " +
        f"$r1%-10s " +
        f"$r2%-10s " +
        f"${order.salesChannel.take(6)}%-10s " +
        f"${order.paymentMethod.take(6)}%-10s"
    )

    logger.info(
      s"Calculated discount $discount%% for ${order.product.name}, Final price: $finalPrice, Reasons: [$r1, $r2]"
    )
  }

  conn.close()
  logger.info("=== Finished Discount Calculation Process ===")
}