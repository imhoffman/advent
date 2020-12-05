
(require '[clojure.string :as str])

(def input-objects
  (map first (re-seq #"\{([^\{]*)\}" (slurp "puzzle.txt"))))

(def red-objects
  (apply str (filter #(str/includes? % "red") input-objects)))

(prn
(-
 (apply + (map #(Long/parseLong (first %)) (re-seq #"([-|0-9]+)" (slurp "puzzle.txt"))))
 (apply + (map #(Long/parseLong (first %)) (re-seq #"([-|0-9]+)" red-objects))))
)

;; 111513 too high
;; 79532  too high

