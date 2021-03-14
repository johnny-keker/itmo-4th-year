mod elliptic_curve;

use elliptic_curve::Curve;
use elliptic_curve::Point;

fn main() {
    let curve = Curve { a: 0, b: -1, c: 1, p: 751 };
    let g = Point { x: 0, y: 1 };
    let k = 3;
    let pm = Point { x: 66, y: 522 };
    let pb = Point { x: 406, y: 397 };
    let b = 45;

    let g3 = curve.mul(&g, k);
    let pmkpb = curve.sum(&pm, &curve.mul(&pb, k));
    println!("3G = {}", g3);
    println!("Pm + kPb = {} + {} * {} = {}", pm, k, pb, pmkpb);
}
