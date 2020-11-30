
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
      (vec accum)
      (let [look (loop [s stack
                        c 0]
                   (if (or
                         (empty? s)
                         (> 2 (count s))
                         (not= (s 0) (s 1)))
                     (vector (inc c) (first stack))
                     (recur
                       (vec (rest s))
                       (inc c))))]
        (recur
          (vec (nthrest stack (look 0)))
          (concat accum look))))))

;(println (look-and-say input-as-vec-of-longs))

(println " length of 40th iteration:"
(loop [i 0
       s input-as-vec-of-longs]
  (if (= i 40)
    (count s)
    (recur
      (inc i)
      (look-and-say s))))
)

