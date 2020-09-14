object Geek {
  def main(args: Array[String]):Unit = {
    println("What's up?")
    println(1 + 1)
    println(1.0 / 5)
    println(1.5 * 3)
    println(5.0 / 2)
  }
  var foo = (x: Int) => x + 2
  var bar = foo
  println(bar(5))
}