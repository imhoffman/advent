
(require '[clojure.string :as str])
(require '[clojure.set :as set])
(require '[clojure.core.reducers :as r])


(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"(?:([%&])(\w{2})|(broadcaster)) -> (.*)" %) ,,)
                (map rest ,,)
                (map #(filter some? %) ,,)
                (reduce
                  (fn [a b]
                    (if (= "broadcaster" (first b))
                      (assoc a (first b) {:op "b", :dest (last b)})
                      (assoc a (second b) {:op (first b), :dest (last b)}))) {} ,,)))


(prn input)

