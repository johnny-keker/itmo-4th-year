use std::env;
use std::fs;

const ALPHABET: [char; 26] = [
    'a', 'b', 'c', 'd', 'e', 'f', 'g',
    'h', 'i', 'j', 'k', 'l', 'm', 'n',
    'o', 'p', 'q', 'r', 's', 't', 'u',
    'v', 'w', 'x', 'y', 'z'
];

fn main() {
    let args: Vec<String> = env::args().collect();

    let filename = &args[1];
    let contents = fs::read_to_string(filename)
        .expect("Something went wrong reading the file");
    
    println!("Source text:\n{}", contents);

    println!("Encoded text:");
    for cur_char in contents.chars() {
        let lower_case = cur_char.to_ascii_lowercase();
        let idx = ALPHABET.iter().position(|&r| r == lower_case).unwrap_or(27);

        let new_char = if idx == 27 {lower_case} else {ALPHABET[(idx + 2) % 26]};
        
        print!("{}", new_char);
    }
}
