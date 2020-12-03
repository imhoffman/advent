
(require '[clojure.string :as str])

(def input-string
  (slurp "puzzle.txt"))

(def alphabet
  "abcdefghijklmnopqrstuvwxyz")


(defn valid? [s]
  (and
    (not (re-seq #"(i|o|l)" s))                           ;;  bad letters
    (<= 2 (count (set (map first (re-seq #"(.)\1" s)))))  ;;  two or more different doubles
    (loop [i 0]                                           ;;  straight
      (cond                                               ;;   bad letters are gone by now,
        (= i (- (count s) 3))                             ;;   but stuff like "ghj" must still fail
          false
        (str/includes? alphabet (subs s i (+ i 3)))
          true
        :else
          (recur (inc i))))))


(defn increment-password [s]
  (let [p (vec "abcdefghjkmnpqrstuvwxyz")  ;;  remove the bad letters now, rather than
        n (dec (count p))]                 ;;   waste the validator's time
    (loop [stack (reverse s)
           accum []]
      (if (empty? stack)
        (str/join (reverse accum))
        (let [next-index (mod (inc (str/index-of (first stack))) n)]
          (recur
            (if (= 0 next-index)
              (loop ;over letters until no carry... then return rest of that stack
                )
              (rest stack))  ;; no carry needed
            (conj accum (p next-index))))))))






