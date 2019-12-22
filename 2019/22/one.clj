(require '[clojure.string :as str])

;;  parse the instruction from the puzzle input into a
;;  two-elem list of a technique and its accompanying integer
(defn technique-parser [technique-instruction]
  (let [technique (str/split technique-instruction #"\s+")]
    (cond
      (and (= (first technique) "deal") (= (second technique) "into"))
        (list "deal" -1)
      (and (= (first technique) "deal") (= (second technique) "with"))
        (list "increment" (Integer/parseInt (last technique)))
      (= (first technique) "cut")
        (list "cut" (Integer/parseInt (last technique)))
      :else (println " problem parsing technique"))))



;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj [] (line-seq f))))           ;; order matters, so vector
(println "Read" (count input) "lines.")

;; dummy test
(doseq [x input]
  (println (technique-parser x)))

