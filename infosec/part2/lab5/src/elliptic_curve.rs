#[path = "./point.rs"] mod point;
pub use point::Point;

pub struct Curve {
    pub a: i64,
    pub b: i64,
    pub c: i64,
    pub p: i64
}

impl Curve {
    pub fn mul(&self, p: &Point, m: i32) -> Point {
        let mut res = Point { x: p.x, y: p.y };
        for _ in 0..(m - 1) {
            res = self.sum(p, &res);
        }
        return res;
    }

    pub fn sum(&self, p1: &Point, p2: &Point) -> Point {
        let lambda = self.get_lambda(p1, p2);
        //println!("labbda = {}", lambda);
        let mut tmp = lambda * lambda - self.a - p1.x - p2.x;
        let r_x = if tmp >= 0 {tmp % self.p} else {(tmp % self.p) + self.p};
        tmp = lambda * (p1.x - r_x) - p1.y;
        let r_y = if tmp >= 0 {tmp % self.p} else {(tmp % self.p) + self.p};
        return Point { x: r_x, y: r_y };
    }

    fn get_lambda(&self, p1: &Point, p2: &Point) -> i64 {
        let nominator: i64;
        let mut denominator: i64;
        if (p1.x == p2.x) && (p1.y == p2.y) {
            nominator = 3 * p1.x * p1.x + 2 * self.a * p1.x + self.b;
            //println!("(3 * {}^2 + 2 * {} * {} + {}) = {}", p1.x, self.a, p1.x, self.b, nominator);
            denominator = 2 * p1.y;
        } else {
            nominator = p2.y - p1.y;
            denominator = p2.x - p1.x;
        }
        //println!("p1 = ({}, {}), p2 = ({}, {}), n = {}, d = {}", p1.x, p1.y, p2.x, p2.y, nominator, denominator);

        for i in 0..self.p {
            if (denominator * i) % self.p == 1 {
                denominator = i;
                break;
            }
        }
        //println!("denominator = {}", denominator);

        return (nominator * denominator) % self.p +
            if nominator * denominator < 0 {self.p} else {0};
    }
}