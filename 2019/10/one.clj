(require '[clojure.string :as str])

;;  "array" fetcher
(defn get-map-val [input x y] (get (get input y) x))

;;  visualize data, optionally with "solution"
(defn show-map [input & ordered-pair]
  (let [x (first ordered-pair)
        y (second ordered-pair)
        height (count input)]
    (doseq [i (range height)]
      (if (= i y)
        (println (str (subs (get input i) 0 x)
                      "\033[31m\033[1m\033[43m"
                      (get-map-val input x y)
                      "\033[0m"
                      (subs (get input i) (inc x))))
        (println (get input i))))))


;;  call initially with empty hash-set for `nearest-found`
;;   the hash-set is the accumulator
;;   not sure what slope is yet
(defn los-search [input x y slope nearest-found]
  (let [width (count (get input 0))
        height (count input)]
    (if (= y height)
      nearest-found
      (recur input x (inc y) slope (conj nearest-found (list x y))))))






;;
;;  main program
;;
;;   file I/O
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj [] (vec (line-seq f)))))
;(println "Read" (count input) "lines.")


;(show-map input)
(show-map input 11 13)

