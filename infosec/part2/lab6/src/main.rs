mod elliptic_curve;
mod alphabet;

extern crate regex;

use regex::Regex;
use elliptic_curve::Curve;
use elliptic_curve::Point;
use getopts::Options;
use yaml_rust::YamlLoader;
use std::fs;
use std::env;

fn find_in_alphabet(p: Point) -> Option<char> {
  for point in alphabet::ALPHABET.entries() {
    let c_p = Point { x: point.1.x, y: point.1.y };
    if c_p.equal(&p) {
      return Some(*point.0);
    }
  }
  return None;
}

fn main() {
  // creating a E(-1, 1) mod 751 curve
  let curve = Curve { a: -1, b: 1, p: 751 };

  // --- Adding cmd line arguments: ------------------
  //      -f: Path to input data yaml-file
  let args: Vec<String> = env::args().collect();

  let mut opts = Options::new();
  opts.optopt("f", "file", "input file path", "input.yaml");

  let matches = match opts.parse(&args[1..]) {
    Ok(m) => { m }
    Err(f) => { panic!(f.to_string()) }
  };

  if !matches.opt_present("f") {
    println!("You must specify the input file path!");
    return;
  }
  // -------------------------------------------------
  // --- Parsing input yaml-file ---------------------
  let file_contents = fs::read_to_string(matches.opt_str("f")
                                            .unwrap()).unwrap();
  let docs = YamlLoader::load_from_str(&file_contents).unwrap();
  let doc = &docs[0];
  let n = doc["n"].as_i64().unwrap();
  println!("n = {}", n);
  // -------------------------------------------------
  // --- Parsing the textual representation of points
  let cm_regex = Regex::new(r"\((\d+), (\d+)\), \((\d+), (\d+)\)").unwrap();
  let mut input: Vec<[Point; 2]> = Vec::new();
  for point_text in doc["dots"].as_vec().unwrap() {
    let p_text = point_text.as_str().unwrap();
    let cap = cm_regex.captures(p_text).unwrap();
    let kg = Point { x: cap[1].parse::<i64>().unwrap(), y: cap[2].parse::<i64>().unwrap() };
    let pmkpb = Point { x: cap[3].parse::<i64>().unwrap(), y: cap[4].parse::<i64>().unwrap() };
    input.push([kg, pmkpb]);
  }
  // -------------------------------------------------
  // --- Decoding message ----------------------------
  let mut res = String::new();
  println!("--------------------------------------------------------------------------------------------------");
  for sym in input {
    let minus_kg = Point { x: sym[0].x, y: -sym[0].y }; // -kG = (kG.x, -kG.y)
    let n_m_kg = curve.mul(&minus_kg, n as i32); // -nkG = n * -kG
    let res_point = curve.sum(&sym[1], &n_m_kg); // original point = Pm + kPb - nkG
    let r_char = find_in_alphabet(res_point).unwrap(); // find the character by a point
    println!("Decoding [{} {}]; -nkG = {}; Pm+kPb-nkG = {}; char = {}",
                sym[0], sym[1], n_m_kg, res_point, r_char);
    println!("--------------------------------------------------------------------------------------------------");
    res.push(r_char);
  }
  println!("Decoded message: {}", res);
}
