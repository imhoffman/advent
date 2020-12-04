
(require '[clojure.string :as str])

(prn
(apply + (map #(Long/parseLong (first %)) (re-seq #"([-|0-9]+)" (slurp "puzzle.txt"))))
)

;; 126674 too high

