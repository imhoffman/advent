
(require '[clojure.string :as str])

(def input
  ((fn [s] (->> s
                slurp
                (re-seq #"\w+")
                (map #(Long/parseLong %))))
   "puzzle.txt"))


(println
(loop [stack input
       a     (first stack)
       bs    (rest stack)
       cs    (rest bs)]
  (let [b (first bs)
        c (first cs)]
    (if (= 2020 (+ a b c))
      (* a b c)
      (cond
        (empty? (rest (rest bs)))
          (recur
            (rest stack)
            (first (rest stack))
            (rest (rest stack))
            (rest (rest (rest stack))))
        (empty? (rest cs))
          (recur
            stack
            a
            (rest bs)
            (rest (rest bs)))
        :else
          (recur
            stack
            a
            bs
            (rest cs))))))
)


