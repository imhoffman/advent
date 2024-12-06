
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (re-seq #"([\^.#]+)\n?" ,,)
                (mapv #(apply vec (rest %)) ,,)))

;(prn input)

(def start-coords
  (loop [r 0
         c 0]
    (if (= \^ ((input r) c))
      (vector r c)
      (if (= (inc c) (count (input 0)))
        (recur (inc r) 0)
        (recur r (inc c))))))

(prn start-coords)

(let [input (assoc input (first start-coords)
                   (assoc (input (first start-coords)) (second start-coords) \.))]
  (println
    (loop [d {:r (first start-coords), :c (second start-coords), :dir \u, :n #{start-coords}}]
      (let [r (d :r), c (d :c)]
        (cond
          (or
            (and (= \u (d :dir)) (= r 0))
            (and (= \d (d :dir)) (= r (dec (count input))))
            (and (= \l (d :dir)) (= c 0))
            (and (= \r (d :dir)) (= c (dec (count (input 0))))))
          (count (d :n))

          (and (= \u (d :dir)) (= \# ((input (dec r)) c)))
          (recur (reduce #(assoc %1 (first %2) (second %2)) d [[:dir \r]]))

          (and (= \d (d :dir)) (= \# ((input (inc r)) c)))
          (recur (reduce #(assoc %1 (first %2) (second %2)) d [[:dir \l]]))

          (and (= \u (d :dir)) (= \. ((input (dec r)) c)))
          (recur (reduce #(assoc %1 (first %2) (second %2)) d [[:r (dec (d :r))],[:n (conj (d :n) (vector (dec (d :r)) (d :c)))]]))

          (and (= \d (d :dir)) (= \. ((input (inc r)) c)))
          (recur (reduce #(assoc %1 (first %2) (second %2)) d [[:r (inc (d :r))],[:n (conj (d :n) (vector (inc (d :r)) (d :c)))]]))

          (and (= \r (d :dir)) (= \# ((input r) (inc c))))
          (recur (reduce #(assoc %1 (first %2) (second %2)) d [[:dir \d]]))

          (and (= \l (d :dir)) (= \# ((input r) (dec c))))
          (recur (reduce #(assoc %1 (first %2) (second %2)) d [[:dir \u]]))

          (and (= \r (d :dir)) (= \. ((input r) (inc c))))
          (recur (reduce #(assoc %1 (first %2) (second %2)) d [[:c (inc (d :c))],[:n (conj (d :n) (vector (d :r) (inc (d :c))))]]))

          (and (= \l (d :dir)) (= \. ((input r) (dec c))))
          (recur (reduce #(assoc %1 (first %2) (second %2)) d [[:c (dec (d :c))],[:n (conj (d :n) (vector (d :r) (dec (d :c))))]]))

          :else
          (println "something went wrong"))))))











