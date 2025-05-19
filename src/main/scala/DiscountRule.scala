// A trait as abase like an interface for the discount rules.
// Each rule checks if an order qualifies for a discount.
// If it does, return Some(Discount); if not, return None.


trait DiscountRule {
  def qualify(order: Order): Option[Discount]
}



