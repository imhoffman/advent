
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map #(re-matches #"(\w+)-(\w+)" %) ,,)
       (map rest)
       (map vec)))


(def inputd
  (let [o (reduce
            #(assoc %1 (%2 0) (set/union (%1 (%2 0)) (set (vector (%2 1)))))
            (into {} (for [e input] (vector (first e) #{})))
            input)]
    (reduce
      #(assoc %1 (%2 1) (set/union (%1 (%2 1)) (set (vector (%2 0)))))
      o input)))

(println inputd)





