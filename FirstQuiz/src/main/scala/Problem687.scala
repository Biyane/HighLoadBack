object Problem687 {
  def longestUnivaluePath(rootBS: TreeNode): Int = {
    var ans: Int = 0
    BS(rootBS)
    def BS(root: TreeNode): Int = {
      var temp: Int = 0
      if (root == null) temp
      else {
        var left: Int = BS(root.left)
        var right: Int = BS(root.right)
        var leftCnt: Int = 0
        var rightCnt: Int = 0
        if (root.left != null && root.left.value == root.value)
          leftCnt += left + 1
        if (root.right != null && root.value == root.right.value)
          rightCnt += right + 1
        ans = math.max(ans, leftCnt + rightCnt)
        temp = math.max(leftCnt, rightCnt)
      }
      temp
    }
    ans
  }
}
