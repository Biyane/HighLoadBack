class Problem783 {
  def minDiffInBST(BSTroot: TreeNode): Int = {
    var ans: Int = Int.MaxValue
    var arr: Array[Int] = Array[Int]()
    BST(BSTroot)
    def BST(root: TreeNode): Unit = {
      if (root != null){
        BST(root.left)
        arr :+= root.value
        BST(root.right)
      }
    }
    for(i <- 0 until arr.length - 1){
      for(j <- i + 1 until arr.length){
        if ((arr(i) - arr(j)).abs < ans) ans = (arr(i) - arr(j)).abs
      }
    }
    ans
  }
}
