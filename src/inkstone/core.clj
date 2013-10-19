(ns inkstone.core
  (:use [inkstone.opennlp]
        [inkstone.curry :only [defcur cur]]
        [clojure.tools.nrepl.server :only [start-server stop-server]]
        [clojure.tools.cli :only [cli]])
  (:gen-class :main true))

(defonce server (start-server :port 7888))

(defn- parse-cmdline-args
  [args]
  (cli args
     ["-s" "--sentences" :flag true :default false]))

(defn -main
  [& args]
  (let [v (parse-cmdline-args args)
        p (first v)
        text (second v)
        output (cond
                (:sentences p) "sentences you want"
                :default (tokenize :en (clojure.string/join " " text)))]
   (println output)
   (System/exit 0)))
