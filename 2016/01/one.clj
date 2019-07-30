(require '[clojure.string :as str])

;;  cheat by adding trailing comma
(def directions (apply str (str/trim-newline (slurp "test3.txt")) ","))


;;  recursive function to obtain vector of directions
(conj []
  (loop [i 0
         j 0]
    (let [s (subs directions i)
          b ""]
    (for [c s]
      (if (not (= c \,))
        (recur i (+ 1 j))
        (do (subs s 0 j) (recur (+ 1 i) j)))))))

(conj []
  (for [a directions]
    (fn [c]
      (let [i (
