(ns inkstone.opennlp
  (:require [opennlp.nlp :as nlp]
            [opennlp.tools.lazy :as lazy]
            [opennlp.tools.filters :as filter]))

(def nouns (comp filter/nouns second))
(def verbs (comp filter/verbs second))
(def proper-nouns (comp filter/proper-nouns second))
(def nouns-and-verbs (comp filter/nouns-and-verbs second))

(filter/pos-filter raw-simple-concepts #"^[NVJ]")
(filter/pos-filter raw-concepts #"^[NVJRPMW]")
(def simple-concepts (comp raw-simple-concepts second))
(def concepts (comp raw-concepts second))

(def lazy-sentence-seq lazy/sentence-seq)

(defn- should-partially-apply
  "In this context, deciding to partially apply is simply a matter of
   checking if we got a single keyword for argument. That represents
   the language, and so tells us to return a function with the
   language partially applied."
  [k]
  (= k (keyword k)))

(comment "TOKENIZERS")

(defonce ^:private en-tokenize!
  (nlp/make-tokenizer
   (clojure.java.io/resource "lang/en/opennlp-models/en-token.bin")))

(defonce ^:private de-tokenize!
  (nlp/make-tokenizer
   (clojure.java.io/resource "lang/de/opennlp-models/de-token.bin")))

(def ^:private ltokenize
  {:en en-tokenize!
   :de de-tokenize!})

(defn tokenize
  ([lang text] [lang ((lang ltokenize) text)])
  ([v] (if (should-partially-apply v)
           (v ltokenize)
           (tokenize (first v) (second v)))))

(comment "SENTENCERS")

(defonce ^:private en-get-sentences!
  (nlp/make-sentence-detector
   (clojure.java.io/resource "lang/en/opennlp-models/en-sent.bin")))

(defonce ^:private de-get-sentences!
  (nlp/make-sentence-detector
   (clojure.java.io/resource "lang/de/opennlp-models/de-sent.bin")))

(def ^:private lget-sentences
  {:en en-get-sentences!
   :de de-get-sentences!})

(defn get-sentences
  ([lang text] [lang ((lang lget-sentences) text)])
  ([v] (if (should-partially-apply v)
           (v lget-sentences)
           (get-sentences (first v) (second v)))))

(comment "POS-TAGGERS")

(defonce ^:private en-pos-tag!
  (nlp/make-pos-tagger
   (clojure.java.io/resource "lang/en/opennlp-models/en-pos-perceptron.bin")))

(defonce ^:private de-pos-tag!
  (nlp/make-pos-tagger
   (clojure.java.io/resource "lang/de/opennlp-models/de-pos-perceptron.bin")))

(def ^:private lpos-tag
  {:en en-pos-tag!
   :de de-pos-tag!})

(defn pos-tag
  ([lang text] [lang ((lang lpos-tag) text)])
  ([v] (if (should-partially-apply v)
           (v lpos-tag)
           (pos-tag (first v) (second v)))))
