
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n\n") ,,)
                (map #(re-matches #"(.*?):\s([\d\s]*?)" %) ,,)
                (map rest ,,)
                (reduce (fn [a b] (assoc a (first b) (second b))) {} ,,)))


(def seeds
  (->> (input "seeds")
       (re-seq #"\d+" ,,)
       (map #(Long/parseLong %) ,,)
       (partition 2 2 ,,)
       (reduce (fn [a b] (apply conj a (range (first b) (+ (first b) (second b))))) [] ,,)))


(def parsed-ds
  (loop [ds (dissoc input "seeds")
         o {}]
    (if (empty? ds)
      o
      (let [[k,v] (first ds)]
        (recur
          (dissoc ds k)
          (assoc o k
                 (->> v
                      (#(str/split % #"\n") ,,)
                      (map #(re-matches #"(\d+) (\d+) (\d+)" %) ,,)
                      (map rest ,,)
                      (map (fn [ss] (mapv #(Long/parseLong %) ss)) ,,))))))))

;(prn parsed-ds)
;(prn seeds)

(defn mapper [map-string n]
  (let [ranges (parsed-ds (str map-string " map"))]
    (loop [vs ranges]
      (let [v (first vs)]
        ;(println n v)
        (cond
          (not v) n
          (and (>= n (v 1)) (< n (+ (v 1) (v 2)))) (+ (- n (v 1)) (v 0)) 
          :default (recur (rest vs)))))))


;(println "seed" "soil")
;(doseq [n (range 100)] (println n (seed-to-soil n)))

(println
  (reduce (fn [a b] (let [temp
                          (->> b
                               (mapper "seed-to-soil" ,,)
                               (mapper "soil-to-fertilizer" ,,)
                               (mapper "fertilizer-to-water" ,,)
                               (mapper "water-to-light" ,,)
                               (mapper "light-to-temperature" ,,)
                               (mapper "temperature-to-humidity" ,,)
                               (mapper "humidity-to-location" ,,))]
                      (if (< temp a) temp a)))
          Long/MAX_VALUE 
          seeds))



