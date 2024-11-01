import kotlin.random.Random
import java.io.File
import java.io.IOException

class Logger (filePath: String = "logs/Log.txt") {
  private var logFile: File = File(filePath)
  init {
    if (!logFile.exists()) {
      logFile.createNewFile()
    }
  }
  fun log (message: String) {
    try {
      logFile.appendText("$message\n")
    } catch (e: IOException) {
      println("Error with writing in log: ${e.message}")
    }
  }
}

interface BankInterface {
  fun createCashRegister(): CashRegister
}

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

interface CashRegisterInterface {
  fun exchangeRubToUsd(rubAmount: Double): Double
  fun exchangeUsdToRub(usdAmount: Double): Double
  fun checkExchangeRate(): Double
}

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