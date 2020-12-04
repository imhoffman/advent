
(require '[clojure.string :as str])

(def input-string
  (-> "puzzle.txt"
      slurp
      str/trim))

(def alphabet
  "abcdefghijklmnopqrstuvwxyz")


(defn valid? [s]
  (and
    (not (re-seq #"(i|o|l)" s))                           ;;  bad letters
    (<= 2 (count (set (map first (re-seq #"(.)\1" s)))))  ;;  two or more different doubles
    (loop [i 0]                                           ;;  straight
      (cond                                               ;;   bad letters are gone by now, but
        (= i (- (count s) 3))                             ;;   subs like "ghj" must still fail
          false
        (str/includes? alphabet (subs s i (+ i 3)))
          true
        :else
          (recur (inc i))))))


(defn increment-password [s]
  (let [a alphabet     ;;  remove the bad letters now? rather than
        p (vec a)      ;;   waste the validator's time later?
        n (count p)]
    (loop [v     (vec (reverse s))
           place 0
           ni    (mod (inc (str/index-of a (v place))) n)]
      (let [out (assoc v place (p ni))]
        (if (not= ni 0)              ;;  carry the z?
          (str/join (reverse out))
          (recur
            out
            (inc place)
            (mod (inc (str/index-of a (v (inc place)))) n)))))))


(println
  " the first valid password after"
  input-string
  "is"
  (loop [s (increment-password input-string)]
    (if (valid? s)
      s
      (recur (increment-password s)))))

