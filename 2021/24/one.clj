
(require '[clojure.string :as str])

(def program (->> "puzzle.txt"
                  slurp
                  (#(str/split % #"\n") ,,)
                  (map #(re-matches #"([a-z]{3}) ([w-z])\s*([w-z0-9]*)" %) ,,)
                  (map #(into {} (map vector [:op :a :b] (rest %))) ,,)))

(println program)
                  
                  

