
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map vec)
       (map (fn [row] (mapv #(Integer/parseInt (str %)) row)) ,,)
       (vec)))


(def nrows (count input))
(def ncols (count (input 0)))


(defn check-height [r c o]
  (let [curr ((input r) c)]
    (cond
      (= r 0)
      (cond
        (= c 0)
        (if (and (> ((input (inc r)) c) curr)
                 (> ((input r) (inc c)) curr))
          (assoc o (vector r c) curr)
          o)

        (= c (dec ncols))
        (if (and (> ((input (inc r)) c) curr)
                 (> ((input r) (dec c)) curr))
          (assoc o (vector r c) curr)
          o)

        :else
        (if (and (> ((input (inc r)) c) curr)
                 (> ((input r) (dec c)) curr)
                 (> ((input r) (inc c)) curr))
          (assoc o (vector r c) curr)
          o))

      (= r (dec nrows))
      (cond
        (= c 0)
        (if (and (> ((input (dec r)) c) curr)
                 (> ((input r) (inc c)) curr))
          (assoc o (vector r c) curr)
          o)

        (= c (dec ncols))
        (if (and (> ((input (dec r)) c) curr)
                 (> ((input r) (dec c)) curr))
          (assoc o (vector r c) curr)
          o)

        :else
        (if (and (> ((input (dec r)) c) curr)
                 (> ((input r) (dec c)) curr)
                 (> ((input r) (inc c)) curr))
          (assoc o (vector r c) curr)
          o))

      (= c 0)
      (if (and (> ((input (dec r)) c) curr)
               (> ((input r) (inc c)) curr)
               (> ((input (inc r)) c) curr))
        (assoc o (vector r c) curr)
        o)

      (= c (dec ncols))
      (if (and (> ((input (dec r)) c) curr)
               (> ((input r) (dec c)) curr)
               (> ((input (inc r)) c) curr))
        (assoc o (vector r c) curr)
        o)

      :else
      (if (and (> ((input (dec r)) c) curr)
               (> ((input r) (inc c)) curr)
               (> ((input r) (dec c)) curr)
               (> ((input (inc r)) c) curr))
        (assoc o (vector r c) curr)
        o))))








;;
;;  main
;;
(loop [row 0
       col 0
       out {}]
  (cond
    (= row nrows)
    (println (apply + (map inc (vals out))))

    (= col ncols)
    (recur (inc row) 0 out)

    :else
    (recur row (inc col) (check-height row col out))))




