use std::env;
use std::fs;

const ALPHABET: [char; 26] = [
    'a', 'b', 'c', 'd', 'e', 'f', 'g',
    'h', 'i', 'j', 'k', 'l', 'm', 'n',
    'o', 'p', 'q', 'r', 's', 't', 'u',
    'v', 'w', 'x', 'y', 'z'
];

fn encode_text(input_text: String, offset: usize) -> String {
    let len = input_text.len();
    let mut result = String::with_capacity(len);

    for cur_char in input_text.chars() {
        let lower_case = cur_char.to_ascii_lowercase();
        let idx = ALPHABET.iter().position(|&r| r == lower_case).unwrap_or(27);

        let mut new_char = if idx == 27 {lower_case} else {ALPHABET[(idx + offset) % 26]};
        if cur_char.is_ascii_uppercase() {
            new_char = new_char.to_ascii_uppercase();
        }

        result.push(new_char);
    };

    return result;
}

fn main() {
    let args: Vec<String> = env::args().collect();

    let filename = &args[1];
    let offset = *(&args[2].parse::<usize>().unwrap());
    let contents = fs::read_to_string(filename)
        .expect("Something went wrong reading the file");
    
    println!("Source text:\n{}", contents);

    println!("Encoded text:");
    println!("{}", encode_text(contents, offset));
}
