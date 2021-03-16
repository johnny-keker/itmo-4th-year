use std::fmt;
// simple struct that represents the point in 2D space
#[derive(PartialEq, PartialOrd, Clone, Copy)]
pub struct Point {
    pub x: i64,
    pub y: i64
}

impl Point {
  pub fn equal(&self, other: &Point) -> bool {
    return self.x == other.x && self.y == other.y;
  }
}

impl fmt::Display for Point {
  fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
    write!(f, "({}, {})", self.x, self.y)
  }
}

impl fmt::Debug for Point {
  fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
    write!(f, "({}, {})", self.x, self.y)
  }
}