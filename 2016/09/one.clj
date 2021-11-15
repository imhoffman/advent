
(require '[clojure.string :as str])

(def compressed (->> "puzzle.txt"
                     slurp
                     (re-matches #"^(.*)\n*$" ,,)
                     second))


(defn decompress [s-in]
  (loop [s s-in
         out ""]
    (cond
      (empty? s) out
      (not= \( (first s))
        (recur
          (apply str (rest s))
          (str out (first s)))
      :else
        (let [[_ nstr mstr bulk] (re-matches #"^\((\d+)x(\d+)\)(.*)" s)
              [n m]              (vector (Integer/parseInt nstr) (Integer/parseInt mstr))
              data               (apply str (take n bulk))]
          (recur
            (apply str (drop n bulk))
            (apply str out (repeat m data)))))))


;(println (decompress compressed))
(println (count (decompress compressed)))

