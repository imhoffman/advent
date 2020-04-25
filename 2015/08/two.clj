
(def file-slurp (slurp "puzzle.txt"))


(def total-encoded-characters
  (count 
    (filter #(not (= % \newline)) file-slurp)))


(def total-additional-literals
  (let [freqs (frequencies file-slurp)]
    (+
     (* 2 (freqs \newline))   ;; the quotes on every line in the file
     (freqs \\)
     (freqs \"))))


(println " total encoded characters:" total-encoded-characters)

(println " part two:" total-additional-literals)

