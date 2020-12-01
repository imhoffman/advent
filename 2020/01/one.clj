
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
       bs    (rest stack)]
  (let [b (first bs)]
    (if (= 2020 (+ a b))
      (* a b)
      (if (empty? (rest bs))
        (recur
          (rest stack)
          (first (rest stack))
          (rest (rest stack)))
        (recur
          stack
          (first stack)
          (rest bs))))))
)


