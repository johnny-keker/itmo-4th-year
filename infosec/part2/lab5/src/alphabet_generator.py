# This script is parsing the text file that was produced from the task pdf
# and producing the rust code that contains the definition of the alphabet
# as a Map<char, Point>. It was created because the alphabet is pretty
# big, and writing the rust module manually will be problematic, and I
# will definitely do lots of mistakes while retyping the values :)

buffer = ""
buffer += "#[path = \"./point.rs\"] mod point;\n"
buffer += "use point::Point;\n"
buffer += "use phf::phf_map;\n\n"
buffer += "pub static ALPHABET: phf::Map<char, Point> = phf_map! {\n"

with open('alphabet.txt', 'r') as d:
  lines = d.readlines()

for line in lines:
  values = line.split('\t')
  [x, y] = values[1].replace('\n', '').replace('(', '').replace(')', '').replace(' ', '').split(',')
  buffer += f" '{values[0]}' => Point {{ x: {x}, y: {y} }},\n"

buffer += "};\n"
buffer += "\n"

with open('alphabet.rs', 'w') as d:
    d.write(buffer)
