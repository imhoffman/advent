(require '[clojure.string :as str])

;;  retrieve integers from input strings
(defn parser [s]
  (for [r s]
    (for [a (str/split (str/trim r) #"\s+" 3)] (Integer/parseInt a))))

;;  predicate for valid triples
(defn valid? [t]
  (assert (= 3 (count t)))
  (let [a (first t)
        b (first (rest t))
        c (last t)]
    (and (> (+ b c) a)
         (> (+ a c) b)
         (> (+ b a) c))))

;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def input
  (with-open [f (clojure.java.io/reader "input.txt")]
    (reduce conj () (line-seq f))))
(println " number of lines read from input file:" (count input))

(println " number of valid listings:" (get (frequencies (for [u (parser input)] (valid? u))) true))

