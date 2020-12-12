
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       (map vec)
       vec))

(def nrows (count input))
(def ncols (count (first input)))

;(prn nrows ncols)


;;
;;  inspect a ray out from r,c along slope mr/mc
;;   return true if a filled seat is found, else false
;;
(defn occupied? [lay r c mr mc]
  (loop [r1 r
         c1 c]
    (let [r2 (+ r1 mr)
          c2 (+ c1 mc)]
      (cond
        (or (= r2 nrows) (< r2 0) (= c2 ncols) (< c2 0)) false
        (= \L ((lay r2) c2)) false
        (= \# ((lay r2) c2)) true
        :else
          (recur r2 c2)))))


;;  returns the char that replaces r,c
(def slopes (vector [-1 0] [-1 1] [0 1] [1 1] [1 0] [1 -1] [0 -1] [-1 -1]))

(defn seat [lay r c]
  (let [ch ((lay r) c)]
    (cond
      (= \. ch) \.
      (= \L ch)
        (loop [s slopes]
          (cond
            (empty? s) \#
            (occupied? lay r c ((first s) 0) ((first s) 1)) \L
            :else (recur (rest s))))
      (= \# ch)
        (loop [s slopes
               o 0]
          (cond
            (> o 4) \L
            (empty? s) \#
            (occupied? lay r c ((first s) 0) ((first s) 1))
              (recur (rest s) (inc o))
            :else (recur (rest s) o)))
      :else (prn " fail: bad char at" r c))))


(loop [old-layout nil
       new-layout input]
  ;(println " old:\n" old-layout "\n new:\n" new-layout)
  (if (= old-layout new-layout)
    (prn ((frequencies (flatten new-layout)) \#))
    (let [old new-layout]
      (recur
        old
        (loop [row 0
               out []]
          (if (= row nrows)
            out
            (recur
              (inc row)
              (conj out
                (loop [col 0
                       rowv []]
                  (if (= col ncols)
                    rowv
                    (recur
                      (inc col)
                      (conj rowv (seat old row col)))))))))))))

;; 1981 too low
;; 2671 too high

