package com.rfq

trait LiveOrderService {

  def request(currency: Currency.Value): List[Order]

}
