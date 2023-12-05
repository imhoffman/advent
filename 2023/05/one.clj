
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


