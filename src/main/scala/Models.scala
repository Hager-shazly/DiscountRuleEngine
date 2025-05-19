import java.time.LocalDate

// Represents a product being sold
case class Product(name: String, expiryDate: LocalDate)

// Represents a customer order
case class Order(product: Product, quantity: Int, timestamp: LocalDate, pricePerUnit: Double, paymentMethod: String = "Cash", salesChannel: String = "Store")

// Represents a discount applied to an order
case class Discount(reason: String, percent: Double)

// Represents the result after applying discounts to an order
case class FinalOrder(product: String, channel: String, payment: String, discount: Double, finalPrice: Double, reason1: String, reason2: String)
