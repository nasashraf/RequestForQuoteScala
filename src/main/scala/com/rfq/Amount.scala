package com.rfq

class Amount(amount: Int) {

  val value: Int = amount

  override def toString = s"Amount($value)"

  def canEqual(other: Any): Boolean = other.isInstanceOf[Amount]

  override def equals(other: Any): Boolean = other match {
    case that: Amount =>
      (that canEqual this) &&
        value == that.value
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(value)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

object Amount {

  def apply(value: Int): Amount = new Amount(value)
}
