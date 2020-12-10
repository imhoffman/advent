
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n"))
       (map #(Long/parseLong %))
       sort
       reverse
       (#(conj % (+ 3 (first %))))
       reverse
       (#(conj % 0))))


(defn sequence-counter [n]
  (loop [stack input
         c     0
         cs    []]
    (if (empty? (rest stack))
      cs
      (let [found (= n (- (second stack) (first stack)))]
        (recur
          (rest stack)
          (if found (inc c) 0)
          (if found cs (if (< 0 c) (conj cs c) cs)))))))


;(prn input)
;(prn (sequence-counter 3))

(defn factorial [n]
  (loop [counter n
         accum   1]
    (if (<= counter 0)
      accum
      (recur (dec counter) (* accum counter)))))


(prn
(let [ones   (sequence-counter 1)
      twos   (sequence-counter 2)
      threes (sequence-counter 3)]
  (apply
    *
    (concat
      (for [n ones] (factorial (- n 1)))
      (for [n twos] (* (factorial (- n 1)) (factorial (- n 2))))
      (for [n threes] n))))
)
           






