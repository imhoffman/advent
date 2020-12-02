
(require '[clojure.string :as str])

(def input-list-of-lines
  ((fn [s] (->> s
                slurp
                (re-seq #"\w+")
                (partition 4)))
   "puzzle.txt"))


(defn valid? [list-of-four]
  (let [v     (vec list-of-four)
        freqs (frequencies (v 3))
        nmin  (Long/parseLong (v 0))
        nmax  (Long/parseLong (v 1))
        c     (first (v 2))]    ;;  char
    (and (contains? freqs c) (>= (freqs c) nmin) (<= (freqs c) nmax) true)))


(println
  (count (filter true?
            (for [e input-list-of-lines] (valid? e)))))


