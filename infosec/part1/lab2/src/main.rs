extern crate getopts;
use getopts::Options;
use std::env;

mod encrypt;

// Just parsing cmd args and calling encrypt function
fn main() {
    let args: Vec<String> = env::args().collect();

    let mut opts = Options::new();
    opts.optopt("f", "file", "input file path", "input.txt");
    opts.optopt("s", "sec_file", "passphase file path", "secret.txt");
    opts.optopt("o", "out_file", "output file path", "output");

    let matches = match opts.parse(&args[1..]) {
        Ok(m) => { m }
        Err(f) => { panic!(f.to_string()) }
    };

    if !matches.opt_present("f") {
        println!("You must specify the input file path!");
        return;
    }

    if !matches.opt_present("s") {
        println!("You must specify the passphrase file path!");
        return;
    }

    if !matches.opt_present("o") {
        println!("You must specify the output file path!");
        return;
    }

    let input_file = matches.opt_str("f").unwrap();
    let output_file = matches.opt_str("o").unwrap();
    let secret_file = matches.opt_str("s").unwrap();

    let (iv, k) = encrypt::get_iv_and_ks(secret_file)
        .expect("Secret phrase must contain at least 40 characters!");

    encrypt::encrypt(input_file, output_file, iv, k);
}
