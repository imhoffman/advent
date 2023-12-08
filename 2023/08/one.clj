
(require '[clojure.string :as str])
(require '[clojure.set :as set])
(require '[clojure.core.reducers :as r])

(def nodes (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n\n") ,,)
                second
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"(\w{3}) = \((\w{3}), (\w{3})\)" %) ,,)
                (map rest ,,)
                (reduce (fn [a b] (assoc a (first b) {\L (nth b 1), \R (nth b 2)})) {} ,,)))

(def directions (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n\n") ,,)
                first
                vec))

;(prn nodes)
;(prn directions)


(println
  (loop [d directions
         loc "AAA"
         c 0]
    (cond
      (= loc "ZZZ") c
      (empty? d) (recur directions loc c)
      :default
      (recur
        (rest d)
        ((nodes loc) (first d))
        (inc c)))))

;;  13674 wrong


