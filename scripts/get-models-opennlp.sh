#!/bin/bash

mkdir -p ../resources/models/opennlp/
url="http://opennlp.sourceforge.net/models-1.5"
function get() {
  command wget $url/$@ -O ../resources/models/opennlp/$@
}

get en-token.bin
get en-sent.bin
get en-pos-maxent.bin
get en-pos-perceptron.bin

get de-token.bin
get de-sent.bin
get de-pos-maxent.bin
get de-pos-perceptron.bin
