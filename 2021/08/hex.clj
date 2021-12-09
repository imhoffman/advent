
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map #(re-matches #"(\w+) (\w+) (\w+) (\w+) (\w+) (\w+) (\w+) (\w+) (\w+) (\w+) \| (\w+) (\w+) (\w+) (\w+)" %) ,,)
       (map rest)
       (map #(vector (vec (take 10 %)) (vec (nthrest % 10))) ,,)))


;;
;;    A
;;  F   B
;;    G
;;  E   C
;;    D
;;

(def seven-seg-dict
  {#{:B :C} \1
   #{:A :B :C} \7
   #{:F :G :B :C} \4
   #{:A :B :G :E :D} \2
   #{:A :B :G :C :D} \3
   #{:A :F :G :C :D} \5
   #{:A :F :G :C :D :E} \6
   #{:A :B :F :G :C :D} \9
   #{:A :B :C :D :E :F} \0
   #{:A :B :C :D :E :F :G} \8
   #{:A :B :C :E :F :G} \a
   #{:C :D :E :F :G} \b
   #{:A :D :E :F} \C
   #{:D :E :G} \c
   #{:B :C :D :E :G} \d
   #{:A :D :E :F :G} \e
   #{:A :E :F :G} \f
   })


(defn deduce [line]
  (let [ins (first line)
        outs (second line)]
    (loop [wires (into #{} (apply str ins))         ;;  start will all wires in a to-do list
           pins  (reduce #(assoc %1 %2 nil) {} [:A :B :C :D :E :F :G])]  ;;  nil'ed answers
      (cond

        ;;
        ;;  output when finished
        ;;
        (empty? wires)
        (Integer/parseInt
          (apply str
                 (reduce
                   (fn [a b] (conj a
                     (seven-seg-dict    ;;  the key to the numeral dict are the mapped wires
                       (reduce #(conj %1 ((set/map-invert pins) %2)) #{} b))))
                   (vector)
                   outs))
          16)   ;;  10 for decimal, 16 for hex

        ;;
        ;;  A is 1 from 7
        ;;
        (nil? (pins :A))
        (let [A-wire
              (set/difference
                (apply set (filter #(= 3 (count %)) ins))        ;; numeral 7
                (apply set (filter #(= 2 (count %)) ins)))]      ;; numeral 1
          (recur (set/difference wires A-wire)               ;;  remove from to-do stack
                 (assoc pins :A (first A-wire))))            ;;  include in output dict

        ;;
        ;;  of numerals 6, 9, and 0, only 6 doesn't overlay 1
        ;;
        (nil? (pins :B))
        (let [B-wire
              (let [set-of-wires-for-1 (apply set (filter #(= 2 (count %)) ins))
                    set-of-wires-for-6 (first
                                         (remove (fn [s] (set/subset? set-of-wires-for-1 s))
                                                 (map set (filter #(= 6 (count %)) ins))))]
                (set/difference set-of-wires-for-1 set-of-wires-for-6))]
          (recur (set/difference wires B-wire)
                 (assoc pins :B (first B-wire))))

        ;;
        ;;  C is B from 1
        ;;
        (nil? (pins :C))
        (let [C-wire (first (remove #(= (pins :B) %) (first (filter #(= 2 (count %)) ins))))]
          (recur (set/difference wires (set (list C-wire)))
                 (assoc pins :C C-wire)))

        ;;
        ;;  4 less 2 less 1
        ;;
        (nil? (pins :F))
        (let [F-wire (set/difference
                       (apply set (filter #(= 4 (count %)) ins))    ;; numeral 4
                       (first (remove #(contains? % (pins :C))      ;; numeral 2
                                      (map set (filter #(= 5 (count %)) ins))))
                       (set (list (pins :B) (pins :C))))]           ;; numeral 1
          (recur (set/difference wires F-wire)
                 (assoc pins :F (first F-wire))))

        ;;
        ;;  the remaining member of 4
        ;;
        (nil? (pins :G))
        (let [G-wire (set/difference
                       (apply set (filter #(= 4 (count %)) ins))    ;; digit 4
                       (set (list (pins :B) (pins :C) (pins :F))))] 
          (recur (set/difference wires G-wire)
                 (assoc pins :G (first G-wire))))

        ;;
        ;;  D, A, and G are common to all five-wire decimal digits
        ;;
        (nil? (pins :D))
        (let [D-wire (set/difference
                       (apply set/intersection
                         (map set (filter #(= 5 (count %)) ins)))
                       (set (for [p (list :A :G)] (pins p))))]
          (recur (set/difference wires D-wire)
                 (assoc pins :D (first D-wire))))

        ;;
        ;;  E remains
        ;;
        (nil? (pins :E))
        (let [E-wire (set/difference
                       (set "abcdefg")
                       (set (for [p (list :A :B :C :D :F :G)] (pins p))))]
          (recur (set/difference wires E-wire)
                 (assoc pins :E (first E-wire))))

        :else
        (println " logic problem during analysis")))))



;;
;;  main
;;
(println (apply + (for [line input] (deduce line))))



