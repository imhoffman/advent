;;
;;  main program
;;
;;   file I/O
;;    for a single-line input
(def input (slurp "puzzle.txt"))

;;  https://stackoverflow.com/questions/7491360/how-do-you-return-from-a-function-early-in-clojure
;;   I have applied Chuck's answer here
((fn [directions index floor]
   (if (= floor -1) (println "\n basement encountered at index" (dec index) "\n")
     (if (= (first directions) \()
       (recur (rest directions) (inc index) (inc floor))
       (recur (rest directions) (inc index) (dec floor))))
   ) input 1 0)

