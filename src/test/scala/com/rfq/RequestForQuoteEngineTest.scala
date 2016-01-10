package com.rfq

import com.rfq.Currency._
import com.rfq.Direction._
import com.rfq.LiveOrderServiceBuilder.aLiveOrderService
import com.rfq.Quote.NO_QUOTE
import com.rfq.RequestForQuoteEngineTest.ProfitValue
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class RequestForQuoteEngineTest extends FlatSpec with Matchers with BeforeAndAfter with LiveOrderService  {

  private var liveOrderStub: LiveOrderService = aLiveOrderService.build()
  private val requestForQuoteEngine = new RequestForQuoteEngine(this)

  before {
  }

  "request for quote when no client orders registered" should "return no quote" in {
    val result = requestForQuoteEngine.request(Amount(100), Usd)

    result shouldBe NO_QUOTE
  }

  "when registered client buy orders price is twice the profit then quote" should "have a buy price matching the profit value" in {
    liveOrderStub = aLiveOrderService
                    .withOrder(Buy, Price(0.04), Usd, Amount(100))
                    .withOrder(Sell, Price(0.04), Usd, Amount(100))
                    .build

    val result = requestForQuoteEngine.request(new Amount(100), Usd)

    result.bidPrice shouldBe Price(0.02)
  }

  "when registered client sell orders price is zero then quote" should "have a sell price matching the profit value" in {
    liveOrderStub = aLiveOrderService
      .withOrder(Sell, Price(0.0), Usd, Amount(100))
      .withOrder(Buy, Price(0.0), Usd, Amount(100))
      .build

    val result = requestForQuoteEngine.request(new Amount(100), Usd)

    result.askPrice shouldBe Price(0.02)
  }


  "when a request is made for the same amount as that of a single registered client buy order then quote" should "have a bid price of the buy price minus the profit value" in {
    liveOrderStub = aLiveOrderService
      .withOrder(Buy, Price(100.0), Usd, Amount(100))
      .withOrder(Sell, Price(100.0), Usd, Amount(100))
      .build

    val result = requestForQuoteEngine.request(new Amount(100), Usd)

    result.bidPrice shouldBe Price(100.0 - ProfitValue)
  }

  "when a request is made for the same amount as that of a single registered client sell order then quote" should "have an ask price of the sell price plus the profit value" in {
    liveOrderStub = aLiveOrderService
      .withOrder(Sell, Price(100.0), Usd, Amount(100))
      .withOrder(Buy, Price(100.0), Usd, Amount(100))
      .build

    val result = requestForQuoteEngine.request(new Amount(100), Usd)

    result.askPrice shouldBe Price(100.0 + ProfitValue)
  }

  "when a request is made for the same amount as that of multiple registered client buy orders then quote" should "have a bid price of the highest buy price minus the profit value" in {
    liveOrderStub = aLiveOrderService
      .withOrder(Buy, Price(200.0), Usd, Amount(100))
      .withOrder(Buy, Price(100.0), Usd, Amount(100))
      .withOrder(Sell, Price(100.0), Usd, Amount(100))
      .build

    val result = requestForQuoteEngine.request(new Amount(100), Usd)

    result.bidPrice shouldBe Price(200.0 - ProfitValue)
  }

  "when a request is made for the same amount as that of multiple registered client sell orders then quote" should "have a bid price of the lowest sell price plus the profit value" in {
    liveOrderStub = aLiveOrderService
      .withOrder(Sell, Price(400.0), Usd, Amount(100))
      .withOrder(Sell, Price(300.0), Usd, Amount(100))
      .withOrder(Buy, Price(200.0), Usd, Amount(100))
      .build

    val result = requestForQuoteEngine.request(new Amount(100), Usd)

    result.askPrice shouldBe Price(200.0 + ProfitValue)
  }

  override def request(currency: Currency.Value): List[Order] = liveOrderStub.request(currency)

}

object RequestForQuoteEngineTest {

  val ProfitValue: Double = 0.02
}
