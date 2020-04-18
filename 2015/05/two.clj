
;(require 'clojure.set)
(require '[clojure.set    :as set])
(require '[clojure.string :as str])



(defn has-good-pairs? [s]
  (loop [stack s]
    (if (> 3 (count stack))
      false
      (let [pair (subs stack 0 2)]
        (if (str/includes? (subs stack 2) pair)
          true
          (recur (apply str (rest stack))))))))   ;; re-string seq



(defn has-bookend-repeats? [s]
  (loop [stack (vec s)]
    (if (> 3 (count stack))
      false
      (if (= (first stack) (second (rest stack)))
        true
        (recur (rest stack))))))


;;
;;  main program
;;

(println " part two:"

(loop [work-stack (re-seq #"\w+" (slurp "puzzle.txt"))
       counter    0]
  (if (empty? work-stack)
    counter
    (let [s (first work-stack)]
      (recur
        (rest work-stack)
        (+ counter
           (if (and
                    (has-good-pairs? s)
                    (has-bookend-repeats? s))
             1 0))))))

)


