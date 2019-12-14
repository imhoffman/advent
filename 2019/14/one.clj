(require '[clojure.string :as str])



;;
;;  main program
;;
;;   file I/O
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj [] (vec (line-seq f)))))
(println "Read" (count input) "lines.")


(def ins-and-outs
(for [rxn input]
  (let [j    (dec (str/index-of rxn \=))
        ins  (subs rxn 0 j)
        outs (subs rxn (+ j 4))]
    (list ins outs)))
   )

(doseq [s ins-and-outs] (println s))

