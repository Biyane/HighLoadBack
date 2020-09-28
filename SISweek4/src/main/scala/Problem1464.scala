object Problem1464 {
  def maxProduct(nums: Array[Int]): Int = {
    var high1: Int = 0
    var high2: Int = 0
    for(num <- nums){
      if (num > high1){
        high2 = high1
        high1 = num
      }
      else if (num > high2) high2 = num
    }
    def some_hof(high1: Int, high2: Int): Int = {
      val someNum: Int = ( high1 - 1) * (high2 - 1)
      someNum
    }
    high1 = some_hof(high1, high2)
    high1
  }


  def main(args: Array[String]): Unit = {
    println("hello")
  }
}
