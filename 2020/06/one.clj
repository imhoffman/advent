
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input-sets
  (->> "puzzle.txt"
       slurp
       (#(str % "\n\n"))
       (re-seq #"([a-z]+\n)+\n")
       (map first)
       (map #(str/split % #"\n"))
       (map #(apply concat %))
       (map set)))


;(prn (nth input-sets 2))

(prn (apply + (for [s input-sets] (count s))))

;(doseq [s input-sets] (prn (count s)))


;; 1603
;; 6694

