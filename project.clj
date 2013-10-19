(defproject inkstone "0.1.0"
  :description "In-context spaced repetition learning."
  :url "http://inkstone.inkspree.com"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :main inkstone.core
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.nrepl "0.2.3"]
                 [org.clojure/tools.cli "0.2.4"]
                 [clojure-opennlp "0.3.1"]])
