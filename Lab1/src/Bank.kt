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
  private val name: String = "unknown",
  private var rubBalance: Double = 0.0,
  private var usdBalance: Double = 0.0,
  internal var exchangeRate: Double = 1.0
) {
  private var logger: Logger = Logger("/logs/Bank_log.txt")
  private var registerCodes: MutableList<Int> = mutableListOf()

  internal fun sendRub(amount: Double, registerCode: Int): Double {
    if (!registerCodes.contains(registerCode)) {
      throw Exception("Bank '$name': Unknown RegisterCode $registerCode")
    }
    var toSend = amount
    if (rubBalance < toSend)
      toSend = rubBalance
    rubBalance -= toSend
    logger.log("Bank '$name': Send $toSend roubles.")
    return toSend
  }

  internal fun sendUsd(amount: Double, registerCode: Int): Double {
    if (!registerCodes.contains(registerCode)) {
      throw Exception("Bank '$name': Unknown RegisterCode $registerCode")
    }
    var toSend = amount
    if (usdBalance < toSend)
      toSend = usdBalance
    usdBalance -= toSend
    logger.log("Bank '$name': Send $toSend baksov.")
    return toSend
  }

  internal fun receiveRub(amount: Double, registerCode: Int) {
    if (!registerCodes.contains(registerCode)) {
      throw Exception("Bank '$name': Unknown RegisterCode $registerCode")
    }
    rubBalance += amount
    logger.log("Bank '$name': Received $amount rubs.")
  }

  internal fun receiveUsd(amount: Double, registerCode: Int) {
    if (!registerCodes.contains(registerCode)) {
      throw Exception("Bank '$name': Unknown RegisterCode $registerCode")
    }
    usdBalance += amount
    logger.log("Bank '$name': Received $amount baksov.")
  }

  fun createCashRegister(): CashRegister {
    var registerCode = Random.nextInt()
    while (!registerCodes.contains(registerCode)) {
      registerCode = Random.nextInt()
    }
    registerCodes.addLast(registerCode)
    logger.log("Bank '$name': Created CashRegister with code: $registerCode")
    return CashRegister(bank = this, code = registerCode)
  }

}

interface CashRegisterInterface {
  fun exchangeRubToUsd(rubAmount: Double): Double
  fun exchangeUsdToRub(usdAmount: Double): Double
  fun checkExchangeRate(): Double
}

class CashRegister (private var bank: Bank, private val code: Int) : CashRegisterInterface
{

  override fun exchangeRubToUsd(rubAmount: Double): Double {
    val usdAmount = bank.sendUsd(rubAmount * bank.exchangeRate, code)
    bank.receiveRub(rubAmount, code)
    return usdAmount
  }

  override fun exchangeUsdToRub(usdAmount: Double): Double{
    val rubAmount = bank.sendRub(usdAmount * (1 / bank.exchangeRate), code)
    bank.receiveUsd(usdAmount, code)
    return rubAmount
  }

  override fun checkExchangeRate(): Double {
    return bank.exchangeRate
  }
}

fun main() {
  println("Test commit")
}