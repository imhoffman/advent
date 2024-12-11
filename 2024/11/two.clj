
(require '[clojure.string :as str])
(require '[clojure.set :as set])

(def input (->> "puzzle.txt"
                slurp
                (re-seq #"(\d+)" ,,)
                (map rest ,,)
                (mapv #(Long/parseLong (first %)) ,,)))

(prn input)

(defn insertv [v x idx]
  (let [[beginning end] (split-at v idx)]
    (vec (apply concat beginning x end))))


;;
;;  change this to [v i] and recur over only the first number, then move on;
;;  the main loop below will still also be over the blink number, but this
;;   routine will also consume it;
;;  not sure that this is a win, since maybe this is a number-theory game,
;;   but at least it will clean up the consumption;
;;
(defn blink [v]
  (vec (flatten
         (reduce
           (fn [a b]
             (conj a
                   (cond
                     (= b 0)
                     1N

                     (= 0 (mod (count (str b)) 2))
                     (let [s (str b)
                           vs (split-at (quot (count s) 2) s)
                           ss (mapv #(apply str %) vs)
                           vn (mapv #(Long/parseLong %) ss)]
                       vn)

                     :else
                     (* 2024N b)))) [] v))))

(println
  (loop [v input
         r (vector (first v))
         i 0
         T 0N]
    (println i)
    (cond
      ;(nil? r)
      (empty? v)
      T

      (= i 25)
      (recur
        (vec (rest v))
        (vector (first (rest v)))
        0
        (+ T (count r)))

      :else
      (recur
        v
        (blink r)
        (inc i)
        T))))









