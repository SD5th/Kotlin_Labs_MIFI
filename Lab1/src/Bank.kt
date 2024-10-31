import kotlin.random.Random
import java.io.File
import java.io.IOException

class Logger (filePath: String = "/logs/Log.txt") {
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
      println("Ошибка при записи в лог: ${e.message}")
    }
  }
}

class Bank(
  private var rubBalance: Double = 0.0,
  private var usdBalance: Double = 0.0,
  internal var exchangeRate: Double = 1.0  // exchangeRate rub for 1 usd
) {                                        // 1/exchangeRate usd for 1 rub
  internal fun sendRub(amount: Double): Double {
    var toSend = amount
    if (rubBalance < toSend)
      toSend = rubBalance
    rubBalance -= toSend
    recalculateExchangeRate()
    return toSend
  }

  internal fun sendUsd(amount: Double): Double {
    var toSend = amount
    if (usdBalance < toSend)
      toSend = usdBalance
    usdBalance -= toSend
    recalculateExchangeRate()
    return toSend
  }

  internal fun receiveRub(amount: Double) {
    rubBalance += amount
    recalculateExchangeRate()
  }

  internal fun receiveUsd(amount: Double) {
    usdBalance += amount
    recalculateExchangeRate()
  }

  private fun recalculateExchangeRate() {
    if (Random.nextBoolean())
      exchangeRate *= Random.nextDouble(1.0, 2.0)
    else
      exchangeRate /= Random.nextDouble(1.0, 2.0)
  }

  fun createCashRegister(): CashRegister {
    return CashRegister(bank = this)
  }

  private class BankLogger (filePath: String = "/log/Log.txt") : Logger(filePath)
  {

  }

}

interface CashRegisterInterface {
  fun exchangeRubToUsd(rubAmount: Double): Double
  fun exchangeUsdToRub(usdAmount: Double): Double
  fun checkExchangeRate(): Double
}

class CashRegister (private var bank: Bank) : CashRegisterInterface {

  override fun exchangeRubToUsd(rubAmount: Double): Double {
    val usdAmount = bank.sendUsd(rubAmount * bank.exchangeRate)
    bank.receiveRub(rubAmount)
    return usdAmount
  }

  override fun exchangeUsdToRub(usdAmount: Double): Double{
    val rubAmount = bank.sendRub(usdAmount * (1 / bank.exchangeRate))
    bank.receiveUsd(usdAmount)
    return rubAmount
  }

  override fun checkExchangeRate(): Double {
    return bank.exchangeRate
  }
}

fun main() {
  println("Test commit")
}