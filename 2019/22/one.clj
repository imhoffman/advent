(require '[clojure.string :as str])

;;  parse the instruction from the puzzle input into a
;;  two-elem list of a technique and its accompanying integer
(defn technique-parser [technique-instruction]
  (let [technique (str/split technique-instruction #"\s+")]
    (cond
      (and (= (first technique) "deal") (= (second technique) "into"))
        (list "deal" -1)
      (and (= (first technique) "deal") (= (second technique) "with"))
        (list "increment" (Integer/parseInt (last technique)))
      (= (first technique) "cut")
        (list "cut" (Integer/parseInt (last technique)))
      :else (println " problem parsing technique"))))


;;  ruleset for "deal with increment N" on current deck
;;  "Of course, this technique is carefully designed..."  :)
;;  I'm going to cheat the Lisp gods and make a mutable Java array
(defn increment-deal [N deck]
  (let [deck-array (into-array Integer/TYPE deck)]
    (dotimes [i (count deck)]
      (let [j (mod (+ N i) N)]
        (aset deck-array j (get deck j)))
      (into [] deck-array))))



;;  perform the requested shuffle technique on the
;;  current state of the deck...return the new state of the deck
(defn shuffle-exec [instruction deck]
  (let [technique (technique-parser instruction)]
    (cond
      (= (first technique) "deal")
        (reverse deck)
      (= (first technique) "cut")
        (let [n (second technique)]
          (conj (subvec deck n) (subvec deck 0 n)))
      (= (first technique) "increment")
        (let [n (second technique)]
          (increment-deal n deck))
      :else
        (println " problem executing shuffle technique"))))


;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj [] (line-seq f))))           ;; order matters, so vector
(println "Read" (count input) "lines.")


(def number-of-cards 10007)

(def factory-deck (vec (range number-of-cards)))

(doseq [x input] (get (shuffle-exec (technique-parser x) factory-deck) 0))

;(println " card 2019 is at location"
;         (.indexOf 
;  ((fn [list-of-instructions deck]
;    (if (empty? list-of-instructions)
;      deck
;      (recur
;        (rest list-of-instructions)
;        (shuffle-exec (technique-parser (first list-of-instructions)) deck))))
;    input factory-deck)
;  2019))

