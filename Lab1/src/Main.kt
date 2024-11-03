
fun main() {
  val bank = Bank("Vasilisa", 10000.0, 10000.0, 1.0)
  val cashRegister = bank.createCashRegister()
  var userInput: Int
  while (true) {
    println("1. Exchange Rub To Usd\n" +
            "2. Exchange Usd To Rub\n" +
            "3. Check Exchange Rate")
    userInput = readln().toInt()
    when (userInput) {
      1 -> {
        println("How much Rub you want to exchange?")
        val rubAmount = readln().toDouble()
        val usdAmount = cashRegister.exchangeRubToUsd(rubAmount)
        println("$rubAmount of roubles was exchanged for $usdAmount of baksov.")
      }
      2 -> {
        println("How much Usd you want to exchange?")
        val usdAmount = readln().toDouble()
        val rubAmount = cashRegister.exchangeRubToUsd(usdAmount)
        println("$usdAmount of baksov was exchanged for $rubAmount of roubles.")
      }
      3 -> {
        println("Current Exchange Rate:\n" +
                "${cashRegister.checkExchangeRate()} of Usd for 1 Rub\n" +
                "${1.0/cashRegister.checkExchangeRate()} of Rub for 1 Usd")
      }
    }
    println("Press Enter to continue.")
    readlnOrNull()
  }
}