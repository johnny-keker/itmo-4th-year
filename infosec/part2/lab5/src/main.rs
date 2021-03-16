mod elliptic_curve;
mod alphabet;

use elliptic_curve::Curve;
use elliptic_curve::Point;
use getopts::Options;
use yaml_rust::YamlLoader;
use std::fs;
use std::env;

fn main() {
  // creating a E(-1, 1) mod 751 curve
  let curve = Curve { a: -1, b: 1, p: 751 };
  let g = Point { x: 0, y: 1 }; // G = (0, 1)

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
  let bx = doc["Bx"].as_i64().unwrap();
  let by = doc["By"].as_i64().unwrap();
  let pb = Point { x: bx, y: by };
  let text = doc["T"].as_str().unwrap();
  println!("Pb = {}, Message: {}", pb, text);
  let mut k: Vec<i32> = Vec::new();
  for c_k in doc["k"].as_vec().unwrap() {
    k.push(c_k.as_i64().unwrap() as i32);
  }
  // -------------------------------------------------
  // --- Encrypting message --------------------------
  let mut i = 0;
  let mut res: Vec<Point> = Vec::new();
  println!("-------------------------------------------------");
  for c in text.chars() {
    let a_pm = alphabet::ALPHABET[&c]; // get the corresponding point for symbol
    let pm = Point { x: a_pm.x, y: a_pm.y };
    let c_k = k[i]; // get k for current symbol
    let kg = curve.mul(&g, c_k); // kG = k * G
    let kpb = curve.mul(&pb, c_k); // kPb = k * Pb
    let pmkpb = curve.sum(&kpb, &pm); // Pm + kPb
    println!("Symbol: '{}'; k = {}; Pm = {}; kPb = {}", c, c_k, pm, kpb);
    println!("Cm = (kG, Pm+kPb) = ({}, {})", kg, pmkpb);
    println!("-------------------------------------------------");
    res.push(kg);
    res.push(pmkpb);
    i += 1;
  }
  println!("Encrypted message: {:?}", res);
}
