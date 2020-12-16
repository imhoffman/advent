
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


;;
;;  returns vector of erroroneous values
;;
(defn errors [rules ticket]
  (loop [stack  ticket
         errors []]
    (if (empty? stack)
      errors
      (let [n (first stack)]
        (recur
          (rest stack)
          (if (not-every? false?
                      (for [rule rules]
                        (or
                         (and (>= n (rule 0))
                              (<= n (rule 1)))
                         (and (>= n (rule 2))
                              (<= n (rule 3))))))
            errors
            (conj errors n)))))))

(prn
  (apply +
    (apply concat
      (for [ticket nearby-tickets] (errors input-rules ticket)))))




;; 110320



