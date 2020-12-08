
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def program
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       (map #(str/split % #" "))
       (map #(vector (first %) (Long/parseLong (second %))))
       vec))

;(prn program)

(defn execute [ram]
  (loop [pc    0
         accum 0
         pcs   #{}]
    (println " pc:" pc ", instr:" (ram pc) ", accum:" accum)
    (if (contains? pcs pc)
      accum
      (let [op  ((ram pc) 0)
            arg ((ram pc) 1)]
        (recur
          (if (= op "jmp")
            (+ pc arg)
            (inc pc))
          (if (= op "acc")
            (+ accum arg)
            accum)
          (conj pcs pc))))))


(println " accumulator when an address is first revisited:" (execute program))


