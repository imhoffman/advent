
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n\n") ,,)))


(def stacks (->> (first input)
                 ))


(def instr (->> (second input)
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"move (\d+) from (\d+) to (\d+)" %) ,,)
                (map #(rest %) ,,)
                (mapv (fn [s] (mapv #(Long/parseLong %) s)) ,,)))



(println instr)




