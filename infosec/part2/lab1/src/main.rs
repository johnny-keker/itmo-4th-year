extern crate encoding_rs;
extern crate byteorder;
extern crate yaml_rust;
extern crate getopts;

mod rsa;

use getopts::Options;
use yaml_rust::YamlLoader;
use std::fs;
use std::env;

fn main() {
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
  let n = doc["N"].as_i64().unwrap();
  let e = doc["e"].as_i64().unwrap();
  // -------------------------------------------------
  // --- Finding d as a result of factorization of n -
  let d = rsa::get_d(n, e);
  // -------------------------------------------------
  // --- Decoding every present chunk of data --------
  let mut res = String::new();
  for c in doc["C"].as_vec().unwrap() {
    res.push_str(&rsa::decode_rsa(d, n, c.as_i64().unwrap()));
  }
  println!("\nDecoded text: {}", res);
  // -------------------------------------------------
}
