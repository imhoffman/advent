(require '[clojure.string :as str])
(require '[clojure.set :as sets])

(def sep #"[)]")     ;; my vim highlighter is reading the guts of the regex!

;;  the input list is called `the orbit map` on advent
;;   parses out the parenthesis notation in the puzzle input
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
  (let [b (bary (first pairs))
        s (satl (first pairs))]
    (if (empty? b)
      output-dict
      (recur (rest pairs)
             (assoc output-dict b
                    (if (contains? output-dict b)
                      (conj (get output-dict b) s)
                      (list s)))))))

;;  part two

;;   invert orbit dict with every satl in val lists becoming keys
;;  https://stackoverflow.com/questions/15595986/swap-keys-and-values-in-a-map
;;   I didn't use the above technique ... the following makes more
;;   sense to me, but it may be slow
(defn invert-one-to-many [input-dict output-dict]
  (if (empty? input-dict)    ;; work through dict stack recursively until empty
    output-dict
    (let [dict-entry (first input-dict)]
      (recur
        (dissoc input-dict (key dict-entry))  ;; pop job out of dictionary
        ((fn [satls bary-dict]                ;; lambda to assoc satls of the bary as keys
           (if (empty? satls)                 ;; recursively work through satl list
             bary-dict
             (recur
               (pop satls)
               (assoc bary-dict (peek satls) (key dict-entry)))))
         (val dict-entry) output-dict)))))


;;   climb toward COM and keep a dict of {bary, orbit count}
;;   for later comparison b/w YOU and SAN for the closest commonality
(defn climb-list [inverted-dict-of-satl satellite accum-dict orbit-count]
  (let [d inverted-dict-of-satl
        bary-of-satl (get d satellite)]
    (if (= bary-of-satl "COM")
      accum-dict
      (recur d bary-of-satl (assoc accum-dict bary-of-satl orbit-count) (inc orbit-count)))))


;;  Of the barycenters that the initial and final nodes have in common, the ones
;;  in the respective dicts that are furthest from COM on their path
;;  will have the smallest orbit-count val from `climb-list`, thus find the `min`.
;;  However, `min` is likely O(n) and there may be a creative way to garauntee that
;;  the commonality of interest is at the beginning or end of the `select`ed dict
;;  below, and thus be O(1) ... or speed up `min` with transients?
;;   https://clojure.org/reference/transients
;;  start recursion at 0
;;   "Between the objects they are orbiting - not between YOU and SAN."
(defn find-path-total [inverted-dict initial-satl final-satl]
  (let [initial-satl-count-dict (climb-list inverted-dict initial-satl {} 0)
        final-satl-count-dict   (climb-list inverted-dict   final-satl {} 0)
        common-barys (sets/intersection (set (keys initial-satl-count-dict))
                                        (set (keys final-satl-count-dict)))
        common-barys-initial  (select-keys initial-satl-count-dict common-barys)
        common-barys-final    (select-keys final-satl-count-dict   common-barys)]
    (+ (apply min (vals common-barys-initial)) (apply min (vals common-barys-final)))))


;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj () (line-seq f))))             ;; order doesn't matter, so ()
(println "Read" (count input) "lines.")

;;  load up vector of pairs for processing going forward
(def pairs (parse-orbits input))

;;  part two
;;   from the CLI
;;   https://clojure.org/reference/repl_and_main
(let [argv *command-line-args*]
  (if (not (= 2 (count argv)))
    (do
      (println " usage: clj two.clj INITIAL FINAL")   ;; $ clj two.clj YOU SAN
      (System/exit 1))
    (let [initial (first argv)
          final   (second argv)]
      (println
        " minimum number of moves from" initial "to" final ":"
        (find-path-total
          (invert-one-to-many
            (dictionary-of-satellites pairs {}) {})
          initial final)))))

;;
;; What are the ways around me having to pass an empty accumulator when calling a tail-
;; recursive function?  (into {} map ... ?   (reduce ... ?
;; I like having the argument be passed in, but is it slow or worse somehow?

