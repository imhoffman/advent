(require '[clojure.string :as str])

(def constants {
   :alpha "abcdefghijklmonpqrstuvwxyz",
   :nums "0123456789",
   :max_lines 8192
   } )

(def registry
  (with-open [f (clojure.java.io/reader "input.txt")]
    (reduce conj () (line-seq f))))

(println (count registry))

(def total 0)

(doseq [s registry]
  (doseq [a s]
    (

(doseq [s strg] (if (> (or (str/index-of nums s) -2) -1) (do (def id (Integer/parseInt (str s))) (println id))))
