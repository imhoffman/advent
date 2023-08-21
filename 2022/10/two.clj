
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"(addx|noop)\s?(\-?\d+)?" %) ,,)
                (map #(vec (rest %)) ,,)
                (map #(if (% 1) (vector (% 0) (Integer/parseInt (% 1))) %) ,,)))

;(prn input)

;;  memoize this
(defn execute [program stop-time]
  (let [wall-clock        1
        instruction-timer 0
        register          1]
    (loop [p  program
           wc wall-clock
           ic instruction-timer
           x  register]
      ;(println wc (first p) ic x)
      (if (= wc stop-time)
        x
        (let [instr      ((first p) 0)
              [reg,tick] (if (= instr "noop")
                           (vector x 0)
                           (if (= ic 1)
                             (vector (+ x ((first p) 1)) 0)
                             (vector x (inc ic))))]
            (recur (if (= tick 0) (rest p) p) (inc wc) tick reg))))))


;;
;;  main
;;
(doseq [row (range 6)]
  (doseq [col (range 40)]
    (let [wc (+ col (* row 40))
          x  (execute input (inc wc))
          p  (mod wc 40)]
      (print (if (or (= x p) (= x (inc p)) (= x (dec p))) "#" "."))))
  (println))






