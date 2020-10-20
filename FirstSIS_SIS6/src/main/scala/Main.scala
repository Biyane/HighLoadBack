class Main(var numbers:Array[Double], var operators: String) {

  var res: Double = 0
  var temp:Int = 0
  val indexedSeq = for(i <- 0 to 10 if i % 2 == 0) yield i

  operators(0) match {
    case '+' => res = numbers(0) + numbers(1)
    case '-' => res = numbers(0) - numbers(1)
    case '*' => res = numbers(0) * numbers(1)
    case '/' => res = numbers(0) / numbers(1)
  }
  for(i <- 1 to operators.length - 1; j <- 2 to numbers.length - 1){
      operators(i) match {
        case '+' => res += numbers(j)
        case '*' => res *= numbers(j)
        case '-' => res -= numbers(j)
        case '/' => res /= numbers(j)
      }
  }
  def printNum() = print(res)
}
