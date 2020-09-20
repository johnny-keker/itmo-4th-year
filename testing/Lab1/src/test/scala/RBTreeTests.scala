import java.lang.reflect.Field
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions._

class RBTreeTests {
  val rootField: Field  = classOf[RBTree].getDeclaredField("root")
  rootField.setAccessible(true)

  @Test
  def rbTreeInitTest(): Unit = {
    val tree = new RBTree()
    tree.insertNodeByKey(1)
    tree.insertNodeByKey(2)
    tree.insertNodeByKey(3)


    val rootX = rootField.get(tree).asInstanceOf[Node]
    assertEquals(1, rootX.left.key)
    assertEquals(3, rootX.right.key)
  }
}
