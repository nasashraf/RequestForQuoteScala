package com.rfq

class Price(price: Double) {
  def +(otherPrice: Price) = Price(value + otherPrice.value)

  def -(otherPrice: Price) = Price(value - otherPrice.value)

  def >(otherPrice: Price): Boolean =  if (value > otherPrice.value) true else false

  def <(otherPrice: Price) = ! >(otherPrice)


  val value: Double = price;

  def canEqual(other: Any): Boolean = other.isInstanceOf[Price]

  override def equals(other: Any): Boolean = other match {
    case that: Price =>
      (that canEqual this) &&
        value == that.value
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(value)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"Price($value)"
}

object Price {

  val NO_PRICE = new Price(-1.0) {
    override def toString = "NO_PRICE"
  }

  def apply(value: Double): Price = new Price(value)
}
