package finance

class CashRegister internal constructor(private var bank: Bank) : CashRegisterInterface
{
  private var exchangeRate: Double = bank.exchangeRate
  private var logger: Logger = Logger("logs/CashRegister_log.txt")

  override fun exchangeRubToUsd(rubAmount: Double): Double {
    val usdAmount = bank.sendUsd(rubAmount * bank.exchangeRate)
    bank.receiveRub(rubAmount)
    exchangeRate = bank.exchangeRate
    logger.log("Bank '${bank.name}': Exchanged $rubAmount of roubles for $usdAmount of baksov.")
    return usdAmount
  }

  override fun exchangeUsdToRub(usdAmount: Double): Double{
    val rubAmount = bank.sendRub(usdAmount * (1 / bank.exchangeRate))
    bank.receiveUsd(usdAmount)
    logger.log("Bank '${bank.name}': Exchanged $usdAmount of baksov for $rubAmount of roubles.")
    exchangeRate = bank.exchangeRate
    return rubAmount
  }

  override fun checkExchangeRate(): Double {
    logger.log("Bank '${bank.name}': Checked ExchangeRate. Currently: $exchangeRate.")
    return exchangeRate
  }
}