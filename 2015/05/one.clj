
;(require 'clojure.set)
(require '[clojure.set    :as set])
(require '[clojure.string :as str])


(defn has-three-vowels? [s]
  (let [freqs      (frequencies s)
        vowel-list (into () (set/intersection
                              #{\a \e \i \o \u}
                              (set (keys freqs))))]
    (loop [work-stack vowel-list
           counter    0]
      (if (empty? work-stack)
        (> counter 2)
        (recur
          (rest work-stack)
          (+ counter (freqs (first work-stack))))))))



(defn free-of-bad-pairs? [s]
  (if (some true? (for [pair (list "ab" "cd" "pq" "xy")]
                    (str/includes? s pair)))
    false
    true))    ;; `some` returns the value, not the logical



(defn has-repeats? [s]
  (loop [stack (vec s)]
    (if (or (empty? stack) (> 2 (count stack)))
      false
      (if (= (first stack) (second stack))
        true
        (recur (rest stack))))))


;;
;;  main program
;;

(println " part one:"

(loop [work-stack (re-seq #"\w+" (slurp "puzzle.txt"))
       counter    0]
  (if (empty? work-stack)
    counter
    (let [s (first work-stack)]
      (recur
        (rest work-stack)
        (+ counter
           (if (and
                    (free-of-bad-pairs? s)
                    (has-three-vowels? s)
                    (has-repeats? s))
             1 0))))))

)


