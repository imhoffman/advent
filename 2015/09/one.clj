
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
                    (assoc d #{(s 0) (s 2)} (s 4)))))))
          ))
     "puzzle.txt"))

(def set-of-cities
  (set
    (flatten
      (for [s (keys distances-dict)] (apply list s)))))

(def cities-list
  (apply list set-of-cities))

(def num-cities (count cities-list))

;;
;;  could check `frequencies` for suspects, but
;;   might as well loop over all of them
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
;;  utility to help make a coll hot-swappable by elem/index
;;  calls `seq` on its argument
;;  outputs a dictionary of keys/vals
;;   that are the index/elems of the input coll
;;   e.g., [3 4 5] --> {0 3, 1 4, 2 5}
;;
(defn coll-to-hash-map [input-vec]
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
                    (second (first inp)))))))


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
(defn permutations [& xs]
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
            (vector s)))))           ;;  trivial scalar case


(defn cost [city set-of-cities dictionary-of-distances]
  (let [d dictionary-of-distances
        stack-set (if (contains? set-of-cities city)
                    (disj set-of-cities city)
                    set-of-cities)]
    (loop [s   stack-set
           out []]
      (if (empty? stack-set)
        (min)))))





(println " cities:" set-of-cities)
;(println "    number of cities:" num-cities)
;(println " number of distances:" (count amended-distances-dict))
;(doseq [kv amended-distances-dict]
;  (println (key kv) (val kv)))

