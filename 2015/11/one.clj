
(require '[clojure.string :as str])

(def input-string
  (slurp "puzzle.txt"))

(def alphabet
  "abcdefghijklmnopqrstuvwxyz")


(defn valid? [s]
  (and
    (not (re-seq #"(i|o|l)" s))                           ;;  bad letters
    (<= 2 (count (set (map first (re-seq #"(.)\1" s)))))  ;;  two or more different doubles
    (loop [i 0]
      (cond
        (= i (- (count s) 3))
          false
        (str/includes? alphabet (subs s i (+ i 3)))
          true
        :else
          (recur (inc i))))))


;(defn increment-password [s]




