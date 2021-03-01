extern crate getopts;
use getopts::Options;
use std::env;
use std::fs;

use std::io::prelude::*;
use std::fs::File;

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

fn get_array_value(array: &Vec<u8>, i: usize, offset: usize) -> u64 {
    let len = array.len();
    if i + offset >= len {
        return 0;
    }
    return array[i + offset] as u64;
}

fn as_u64_le(array: &Vec<u8>, offset: usize) -> u64 {
    return
        (get_array_value(array, 0, offset) <<  0) +
        (get_array_value(array, 1, offset) <<  8) +
        (get_array_value(array, 2, offset) << 16) +
        (get_array_value(array, 3, offset) << 24) +
        (get_array_value(array, 4, offset) << 32) +
        (get_array_value(array, 5, offset) << 40) +
        (get_array_value(array, 6, offset) << 48) +
        (get_array_value(array, 7, offset) << 56);
}

fn as_u32_le(array: &Vec<u8>, offset: usize) -> u32 {
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

fn encrypt(filename: String, iv: u64, k: [u32; 8]) {
    let file_bytes = fs::read(filename)
        .expect("Cannot read input file, check the spelling");
    
    let byte_count = file_bytes.len();
    let mut encoded_bytes = 0;

    let mut res: Vec<u8> = Vec::new();

    let mut gamma = gost(iv, k);
    let mut block = as_u64_le(&file_bytes, 0);
    let mut encoded = block ^ gamma;
    res.extend_from_slice(&encoded.to_le_bytes());
    encoded_bytes += 8;

    while encoded_bytes < byte_count {
        gamma = gost(gamma, k);
        block = as_u64_le(&file_bytes, encoded_bytes);
        encoded = block ^ gamma;
        res.extend_from_slice(&encoded.to_le_bytes());
        encoded_bytes += 8;
    }

    let mut pos = 0;
    let mut buffer = File::create("foo.txt").unwrap();

    while pos < res.len() {
        let bytes_written = buffer.write(&res[pos..]).unwrap();
        pos += bytes_written;
    }
}

fn get_iv_and_passphrase(filename: String) -> Option<(u64, [u32; 8])> {
    let bytes = fs::read(filename)
        .expect("Cannot read passphrase file, check the spelling");
    if bytes.len() <= 40 {
        return None;
    }
    let iv = as_u64_le(&bytes, 0);
    let mut k: [u32; 8] = [0; 8];
    for i in 0..8 {
        k[i] = as_u32_le(&bytes, 8 + i * 4);
    }

    return Some((iv, k));
}

fn main() {
    let args: Vec<String> = env::args().collect();

    let mut opts = Options::new();
    opts.optopt("f", "file", "input file name", "input.txt");
    opts.optopt("s", "sec_file", "file with passphase", "secret.txt");
    opts.optflag("d", "decode", "decode input file (default is encode)");

    let matches = match opts.parse(&args[1..]) {
        Ok(m) => { m }
        Err(f) => { panic!(f.to_string()) }
    };

    if !matches.opt_present("f") {
        println!("You must specify the input file!");
        return;
    }

    if !matches.opt_present("s") {
        println!("You must specify the passphrase file!");
        return;
    }

    let input_file = matches.opt_str("f").unwrap();
    let secret_file = matches.opt_str("s").unwrap();

    let (iv, k) = get_iv_and_passphrase(secret_file)
        .expect("Secret phrase must contain at least 40 characters!");

    encrypt(input_file, iv, k);
}
