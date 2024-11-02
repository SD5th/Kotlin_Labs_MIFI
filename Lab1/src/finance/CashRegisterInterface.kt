package finance

interface CashRegisterInterface {
  fun exchangeRubToUsd(rubAmount: Double): Double
  fun exchangeUsdToRub(usdAmount: Double): Double
  fun checkExchangeRate(): Double
}