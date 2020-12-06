
(require '[clojure.string :as str])

(def input-happiness-vecs
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       (map #(re-matches #"(\w+) \w+ (\w+) (\d+).*\s(\w+)." %))
       (mapv rest)))

(println input-happiness-vecs)


(def happy-dict
  (loop [vs input-happiness-vecs
         out {}]))


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


