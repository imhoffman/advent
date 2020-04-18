
;;  https://stackoverflow.com/questions/7491360/how-do-you-return-from-a-function-early-in-clojure
;;   I have applied Chuck's answer here
(defn basement-finder [directions index floor]
   (if (= floor -1) (dec index)    ;; return one fewer b/c of the recur inc
     (if (= (first directions) \()
       (recur (rest directions) (inc index) (inc floor))
       (recur (rest directions) (inc index) (dec floor)))))

;;
;;  main program
;;
;;   file I/O
;;    for a single-line input
(def input (slurp "puzzle.txt"))

(println "\n basement encountered at index" (basement-finder input 1 0) "\n")



;;  solutions from outschool class

(println " part one: the elevator ride ends on floor"
  (apply +
         (map #(if (= \( %) +1 -1) (slurp "puzzle.txt"))))


(println
  " the loop stops and prints at:"
  (loop [floor   0
         counter 0
         instr   (slurp "puzzle.txt")]
    (if (< floor 0)
      counter
      (recur
        (if (= \( (first instr)) (inc floor) (dec floor))
        (inc counter)
        (rest instr)))))



