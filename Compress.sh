#!/bin/bash
for file; do
    echo "Compressing ${file}"
    cat ${file} | java -cp bin encoder.LZ78Encoder | java -cp bin bitpacker.Packer > ${file}.lz78
done 
