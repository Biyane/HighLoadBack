class TreeNode(_value: Int = 0, _left: TreeNode = null, _right: TreeNode = null) {
  var value: Int = _value
  var left: TreeNode = _left
  var right: TreeNode = _right
}

object Problem938 {
  var temp: Int = 0
  def rangeSumBST(root: TreeNode, L: Int, R: Int): Int = {
    temp = 0
    var ans: Int = 0
    ans = BST(root, L, R)
    ans
  }
  def BST(root: TreeNode, L: Int, R: Int): Int = {
    if (root != null){
      BST(root.left, L, R)
      if (root.value >= L && root.value <= R){temp += root.value}
      BST(root.right, L ,R)
    }
    temp
  }
}
