#!/bin/bash

ctph_file=$1

if [ ! -f $ctph_file ]
then
	exit 1
fi

echo 'ssdeep,1.1--blocksize:hash:hash,filename' | cat - "$ctph_file" > temp && mv temp "$ctph_file"

mkdir -p ../../hashes
if [ "$(ls -A ../../hashes)" ]
then
	for file in ../../hashes/*
	do
		ssdeep -k "$file" "$ctph_file" >> "$ctph_file.out"
	done
fi
mv "$ctph_file" ../../hashes/
