(ns inkstone.morph
  (:use [clojure.java.shell :only [sh]]))

(defn- raw-parse-lex
  [lex]
  (or
   (second (clojure.string/split lex #"[/$]"))
   (clojure.string/trim-newline lex)))

(defn- struct-parse-lex
  [lex]
  (let [result (not-empty
                (filter seq
                        (clojure.string/split (raw-parse-lex lex) #"[<>]")))
        [stem & morph] result]
    [stem (map keyword morph)]))

(def ^:private mparse-lex
  {:struct struct-parse-lex
   :raw raw-parse-lex})

(defn- build-raw-lex
  [structured]
  (str (first structured)
       (clojure.string/join (map #(str "<" (name  %) ">")
                                 (second structured)))))

(defn- direction [from to] (str (name from) "-" (name to)))

(defn- apertium-dict
  [from to word]
  (sh "apertium" (direction from to) :in word))

(defn- apertium-proc
  [lang type word]
  (let [proc-tool (str "resources/lang/" (name lang)
                       "/lttoolbox/" (name lang) "." (name type) ".bin")]
    (if (= type :generator)
      (sh "lt-proc" "-g" proc-tool :in word)
      (sh "lt-proc" proc-tool :in (str word "\n")))))

(defn analyze
  ([lang word] (analyze lang :struct word))
  ([lang mode word]
     (let [output (apertium-proc lang :analyzer word)
           result (:out output)]
       (or
        (and
         (clojure.string/blank? (:err output))
         result
         ((mode mparse-lex) result))
        (println
         (str "Couldn't analyze '" word "' (lang: " (name lang) ")"))))))

(defn stem
  [lang word]
  (first (analyze lang word)))

(defn generate
  [lang analyzed]
  (if (seq (second analyzed))
    ((comp clojure.string/trim-newline :out)
     (apertium-proc lang :generator (str "^" (build-raw-lex analyzed) "$\n")))
    (first analyzed)))

(defn- pick-translation
  [from to candidate not-stem original-word dict-fn]
  (let [unknown (re-find #"[*@]" candidate)
        known (re-find #"[#]" candidate)
        sigil-less (subs candidate 1)]
    (or
     (when (and known unknown)
       (dict-fn from to (second (clojure.string/split candidate #"@")) false))
     (when (and not-stem unknown (not= original-word sigil-less))
       (let [stemmed (stem from original-word)]
         (when-not (= stemmed candidate)
           (dict-fn from to stemmed false))))
     (cond
      unknown nil
      known (generate to (analyze to (subs candidate 1)))
      (seq candidate) candidate))))

(defn dict
  ([from to word] (dict from to word true))
  ([from to word not-stem]
     (let [result (apertium-dict from to word)]
       (or
        (and
         (clojure.string/blank? (:err result))
         (pick-translation from to (:out result) not-stem word dict))
        (println 
         (str "Couldn't translate '" word "' (lang: " (name from) ")"))))))
