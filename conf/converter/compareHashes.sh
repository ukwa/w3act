#!/bin/bash

ctph_file=$1

echo 'ssdeep,1.1--blocksize:hash:hash,filename' | cat - "$ctph_file" > temp && mv temp "$ctph_file"

mkdir -p ../../hashes
if [ "$(ls -A ../../hashes)" ]
then
	for file in ../../hashes/*
	do
		ssdeep -k "$file" "$ctph_file" >> matches.out
	done
fi
mv "$ctph_file" ../../hashes/
