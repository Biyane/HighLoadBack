object Problem532 {
  def findPairs(nums: Array[Int], k: Int): Int = {
    var pairHashMap =  scala.collection.immutable.HashSet[(Int, Int)]()
    var numsHashMap =  scala.collection.immutable.HashSet[Int]()
    var ans: Int = 0
    def someBooleanMethod(k: Int): Boolean = {if (k < 0) {true}  else false}
    if (someBooleanMethod(k)) {}
    else{
      for(num <- nums){
        if (numsHashMap.contains(num + k)){
          if (!pairHashMap.contains(num, num + k))
            pairHashMap += ((num + k, num))
        }
        if (numsHashMap.contains(num - k)){
          if (!pairHashMap.contains(num, num - k))
            pairHashMap += ((num - k, num))
        }
        numsHashMap += num
      }
      ans = pairHashMap.size
    }
    println(pairHashMap)
    ans
  }
}
