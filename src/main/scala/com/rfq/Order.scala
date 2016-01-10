package com.rfq


class Order(val direction: Direction.Value, val price: Price, val currency: Currency.Value, val amount: Amount) {


  override def toString = s"Order($direction, $price, $currency, $amount)"
}
