import scala.annotation.tailrec
// https://en.wikipedia.org/wiki/Red%E2%80%93black_tree#Operations
// rewritten in scala

object Color extends Enumeration {
  val Black, Red = Value
}

class Node(
  var parent: Node = null,
  var left: Node = null,
  var right: Node = null,
  var color: Color.Value = null,
  var key: Int
)

class RBTree {
  var root: Node = _

  def insertNodeByKey(key: Int): Unit = {
    root = RBTree.insert(root, new Node(key = key))
  }

  def keyExists(key: Int): Boolean = RBTree.findByKey(root, key) != null
}

private object RBTree {
  //<editor-fold desc="Utils">

  def getParent(n: Node): Node = n match {
    case null => null
    case x => x.parent
  }

  def getGrandParent(n: Node): Node = n match {
    case null => null
    case x => getParent(getParent(x))
  }

  def getSibling(n: Node): Node = {
    val p = getParent(n)
    if (p eq null) return null

    if (n == p.left)
      return p.right
    else if (n == p.right)
      return p.left

    null
  }

  def getUncle(n: Node): Node = n match {
    case null => null
    case x => getSibling(getParent(x))
  }

  def rotateLeft(n: Node): Unit = {
    val nnew = n.right
    val p = getParent(n)
    assert(nnew != null)

    n.right = nnew.left
    nnew.left = n
    n.parent = nnew

    if (n.right != null)
      n.right.parent = n

    if (p != null) {
      if (n == p.left)
        p.left = nnew
      else
        p.right = nnew
    }
    nnew.parent = p
  }

  def rotateRight(n: Node): Unit = {
    val nnew = n.left
    val p = getParent(n)
    assert(nnew != null)

    n.left = nnew.right
    nnew.right = n
    n.parent = nnew

    if (n.left != null)
      n.left.parent = n

    if (p != null) {
      if (n == p.left)
        p.left = nnew
      else
        p.right = nnew
    }
    nnew.parent = p
  }
  //</editor-fold>

  //<editor-fold desc="Insert">

  def insert(root: Node, n: Node): Node = {
    insertRecurse(root, n)
    insertRepairTree(n)
    var newRoot = n
    while (getParent(newRoot) != null)
      newRoot = getParent(newRoot)
    newRoot
  }

  def insertRecurse(root: Node, n: Node): Unit = {
    if (root != null) {
      if (n.key < root.key) {
        if (root.left != null) {
          insertRecurse(root.left, n)
          return
        } else {
          root.left = n
        }
      } else {
        if (root.right != null) {
          insertRecurse(root.right, n)
          return
        } else {
          root.right = n
        }
      }
    }

    n.parent = root
    n.left = null
    n.right = null
    n.color = Color.Red
  }

  def insertRepairTree(n: Node): Unit = {
    if (getParent(n) == null)
      insertCase1(n)
    else if (getParent(n).color == Color.Black)
      insertCase2(n)
    else if (getUncle(n) != null && getUncle(n).color == Color.Red)
      insertCase3(n)
    else
      insertCase4(n)
  }

  def insertCase1(n: Node): Unit = n.color = Color.Black

  def insertCase2(n: Node): Unit = {}

  def insertCase3(n: Node): Unit = {
    getParent(n).color = Color.Black
    getUncle(n).color = Color.Black
    getGrandParent(n).color = Color.Red
    insertRepairTree(getGrandParent(n))
  }

  def insertCase4(n: Node): Unit = {
    val p = getParent(n)
    val g = getGrandParent(n)

    if (n == p.right && p == g.left) {
      rotateLeft(p)
      insertCase4Step2(n.left)
      return
    } else if(n == p.left && p == g.right) {
      rotateRight(p)
      insertCase4Step2(n.right)
      return
    }

    insertCase4Step2(n)
  }

  def insertCase4Step2(n: Node): Unit = {
    val p = getParent(n)
    val g = getGrandParent(n)

    if (n == p.left)
      rotateRight(g)
    else
      rotateLeft(g)

    p.color = Color.Black
    g.color = Color.Red
  }
  //</editor-fold>

  //<editor-fold desc="Find">

  @tailrec
  def findByKey(root: Node, key: Int): Node = {
    if (key == root.key) return root
    if (key > root.key) {
      if (root.right == null) null
      else findByKey(root.right, key)
    } else {
      if (root.left == null) null
      else findByKey(root.left, key)
    }
  }
  //</editor-fold>
}
