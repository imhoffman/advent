
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                vec
                butlast
                (map #(Integer/parseInt (str %)) ,,)))

;(prn input)

(def file-dict
  (loop [id 0
         files (partition 1 2 input)
         d {}]
    (if (empty? files)
      d
      (recur
        (inc id)
        (rest files)
        (assoc d id (first files))))))


