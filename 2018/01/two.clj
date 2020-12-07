
(require '[clojure.string :as str])

(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       (map #(Long/parseLong %))))

(prn
(loop [stack input
       freq  0
       accum #{freq}]
  (if (empty? stack)
    (recur input freq accum)
    (let [new-freq (+ freq (first stack))]
      (if (contains? accum new-freq)
        new-freq
        (recur
          (rest stack)
          new-freq
          (conj accum new-freq))))))
)

;; 13

