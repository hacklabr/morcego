#!/bin/bash

RPC_VERSION=1.5.1
PARSER_VERSION=1.2.8
XAJAX_VERSION=0.5_beta_4b_Standard
SMARTY_VERSION=2.6.18

mkdir templates_c
chmod 777 templates_c

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

echo '*** NOW COPY morcego-0.6.0-dev.jar TO HERE! ***'

