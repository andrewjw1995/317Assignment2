1166608, Vishnu Priya Mallela
1215027, Andrew Williamson

Full project available at:
https://github.com/andrewjw1995/317Assignment2


Compile first with ./Compile.sh
Compiles all src files to the bin directory, to keep them separate.


Encode and decode with ./Compress.sh and ./Uncompress.sh



For plain lz78 encoding and decoding with no bit packing:
    cat "Input file" | java -cp bin encoder.LZ78Encoder > "Output file"
    cat "Input file" | java -cp bin encoder.LZ78Decoder > "Output file"

For bit packing only:
    cat "Input file" | java -cp bin bitpacker.Packer > "Output file"
    cat "Input file" | java -cp bin bitpacker.Unpacker > "Output file"
