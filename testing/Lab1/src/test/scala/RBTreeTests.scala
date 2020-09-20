import java.lang.reflect.Field
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions._

class RBTreeTests {
  val rootField: Field  = classOf[RBTree].getDeclaredField("root")
  rootField.setAccessible(true)

  @Test
  def rbTreeInsertTest(): Unit = {
    val tree = new RBTree()
    for (i <- 0 to 4)
      tree.insertNodeByKey(i)

    for (i <- 9 to 5 by -1)
      tree.insertNodeByKey(i)

    val root = rootField.get(tree).asInstanceOf[Node]
    // checking node keys
    assertEquals(3, root.key)
    assertEquals(1, root.left.key)
    assertEquals(8, root.right.key)
    // ----------------------
    assertEquals(0, root.left.left.key)
    assertEquals(2, root.left.right.key)
    // ----------------------
    assertEquals(6, root.right.left.key)
    assertEquals(9, root.right.right.key)
    // ----------------------
    assertEquals(4, root.right.left.left.key)
    assertEquals(7, root.right.left.right.key)
    // ----------------------
    assertEquals(5, root.right.left.left.right.key)

    // checking node colors
    assertEquals(Color.Black, root.color)
    assertEquals(Color.Black, root.left.color)
    assertEquals(Color.Black, root.right.color)
    // ----------------------
    assertEquals(Color.Black, root.left.left.color)
    assertEquals(Color.Black, root.left.right.color)
    // ----------------------
    assertEquals(Color.Red, root.right.left.color)
    assertEquals(Color.Black, root.right.right.color)
    // ----------------------
    assertEquals(Color.Black, root.right.left.left.color)
    assertEquals(Color.Black, root.right.left.right.color)
    // ----------------------
    assertEquals(Color.Red, root.right.left.left.right.color)
  }

  @Test
  def keyExistsTest(): Unit = {
    val tree = new RBTree()
    tree.insertNodeByKey(1)
    tree.insertNodeByKey(2)
    tree.insertNodeByKey(3)

    assertTrue(tree.keyExists(3))
    assertTrue(tree.keyExists(2))
    assertTrue(tree.keyExists(1))
    assertFalse(tree.keyExists(0))
    assertFalse(tree.keyExists(4))
  }
}
