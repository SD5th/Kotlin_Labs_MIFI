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