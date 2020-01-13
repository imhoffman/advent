;;  using `filter` to cut down list using predicates rather than
;;   build up a list, thus no need for sets
(require '[clojure.string :as str])
;(require '[clojure.set :as sets])


;;  since there are no leading zeros, a zero anywhere
;;  would break the always-increasing rule
(defn has-zeros? [number]
  (some (map #(= \0 %) (str number))))


;;  using `partition` as seen on (apropos
(defn has-repeats? [number]
  (some (map #(= (first %) (second %)) (partition 2 1 (str number)))))



(defn tally-possibilities [ni nf]
  (let [zeros-free-list (range ni nf)]   ; ... `filter` here, etc
    (count zeros-free-list)))


;;
;;  main program
;;
;;   from the CLI
;;   https://clojure.org/reference/repl_and_main
(let [argv *command-line-args*]
  (if (not (= 2 (count argv)))
    (do
      (println " usage: clj one.clj lower-limit upper-limit")   ;; $ clj one.clj Int Int
      (System/exit 1))
    (let [lower-limit (Integer/parseInt (first argv))
          upper-limit (Integer/parseInt (second argv))]
      (println
        " number of possible passwords between" lower-limit "and" upper-limit ":"
        (tally-possibilities lower-limit upper-limit)))))


