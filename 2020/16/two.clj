
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


(def my-ticket
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"your ticket:\n"))
       second
       (#(str/split % #"\n"))
       first
       (#(str/split % #","))
       (map #(Long/parseLong %))
       vec))

;(prn my-ticket)


(def departure-rules
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       (map #(re-matches #"departure.*: (\d+)-(\d+) or (\d+)-(\d+)" %))
       (filter #(not (nil? %)))
       (map rest)
       (map (fn [s] (map #(Long/parseLong %) s)))
       (map vec)))

;(prn departure-rules)



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


;;
;;  return list of fields that might be departure
;;
(defn departure-fields [rules ticket]
  (loop [stack  ticket
         field  0
         fields []]
    (if (empty? stack)
      fields
      (recur
        (rest stack)
        (inc field)
        (let [n (first stack)]
          (if (some true?
              (for [rule rules]
                (or
                  (and (>= n (rule 0))
                       (<= n (rule 1))
                       true)
                  (and (>= n (rule 2))
                       (<= n (rule 3))
                       true))))
            (conj fields field)
            fields))))))



;;  assemble set of fields that are departure and apply * to my-ticket
(loop [stack (filter (partial valid? input-rules) nearby-tickets)
       out (set (range (count (first nearby-tickets))))]
  (if (empty? stack)
    (let [v (vec my-ticket)]
      (prn out)
      (prn (apply * (for [i out] (biginteger (v i))))))
    (recur
      (rest stack)
      (set/intersection out (set (departure-fields departure-rules (first stack)))))))




