extern crate getopts;
use getopts::Options;
use std::env;
use std::fs;

mod caesar;
mod freq_analysis;
// Just parsing cmd args and calling encode or decode function
fn main() {
    let args: Vec<String> = env::args().collect();

    let mut opts = Options::new();
    opts.optopt("f", "file", "input file path", "input.txt");
    opts.optopt("o", "offset", "the chipher offset", "4");
    opts.optflag("l", "left", "left shift mode (default is right)");
    opts.optflag("d", "decode", "decode mode");

    let matches = match opts.parse(&args[1..]) {
        Ok(m) => { m }
        Err(f) => { panic!(f.to_string()) }
    };

    if !matches.opt_present("f") {
        println!("You must specify the input file path!");
        return;
    }

    if !matches.opt_present("o") && !matches.opt_present("d") {
        println!("You must specify the offset!");
        return;
    }

    let filename = matches.opt_str("f").unwrap();
    let left = matches.opt_present("l");
    let decode = matches.opt_present("d");

    let contents = fs::read_to_string(filename)
        .expect("Something went wrong reading the file");

    if !decode {
        let offset = matches.opt_str("o").unwrap().parse::<usize>().unwrap();
        let encoded = caesar::encode_text(contents, offset, left);
        println!("{}", encoded);
    } else {
        let decoded = freq_analysis::decode_text(contents);
        println!("{}", decoded);
    }
}
