
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
;;  make sense of the regex groups in a single input line; parsing
;;   any literals alongs with the op, etc.
;;   return the dict-of-dicts entry
;;   for NOT, enter the argument as an input
;;
(defn parser [rev]
  (let [v  (vec (rest rev))
        pd (cond
             ;;  NOT is the only leading op
             (= (v 0) "NOT")
             {:inputs (vector (v 1)), :op (v 0), :output (v 2)}

             ;;  numerical literal assigmments
             ;;   there are also literal inputs to binary OPs...
             (and (= 2 (count v)) (re-matches #"\d+.*" (v 0)))
             {:inputs (vector (v 0)), :op nil, :output (v 1)}

             ;;  normal val OP val structure
             :else
             {:inputs (vector (v 0) (v 2)), :op (v 1), :output (v 3)})]
    ;; find the numbers ...
    ;;(#(Integer/parseInt (second %)) ,,))
    pd))


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map #(re-matches #"([0-9a-z]*?)\s*([A-Z]*)\s*([0-9a-z]+) -> ([a-z]+)" %) ,,)
       (map parser ,,)))


;;
;;  main
;;

(println input)
