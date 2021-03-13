#[path = "./alphabet.rs"] mod alphabet;
// calculate the correct index when shifting to the left
fn get_left_idx(idx: usize, offset: usize) -> usize {
    if idx >= offset {
        return idx - offset;
    } else {
        return 26 - (offset - idx);
    }
}
// caesar chipher implementation
pub fn encode_text(input_text: String, offset: usize, left: bool) -> String {
    let len = input_text.len();
    let mut result = String::with_capacity(len);

    for cur_char in input_text.chars() {
        let lower_case = cur_char.to_ascii_lowercase();
        // find the index of the letter in alphabet
        let idx = alphabet::ALPHABET.iter().position(|&r| r == lower_case).unwrap_or(27);
        // find the shifted index of the letter
        let new_idx = if left {get_left_idx(idx, offset % 26)} else {(idx + offset) % 26};
        // get the new character based on shifted index
        let mut new_char = if idx == 27 {lower_case} else {alphabet::ALPHABET[new_idx]};
        // preserve original case
        if cur_char.is_ascii_uppercase() {
            new_char = new_char.to_ascii_uppercase();
        }
        // store new character
        result.push(new_char);
    };

    return result;
}