;;   2019 Advent Day 01
;;   https://adventofcode.com/2019/day/1
;;

;; part one
(prn
  ((fn [s]
     (->> s
          slurp
          (re-seq #"\w+")
          (map #(Long/parseLong %))
          (map #(- (/ (- % (mod % 3)) 3) 2))
          (apply +)))
   "puzzle.txt"))


;; part two
(pr
  ((fn [s]
     (->> s
          slurp
          (re-seq #"\w+")
          (map #(Long/parseLong %))
          (map (partial (fn [a m]
                 (let [f (#(- (/ (- % (mod % 3)) 3) 2) m)]
                   (if (<= f 0) a (recur (+ a f) f)))) 0))
          (apply +)))
   "puzzle.txt"))


