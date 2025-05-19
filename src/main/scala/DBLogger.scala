//This DBLogger object is responsible for saving finalized order details
// into a database table called final_orders.

import java.sql.Connection

object DBLogger {
  def insertFinalOrder(conn: Connection, order: FinalOrder): Unit = {
    val sql =
      """INSERT INTO final_orders
        |(product, discount, final_price, reason1, reason2, channel, payment)
        |VALUES (?, ?, ?, ?, ?, ?, ?)""".stripMargin


    val stmt = conn.prepareStatement(sql)
    stmt.setString(1, order.product)
    stmt.setDouble(2, order.discount)
    stmt.setDouble(3, order.finalPrice)
    stmt.setString(4, order.reason1)
    stmt.setString(5, order.reason2)
    stmt.setString(6, order.channel)
    stmt.setString(7, order.payment)

    stmt.executeUpdate()
    stmt.close()
  }
}
