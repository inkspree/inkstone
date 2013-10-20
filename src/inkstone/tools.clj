(ns inkstone.tools)

(defn char-at [pos string] (.charAt string pos))

(defn expand-file-name
  [path]
  (case (char-at 0 path)
    \/ path
    \~ (str (System/getProperty "user.home") (subs path 1))
    (.getCanonicalPath (java.io.File. path))))

(defn- curry
  [[params1 params2] body]
  (cons (vec params1)
        (if (empty? params2)
          body
          (list (apply list 'fn (vec params2) body)))))

(defn do-curried [symbol to-fn params]
  (let [result (split-with (complement vector?) params)
        [[name doc meta] [args & body]] result
        [doc meta] (if (string? doc) [doc meta] [nil doc])
        body (if meta (cons meta body) body)
        arity-for-n #(-> % inc (split-at args) (to-fn body))
        arities (->>
                 (range 0 (count args))
                 (map arity-for-n)
                 reverse)
        before (keep identity [symbol name doc])]
    (concat before arities)))

(defmacro defcur
  "Builds a multiple arity function similar that returns closures
          for the missing parameters, similar to ML's behaviour."
  [& params]
  (do-curried 'defn curry params))

(defmacro cur
  "Builds a multiple arity function similar that returns closures
          for the missing parameters, similar to ML's behaviour."
  [& params]
  (do-curried 'fn curry params))
