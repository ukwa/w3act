#!/bin/bash

PDF_URL=$1
BASE_FILE_NAME=$2

wget -O "$BASE_FILE_NAME.pdf" "$PDF_URL"

openssl sha256 "$BASE_FILE_NAME.pdf" | awk '{print $2}' > "$BASE_FILE_NAME.sha256"

pdf2htmlEX --embed-font 0 --process-outline 0 "$BASE_FILE_NAME.pdf"

rm "$BASE_FILE_NAME.pdf"
rm *.woff

sed -i s/@font-face{[^}]*}// "$BASE_FILE_NAME.html"
sed -i "s/\(\.fs.*{font-size:\)\(.*px\)/\1calc(\2 * 0.9)/" "$BASE_FILE_NAME.html"

mkdir -p ../../../html
mv "$BASE_FILE_NAME.html" ../../../html/