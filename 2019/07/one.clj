
(require '[clojure.set :as set])


(defn factorial [n]
  (loop [counter n
         accum   1]
    (if (= counter 0)
      accum
      (recur (dec counter) (* accum counter)))))

;;
;;  calls `seq` on its argument
;;  outputs a dictionary of keys/vals
;;   that are index/elems of the input coll
;;   e.g., [3 4 5] --> {0 3, 1 4, 2 5}
;;
(defn coll-to-hash-map [input-vec]
  (loop [inp (loop [index 0
                    elems (seq input-vec)
                    accum ()]         ;;  assemble a list of pairs
               (if (empty? elems)
                   accum
                   (recur (inc index)
                          (rest elems)
                          (conj accum (list index (first elems))))))
         out {}]
    (if (empty? inp)
      out
      (recur (rest inp)               ;;  assoc the pairs into a dict
             (assoc out
                    (first (first inp))
                    (second (first inp)))))))


;;
;;  utility to remove an item
;;   from a coll by addressing
;;   the item by its index
;;
(defn yoink [coll index]
  (let [input-dict (coll-to-hash-map coll)]
    (vals (dissoc input-dict index))))



;;
;;  input a coll
;;  outputs a list of vectors
;;  each output vector in the list is one
;;   of the permutations
;;  repeated members of the input coll are
;;   treated as independent
;;  the output is not sorted
;;
(defn permutations [s]
  (if (empty? (rest s))
    (vector s)
    (let [s-dict (coll-to-hash-map s)]
      (apply concat
        (for [[k v] s-dict]
          (map
            #(into (vector v) %)
            (permutations (vals (dissoc s-dict k)))))))))




