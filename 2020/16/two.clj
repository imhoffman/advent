
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input-rules
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       (map #(re-matches #".*: (\d+)-(\d+) or (\d+)-(\d+)" %))
       (filter #(not (nil? %)))
       (map rest)
       (map (fn [s] (map #(Long/parseLong %) s)))
       (map vec)))

;(prn input-rules)


(def nearby-tickets
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"nearby tickets:\n"))
       second
       (#(str/split % #"\n"))
       (map #(str/split % #","))
       (map (fn [s] (map #(Long/parseLong %) s)))
       (map vec)))

;(prn nearby-tickets)


(defn valid? [rules ticket]
  (loop [stack ticket]
    (cond
      (empty? stack)
        true
      (let [n (first stack)]
        (not-every? false?
          (for [rule rules]
            (or
              (and (>= n (rule 0))
                   (<= n (rule 1)))
              (and (>= n (rule 2))
                   (<= n (rule 3)))))))
        (recur (rest stack))
      :else
        false)))



;(prn (for [ticket nearby-tickets] (valid? input-rules ticket)))



