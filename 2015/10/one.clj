
(require '[clojure.string :as str])

(def input-as-vec-of-longs
  (vec (for [c (str/trim (slurp "puzzle.txt"))] (Long/parseLong (str c)))))

(println input-as-vec-of-longs)

;;
;;  the ruleset
;;   returns a vector of longs
;;
(defn look-and-say [vector-of-longs]
  (loop [stack vector-of-longs
         accum []]
    (if (empty? stack)
      accum
      (let [look (loop [s stack
                        c 0]
                   (println " s:" s ", c:" c)
                   (if (or
                         (= (inc c) (count s))
                         (not= (s c) (s (inc c))))
                     (vector (inc c) (first stack))
                     (recur
                       (vec (rest s))
                       (inc c))))
            say  (let [digit (look 1)
                       times (look 0)]
                   (loop [c   0
                          out []]
                     (if (= c times)
                       out
                       (recur
                         (inc c)
                         (conj out digit)))))]
        (recur
          (vec (nthrest stack (look 0)))
          (concat accum look))))))

(println (look-and-say input-as-vec-of-longs))

