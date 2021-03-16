#[path = "./mod_ops.rs"] mod mod_ops;
use mod_ops::{mod_exp, mod_inv};

// function that performs the factorization of n
pub fn get_d(n: i64, e: i64) -> i64 {
  let mut i = 0;
  let mut sqrt_w = (n as f64).sqrt(); // first candidate - sqrt(n)
  let mut t = sqrt_w.round() as i64;
  println!("n = {}", t);
  println!("--------------------------------");
  // keep moving until current w is not whole
  while sqrt_w.fract() != 0.0 {
    i += 1; // increment iteration counter
    t += 1; // current t - increments every iteration
    let w_i = (t * t) - n; // w_i = t^2 - n
    println!("t_{} = {}; w_{} = {}", i, t, i, w_i);
    sqrt_w = (w_i as f64).sqrt(); // count sqrt(w_i) to check is it whole
  }
  println!("--------------------------------");
  let r_w = sqrt_w as i64; // resulting w converts to int
  println!("t = {}; sqrt(w) = {}", t, r_w);
  let p = t + r_w; // p = (t + sqrt(w))
  let q = t - r_w; // q = (t - sqrt(w))
  println!("p = {}; q =, {}", p, q);
  let phi_n = (p - 1) * (q - 1); // Phi(n) = (p - 1)(q - 1)
  println!("Phi(N) = {}", phi_n);
  let d = mod_inv(e as isize, phi_n as isize) as i64; // d = e^-1 mod Phi(n)
  println!("d = {}", d);
  return d;
}

// function that decodes chunk of data based on given d and n
pub fn decode_rsa(d: i64, n: i64, c: i64) -> String {
  println!();
  // encoder for win1251 to support cyrillic symbols
  let encoder = encoding_rs::WINDOWS_1251;
  println!("C = {}", c);
  let m = mod_exp(&c, &d, &n); // M = C^d mod N
  println!("M = {}", m);
  // convert M to symbols in win1251
  let bs = m.to_bytes_be().1;
  let (res, _, _) = encoder.decode(&bs);
  println!("Message = {}", res);
  println!();
  return res.to_string();
}