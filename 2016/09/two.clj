
(require '[clojure.string :as str])

(def compressed (->> "puzzle.txt"
                     slurp
                     (re-matches #"^(.*)\n*$" ,,)
                     second))


(defn decompressed-length [s-in]
  (loop [s s-in
         len 0N]
    (let [k (str/index-of s \()]
      (cond
        (not k)
          (+ len (count s))
        (> k 0)
          (recur
            (apply str (nthrest s k))
            (+ len k))
        :else
          (let [[_ nstr mstr bulk] (re-matches #"^\((\d+)x(\d+)\)(.*)" s)
                [n m]              (vector (Integer/parseInt nstr) (Integer/parseInt mstr))
                data               (apply str (take n bulk))]
            (recur
              (apply str (drop n bulk))
              (+ len (* m (decompressed-length data)))))))))


(println (decompressed-length compressed))


