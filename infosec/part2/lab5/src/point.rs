use std::fmt;

#[derive(PartialEq, PartialOrd)]
pub struct Point {
    pub x: i64,
    pub y: i64
}

impl fmt::Display for Point {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "({}, {})", self.x, self.y)
    }
}