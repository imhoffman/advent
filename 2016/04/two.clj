(def constants {
   :alpha "abcdefghijklmonpqrstuvwxyz",
   :nums "0123456789",
   :max_lines 8192
   } )

(def registry
  (with-open [f (clojure.java.io/reader "input.txt")]
    (reduce conj () (line-seq f))))

(println (count registry))
