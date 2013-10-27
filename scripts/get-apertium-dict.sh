#!/bin/bash

cd /tmp/apertium-incubator-$USER/apertium-$1-$2
PKG_CONFIG_PATH=/usr/local/lib/pkgconfig ./autogen.sh
make && sudo make install

function compile() {
  bin="$1.$4.bin"
  dix="apertium-$1-$2.$1.dix"
  lt-comp $3 $dix $bin && mv $bin ~-/../resources/lang/$1/lttoolbox/
}

compile $1 $2 lr analyzer
compile $1 $2 rl generator

compile $2 $1 lr analyzer
compile $2 $1 rl generator
