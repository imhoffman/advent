
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(defn to-2r [n]
  (->> n
      (#(Integer/toString % 2) ,,)
      (Integer/parseInt ,,)
      (format "%08d" ,,)
      (list "2r" ,,)
      str/join))



;;
;;  make sense of the regex groups
;;   return the dictionary entry
;;   for NOT, enter the argument as an input
;;    that is, for "NOT a -> b" enter {:in1 "a", :in2 nil, :op "NOT", :out "b"}
;(defn parser [s]
;  (cond
;       (#(Integer/parseInt (second %)) ,,))


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)))
       ;(re-matches #"([a-z]*)\s*([A-Z]+)\s([0-9a-z]+) -> ([a-z]+)" ,,)))


;;
;;  main
;;


