import java.lang.reflect.Field
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions._
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class RBTreeTests {
  val rootField: Field  = classOf[RedBlackTree].getDeclaredField("root")
  rootField.setAccessible(true)
  val tnullField: Field  = classOf[RedBlackTree].getDeclaredField("TNULL")
  tnullField.setAccessible(true)

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
    val tree: RedBlackTree = new RedBlackTree
    tree.insert(55)
    tree.insert(40)
    tree.insert(65)
    tree.insert(60)
    tree.insert(75)
    tree.insert(57)

    tree.deleteNode(40)

    val root = rootField.get(tree).asInstanceOf[Node]
    // checking node keys
    assertEquals(65, root.data)
    assertEquals(57, root.left.data)
    assertEquals(75, root.right.data)
    // ----------------------
    assertEquals(55, root.left.left.data)
    assertEquals(60, root.left.right.data)

    // checking node colors
    assertEquals(0, root.color)
    assertEquals(1, root.left.color)
    assertEquals(0, root.right.color)
    // ----------------------
    assertEquals(0, root.left.left.color)
    assertEquals(0, root.left.right.color)
  }

  @Test
  def deleteAllNodesTest(): Unit = {
    val tree: RedBlackTree = new RedBlackTree
    tree.insert(55)
    tree.insert(40)
    tree.insert(65)
    tree.insert(60)
    tree.insert(75)
    tree.insert(57)

    tree.deleteNode(65)
    tree.deleteNode(57)
    tree.deleteNode(75)
    tree.deleteNode(40)
    tree.deleteNode(55)
    tree.deleteNode(60)

    val root = rootField.get(tree).asInstanceOf[Node]
    val tnull = tnullField.get(tree).asInstanceOf[Node]
    assertEquals(tnull, root)
  }

  @Test
  def deleteRootTest(): Unit = {
    val tree: RedBlackTree = new RedBlackTree
    tree.insert(55)
    tree.insert(40)
    tree.insert(65)
    tree.insert(60)
    tree.insert(75)
    tree.insert(57)

    tree.deleteNode(55)

    val root = rootField.get(tree).asInstanceOf[Node]
    // checking node keys
    assertEquals(57, root.data)
    assertEquals(40, root.left.data)
    assertEquals(65, root.right.data)
    // ----------------------
    assertEquals(60, root.right.left.data)
    assertEquals(75, root.right.right.data)

    // checking node colors
    assertEquals(0, root.color)
    assertEquals(0, root.left.color)
    assertEquals(1, root.right.color)
    // ----------------------
    assertEquals(0, root.left.left.color)
    assertEquals(0, root.left.right.color)
  }

  @Test
  def throwsOnDeleteNodeThatDoNotExistTest(): Unit = {
    val tree = new RedBlackTree
    tree.insert(1)
    tree.insert(2)

    assertFalse(tree.keyExists(3))
    assertThrows(classOf[ElementNotFoundException], () => tree.deleteNode(3))
  }

  @Test
  def printTreeTest(): Unit = {
    val outContent = new ByteArrayOutputStream
    System.setOut(new PrintStream(outContent))

    val tree = new RedBlackTree
    tree.printTree()
    assertEquals("", outContent.toString())

    tree.insert(1)
    tree.printTree()
    assertEquals(s"R----1(BLACK)${System.lineSeparator()}", outContent.toString())
    outContent.reset()

    val expected =
    """ |R----40(BLACK)
        |   L----1(BLACK)
        |   R----60(RED)
        |      L----55(BLACK)
        |      |  R----57(RED)
        |      R----65(BLACK)
        |         R----75(RED)
""".stripMargin
    tree.insert(55)
    tree.insert(40)
    tree.insert(65)
    tree.insert(60)
    tree.insert(75)
    tree.insert(57)
    tree.printTree()
    assertEquals(expected, outContent.toString())

    System.setOut(System.out)
  }
}
