
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (mapv vec ,,)))

;(prn input)

(def nr (count input))
(def nc (count (input 0)))

(defn search [r c]
  (if (= \A ((input r) c))
    (if
      (and
        (or
          (and (= \M ((input (dec r)) (inc c))) (= \S ((input (inc r)) (dec c))))
          (and (= \S ((input (dec r)) (inc c))) (= \M ((input (inc r)) (dec c)))))
        (or
          (and (= \M ((input (dec r)) (dec c))) (= \S ((input (inc r)) (inc c))))
          (and (= \S ((input (dec r)) (dec c))) (= \M ((input (inc r)) (inc c))))))
      1 0)
    0))


(println 
  (loop [r 1
         c 1
         o 0]
    (if (= r (- nr 1))
      o
      (if (= c (- nc 2))
        (recur (inc r) 1 (+ o (search r c)))
        (recur r (inc c) (+ o (search r c)))))))

