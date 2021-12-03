
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)))


(loop [index 0
       gamma-bits (list)
       epsilon-bits (list)]
  (if (= index (count (first input)))
    (do (prn (apply str (reverse gamma-bits))) (prn (apply str (reverse epsilon-bits))))
    (let [freqs (frequencies (reduce #(conj %1 ((vec %2) index)) (list) input))
          most  (if (> (freqs \0) (freqs \1)) \0 \1)
          least (if (> (freqs \0) (freqs \1)) \1 \0)]
      (do (println "freqs for index" index freqs)
      (recur
        (inc index)
        (conj gamma-bits most)
        (conj epsilon-bits least))))))












