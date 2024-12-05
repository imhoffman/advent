
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

(defn correct-middle-page [pages]
  (let [ps (filter #(set/subset? (into #{} %) (into #{} pages)) pairs)]
    (if
      (= (count ps)
         (count (filter #(< (.indexOf pages (first %)) (.indexOf pages (second %))) ps)))
      (pages (quot (count pages) 2))
      0)))


(println (apply + (for [p updates] (correct-middle-page p))))






