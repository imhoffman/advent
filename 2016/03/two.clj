(require '[clojure.string :as str])

;;  retrieve integers from input strings
;;   must parse into a vector of vectors for column-specific addressing by `bundler`
;;   NB `vec` is not the same as `vector`
(defn parser [s]
  (vec (for [r s] (vec
    (for [a (str/split (str/trim r) #"\s+" 3)] (Integer/parseInt a))))))

;;  predicate for valid triples
(defn valid? [t]
  (println "in valid")
  (assert (= 3 (count t)))
  (let [a (first t)
        b (first (rest t))
        c (last t)]
    (println " valid? is considering:" a b c)
    (and (> (+ b c) a)
         (> (+ a c) b)
         (> (+ b a) c))))

;;  part two column-bundler
;user=> (for [i (range 0 16 3)] ((fn [j] (list (+ j 1) (+ j 2) (+ j 3))) i))
;((1 2 3) (4 5 6) (7 8 9) (10 11 12) (13 14 15) (16 17 18))
;user=> (for [a (for [i (range 0 16 3)] ((fn [j] (list (+ j 1) (+ j 2) (+ j 3))) i))] (first a))
;(1 4 7 10 13 16)
;(user=> (for [a (for [i (range 0 16 3)] ((fn [j] (vector (+ j 1) (+ j 2) (+ j 3))) i))] (first a))
;(1 4 7 10 13 16)
(defn bundler [v]
  (assert (= 0 (mod (count v) 3)))
  ;(println " passed to bundler:" v)
  ;(println " a sample output list:"
  ;         (let [i 3]
  ;         (list (get (get v i) 0) (get (get v (+ i 1)) 0) (get (get v (+ i 2)) 0))))
  (for [i (range 0 (mod (count v) 3) 3)]
    (reduce conj ()    ; HERE --- need to unpack this list of lists ... perhaps `map`
           (list (get (get v i) 0) (get (get v (+ i 1)) 0) (get (get v (+ i 2)) 0)))))

;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def input (reverse             ; conj appends to front of list
  (with-open [f (clojure.java.io/reader "input.txt")]
    (reduce conj () (line-seq f)))))
(println " number of lines read from input file:" (count input))

;(println " number of valid listings:" (get (frequencies (for [u (parser input)] (valid? u))) true))
(println " number of valid listings:"
         (get (frequencies
                (for [u (bundler (parser input))]
                  (valid? u))) true))

(println " output from parser:")
(println (parser input))

(println " output from bundler:")
(println (bundler (parser input)))

