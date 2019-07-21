(def registry
  (with-open [f (clojure.java.io/reader "input.txt")]
    (reduce conj () (line-seq f))))

(println (count registry))
