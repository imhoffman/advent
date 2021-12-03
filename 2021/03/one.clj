
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)))


(loop [index 0
       gamma-bits (vector)
       epsilon-bits (vector)]
  (if (= index (count (first input)))
    (println (* (Integer/parseInt (apply str epsilon-bits) 2) (Integer/parseInt (apply str gamma-bits) 2)))
    (let [freqs (frequencies (reduce #(conj %1 ((vec %2) index)) (list) input))
          most  (if (> (freqs \0) (freqs \1)) \0 \1)
          least (if (> (freqs \0) (freqs \1)) \1 \0)]
      (recur
        (inc index)
        (conj gamma-bits most)
        (conj epsilon-bits least)))))












