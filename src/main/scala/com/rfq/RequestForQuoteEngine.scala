package com.rfq

import com.rfq.Quote.NO_QUOTE
import com.rfq.RequestForQuoteEngine._

import scala.collection.immutable.List.empty

class RequestForQuoteEngine(val liveOrderService: LiveOrderService) {

  def request(amount: Amount, currency: Currency.Value): Quote = {

    val orders = liveOrderService.request(currency)

    if (orders == empty)
      NO_QUOTE
    else {
      val prices = orders map (_.price)

      val maxPrice = (Price(0.0) /: prices) ((a,b) => if (a > b) a else b)

      val minPrice = minimum(prices)

      Quote(maxPrice - Price(ProfitValue), minPrice + Price(ProfitValue))
    }

  }

  private def minimum(prices: List[Price]) :Price  = {

    def go(currentLowest: Price, prices: List[Price]) :Price = {
      if (prices.isEmpty) return currentLowest

      val lowest = {
        if (currentLowest < prices.head)
          currentLowest
        else
          prices.head
      }

      go(lowest, prices.tail)
    }

    go(prices.head, prices.tail)

  }

}

private object RequestForQuoteEngine {

  val ProfitValue: Double = 0.02
}
