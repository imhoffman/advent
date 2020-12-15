
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       str/trim
       (#(str/split % #","))
       (map #(Long/parseLong %))
       vec))

(prn input)


(loop [v input
       i (count v)]
  (if (= i 2020) (prn (last v))
    (let [found (.lastIndexOf (butlast v) (last v))]
      (recur
        (if (= -1 found)
          (conj v 0)
          (conj v (- i (inc found))))
        (inc i)))))


