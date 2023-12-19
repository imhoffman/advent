
(require '[clojure.string :as str])
(require '[clojure.set :as set])
(require '[clojure.core.reducers :as r])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (map #(re-matches #"([AKQJT\d]{5}) (\d+)" %) ,,)
                (map rest ,,)
                (map #(vector (first %) (Integer/parseInt (second %))) ,,)
                (into {} ,,)))

;(prn input)

(def ranks {\A 14, \K 13, \Q 12, \J 11, \T 10, \9 9, \8 8, \7 7, \6 6, \5 5, \4 4, \3 3, \2 2, \1 1})


(defn ranker [pair1 pair2]
  (let [h1 (first pair1)
        h2 (first pair2)
        f1 (frequencies h1)
        f2 (frequencies h2)
        m1 (apply max (vals f1))
        m2 (apply max (vals f2))
        v1 (vec h1)
        v2 (vec h2)]
    (letfn [(tie-break [v1 v2]
              (loop [i 0]
                (cond
                  (= i 5) 0
                  (> (ranks (v1 i)) (ranks (v2 i))) +1
                  (< (ranks (v1 i)) (ranks (v2 i))) -1
                  :default (recur (inc i)))))]
      (cond
        (or (= m1 5) (= m2 5))
        (cond
          (> m1 m2) +1
          (< m1 m2) -1
          :default (tie-break v1 v2))

        (or (= m1 4) (= m2 4))
        (cond
          (> m1 m2) +1
          (< m1 m2) -1
          :default (tie-break v1 v2))

        ;;  full-house possiblities
        (and (= '(2 3) (sort (vals f1))) (not= '(2 3) (sort (vals f2)))) +1
        (and (= '(2 3) (sort (vals f2))) (not= '(2 3) (sort (vals f1)))) -1
        (and (= '(2 3) (sort (vals f2))) (= '(2 3) (sort (vals f1))))
        (tie-break v1 v2)

        (or (= m1 3) (= m2 3))
        (cond
          (> m1 m2) +1
          (< m1 m2) -1
          :default (tie-break v1 v2))

        ;;  two pair in one or both
        (and (= '(1 2 2) (sort (vals f1))) (not= '(1 2 2) (sort (vals f2)))) +1
        (and (= '(1 2 2) (sort (vals f2))) (not= '(1 2 2) (sort (vals f1)))) -1
        (and (= '(1 2 2) (sort (vals f2))) (= '(1 2 2) (sort (vals f1))))
        (tie-break v1 v2)

        ;;  one pair in each
        ;(and (= '(1 1 1 2) (sort (vals f2))) (= '(1 1 1 2) (sort (vals f1))))
        ;(tie-break v1 v2)

        ;;  one pair possibilities
        (or (= m1 2) (= m2 2))
        (cond
          (> m1 m2) +1
          (< m1 m2) -1
          :default (tie-break v1 v2))

        ;;  high card in each
        (and (= 1 m1) (= 1 m2))  ;;  or simply (= 1 m1)
        (tie-break v1 v2)

        :default 0))))


(println
  (loop [v (sort ranker input)
         i 1
         c 0]
    (if (empty? v)
      c
      (recur
        (rest v)
        (inc i)
        (+ c (* i (second (first v))))))))


;;  254609753 too high
;;  247280583 too high
;;  246390747 too low

;;  247242050 wrong
;;  246955966 wrong
;;  246548099 wrong


