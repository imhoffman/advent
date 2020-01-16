;;  using `filter` to cut down list using predicates rather than
;;   build up a list, thus no need for sets
(require '[clojure.string :as str])
;(require '[clojure.set :as sets])


;;  updated for part two
;;   since `has-repeats?` is applied after `is-increasing?`,
;;   multiple occurences are garaunteed to be consecutive
(defn has-repeats? [number]
  (some #(= 2 %) (vals (frequencies (str number)))))   ;; exactly 2


;;  using `partition` as seen on (apropos
;;  chars come out of partition,
;;  but `>=` requires numbers,
;;  and `parseInt` requires strings
(defn old-is-increasing? [number]
  (every? #(<=
             (Integer/parseInt (str (first %)))
             (Integer/parseInt (str (second %))))
           (partition 2 1 (str number))))

;;  `compare` can work on the ascii chars directly --- no `parseInt`
(defn is-increasing? [number]
  (every? #(>= 0 (apply compare %)) (partition 2 1 (str number))))


;;  not sure if the boundaries of `range` satisfy the rule "within the range" ...
;;   the user should inspect the two input values for compliance
(defn tally-possibilities [ni nf]
  (count (filter has-repeats? (filter is-increasing? (range ni nf)))))


;;
;;  main program
;;
;;   from the CLI
;;   https://clojure.org/reference/repl_and_main
(let [argv *command-line-args*]
  (if (not (= 2 (count argv)))
    (do
      (println " usage: clj two.clj lower-limit upper-limit")   ;; $ clj two.clj Int Int
      (System/exit 1))
    (let [lower-limit (Integer/parseInt (first argv))
          upper-limit (Integer/parseInt (second argv))]
      (println
        " number of possible passwords between" lower-limit "and" upper-limit ":"
        (tally-possibilities lower-limit upper-limit)))))


