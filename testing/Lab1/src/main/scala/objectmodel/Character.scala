package objectmodel

class Character(
  var Name: String,
  var Age: Int
) {
  def greet() = s"Hello, my name is $Name"
}
