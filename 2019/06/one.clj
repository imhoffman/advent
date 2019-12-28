(require '[clojure.string :as str])

(def sep #"[)]")     ;; my vim highlighter is reading the guts of the regex!

;;  the input list is called `the orbit map` on advent
;;   produces a vector of barycenter--satellite vector pairs
(defn parse-orbits [orbit-map] 
  (vec (for [listing orbit-map]
         (str/split listing sep))))

(defn bary [pair] (first pair))

(defn satl [pair] (last pair))

;;  for determining direct orbits
;;   call initially with dict as an empty hash map (i.e., dictionary)
(defn direct-orbit-dict [pairs output-dict]
  (let [a (bary (first pairs))]
    (if (empty? a)
      output-dict
      (recur (rest pairs)
             (assoc output-dict a (if (contains? output-dict a) (inc (get output-dict a)) 1)))
      )))

;;  I don't think that this is useful---simply writing it, just in case
(defn indirect-orbit-dict [pairs output-dict]
  (let [a (satl (first pairs))]
    (if (empty? a)
      output-dict
      (recur (rest pairs)
             (assoc output-dict a (if (contains? output-dict a) (inc (get output-dict a)) 1)))
      )))


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


;;  recursive counter ... call initially with accum of zero
;;  depth-first traversal; it is not important that the dictionary
;;  is not sorted; the list from dict-of-satl is the peek/pop work stack
(defn tally-up-children [dict-of-satl children accum]
  (let [child (peek children)]
    (if child
    ;(if (contains? dictionary-of-satellites child) ; is the child/val also a parent/key
      (recur dict-of-satl (pop children) (inc accum))
      accum)))


;; run final sum as something like (apply + (total-tally master-dictionary))
;;  perhaps invoke directly rather than with `get`:
;;  https://clojure.org/guides/learn/hashed_colls#_looking_up_by_key
(defn total-tally [dict-of-satl accum]
    (if (empty? dict-of-satl)
      accum
      (let [parent (first (keys dict-of-satl))
            children (get dict-of-satl parent)]
        (if children     ;; problem ?? ... is this always true because we don't pop them down here ??
          (recur
            dict-of-satl
            (tally-up-children dict-of-satl children accum))
          (recur
            (dissoc dict-of-satl parent)
            accum)))))



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

(println (dictionary-of-satellites pairs {}))

(println
 (total-tally (dictionary-of-satellites pairs {}) 0))


