class Problem1137 {
  def tribonacci(n: Int): Int = {
    var n1: Int = 0
    var n2: Int = 1
    var n3: Int = 1
    var ans: Int = 0
    if (n == 0) ans = 0
    else if (n == 1) ans = 1
    else if (n == 2) ans = 1
    else {
      for(i <- 3 to n){
        ans= (n1 + n2 + n3)
        n1 = n2
        n2 = n3
        n3 = ans
      }
      ans
    }
    ans
  }
}
