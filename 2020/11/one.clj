
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


;;  returns the char that replaces r,c
(defn seat [lay r c]
  (let [vac #{\. \L}]  ;;  vacancies
    (cond
      ;;
      ;;  the four corners
      ;;
      (and (= r 0) (= c 0))
        (cond
          (= \L ((lay r) c))
            (if (and (contains? vac ((lay (inc r)) c))
                     (contains? vac ((lay (inc r)) (inc c)))
                     (contains? vac ((lay r) (inc c))))
              \# \L)
          :else
            ((lay r) c))
      (and (= r (dec nrows)) (= c (dec ncols)))
        (cond
          (= \L ((lay r) c))
            (if (and (contains? vac ((lay (dec r)) c))
                     (contains? vac ((lay (dec r)) (dec c)))
                     (contains? vac ((lay r) (dec c))))
              \# \L)
          :else
            ((lay r) c))
      (and (= r 0) (= c (dec ncols)))
        (cond
          (= \L ((lay r) c))
            (if (and (contains? vac ((lay (inc r)) c))
                     (contains? vac ((lay (inc r)) (dec c)))
                     (contains? vac ((lay r) (dec c))))
              \# \L)
          :else
            ((lay r) c))
      (and (= r (dec nrows)) (= c 0))
        (cond
          (= \L ((lay r) c))
            (if (and (contains? vac ((lay (dec r)) c))
                     (contains? vac ((lay (dec r)) (inc c)))
                     (contains? vac ((lay r) (inc c))))
              \# \L)
          :else
            ((lay r) c))
      ;;
      ;;  the four edges (not yet caught by the outer cond)
      ;;
      (= r 0)
        (cond
          (= \L ((lay r) c))
            (if (every? #(not (false? %))
                        (list
                          (contains? vac ((lay (inc r)) (dec c)))
                          (contains? vac ((lay (inc r)) c))
                          (contains? vac ((lay (inc r)) (inc c)))
                          (contains? vac ((lay r) (inc c)))
                          (contains? vac ((lay r) (dec c)))))
              \# \L)
          (= \# ((lay r) c))
            (if (<= 4 (count (filter true? (list
                        (= \# ((lay (inc r)) (dec c)))
                        (= \# ((lay (inc r)) c))
                        (= \# ((lay (inc r)) (inc c)))
                        (= \# ((lay r) (inc c)))
                        (= \# ((lay r) (dec c)))))))
              \L \#)
          :else
            ((lay r) c))
      (= r (dec nrows))
        (cond
          (= \L ((lay r) c))
            (if (every? #(not (false? %))
                        (list
                          (contains? vac ((lay (dec r)) (dec c)))
                          (contains? vac ((lay (dec r)) c))
                          (contains? vac ((lay (dec r)) (inc c)))
                          (contains? vac ((lay r) (inc c)))
                          (contains? vac ((lay r) (dec c)))))
              \# \L)
          (= \# ((lay r) c))
            (if (<= 4 (count (filter true? (list
                        (= \# ((lay (dec r)) (dec c)))
                        (= \# ((lay (dec r)) c))
                        (= \# ((lay (dec r)) (inc c)))
                        (= \# ((lay r) (inc c)))
                        (= \# ((lay r) (dec c)))))))
              \L \#)
          :else
            ((lay r) c))
      (= c 0)
        (cond
          (= \L ((lay r) c))
            (if (every? #(not (false? %))
                        (list
                          (contains? vac ((lay (inc r)) (inc c)))
                          (contains? vac ((lay r) (inc c)))
                          (contains? vac ((lay (dec r)) (inc c)))
                          (contains? vac ((lay (inc r)) c))
                          (contains? vac ((lay (dec r)) c))))
              \# \L)
          (= \# ((lay r) c))
            (if (<= 4 (count (filter true? (list
                        (= \# ((lay (inc r)) (inc c)))
                        (= \# ((lay r) (inc c)))
                        (= \# ((lay (dec r)) (inc c)))
                        (= \# ((lay (inc r)) c))
                        (= \# ((lay (dec r)) c))))))
              \L \#)
          :else
            ((lay r) c))
      (= c (dec ncols))
        (cond
          (= \L ((lay r) c))
            (if (every? #(not (false? %))
                        (list
                          (contains? vac ((lay (inc r)) (dec c)))
                          (contains? vac ((lay r) (dec c)))
                          (contains? vac ((lay (dec r)) (dec c)))
                          (contains? vac ((lay (inc r)) c))
                          (contains? vac ((lay (dec r)) c))))
              \# \L)
          (= \# ((lay r) c))
            (if (<= 4 (count (filter true? (list
                        (= \# ((lay (inc r)) (dec c)))
                        (= \# ((lay r) (dec c)))
                        (= \# ((lay (dec r)) (dec c)))
                        (= \# ((lay (inc r)) c))
                        (= \# ((lay (dec r)) c))))))
              \L \#)
          :else
            ((lay r) c))
      ;;
      ;;  the main body of the layout
      ;;
      :else
        (cond
          (= \L ((lay r) c))
            (if (every? #(not (false? %))
                        (list
                          (contains? vac ((lay (inc r)) (dec c)))
                          (contains? vac ((lay r)       (dec c)))
                          (contains? vac ((lay (dec r)) (dec c)))
                          (contains? vac ((lay (inc r)) (inc c)))
                          (contains? vac ((lay r)       (inc c)))
                          (contains? vac ((lay (dec r)) (inc c)))
                          (contains? vac ((lay (inc r)) c))
                          (contains? vac ((lay (dec r)) c))))
              \# \L)
          (= \# ((lay r) c))
            (if (<= 4 (count (filter true? (list
                          (= \# ((lay (inc r)) (dec c)))
                          (= \# ((lay r)       (dec c)))
                          (= \# ((lay (dec r)) (dec c)))
                          (= \# ((lay (inc r)) (inc c)))
                          (= \# ((lay r)       (inc c)))
                          (= \# ((lay (dec r)) (inc c)))
                          (= \# ((lay (inc r)) c))
                          (= \# ((lay (dec r)) c))))))
              \L \#)
          :else
            ((lay r) c)))))


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



