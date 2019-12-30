(require '[clojure.string :as str])
(require '[clojure.set :as sets])

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


;;  part two

;;   invert orbit dict with every satl in val lists becoming keys
;;  https://stackoverflow.com/questions/15595986/swap-keys-and-values-in-a-map
;;  https://clojure.org/reference/transients
;;   I didn't use the above technique ... the following makes more
;;   sense to me, but may be slow
(defn invert-one-to-many [input-dict output-dict]
  (if (empty? input-dict)    ;; work through dictionary recursively until empty
    output-dict
    (let [dict-entry (first input-dict)]
      (recur
        (dissoc input-dict (key dict-entry))  ;; pop job out of dictionary
        ((fn [satls bary-dict]                ;; assoc satls of the bary as keys
           (if (empty? satls)                 ;; recursively work through satl list
             bary-dict
             (recur
               (pop satls)
               (assoc bary-dict (peek satls) (key dict-entry)))))
         (val dict-entry) output-dict)))))


;;   climb toward COM and keep a dict of {bary, orbit count}
;;   then compare YOU and SAN for the closest commonality
(defn climb-list [inverted-dict-of-satl satellite accum-dict orbit-count]
  (let [d inverted-dict-of-satl
        bary-of-satl (get d satellite)]
    (if (= bary-of-satl "COM")
      accum-dict
      (recur d bary-of-satl (assoc accum-dict bary-of-satl orbit-count) (inc orbit-count)))))


(defn find-path-total [initial-satl-count-dict final-satl-count-dict]
  (let [common-barys
        (sets/intersection (set (keys initial-satl-count-dict))
                           (set (keys final-satl-count-dict)))
        common-barys-initial (select-keys initial-satl-count-dict common-barys)
        common-barys-final (select-keys final-satl-count-dict common-barys)]
    (+ (apply min (vals common-barys-initial)) (apply min (vals common-barys-final)))))


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


;;  testing inverted dictionary...
(println
  (dictionary-of-satellites pairs {}))

(println
  (invert-one-to-many (dictionary-of-satellites pairs {}) {}))


;;  part two
;;   start recursion at 0
;;   "Between the objects they are orbiting - not between YOU and SAN."
;;  testing branch climber
(println
  (climb-list (invert-one-to-many (dictionary-of-satellites pairs {}) {}) "YOU" {} 0))

(println
  (find-path-total
    (climb-list (invert-one-to-many (dictionary-of-satellites pairs {}) {}) "YOU" {} 0)
    (climb-list (invert-one-to-many (dictionary-of-satellites pairs {}) {}) "SAN" {} 0)))


