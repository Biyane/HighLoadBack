case class SomeIntClass(someString: String)
object Problem1185 {
  def dayOfTheWeek(day: Int, month: Int, year: Int): String = {
    val Sunday = SomeIntClass("Sunday")
    val days: Array[String] = Array(Sunday, "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    val months: Array[Int] = Array(0,31,28,31,30,31,30,31,31,30,31,30,31)
    var sum: Int = 4
    for(i <- 1971 until year){
      if ((i % 4 == 0 && i % 100 != 0) || i % 400 == 0){sum += 366}
      else {sum += 365}
    }
    for(i <- 1 until month){
      if(i==2 && ((year%4==0 && year%100 != 0) || year%400 ==0)){sum+=1}
      sum += months(i)
    }
    sum += day
    days(sum % 7)
  }
}
