use num::bigint::BigInt;
use num::bigint::ToBigInt;

// The modular_exponentiation() function takes three identical types
// (which get cast to BigInt), and returns a BigInt:
#[allow(dead_code)]
pub fn mod_exp<T: ToBigInt>(n: &T, e: &T, m: &T) -> BigInt {
    // Convert n, e, and m to BigInt:
    let n = n.to_bigint().unwrap();
    let e = e.to_bigint().unwrap();
    let m = m.to_bigint().unwrap();
 
    // Sanity check:  Verify that the exponent is not negative:
    assert!(e >= Zero::zero());
 
    use num::traits::{Zero, One};
 
    // As most modular exponentiations do, return 1 if the exponent is 0:
    if e == Zero::zero() {
        return One::one()
    }
 
    // Now do the modular exponentiation algorithm:
    let mut result: BigInt = One::one();
    let mut base = n % &m;
    let mut exp = e;
 
    // Loop until we can return out result:
    loop {
        if &exp % 2 == One::one() {
            result *= &base;
            result %= &m;
        }
 
        if exp == One::one() {
            return result
        }
 
        exp /= 2;
        base *= base.clone();
        base %= &m;
    }
}

#[allow(dead_code)]
pub fn mod_inv(a: isize, module: isize) -> isize {
  let mut mn = (module, a);
  let mut xy = (0, 1);
  
  while mn.1 != 0 {
    xy = (xy.1, xy.0 - (mn.0 / mn.1) * xy.1);
    mn = (mn.1, mn.0 % mn.1);
  }
  
  while xy.0 < 0 {
    xy.0 += module;
  }
  xy.0
}