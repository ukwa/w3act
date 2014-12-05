#!/bin/bash

# Verified: works on CentOS 6.5 x86_64.

#(deps)
echo "Installing Dependencies"
yum -y install cmake libtool libpng-devel libjpeg-devel fontconfig-devel xz unzip yum gettext wget libtool-ltdl libtool-ltdl-devel glib2 glib2-devel libxml2-devel java-1.7.0-openjdk

#(installing gcc 4.8.x from devtools repository)
wget -O /etc/yum.repos.d/devtools-2.repo http://people.centos.org/tru/devtools-2/devtools-2.repo
yum -y install devtoolset-2-gcc devtoolset-2-binutils devtoolset-2-gcc-gfortran devtoolset-2-gcc-c++
ln -s /opt/rh/devtoolset-2/root/usr/bin/* /usr/local/bin/
hash -r

#(tmpdir)
echo "Preparing Directory"
mkdir -p /tmp/code
cd /tmp/code

#(resources)
echo "Downloading Sources"
wget http://poppler.freedesktop.org/poppler-0.26.3.tar.xz
wget http://poppler.freedesktop.org/poppler-data-0.4.6.tar.gz
wget -O fontforge-pdf2htmlEX.zip https://github.com/coolwanglu/fontforge/archive/pdf2htmlEX.zip
wget https://github.com/coolwanglu/pdf2htmlEX/archive/v0.12.tar.gz

tar xfv poppler-0.26.3.tar.xz
tar xfv poppler-data-0.4.6.tar.gz
unzip fontforge-pdf2htmlEX.zip
tar xfv v0.12.tar.gz

#(Installing)
echo "Installing"
cd poppler-0.26.3
export PKG_CONFIG_PATH=/usr/local/lib/pkgconfig
./configure "--enable-xpdf-headers"
make
make install

cd ../poppler-data-0.4.6
export PKG_CONFIG_PATH=/usr/local/lib/pkgconfig
cmake .
make
make install

cd ../fontforge-pdf2htmlEX
export PKG_CONFIG_PATH=/usr/local/lib/pkgconfig
sed -i 's/2.68/2.63/' configure.ac
./autogen.sh
./configure "--without-libzmq" "--without-x" "--without-iconv" "--disable-python-scripting" "--disable-python-extension"
make
make install

cd ../pdf2htmlEX-0.12
export PKG_CONFIG_PATH=/usr/local/lib/pkgconfig
export CPATH=/usr/local/include:/usr/include:/usr/include/glib-2.0/:/usr/lib64/glib-2.0/include
cmake .
make
make install

echo /usr/local/lib > /etc/ld.so.conf.d/local.conf
ldconfig

echo -e "\n\n=====\n\n"
pdf2htmlEX --version
echo -e "\n\n=====\n\n"