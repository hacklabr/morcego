#!/bin/bash

##
#
# Backup demo, so that we restore it after packing
# 

cp -a demo demo_bak

##
# 
# Download the libs necessary to demo applicatoin
#

RPC_VERSION=1.5.1
PARSER_VERSION=1.2.8
XAJAX_VERSION=0.5_beta_4b_Standard
SMARTY_VERSION=2.6.18
PROTOTYPE_DATE=2007/10/16

cd demo

mkdir lib
cd lib

mkdir tmp
mkdir XML

cd tmp

wget http://download.pear.php.net/package/XML_RPC-$RPC_VERSION.tgz
wget http://download.pear.php.net/package/XML_Parser-$PARSER_VERSION.tgz

tar zxvf XML_RPC-$RPC_VERSION.tgz
tar zxvf XML_Parser-$PARSER_VERSION.tgz

mv XML_RPC-$RPC_VERSION/* ../XML
mv XML_Parser-$PARSER_VERSION/* ../XML

cd ..
rm -rf tmp

mkdir xajax
cd xajax
wget http://downloads.sourceforge.net/xajax/xajax_$XAJAX_VERSION.zip?modtime=1189446446\&big_mirror=0

unzip xajax_$XAJAX_VERSION.zip
rm xajax_$XAJAX_VERSION.zip
cd ..

wget http://smarty.php.net/do_download.php?download_file=Smarty-$SMARTY_VERSION.tar.gz
tar zxf Smarty-$SMARTY_VERSION.tar.gz
rm Smarty-$SMARTY_VERSION.tar.gz
mv Smarty-$SMARTY_VERSION Smarty

wget http://www.prototypejs.org/assets/$PROTOTYPE_DATE/prototype.js

cd ../..

##
#
# Get Morcego version from pom.xml and update demo application
#

VERSION=`perl -e 'print $1 if \`cat pom.xml\` =~ m|<version>\s*(.+?)\s*</version>|s'`

find demo -type f -exec perl -pi -e "s/\\$\{VERSION\}/$VERSION/g" {} \;

##
#
# Generate a piece of src/assemble/binary.xml according to libraries downloaded
#

echo "PLEASE UPDATE src/assemble/binary.xml TO CONTAIN FOLLOWING FILES, ctrl+d to continue\n";

find demo php -type d | grep -v \.svn |perl -ple 'chomp; s|^(.+)$|        <include>$1/*</include>|'

cat;

##
#
# Pack everything according to src/assemble/*.xml and cleanup
#

mvn assembly:assembly

rm -rf demo
mv demo_bak demo

