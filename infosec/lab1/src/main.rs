use std::env;
use std::fs;

mod caesar;
mod freq_analysis;

fn main() {
    let args: Vec<String> = env::args().collect();

    let filename = &args[1];
    let offset = *(&args[2].parse::<usize>().unwrap());
    let left = if args.len() == 4 {&args[3] == "left"} else {false};
    let contents = fs::read_to_string(filename)
        .expect("Something went wrong reading the file");
    
    println!("Source text:\n{}", contents);

    let encoded = caesar::encode_text(contents, offset, left);
    println!("\nEncoded text:\n{}", encoded);
    let decoded = freq_analysis::decode_text(encoded);
    println!("\nDecoded text:\n{}", decoded);
}
