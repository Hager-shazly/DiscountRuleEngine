import java.time.temporal.ChronoUnit


 //Calculates how many days are left until the product expires
 // If between 1-29 days left, gives a discount equal to (30 - days left)

object ExpiryRule extends DiscountRule {
  override def qualify(order: Order): Option[Discount] = {
    val daysLeft = ChronoUnit.DAYS.between(order.timestamp, order.product.expiryDate)
    if (daysLeft >= 1 && daysLeft <= 29)
      Some(Discount(s"Expiry in $daysLeft days", (30 - daysLeft).toDouble))
    else None
  }
}

//Special discounts for cheese and wine products.
//10% off for any product with cheese in its name and 5% off for any product with wine in its name

object CheeseWineRule extends DiscountRule {
  override def qualify(order: Order): Option[Discount] = {
    val name = order.product.name.toLowerCase
    if (name.contains("cheese")) Some(Discount("Cheese sale", 10.0))
    else if (name.contains("wine")) Some(Discount("Wine sale", 5.0))
    else None
  }
}
//Big one-day-only annual sale.
//Gives 50% off on all orders placed on March 23rd
//Only checks the date, applies to everything that day

object March23Rule extends DiscountRule {
  override def qualify(order: Order): Option[Discount] = {
    if (order.timestamp.getMonthValue == 3 && order.timestamp.getDayOfMonth == 23)
      Some(Discount("March 23 special", 50.0))
    else None
  }
}

//Rewards customers who buy larger quantities (only one discount applies)
//6-9 items → 5% discount  10-14 items → 7% discount  15+ items → 10% discount

object BulkRule extends DiscountRule {
  override def qualify(order: Order): Option[Discount] = {
    order.quantity match {
      case q if q >= 6 && q <= 9  => Some(Discount("Bulk 6-9", 5.0))
      case q if q >= 10 && q <= 14 => Some(Discount("Bulk 10-14", 7.0))
      case q if q >= 15            => Some(Discount("Bulk >15", 10.0))
      case _                       => None
    }
  }
}

//Encourages app usage with quantity based discounts.
//Only applies to orders placed through the mobile app
//Discount increases in 5% increments for every 5 items

object AppUsageRule extends DiscountRule {
  override def qualify(order: Order): Option[Discount] = {
    if (order.salesChannel.equalsIgnoreCase("App")) {
      val roundedQuantity = ((order.quantity + 4) / 5) * 5
      val discountPercent = (roundedQuantity / 5) * 5
      Some(Discount("App usage discount", discountPercent.toDouble))
    } else {
      None
    }
  }
}


//Incentive for using Visa payment.
//Simple 5% discount when customer pays with Visa

object VisaCardRule extends DiscountRule {
  override def qualify(order: Order): Option[Discount] = {
    if (order.paymentMethod.equalsIgnoreCase("Visa")) {
      Some(Discount("Visa card discount", 5.0))
    } else {
      None
    }
  }
}