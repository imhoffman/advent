
(require '[clojure.string :as str])

(def input-list-of-lines
  ((fn [s] (->> s
                slurp
                (re-seq #"\w+")
                (partition 4)))
   "puzzle.txt"))


(defn valid? [list-of-four]
  (let [v     (vec list-of-four)
        charv (vec (v 3))
        i1    (dec (Long/parseLong (v 0)))  ;; 1-indexed
        i2    (dec (Long/parseLong (v 1)))
        c     (first (v 2))]                ;;  char
    (and (< i1 (count charv)) (< i2 (count charv))
         (or (= c (charv i1)) (= c (charv i2)))
         (not (and (= c (charv i1)) (= c (charv i2)))))))


(println
  (count (filter true?
            (for [e input-list-of-lines] (valid? e)))))


