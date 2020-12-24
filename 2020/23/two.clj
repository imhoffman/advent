
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def num-cups (inc 9))
(def num-moves 100000)

(def input-short
  (->> "puzzle.txt"
       slurp
       str/trim
       vec
       (mapv #(Long/parseLong (str %)) ,,)))

(prn input-short)

(def input 
  (vec (concat input-short (range (inc (count input-short)) num-cups))))

;(prn input)


;;
;;  utility to help make a coll hot-swappable by elem/index
;;  calls `seq` on its argument
;;  outputs a dictionary of keys/vals
;;   that are the index/elems of the input coll
;;   e.g., [3 4 5] --> {0 3, 1 4, 2 5}
;;  sometimes the inverse of this dictionary is desired in
;;   which case the caller is reponsible for inverting it
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
;;  utility to invert a dictionary-ified coll
;;   intended for use with unique keys and unique vals
;;  there is a set library function for this, too..
;;
(defn inv-coll-to-dict [coll]
  (reduce
    #(assoc %1 (second %2) (first %2))
    {}
    (seq (coll-to-hash-map coll))))



(let [nmoves num-moves]
  (loop [circle  input
         current (first input)
         moves   0]
    (let [ci        (.indexOf circle current)
          wrap      (vec (concat circle circle))
          pickup    (subvec wrap (+ 1 ci) (+ ci 4))
          leave     (vec (concat
                      (subvec circle ci (+ 1 ci))
                      (subvec wrap
                              (+ ci 4)
                              (+ ci 4 (- (count circle) 4)))))
          [dest di] (loop [stack leave
                           d     (if (= 0 (dec current)) (count input) (dec current))]
                      (println " looping...")
                      (let [found (.indexOf stack d)]
                        (cond
                          (empty? stack) (println " crashed looking for destination")
                          (= -1 found) (recur (rest stack) (if (= 0 (dec d)) (count input) (dec d)))
                          :else (vector d (.indexOf leave d)))))
          new-circ  (vec (concat
                      (subvec leave 0 (+ 1 di))
                      pickup
                      (subvec leave (+ 1 di))))
          new-curr  (new-circ (mod (inc (.indexOf new-circ current)) (count new-circ)))]
      (println "     move:" moves)
      (println "  current:" current)
      ;(println "   circle:" circle)
      (println "   pickup:" pickup)
      ;(println "    leave:" leave)
      (println "     dest:" dest)
      ;(println " put down:" new-circ)
      (println " next cur:" new-curr)
      (println)
      (if (= moves (dec nmoves))
        (let [final-wrap (vec (concat new-circ new-circ))
              i1  (.indexOf final-wrap 1)
              out (* (final-wrap (+ 1 i1)) (final-wrap (+ 2 i1)))]
          (prn out))
        (recur new-circ new-curr (inc moves))))))


