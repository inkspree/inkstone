#!/bin/bash

sudo apt-get install subversion libxml2-dev xsltproc flex libpcre3-dev gawk

repo="svn://svn.code.sf.net/p/apertium/svn"
svn checkout $repo/trunk /tmp/apertium-$USER
svn checkout $repo/incubator /tmp/apertium-incubator-$USER

cd /tmp/apertium-$USER/trunk/lttoolbox
PKG_CONFIG_PATH=/usr/local/lib/pkgconfig ./autogen.sh
make && sudo make install
sudo ldconfig

cd /tmp/apertium-$USER/trunk/apertium
PKG_CONFIG_PATH=/usr/local/lib/pkgconfig ./autogen.sh
make && sudo make install
sudo ldconfig

which apertium
