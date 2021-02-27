#[path = "./alphabet.rs"] mod alphabet;

const REL_FREQ: [f32; 26] = [
    8.2, 1.5, 2.8, 4.3, 13.0, 2.2, 2.0,
    6.1, 7.0, 0.15, 0.77, 4.0, 2.4, 6.7,
    7.5, 1.9, 0.095, 6.0, 6.3, 9.1, 2.8,
    0.98, 2.4, 0.15, 2.0, 0.074
];

pub fn get_closest_alphabet_idx(freq: f32) -> usize {
    let mut min = f32::MAX;
    let mut min_idx = 27;
    for i in 0..26 {
        let d = (REL_FREQ[i] - freq).abs();
        if d < min {
            min = d;
            min_idx = i;
        }
    };

    return min_idx;
}

pub fn decode_text(encoded_text: String) -> String {
    let mut letter_count: [usize; 26] = [0; 26];
    let mut letters_sym_count = 0;
    for c in encoded_text.chars() {
        if c.is_ascii_alphabetic() {
            letters_sym_count += 1;
            let idx = alphabet::ALPHABET.iter().position(|&r| r == c.to_ascii_lowercase()).unwrap();
            letter_count[idx] += 1;
        };
    };


    let mut real_idx_mapping: [usize; 26] = [27; 26];
    //let mut freq_sum = 0.0;
    for (i, c) in letter_count.iter().enumerate() {
        real_idx_mapping[i] = get_closest_alphabet_idx((*c as f32 / letters_sym_count as f32) * 100.0);
        //freq_sum += *c as f32 / letters_sym_count as f32;
        //println!("{} = {}", i, *c as f32 / letters_sym_count as f32);
    }
    //println!("Freq sum = {}", freq_sum);

    let mut res = String::with_capacity(encoded_text.len());

    for c in encoded_text.chars() {
        let idx = alphabet::ALPHABET.iter().position(|&r| r == c.to_ascii_lowercase()).unwrap_or(27);
        let mut new_char = if idx == 27 {c} else {alphabet::ALPHABET[real_idx_mapping[idx]]};
        if c.is_ascii_uppercase() {
            new_char = new_char.to_ascii_uppercase();
        }
        res.push(new_char);
    }

    return res;
}