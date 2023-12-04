
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"Card.*?\d+: (.*?) \| (.*)" %) ,,)
                (map rest ,,)
                (map (fn [x] (map #(re-seq #"\d+" %) x)) ,,)))
                ;(reduce (fn [a b] (assoc a (Integer/parseInt (second b)) (nth b 2))) {} ,,)))

;(prn input)

(println
  (apply +
         (for [game input]
           (let [n (count (set/intersection (set (first game)) (set (second game))))]
             (if (= n 0) 0 (Math/pow 2 (dec n)))))))


