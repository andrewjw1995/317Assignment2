#!/bin/bash
for file; do
    echo "Uncompressing ${file}"
    cat ${file} | java -cp bin bitpacker.Unpacker | java -cp bin encoder.LZ78Decoder > ${file%.lz78}
done 
