
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (re-seq #"(\d+)" ,,)
                (map rest ,,)
                (mapv #(Long/parseLong (first %)) ,,)))

(prn input)

(defn insertv [v x idx]
  (let [[beginning end] (split-at v idx)]
    (vec (apply concat beginning x end))))

(defn blink [v]
  (vec (flatten
         (reduce
           (fn [a b]
             (conj a
                   (cond
                     (= b 0)
                     1N

                     (= 0 (mod (count (str b)) 2))
                     (let [s (str b)
                           vs (split-at (quot (count s) 2) s)
                           ss (mapv #(apply str %) vs)
                           vn (mapv #(Long/parseLong %) ss)]
                       vn)

                     :else
                     (* 2024N b)))) [] v))))

(println
  (loop [v input
         i 0]
    (if
      (= i 25)
      (count v)
      (recur
        (blink v)
        (inc i)))))









