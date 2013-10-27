#!/bin/bash

mkdir -p ../resources/lang/en/opennlp-models/
mkdir -p ../resources/lang/de/opennlp-models/
url="http://opennlp.sourceforge.net/models-1.5"
function get() {
  command wget $url/$1-$2 -O ../resources/lang/$1/opennlp-models/$1-$2
}

get en token.bin
get en sent.bin
get en pos-maxent.bin
get en pos-perceptron.bin

get de token.bin
get de sent.bin
get de pos-maxent.bin
get de pos-perceptron.bin
