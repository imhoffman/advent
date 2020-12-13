
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def two-input-lines
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))))

(def time0 (Long/parseLong (first two-input-lines)))

(def notes
  (->> (second two-input-lines)
       (re-seq #"\d+")
       (map #(Long/parseLong %))))

(prn notes)

(let [[tbus id]
  (loop [t time0
        buses notes]
    (if (= 0 (mod t (first buses)))
      (vector t (first buses))
      (if (empty? (rest buses))
        (recur (inc t) notes)
        (recur t (rest buses)))))]
  (prn (* (- tbus time0) id)))






