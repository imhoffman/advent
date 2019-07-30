(require '[clojure.string :as str])

;;  file I/O
;;   cheat by adding trailing comma
(def directions (apply str (str/trim-newline (slurp "input.txt")) ","))

;;  recursive csv parser to load vector of instructions
;;   use vector b/c conj goes on tail and b/c order matters
(def dirvec
  (loop [a     directions
         accum [] ]
    (let [j (str/index-of a \,)    ; needs final comma added to slurp
          r (subs a 0 j)]
    (if (not (str/index-of a \space))
      (conj accum r)               ; base case ... add final entry and return
      (recur (subs a (+ j 2)) (conj accum r))))))

;(println (first dirvec) (last dirvec))

