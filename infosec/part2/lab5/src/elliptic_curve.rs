#[path = "./point.rs"] mod point;
pub use point::Point;

pub struct Curve {
    pub a: i32,
    pub b: i32,
    pub c: i32,
    pub p: i32
}

impl Curve {
    pub fn mul(&self, p: &Point, m: i32) -> Point {
        let mut res = Point { x: p.x, y: p.y };
        for _ in 0..(m - 1) {
            res = self.sum(p, &res);
        }
        return res;
    }

    fn sum(&self, p1: &Point, p2: &Point) -> Point {
        let lambda = self.get_lambda(p1, p2);
        let mut tmp = lambda * lambda - self.a - p1.x - p2.x;
        let r_x = if tmp >= 0 {tmp % self.p} else {(tmp % self.p) + self.p};
        tmp = lambda * (p1.x - r_x) - p1.y;
        let r_y = if tmp >= 0 {tmp % self.p} else {(tmp % self.p) + self.p};
        return Point { x: r_x, y: r_y };
    }

    fn get_lambda(&self, p1: &Point, p2: &Point) -> i32 {
        let nominator: i32;
        let mut denominator: i32;
        if (p1.x == p2.x) && (p1.y == p2.y) {
            nominator = 3 * p1.x * p1.x + 2 * self.a * p1.x + self.b;
            denominator = 2 * p1.y;
        } else {
            nominator = p2.y - p1.y;
            denominator = p2.x - p1.x;
        }

        for i in 0..self.p {
            if (denominator * i) % self.p == 1 {
                denominator = i;
                break;
            }
        }

        return (nominator * denominator) % self.p +
            if nominator * denominator < 0 {self.p} else {0};
    }
}