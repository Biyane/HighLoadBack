import scala.io.StdIn
object calculator {
  val numbers = new StringBuilder("")
  var operatorsSymbols: String = ""
  var arr = Array[Double]()
  def main(args: Array[String]): Unit = {
    var input = StdIn.readLine()
    while (input != "="){
      if (input == "+" || input == "*" || input == "/" || input == "-")
          operatorsSymbols ++= input
      else {
        arr = arr :+ input.toInt
      }
      input = StdIn.readLine()
    }
    val main = new Main(arr,operatorsSymbols)
    main.printNum()
  }
}
