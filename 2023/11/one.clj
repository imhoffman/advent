
(require '[clojure.string :as str])
(require '[clojure.set :as set])
(require '[clojure.core.reducers :as r])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (mapv vec ,,)))

;(doseq [line input] (prn line))


(defn insert [v i e]
  (apply conj (vec (take i v)) e (vec (drop i v))))


(defn expand-empty-rows [m]
  (let [nrows (count m)]
    (loop [row 0
           c 0
           o m]
      (if (= row nrows)
        o
        (let [expand (every? #(= % \.) (m row))]
          (recur
            (inc row)
            (if expand (inc c) c)
            (if expand (insert o (+ row c) (vec (apply str (repeat (count (m 0)) \.)))) o)))))))


(defn expand-empty-cols [m]
  (let [ncols (count (m 0))]
    (loop [col 0
           c 0
           o m]
      (if (= col ncols)
        o
        (let [expand (every? #(= % \.) (map #(% col) m))]
          (recur
            (inc col)
            (if expand (inc c) c)
            (if expand
              (map #(insert % (+ col c) \.) o)
              o)))))))


;(let [o (expand-empty-cols (expand-empty-rows input))] (doseq [line o] (prn line)))

(def expanded-input
  (expand-empty-cols (expand-empty-rows input)))

(defn all-indices [string c]
  (loop [s string
         o (list)]
    (let [j (str/index-of s c)]
      (if j
        (recur (subs s (inc j)) (conj o j))
        o))))


(defn find-galaxies [m]
  (let [nrows (count m)]
    (loop [row 0
           o (list)]
      (if (= row nrows)
        o
        (recur
          (inc row)
          (apply conj o
                 (map vector (repeat row) (all-indices (apply str (m row)) "#"))))))))

;(println (find-galaxies expanded-input))





