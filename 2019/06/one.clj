(require '[clojure.string :as str])

(def sep #"[)]")     ;; my vim highlighter is reading the guts of the regex!

;;  the input list is called `the orbit map` on advent
;;   produces a vector of barycenter--satellite vector pairs
(defn parse-orbits [orbit-map] 
  (vec (for [listing orbit-map]
         (str/split listing sep))))

(defn bary [pair] (first pair))

(defn satl [pair] (last pair))

;;  key = bary, val = list of satl
;;   the list in val will be recursively traversed by `tally`
;;   as the work stack
(defn dictionary-of-satellites [pairs output-dict]
  (let [a (bary (first pairs))
        b (satl (first pairs))]
    (if (empty? a)
      output-dict
      (recur (rest pairs)
             (assoc output-dict a
                    (if (contains? output-dict a)
                      (conj (get output-dict a) b)
                      (list b)))))))


;; run final sum as something like
;;  (apply + (for [s (keys dict)] (bary-tally dict (get dict s) 1)))
(defn bary-tally [dict-of-satl children accum]
  (let [child (peek children)]    ;; the list of satl's is the job stack
    (if child
      (if (contains? dict-of-satl child) ; is the child/val also a parent/key ?
       (recur
         dict-of-satl
         (get dict-of-satl child)    ;; investigate the child's satl list
         (inc accum))
        (recur
         dict-of-satl
         (pop children)
         (inc accum)))
      accum)))   ; if nothing is orbiting the child: end of the branch


;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj () (line-seq f))))
(println "Read" (count input) "lines.")

;;  load up vector of pairs for processing going forward
(def pairs (parse-orbits input))

;(println (dictionary-of-satellites pairs {}))

;;  start accumulator at 1 since if the body is a key, it must be a bary
;;  and have at least one satellite
(println
  " part one answer:"
  (apply +
        (let [d (dictionary-of-satellites pairs {})]
          (for [s (keys d)] (bary-tally d (get d s) 1)))))

;;  20014 is too low
