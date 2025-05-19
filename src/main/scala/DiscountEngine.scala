// A list of all the discount rules we want to apply to each order.
// This function takes an order and returns:
// the average discount percentage calculated from the top 2 discounts
// a list of up to 2 best discounts applied to the order

object DiscountEngine {

  val rules: List[DiscountRule] = List(ExpiryRule, CheeseWineRule, March23Rule, BulkRule, AppUsageRule, VisaCardRule)

  def calculateDiscount(order: Order): (Double, List[Discount]) = {
    val discounts = rules.flatMap(_.qualify(order))
    val top2 = discounts.sortBy(-_.percent).take(2)
    val avg = if (top2.nonEmpty) top2.map(_.percent).sum / top2.size else 0.0
    (avg, top2)
  }
}
