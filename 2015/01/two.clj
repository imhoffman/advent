(require '[clojure.string :as str])

;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (first (reduce conj () (line-seq f)))))      ; for a single-line input

;; don't use loop; perhaps `reduce` a sum

(loop [index 1
       floor 0
       c (first input)]
  (let [s (first (rest input))]
    (if (= floor -1)
      (println "\n encountered floor -1 at index" index))
    (if (and (= c "(") (not (str/blank? s)))
      (recur (inc index) (inc floor) s)
      (recur (inc index) (dec floor) s))))

(let [freqs (frequencies input)]
  (println " final floor:" (- (get freqs \() (get freqs \)))))

