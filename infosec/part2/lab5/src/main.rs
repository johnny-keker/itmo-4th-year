mod elliptic_curve;

use elliptic_curve::Curve;
use elliptic_curve::Point;

fn main() {
    let curve = Curve { a: 0, b: -1, c: 1, p: 751 };
    let g = Point { x: 0, y: 1 };
    let g3 = curve.mul(&g, 3);
    println!("3G = ({}, {})", g3.x, g3.y)
}
