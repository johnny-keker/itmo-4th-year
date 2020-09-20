import java.lang.reflect.Field
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions._

class RBTreeTests {
  val rootField: Field  = classOf[RedBlackTree].getDeclaredField("root")
  rootField.setAccessible(true)

  @Test
  def rbTreeInsertTest(): Unit = {
    val tree = new RedBlackTree
    for (i <- 0 to 4)
      tree.insert(i)

    for (i <- 9 to 5 by -1)
      tree.insert(i)

    val root = rootField.get(tree).asInstanceOf[Node]
    // checking node keys
    assertEquals(3, root.data)
    assertEquals(1, root.left.data)
    assertEquals(8, root.right.data)
    // ----------------------
    assertEquals(0, root.left.left.data)
    assertEquals(2, root.left.right.data)
    // ----------------------
    assertEquals(6, root.right.left.data)
    assertEquals(9, root.right.right.data)
    // ----------------------
    assertEquals(4, root.right.left.left.data)
    assertEquals(7, root.right.left.right.data)
    // ----------------------
    assertEquals(5, root.right.left.left.right.data)

    // checking node colors
    assertEquals(0, root.color)
    assertEquals(0, root.left.color)
    assertEquals(0, root.right.color)
    // ----------------------
    assertEquals(0, root.left.left.color)
    assertEquals(0, root.left.right.color)
    // ----------------------
    assertEquals(1, root.right.left.color)
    assertEquals(0, root.right.right.color)
    // ----------------------
    assertEquals(0, root.right.left.left.color)
    assertEquals(0, root.right.left.right.color)
    // ----------------------
    assertEquals(1, root.right.left.left.right.color)
  }

  @Test
  def keyExistsTest(): Unit = {
    val tree = new RedBlackTree
    tree.insert(1)
    tree.insert(2)
    tree.insert(3)

    assertTrue(tree.keyExists(3))
    assertTrue(tree.keyExists(2))
    assertTrue(tree.keyExists(1))
    assertFalse(tree.keyExists(0))
    assertFalse(tree.keyExists(4))
  }

  @Test
  def deleteNodeTest(): Unit = {
    val bst: RedBlackTree = new RedBlackTree
    bst.insert(55)
    bst.insert(40)
    bst.insert(65)
    bst.insert(60)
    bst.insert(75)
    bst.insert(57)

    bst.deleteNode(40)

    assertTrue(bst.keyExists(55))
    assertTrue(bst.keyExists(65))
    assertTrue(bst.keyExists(60))
    assertTrue(bst.keyExists(75))
    assertTrue(bst.keyExists(57))
    assertFalse(bst.keyExists(40))
  }

  @Test
  def throwsOnDeleteNodeThatDoNotExistTest(): Unit = {
    val tree = new RedBlackTree
    tree.insert(1)
    tree.insert(2)

    assertFalse(tree.keyExists(3))
    assertThrows(classOf[ElementNotFoundException], () => tree.deleteNode(3))
  }
}
