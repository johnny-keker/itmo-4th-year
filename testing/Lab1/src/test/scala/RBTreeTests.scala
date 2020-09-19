import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions._

class RBTreeTests {
  @Test
  def rbTreeInitTest(): Unit = {
    val tree = new RBTree()
    tree.insertNodeByKey(1)
    tree.insertNodeByKey(2)
    tree.insertNodeByKey(3)

    assertEquals(1, tree.getRootLeftKey())
    assertEquals(3, tree.getRootRightKey())
  }
}
