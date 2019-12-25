(require '[clojure.string :as str])

(defn update-layout [current-scan]
  (let [bordered-scan
        (conj
          ['.......]      ; need to prepend one of these blank lines...
          (into ['.......]
                (vec (for [row current-scan] (str '. row '.)))))]
    bordered-scan))





;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj [] (line-seq f))))           ;; order matters, so vector
(println "Read" (count input) "lines.")

(doseq [row input] (println row))

(doseq [row (update-layout input)] (println row))

