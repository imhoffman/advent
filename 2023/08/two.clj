
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
         locs (filter #(re-matches #"..A" %) (keys nodes))
         c 0]
    (cond
      (every? #(re-matches #"..Z" %) locs) c
      (empty? d) (recur directions locs c)
      :default
      (recur
        (rest d)
        (map #((nodes %) (first d)) locs)
        (inc c)))))



