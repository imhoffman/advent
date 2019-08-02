(require '[clojure.string :as str])

;;  retrieve integers from input strings
(defn parser [s]
  (vec (for [r s] (vec           ; parse into vectors for column-specific addressing by `bundler`
    (for [a (str/split (str/trim r) #"\s+" 3)] (Integer/parseInt a))))))

;;  predicate for valid triples
(defn valid? [t]
  (assert (= 3 (count t)))
  (let [a (first t)
        b (first (rest t))
        c (last t)]
    (and (> (+ b c) a)
         (> (+ a c) b)
         (> (+ b a) c))))

;;  part two column-bundler
(defn bundler [v]
  (assert (= 0 (mod (count v) 3)))
  (list
  (loop [i 0]
  ;(doseq [i (range 0 (- (mod (count v) 3) 3) 3)]
    ;(let [t1 (list (get v i 0) (get v (+ i 1) 0) (get v (+ i 2) 0))
    ;      t2 (list (get v i 1) (get v (+ i 1) 1) (get v (+ i 2) 1))
    ;      t3 (list (get v i 2) (get v (+ i 1) 2) (get v (+ i 2) 2))]
    ;  t1 t2 t3
      (list (get v i 0) (get v (+ i 1) 0) (get v (+ i 2) 0))
      (list (get v i 1) (get v (+ i 1) 1) (get v (+ i 2) 1))
      (list (get v i 2) (get v (+ i 1) 2) (get v (+ i 2) 2))
      (if (not (< i (mod (count v) 3))) (recur (+ i 3))))))

;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def input
  (with-open [f (clojure.java.io/reader "input.txt")]
    (reduce conj () (line-seq f))))
(println " number of lines read from input file:" (count input))

;(println " number of valid listings:" (get (frequencies (for [u (parser input)] (valid? u))) true))
(println " number of valid listings:"
         (get (frequencies
                (for [u (bundler (parser input))]
                  (valid? u))) true))

