(require '[clojure.string :as str])


(defn technique-parser [list-of-techniques]
  (doseq [technique-string list-of-techniques]
    (let [technique (str/split technique-string #"\s+")]
      (cond
        (and (= (first technique) "deal") (= (second technique) "into")) (println "stack deal")
        (and (= (first technique) "deal") (= (second technique) "with")) (println "increment" (last technique) "deal")
        (= (first technique) "cut") (println "cut by" (last technique))
        :else (println " problem parsing technique")))))




;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj [] (line-seq f))))           ;; order matters, so vector
(println "Read" (count input) "lines.")

(technique-parser input)

