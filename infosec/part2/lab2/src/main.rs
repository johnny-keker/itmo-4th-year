mod mod_ops;

extern crate byteorder;
use byteorder::{BigEndian, WriteBytesExt};
use std::mem;
use getopts::Options;
use yaml_rust::YamlLoader;
use std::fs;
use std::env;
use num_traits::cast::ToPrimitive;

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
  // --- Decoding every present chunk of data --------
  let mut in_c = Vec::new(); // initialte input data vector
  for c in doc["C"].as_vec().unwrap() {
    in_c.push(c.as_i64().unwrap());
  }

  println!("Parsed {}\nN: {}\ne: {}\n{} chunks of data",
  matches.opt_str("f").unwrap(), n, e, in_c.len());
  println!("Trying to compute the exponent rank...");

  let mut res = in_c.clone(); // result vector
  let mut i = 1;
  // in this loop we calculate [last_res[i]^e mod n] for every element
  // and update last_res on every iteration until the first element
  // of vector will match the initial chunk of data. That would mean
  // that we have the decoded text in res
  loop {
    let mut last_res = res.clone();
    for j in 0..in_c.len() {
      last_res[j] = mod_ops::mod_exp(&res[j], &e, &n).to_i64().unwrap();
    }
    if last_res[0] == in_c[0] {
      break;
    }
    res = last_res;
    i += 1;
  }
  // Print the answer
  println!("Found the exponent rank: {}", i);
  let mut bs = [0u8; mem::size_of::<i64>()];
  let mut res_s = String::new();
  for r in res {
    bs.as_mut()
      .write_i64::<BigEndian>(r)
      .expect("Unable to write");
    let encoder = encoding_rs::WINDOWS_1251;
    let (s, _, _) = encoder.decode(&bs);
    res_s.push_str(&s);
  }
  println!("Message: {}", res_s);
}
