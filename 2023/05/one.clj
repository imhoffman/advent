
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
       (map #(Long/parseLong %) ,,)))


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

(defn seed-to-soil [n]
  (let [ranges (parsed-ds "seed-to-soil map")]
    (loop [vs ranges]
      (let [v (first vs)]
        (cond
          (empty? vs) n
          (and (> n (v 1)) (< n (+ (v 1) (v 2)))) (+ (v 0) (v 2))
          :default (recur (rest vs)))))))


(defn soil-to-fertilizer [n]
  (let [ranges (parsed-ds "soil-to-fertilizer map")]
    (loop [vs ranges]
      (let [v (first vs)]
        (cond
          (empty? vs) n
          (and (> n (v 1)) (< n (+ (v 1) (v 2)))) (+ (v 0) (v 2))
          :default (recur (rest vs)))))))


(defn fertilizer-to-water [n]
  (let [ranges (parsed-ds "fertilizer-to-water map")]
    (loop [vs ranges]
      (let [v (first vs)]
        (cond
          (empty? vs) n
          (and (> n (v 1)) (< n (+ (v 1) (v 2)))) (+ (v 0) (v 2))
          :default (recur (rest vs)))))))


(defn water-to-light [n]
  (let [ranges (parsed-ds "water-to-light map")]
    (loop [vs ranges]
      (let [v (first vs)]
        (cond
          (empty? vs) n
          (and (> n (v 1)) (< n (+ (v 1) (v 2)))) (+ (v 0) (v 2))
          :default (recur (rest vs)))))))


(defn light-to-temperature [n]
  (let [ranges (parsed-ds "light-to-temperature map")]
    (loop [vs ranges]
      (let [v (first vs)]
        (cond
          (empty? vs) n
          (and (> n (v 1)) (< n (+ (v 1) (v 2)))) (+ (v 0) (v 2))
          :default (recur (rest vs)))))))


(defn temperature-to-humidity [n]
  (let [ranges (parsed-ds "temperature-to-humidity map")]
    (loop [vs ranges]
      (let [v (first vs)]
        (cond
          (empty? vs) n
          (and (> n (v 1)) (< n (+ (v 1) (v 2)))) (+ (v 0) (v 2))
          :default (recur (rest vs)))))))


(defn humidity-to-location [n]
  (let [ranges (parsed-ds "humidity-to-location map")]
    (loop [vs ranges]
      (let [v (first vs)]
        (cond
          (empty? vs) n
          (and (> n (v 1)) (< n (+ (v 1) (v 2)))) (+ (v 0) (v 2))
          :default (recur (rest vs)))))))


(println
  (apply min
         (for [seed seeds]
           (->> seed
                seed-to-soil
                soil-to-fertilizer
                fertilizer-to-water
                water-to-light
                light-to-temperature
                temperature-to-humidity
                humidity-to-location))))


;;  2232050638 too high

