(ns inkstone.opennlp
  (:require opennlp.nlp))

(comment "TOKENIZERS")

(defn tokenize
  [lang text]
  ((tokenize-lang lang) text))

(def ^:private tokenize-lang
  {:en en-tokenize
   :de de-tokenize})

(def ^:private en-tokenize
  (make-tokenizer
   (.getFile (clojure.java.io/resource "models/opennlp/en-token.bin"))))

(def ^:private de-tokenize
  (make-tokenizer
   (.getFile (clojure.java.io/resource "models/opennlp/de-token.bin"))))
