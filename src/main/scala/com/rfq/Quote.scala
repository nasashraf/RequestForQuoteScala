package com.rfq

import com.rfq.Price.NO_PRICE

class Quote(val bidPrice: Price, val askPrice: Price) {

}

object Quote {

  val NO_QUOTE = new Quote(NO_PRICE, NO_PRICE)

  def apply(bidPrice: Price, askPrice: Price) = new Quote(bidPrice, askPrice)

}