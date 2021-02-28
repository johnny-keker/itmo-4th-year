use rand::Rng;

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

const K: [u32; 8] = [
    0x00000001,
    0x00000002,
    0x00000003,
    0x00000004,
    0x00000005,
    0x00000006,
    0x00000007,
    0x00000008
];

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

fn gost(block: u64) -> u64 {
    let mut left = (block >> 32) as u32;
    let mut right = block as u32;
    for i in 0..32 {
        let cur_key = if i < 24 {K[i % 8]} else {K[7 - (i % 8)]};
        let tmp = right ^ gost_func(left, cur_key);
        right = left;
        left = tmp;
    }
    return (right as u64) | ((left as u64) << 32);
}

fn gost_dec(block: u64) -> u64 {
    let mut left = (block >> 32) as u32;
    let mut right = block as u32;
    for i in 0..32 {
        let idx = if i < 8 {i} else {7 - (i % 8)};
        let cur_key = K[idx];
        let tmp = left ^ gost_func(right, cur_key);
        left = right;
        right = tmp;
    }
    return (right as u64) | ((left as u64) << 32);
}

fn encrypt() {
    let mut rng = rand::thread_rng();

    let block: u64 = rng.gen();
    println!("blk = {}", format!{"{:#X}", block});

    let encrypted = gost(block);
    println!("enc = {}", format!{"{:#X}", encrypted});

    let decrypted = gost_dec(encrypted);
    println!("dec = {}", format!{"{:#X}", decrypted});
}

fn main() {
    encrypt();
}