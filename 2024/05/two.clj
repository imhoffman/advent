
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def inputs (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n\n") ,,)))

(def pairs (->> (first inputs)
                (re-seq #"(\d+)\|(\d+)\n?" ,,)
                (map #(vector (Integer/parseInt (nth % 1)) (Integer/parseInt (nth % 2))) ,,)))

(def updates (->> (second inputs)
                  (#(str/split % #"\n") ,,)
                  (map #(re-seq #"(\d+),?" %) ,,)
                  (map #(map rest %) ,,)
                  (map (fn [s] (mapv #(Integer/parseInt (first %)) s)) ,,)))

;(println pairs)
;(prn updates)

(defn correct? [pages]
  (let [ps (filter #(set/subset? (into #{} %) (into #{} pages)) pairs)]
    (= (count ps)
       (count (filter #(< (.indexOf pages (first %)) (.indexOf pages (second %))) ps)))))


(defn corrected-middle-page [pages]
  (let [ps (filter #(set/subset? (into #{} %) (into #{} pages)) pairs)]
    (if
      (reduce #(and %1 %2) (for [p ps] (< (.indexOf pages (first p)) (.indexOf pages (second p)))))
      (pages (quot (count pages) 2))
      (recur
        (let [swaps (filter (complement empty?)
                            (for [p ps]
                              (when (> (.indexOf pages (first p)) (.indexOf pages (second p)))
                                (vector (.indexOf pages (first p)) (.indexOf pages (second p))))))]
          (reduce (fn [a b] (assoc (assoc a (first b) (a (second b))) (second b) (a (first b)))) pages swaps))))))




(println (apply + (for [p (filter (complement correct?) updates)] (corrected-middle-page p))))






