package com.rfq

class LiveOrderServiceBuilder {

  private var orders: List[Order] = List()

  def withOrder(direction: Direction.Value, price: Price, currency: Currency.Value, amount: Amount): LiveOrderServiceBuilder = {
    orders = new Order(direction, price, currency, amount) :: orders

    this
  }

  def build() = new LiveOrderService { override def request(currency: Currency.Value): List[Order] = orders }


}

object LiveOrderServiceBuilder {

  def aLiveOrderService = new LiveOrderServiceBuilder
}
