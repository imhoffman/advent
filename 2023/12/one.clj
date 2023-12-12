
(require '[clojure.string :as str])
;(require '[clojure.set :as set])
;(require '[clojure.core.reducers :as r])
(require '[clojure.math.combinatorics :as combo])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"([\.#\?]*) (.*)" %) ,,)
                (map rest ,,)
                (map #(vector (first %) (re-seq #"\d+" (second %))) ,,)
                (map (fn [p] (vector (p 0) (mapv #(Long/parseLong %) (p 1)))) ,,)))

;(doseq [e input] (prn e))

(defn works? [pair]
  (let [s (first pair)
        p (re-seq #"#+" s)
        ps (map #(count %) p)
        nums (second pair)
        cs (map vector ps nums)]
    (if (not= (count ps) (count nums))
      false
      (every? #(= (% 0) (% 1)) cs))))

;(doseq [pair input] (prn (works? pair)))

(defn swap-in [string permutation]
  (loop [s (vec string)
         i 0
         p permutation]
    (if (empty? p)
      (apply str s)
      (if (= (s i) \?)
        (recur
          (assoc s i (first p))
          (inc i)
          (rest p))
        (recur s (inc i) p)))))


(defn count-the-ways [pair]
  (let [s (first pair)
        len ((frequencies s) \?)
        n (second pair)
        m (apply min n)]
    (loop [c len
           o 0]
      (if (= c 0)
      ;(if (< c m)
        o
        (let [ps (combo/permutations (apply str
                                            (apply conj
                                                   (take c (repeat \#))
                                                   (take (- len c) (repeat \.)))))]
          (recur
            (dec c)
            (+ o (count (filter #(works? (vector % n)) (map #(swap-in s %) ps))))))))))



(println (apply + (for [pair input] (count-the-ways pair))))

;;  7187 too low
;;  7194 too low

