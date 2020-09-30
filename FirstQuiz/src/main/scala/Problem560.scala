object Problem560 {
  def subarraySum(nums: Array[Int], k: Int): Int = {
    var ans: Int = 0
    for(i <- 0 until nums.length){
      var temp: Int = nums(i)
      if (temp == k) ans += 1
      for(j <- i + 1 until nums.length){
        temp += nums(j)
        if (temp == k) ans += 1
      }
    }
    ans
  }
}
