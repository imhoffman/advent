
(require '[clojure.string :as str])

(def orig-program
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       (map #(str/split % #" "))
       (map #(vector (first %) (Long/parseLong (second %))))
       vec))


(defn execute [ram]
  (loop [pc    0
         accum 0]
    (if (>= pc (count ram))
      accum
      (let [op  ((ram pc) 0)
            arg ((ram pc) 1)]
        (recur
          (if (= op "jmp")
            (+ pc arg)
            (inc pc))
          (if (= op "acc")
            (+ accum arg)
            accum))))))


(defn try-prg [ram]
  (let [try-limit (* 3 (count ram))]
  (loop [pc    0
         accum 0
         execs 0]
    (cond
      (>= pc (count ram)) :success
      (= execs try-limit) :fail
      :else
        (let [op  ((ram pc) 0)
              arg ((ram pc) 1)]
          (recur
            (if (= op "jmp")
              (+ pc arg)
              (inc pc))
            (if (= op "acc")
              (+ accum arg)
              accum)
            (inc execs)))))))

(let [p orig-program]
  (loop [last-index 0]
    (let [[swap-index,op] (loop [i last-index] (if (or (= ((p i) 0) "nop") (= ((p i) 0) "jmp")) (vector i ((p i) 0)) (recur (inc i))))
          ram (-> (p swap-index), (assoc ,, 0 (if (= op "nop") "jmp" "nop")), (#(assoc p swap-index %)))]
      (println " swapping out a" op "at index" swap-index)
      (if (= :success (try-prg ram))
        (println " accumulator when the altered program halts:" (execute ram))
        (recur (inc last-index))))))





