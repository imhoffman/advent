
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n") ,,)
                (mapv vec ,,)))

;(prn input)

(def nr (count input))
(def nc (count (input 0)))

;; 1 2 3
;; 4 . 5
;; 6 7 8
(defn search [r c]
  (if (= \X ((input r) c))
  (let [dirs (into []
                   (set (flatten (apply conj
                     (filter some?
                       (vector
                         (if (< (+ r 3) nr) [7] nil)
                         (if (> (- r 2) 0) [2] nil)
                         (if (< (+ c 3) nc) [5] nil)
                         (if (> (- c 2) 0) [4] nil)
                         (if (and (> (- r 2) 0) (> (- c 2) 0)) [1] nil)
                         (if (and (> (- r 2) 0) (< (+ c 3) nc)) [3] nil)
                         (if (and (< (+ r 3) nr) (> (- c 2) 0)) [6] nil)
                         (if (and (< (+ r 3) nr) (< (+ c 3) nc)) [8] nil)))))))]
    (loop [ds dirs
           o 0]
      (if (empty? ds)
        o
        (recur
          (rest ds)
          (case (first ds)
            5 (if (= "XMAS" (apply str (take 4 (subvec (input r) c)))) (inc o) o)
            4 (if (= "XMAS" (apply str (take 4 (reverse (subvec (input r) 0 (inc c)))))) (inc o) o)
            1 (if (and (= \M ((input (- r 1)) (- c 1))) (= \A ((input (- r 2)) (- c 2))) (= \S ((input (- r 3)) (- c 3)))) (inc o) o)
            3 (if (and (= \M ((input (- r 1)) (+ c 1))) (= \A ((input (- r 2)) (+ c 2))) (= \S ((input (- r 3)) (+ c 3)))) (inc o) o)
            6 (if (and (= \M ((input (+ r 1)) (- c 1))) (= \A ((input (+ r 2)) (- c 2))) (= \S ((input (+ r 3)) (- c 3)))) (inc o) o)
            8 (if (and (= \M ((input (+ r 1)) (+ c 1))) (= \A ((input (+ r 2)) (+ c 2))) (= \S ((input (+ r 3)) (+ c 3)))) (inc o) o)
            2 (if (and (= \M ((input (- r 1)) c)) (= \A ((input (- r 2)) c)) (= \S ((input (- r 3)) c))) (inc o) o)
            7 (if (and (= \M ((input (+ r 1)) c)) (= \A ((input (+ r 2)) c)) (= \S ((input (+ r 3)) c))) (inc o) o))))))
  0))


(println 
  (loop [r 0
         c 0
         o 0]
    (if (= nr r)
      o
      (if (= c (dec nc))
        (recur (inc r) 0 (+ o (search r c)))
        (recur r (inc c) (+ o (search r c)))))))



