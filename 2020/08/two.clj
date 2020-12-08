
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

(def sus-op "jmp")

(defn execute [ram]
  (loop [pc    0
         accum 0
         pcs   []]
    ;(println "\n pc:" pc ", instr:" (ram pc) ", accum:" accum)
    (when (< 0 (count pcs)) (println " most common" sus-op (key (apply max-key val (frequencies pcs)))))
    (if (= pc (count ram))
      accum
      (let [op  ((ram pc) 0)
            arg ((ram pc) 1)]
        (when (and (= op "nop") (< 650 (+ pc arg))) (println " sus nop at:" pc))
        (recur
          (if (= op "jmp")
            (+ pc arg)
            (inc pc))
          (if (= op "acc")
            (+ accum arg)
            accum)
          (if (= op sus-op)
            (conj pcs pc)
            pcs))))))


(println " accumulator when program halts:" (execute program))


