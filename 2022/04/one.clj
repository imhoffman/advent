
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"(\d+)\-(\d+),(\d+)\-(\d+)" %) ,,)
                (map #(rest %) ,,)
                (map (fn [s] (mapv #(Long/parseLong %) s)) ,,)))


;(println input)

(println
  (count
    (filter
      #(or
           (and (<= (% 0) (% 2))
                (>= (% 1) (% 3)))
           (and (>= (% 0) (% 2))
                (<= (% 1) (% 3))))
      input)))
            








