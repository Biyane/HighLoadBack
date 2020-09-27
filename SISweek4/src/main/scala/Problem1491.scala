object Problem1491 {
  def average(salary: Array[Int]): Double = {
    val maxNum: Int = salary.max
    val minNum: Int = salary.min
    var ans: Double = 0
    println(maxNum, minNum)
    for(i <- 0 until salary.length){
      if (salary(i) == maxNum || salary(i) == minNum){}
      else ans += salary(i)
    }
    print(ans)
    ans /= (salary.length - 2)
    ans
  }
}
