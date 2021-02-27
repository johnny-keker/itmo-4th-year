#[path = "./alphabet.rs"] mod alphabet;

fn get_left_idx(idx: usize, offset: usize) -> usize {
    if idx >= offset {
        return idx - offset;
    } else {
        return 26 - (offset - idx);
    }
}

pub fn encode_text(input_text: String, offset: usize, left: bool) -> String {
    let len = input_text.len();
    let mut result = String::with_capacity(len);

    for cur_char in input_text.chars() {
        let lower_case = cur_char.to_ascii_lowercase();
        let idx = alphabet::ALPHABET.iter().position(|&r| r == lower_case).unwrap_or(27);

        let new_idx = if left {get_left_idx(idx, offset % 26)} else {(idx + offset) % 26};
        let mut new_char = if idx == 27 {lower_case} else {alphabet::ALPHABET[new_idx]};
        if cur_char.is_ascii_uppercase() {
            new_char = new_char.to_ascii_uppercase();
        }

        result.push(new_char);
    };

    return result;
}