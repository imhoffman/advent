
(prn
  ((fn [filename]
     (->> filename
          slurp
          (re-seq #"\w+")
          (map #(clojure.string/split % #"[x]"))
          (map #(vec (map (fn [s] (Long/parseLong s)) %)))
          (map #(+ (* 2 (% 0) (% 1))
                   (* 2 (% 0) (% 2))
                   (* 2 (% 1) (% 2))
                   (min (* (% 0) (% 1)) (* (% 0) (% 2)) (* (% 1) (% 2)))))
          (apply +)))
   "puzzle.txt"))


(prn
  ((fn [filename]
     (->> filename
          slurp
          (re-seq #"\w+")
          (map #(clojure.string/split % #"[x]"))
          (map #(vec (map (fn [s] (Long/parseLong s)) %)))
          (map #(+ (* (% 0) (% 1) (% 2))
                   (* 2 (min (+ (% 0) (% 1))
                             (+ (% 0) (% 2))
                             (+ (% 1) (% 2))))))
          (apply +)))
   "puzzle.txt"))


(prn
  ((fn [filename]
     (->> filename
          slurp
          (re-seq #"\w+")
          (map #(clojure.string/split % #"[x]"))
          (map #(vec (map (fn [s] (Long/parseLong s)) %)))
          (map #(vector
                  (+ (* 2 (% 0) (% 1))
                     (* 2 (% 0) (% 2))
                     (* 2 (% 1) (% 2))
                     (min (* (% 0) (% 1)) (* (% 0) (% 2)) (* (% 1) (% 2))))
                  (+ (* (% 0) (% 1) (% 2))
                     (* 2 (min (+ (% 0) (% 1))
                               (+ (% 0) (% 2))
                               (+ (% 1) (% 2)))))))
          (reduce (fn [v1 v2] (vector (+ (v1 0) (v2 0)) (+ (v1 1) (v2 1)))))))
   "puzzle.txt"))


