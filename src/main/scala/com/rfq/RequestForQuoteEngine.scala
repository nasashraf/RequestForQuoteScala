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

      val maxPrice = searchPrices(prices, (a, b) => a > b)
      val minPrice = searchPrices(prices, (a, b) => a < b)

      Quote(maxPrice - Price(ProfitValue), minPrice + Price(ProfitValue))
    }

  }

  private def searchPrices(prices: List[Price], comparePrices:(Price,Price) => Boolean): Price = {
    prices reduceLeft ((a,b) => if (comparePrices(a,b)) a else b)
  }

  //Just keeping this method as a reminder of how I could've done this recursively
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
