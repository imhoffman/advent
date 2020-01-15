;;  using `filter` to cut down list using predicates rather than
;;   build up a list, thus no need for sets
(require '[clojure.string :as str])
;(require '[clojure.set :as sets])


;;  since there are no leading zeros, a zero anywhere
;;  would break the always-increasing rule, but this
;;  predicate is superfluous, and is probably slower
;;  then letting `is-increasing?` catch them...
(defn no-zeros? [number]
  (not (some #(= \0 %) (str number))))    ; easier to negate here than in filter


;;  using `partition` as seen on (apropos
;;  updated for part two
(defn has-repeats? [number]
  (some
    #(
      (let [a (first %)
            b (second %)
            c (nth % 2)]    ;; zero-indexed
        (if (= a b c)
          false
          (or (= a b) (= b c)))))
    (partition 3 1 (str number))))


;;  chars come out of partition,
;;  but `>=` requires numbers,
;;  and `parseInt` requires strings
(defn is-increasing? [number]
  (every? #(<=
           (Integer/parseInt (str (first %)))
           (Integer/parseInt (str (second %))))
          (partition 2 1 (str number))))


(defn tally-possibilities [ni nf]
  (let [zero-free-list (filter no-zeros? (range ni nf))]
    (count (filter has-repeats? (filter is-increasing? zero-free-list)))))


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


