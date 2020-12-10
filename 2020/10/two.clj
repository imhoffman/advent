
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


;;  runs of same-n-jumps
(defn sequence-counter [n]
  (loop [stack input
         c     0
         cs    []]
    ;(println " working on" (take 4 stack) "... c:" c)
    (if (empty? (rest stack))
      cs
      (let [found (= n (- (second stack) (first stack)))]
        (recur
          (rest stack)
          (if found (inc c) 0)
          (if found cs (if (< 0 c) (conj cs (inc c)) cs)))))))


;;  any of these (e.g. 2 4 5 or 6 8 9)
;;   that break over same-jump seq counts
(defn two-and-ones-counter [sorted-coll]
  (loop [stack sorted-coll
         c     0
         i     0]
    ;;  I don't have any two-jumps ...
    ))


(defn factorial [n]
  (loop [counter n
         accum   1N]
    (if (<= counter 0)
      accum
      (recur (dec counter) (* accum counter)))))


(defn ways [n]     ;; n is length of one-jumps, e.g. (4 5 6 7) --> 4
   (cond
     (> n 4) (* (* 7 (quot n 5)) (ways (- n 5)))
     (= n 4) 4
     (= n 3) 2
     :else   1))

(prn
(let [ones   (sequence-counter 1)      ;; the 1's are what matters
      twos   (sequence-counter 2)      ;; I don't have any two-seqs
      threes (sequence-counter 3)]     ;; these cannot be rearranged 
  (apply * (for [n ones] (ways n))))
)
           

;; 46438023168 too low
;; 212986666247081951232 too high
;; 371504185344 too low
;; 95105071448064



