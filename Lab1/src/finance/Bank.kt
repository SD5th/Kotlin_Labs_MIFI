package finance

import kotlin.random.Random

class Bank(
  internal val name: String = "unknown",
  private var rubBalance: Double = 0.0,
  private var usdBalance: Double = 0.0,
  internal var exchangeRate: Double = 1.0
) : BankInterface {
  private var logger: Logger = Logger("logs/Bank_log.txt")
  init {
    logger.log("Bank '$name' is created.")
  }

  override fun createCashRegister(): CashRegister {
    logger.log("Bank '$name': Created CashRegister.")
    return CashRegister(bank = this)
  }

  internal fun sendRub(amount: Double): Double {
    var toSend = amount
    if (rubBalance < toSend)
      toSend = rubBalance
    rubBalance -= toSend
    logger.log("Bank '$name': Send $toSend roubles.")
    recalculateExchangeRate()
    return toSend
  }

  internal fun sendUsd(amount: Double): Double {
    var toSend = amount
    if (usdBalance < toSend)
      toSend = usdBalance
    usdBalance -= toSend
    logger.log("Bank '$name': Send $toSend baksov.")
    recalculateExchangeRate()
    return toSend
  }

  internal fun receiveRub(amount: Double) {
    rubBalance += amount
    logger.log("Bank '$name': Received $amount rubs.")
    recalculateExchangeRate()
  }

  internal fun receiveUsd(amount: Double) {
    usdBalance += amount
    logger.log("Bank '$name': Received $amount baksov.")
    recalculateExchangeRate()
  }

  private fun recalculateExchangeRate() {
    exchangeRate *= Random.nextDouble(0.5, 2.0)
    logger.log("Bank '$name': Recalculated ExchangeRate. Currently: $exchangeRate.")
  }
}
