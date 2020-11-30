
(require '[clojure.string :as str])

(def input-as-vec-of-longs
  (vec (for [c (str/trim (slurp "puzzle.txt"))] (Long/parseLong (str c)))))

(def input-as-vec-of-chars
  (vec (for [c (str/trim (slurp "puzzle.txt"))] c)))

(def input-as-vec-of-shorts
  (vec (for [c (str/trim (slurp "puzzle.txt"))] (Short/parseShort (str c)))))

(println input-as-vec-of-longs)
(println input-as-vec-of-chars)
(println input-as-vec-of-shorts)

;;
;;  the ruleset
;;   returns a vector of longs
;;
(defn look-and-say [vector-of-says]
  (loop [stack vector-of-says
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
          (into [] (concat accum look)))))))

(println (look-and-say input-as-vec-of-longs))
(println (look-and-say input-as-vec-of-chars))
(println (look-and-say input-as-vec-of-shorts))

(def num-iters 40)

(println " length of" num-iters "th iteration:"
  (loop [i 0
         s input-as-vec-of-longs]
    (if (= i num-iters)
      (count s)
      (recur
        (inc i)
        (look-and-say s)))))

