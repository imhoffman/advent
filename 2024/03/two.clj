
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (re-seq #"(do\(\)|don't\(\))?[\s\S]*?mul\((\d+),(\d+)\).*?" ,,)
                (map #(conj (nthrest % 2) (first (rest (re-matches #".*?(do\(\)|don't\(\)).*?" (first %))))) ,,)))

;(println input)

(def prog
  (loop [p input
         d true
         o (vector)]
    (if (empty? p)
      o
      (let [t (case (first (first p))
                nil d
                "do()" true
                "don't()" false)]
        (recur
          (rest p)
          t
          (if t (conj o (rest (first p))) o))))))

;(println prog)

(println
  (reduce
    (fn [a b]
      (+ a
         (* (Integer/parseInt (nth b 0)) (Integer/parseInt (nth b 1)))))
    0
    prog))



