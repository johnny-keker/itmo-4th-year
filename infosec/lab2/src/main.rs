use rand::Rng;
use std::io::{self, Read};

const S: [[u32; 16]; 8] = [
    [0xC, 0x4, 0x5, 0x2, 0xA, 0x5, 0xB, 0x9, 0xE, 0x8, 0xD, 0x7, 0x0, 0x3, 0xF, 0x1],
    [0x6, 0x8, 0x2, 0x3, 0x9, 0xA, 0x5, 0xC, 0x1, 0xE, 0x4, 0x7, 0xB, 0xD, 0x0, 0xF],
    [0xB, 0x3, 0x5, 0x8, 0x2, 0xF, 0xA, 0xD, 0xE, 0x1, 0x7, 0x4, 0xC, 0x9, 0x6, 0x0],
    [0xC, 0x8, 0x2, 0x1, 0xD, 0x4, 0xF, 0x6, 0x7, 0x0, 0xA, 0x5, 0x3, 0xE, 0x9, 0xB],
    [0x7, 0xF, 0x5, 0xA, 0x8, 0x1, 0x6, 0xD, 0x0, 0x9, 0x3, 0xE, 0xB, 0x4, 0x2, 0xC],
    [0x5, 0xD, 0xF, 0x6, 0x9, 0x2, 0xC, 0xA, 0xB, 0x7, 0x8, 0x1, 0x4, 0x3, 0xE, 0x0],
    [0x8, 0xE, 0x2, 0x5, 0x6, 0x9, 0x1, 0xC, 0xF, 0x4, 0xB, 0x0, 0xD, 0xA, 0x3, 0x7],
    [0x1, 0x7, 0xE, 0xD, 0x0, 0x5, 0x8, 0x3, 0x4, 0xF, 0xA, 0x6, 0x9, 0xC, 0xB, 0x2] 
];

fn as_u64_le(array: &[u8]) -> u64 {
    return
        ((array[0] as u64) <<  0) +
        ((array[1] as u64) <<  8) +
        ((array[2] as u64) << 16) +
        ((array[3] as u64) << 24) +
        ((array[4] as u64) << 32) +
        ((array[5] as u64) << 40) +
        ((array[6] as u64) << 48) +
        ((array[7] as u64) << 56);
}

fn as_u32_le(array: &[u8], offset: usize) -> u32 {
    return
        ((array[0 + offset] as u32) <<  0) +
        ((array[1 + offset] as u32) <<  8) +
        ((array[2 + offset] as u32) << 16) +
        ((array[3 + offset] as u32) << 24);
}

fn gost_func(block: u32, key: u32) -> u32 {
    let tmp = (block as u64 + key as u64) as u32;
    let mut mask: u32 = 0x0000000F;
    let mut result: u32 = 0;
    for i in 0..8 {
        let cur_num = (tmp & mask) >> (i * 4);
        result |= S[i][cur_num as usize] << (i * 4);
        mask = mask << 4;
    }
    return result.rotate_left(11);
}

fn gost(block: u64, k: [u32; 8]) -> u64 {
    let mut left = (block >> 32) as u32;
    let mut right = block as u32;
    for i in 0..32 {
        let cur_key = if i < 24 {k[i % 8]} else {k[7 - (i % 8)]};
        let tmp = right ^ gost_func(left, cur_key);
        right = left;
        left = tmp;
    }
    return (right as u64) | ((left as u64) << 32);
}

fn gost_dec(block: u64, k: [u32; 8]) -> u64 {
    let mut left = (block >> 32) as u32;
    let mut right = block as u32;
    for i in 0..32 {
        let idx = if i < 8 {i} else {7 - (i % 8)};
        let cur_key = k[idx];
        let tmp = left ^ gost_func(right, cur_key);
        left = right;
        right = tmp;
    }
    return (right as u64) | ((left as u64) << 32);
}

fn encrypt(iv: u64, k: [u32; 8]) {
    let mut rng = rand::thread_rng();

    println!("blk = {}", format!{"{:#X}", iv});

    let encrypted = gost(iv, k);
    println!("enc = {}", format!{"{:#X}", encrypted});

    let decrypted = gost_dec(encrypted, k);
    println!("dec = {}", format!{"{:#X}", decrypted});
}

fn get_IV() -> Option<u64> {
    println!("Enter the initial buffer phrase (at least 8 chars): ");
    let mut IV_string = String::new();
    let mut stdin = io::stdin();
    stdin.read_line(&mut IV_string).unwrap();
    if IV_string.len() <= 8 {
        return None;
    }
    return Some(as_u64_le(IV_string.as_bytes()));
}

fn get_passphrase() -> Option<[u32; 8]> {
    println!("Enter passphrase (at least 32 chars): ");
    let mut pass_string = String::new();
    let mut stdin = io::stdin();
    stdin.read_line(&mut pass_string).unwrap();

    if pass_string.len() <= 32 {
        return None;
    }
    let mut res: [u32; 8] = [0; 8];
    let bytes = pass_string.as_bytes();
    for i in 0..8 {
        res[i] = as_u32_le(bytes, i * 4);
    }

    return Some(res);
}

fn main() {
    let IV = get_IV().expect("Initial buffer phrase must contain at least 8 chars!");
    let K = get_passphrase().expect("Passphrase must contain at least 32 chars!");
    println!("{:?}", K);

    encrypt(IV, K);
}
