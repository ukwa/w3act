#!/bin/bash

pdf_url=$1
base_file_name=$2
ctph_file=$3

wget -O "$base_file_name.pdf" "$pdf_url"
if [ $? -ne 0 ]
then
	rm "$base_file_name.pdf"
	exit 2
fi

openssl sha256 "$base_file_name.pdf" | awk '{print $2}' > "$base_file_name.sha256"
ssdeep -b "$base_file_name.pdf" | tail -n 1 > "$base_file_name.ctp"
cat "$base_file_name.ctp" >> "$ctph_file"

pdf2htmlEX --embed-font 0 --process-outline 0 "$base_file_name.pdf"
exit_code=$?
rm "$base_file_name.pdf"
rm *.woff
if [ $exit_code -ne 0 ]
then
	exit 1
fi

sed -i s/@font-face{[^}]*}// "$base_file_name.html"
sed -i "s/\(\.fs.*{font-size:\)\(.*px\)/\1calc(\2 * 0.9)/" "$base_file_name.html"

mkdir -p ../../../html
mv "$base_file_name.html" ../../../html/
