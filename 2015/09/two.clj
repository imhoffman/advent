
(require '[clojure.string :as str])

(def distances-dict
  ((fn [filename]
     (->> filename
          slurp
          (#(str/split % #"[\r\n|\n|\r]"))
          (map #(str/split % #"\s"))
          (#(loop [e %, d {}]
              (if (empty? e)
                d
                (recur
                  (rest e)
                  (let [s (first e)]
                    (assoc d #{(s 0) (s 2)} (Long/parseLong (s 4))))))))
          ))
     "puzzle.txt"))

;;
;;  ensure that a lack of edges does not crash the search
;;  enter "infinity" for cities that are not connected
;;   could check `frequencies` for suspects, but
;;    might as well loop over all of them
;;  my input has a complete set of pairs, but perhaps
;;   this is not true in general
;;
(defn add-missing-entries [distances-dict]
  (let [set-of-cities (set (flatten
                        (for [s (keys distances-dict)] (apply list s))))
        cities-list   (apply list set-of-cities)
        num-cities    (count set-of-cities)]
  (if (= (count distances-dict)
         (/ (- (* (count cities-list) (count cities-list))
               (count cities-list))
            2))
    distances-dict
    (let [inf 999999]
      (loop [d distances-dict
             c cities-list]
        (if (empty? c)
          d
          (recur
            (loop [stack  c
                   out    d]
              (if (empty? stack)
                out
                (recur
                  (rest stack)
                  (loop [r (rest stack)
                         o out]
                    (if (empty? r)
                      o
                      (let [test-set (set (list (first stack) (first r)))]
                        (if (contains? (set (keys o)) test-set)
                          o
                          (assoc o test-set inf))))))))
            (rest c))))))))

(def amended-distances-dict (add-missing-entries distances-dict))


;;
;;  permutations
;;
;;  input a coll (or a singleton)
;;  outputs a list of vectors (or a list of a singleton)
;;  each vector in the output is one of the permutations
;;  repeated members of the input coll are treated as independent
;;  colls that are members of the input coll are left unchanged
;;  the output is not sorted
;;  the library version differs in some edge/trivial cases, so
;;   I rolled my own
;;
;;  letfn `coll-to-hash-map`
;;  utility to help make a coll hot-swappable by elem/index
;;  calls `seq` on its argument
;;  outputs a dictionary of keys/vals
;;   that are the index/elems of the input coll
;;   e.g., [3 4 5] --> {0 3, 1 4, 2 5}
;;
(defn permutations [& xs]
  (letfn [(coll-to-hash-map [input-vec]
             (loop [inp (loop [index 0
                               elems (seq input-vec)
                               accum ()]
                          (if (empty? elems)
                            accum
                            (recur (inc index)
                                   (rest elems)
                                  (conj accum (list index (first elems))))))
                    out {}] 
               (if (empty? inp)
                 out
                 (recur (rest inp)
                        (assoc out
                               (first (first inp))
                               (second (first inp)))))))]
  (cond
    (empty? xs) (list (vector))    ;;  trivial empty case
    :else
      (let [s (first xs)]          ;;  ignore additional inputs
        (cond
          (coll? s)                ;;  the typical use
            (if (empty? (rest s))
              (vector s)
              (let [s-dict (coll-to-hash-map s)]
                (apply concat 
                       (for [[k v] s-dict]
                         (map
                           #(into (vector v) %)
                           (permutations (vals (dissoc s-dict k))))))))
          :else 
            ;;  (permutations 3)   -->  (3)
            ;;  (permutations [3]) -->  [[3]]
            (vector s))))))          ;;  trivial scalar case


;;  the total distance of any one sequence of cities
(defn cost [vector-of-cities dictionary-of-distances]
  (apply + (for [pair (partition 2 1 vector-of-cities)] (dictionary-of-distances (set pair)))))


(def set-of-cities
  (set
    (flatten
      (for [s (keys distances-dict)] (apply list s)))))


;;  build a dictionary with key/vals of cost/cities
;;   uses set-of-cities and amended-distances-dict
(def dictionary-of-costs
  (loop [p   (permutations set-of-cities)
         out (transient {})]
    (if (empty? p)
      (persistent! out)
      (recur
        (rest p)
        (assoc! out (cost (first p) amended-distances-dict) (first p))))))


(let [d dictionary-of-costs
      m1 (apply min (keys d))
      m2 (apply max (keys d))]
  (println " the minimum distance of" m1 "is for" (d m1))
  (println " the maximum distance of" m2 "is for" (d m2)))



