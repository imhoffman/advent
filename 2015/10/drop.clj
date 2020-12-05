
(require '[clojure.string :as str])

(def input-as-vec-of-longs
  (vec (for [c (str/trim (slurp "puzzle.txt"))] (Long/parseLong (str c)))))

(def input-as-vec-of-chars
  (vec (for [c (str/trim (slurp "puzzle.txt"))] c)))

(def input-as-vec-of-shorts
  (vec (for [c (str/trim (slurp "puzzle.txt"))] (Short/parseShort (str c)))))

;(println input-as-vec-of-longs)
;(println input-as-vec-of-chars)
;(println input-as-vec-of-shorts)

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
          (vec (drop (look 0) stack))
          ;(vec (nthrest stack (look 0)))
          ;;  without `into []` here, I run out of stack space (?)
          (into [] (concat accum look)))))))

;(println (look-and-say input-as-vec-of-longs))
;(println (look-and-say input-as-vec-of-chars))
;(println (look-and-say input-as-vec-of-shorts))

(def max-iters 51)

(let [len (loop [i 0
                 s input-as-vec-of-longs]
            (println " processing iteration" i)
            (when (or (= i 40) (= i 50))
              (println " length of sequence:" (count s)))
            (if (= i max-iters)
              (count s)
              (recur
                (inc i)
                (look-and-say s))))]
  (println " length of sequence after" max-iters "loops:" len))

