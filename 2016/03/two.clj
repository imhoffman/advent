(require '[clojure.string :as str])

;;  retrieve integers from input strings
;;   must parse into a vector of vectors for column-specific addressing by `bundler`
;;   NB `vec` is not the same as `vector`
(defn parser [s]
  (vec (for [r s] (vec
    (for [a (str/split (str/trim r) #"\s+" 3)] (Integer/parseInt a))))))

;;  predicate for valid triples
(defn valid? [t]
  (assert (= 3 (count t)))
  (let [a (first t)
        b (first (rest t))
        c (last t)]
    ;(println " valid? is considering:" a b c)
    (and (> (+ b c) a)
         (> (+ a c) b)
         (> (+ b a) c))))

;;  part two column-bundler
;;   b/c `bundler` makes a list of lists every three rows
(defn unpacker [r accum]
  (if (empty? (rest r))
    (conj accum (first r))
    (unpacker (rest r) (conj accum (first r)))))


;;  repackage triangle triples from column chunks
;;   using `get` from a `get` but could also `let` a 2d array and `aget` from it
(defn bundler [v]
  (assert (= 0 (mod (count v) 3)))
  (let [w
    (for [i (range 0 (count v) 3) j (range 0 3)]
      (list
        (get (get v i) j) (get (get v (+ i 1)) j) (get (get v (+ i 2)) j)))]
  (for [x w] (unpacker x ()))))


;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
;;    `reverse` b/c `conj` appends to front of list
;;    although order doesn't matter in this puzzle
(def input (reverse
  (with-open [f (clojure.java.io/reader "input.txt")]
    (reduce conj () (line-seq f)))))
(println " number of lines read from input file:" (count input))

(println " number of valid listings:"
         (get (frequencies
                (for [u (bundler (parser input))]
                  (valid? u))) true))

