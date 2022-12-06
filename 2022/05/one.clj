
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n\n") ,,)))

(def stack-names  
  (map str/trim (str/split (last (str/split (first input) #"\n")) #"   ")))

(def num-stacks
  (count stack-names))

(def stacks (->> (first input)


                 ))


(def instr (->> (second input)
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"move (\d+) from (\d+) to (\d+)" %) ,,)
                (map #(rest %) ,,)
                (mapv (fn [s] (mapv #(Long/parseLong %) s)) ,,)))



(println instr)




