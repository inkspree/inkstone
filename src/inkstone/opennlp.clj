(ns inkstone.opennlp
  (:require [opennlp.nlp :as nlp])
  (:use [inkstone.curry :only [defcur cur]]))

(comment "TOKENIZERS")

(defonce ^:private en-tokenize!
  (nlp/make-tokenizer
   (clojure.java.io/resource "models/opennlp/en-token.bin")))

(defonce ^:private de-tokenize!
  (nlp/make-tokenizer
   (clojure.java.io/resource "models/opennlp/de-token.bin")))

(def ^:private tokenize-lang
  {:en en-tokenize!
   :de de-tokenize!})

(defcur tokenize
  [lang text]
     ((tokenize-lang lang) text))
