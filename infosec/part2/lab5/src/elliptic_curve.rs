#[path = "./point.rs"] mod point;
pub use point::Point;

static DEBUG: bool = false;

// define tne elliptic curve as E(a,b) mod p
pub struct Curve {
  pub a: i64,
  pub b: i64,
  pub p: i64
}

impl Curve {
  // multiplication as a series of P + P ... + P
  pub fn mul(&self, p: &Point, m: i32) -> Point {
    if DEBUG {println!("------- {} * {}", p, m);}
    let mut res = Point { x: p.x, y: p.y };
    for i in 1..m {
      res = self.sum(&res, p);
      if DEBUG {println!("R_{} = {}", i, res);}
    }
    return res;
  }

  // calculate the sum of two point on eliptic curve
  pub fn sum(&self, p1: &Point, p2: &Point) -> Point {
    let lambda = self.get_lambda(p1, p2);
    let mut tmp = lambda * lambda - p1.x - p2.x; // x3 = lambda^2 - x1 - x2
    let r_x = if tmp >= 0 {tmp % self.p} else {(tmp % self.p) + self.p}; // imitate the mod() behaviour
    tmp = lambda * (p1.x - r_x) - p1.y; // y3 = lambda * (x1 - x3) - y1
    let r_y = if tmp >= 0 {tmp % self.p} else {(tmp % self.p) + self.p}; // imitate the mod() behaviour
    return Point { x: r_x, y: r_y };
  }

  // calculate the lambda
  fn get_lambda(&self, p1: &Point, p2: &Point) -> i64 {
    // numenator = y2 - y1 if p1 != p2 and 3x1^2 + a otherwise
    let numerator = if !p1.equal(p2) {p2.y - p1.y}
      else {3*p1.x*p1.x + self.a};
    // denominator = x2 - x1 if p1 != p2 and 2y1 otherwise
    let mut denominator = if !p1.equal(p2) {p2.x - p1.x}
      else {2 * p1.y};
    // the implementation of the inv_mod from mod_ops seems to be buggy
    // when it comes to negative values, so I use my own implementation
    // we compute the corresponding numenator for denominator by solving
    // the `i * x mod p` equation with respect to negative values
    for i in 0..self.p {
      if ((denominator * i) % self.p + if denominator < 0 {self.p} else {0}) == 1 {
          denominator = i;
          break;
      }
    }
    
    let res = denominator * numerator;
    if DEBUG {
      if !p1.equal(p2) {
        println!("lambda = (y2 - y1) * invmod (x2 - x1) = ({} - {}) * {} = {}",
          p2.y, p1.y, denominator, res);
      }
      else {
        println!("lambda = (3x1^2 + a) * invmod (2*y1) = {} * {} = {}",
          (3*p1.x*p1.x + self.a), denominator, res);
      }
    }
    return res;
  }
}