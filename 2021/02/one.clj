
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map #(re-matches #"(\w+) (\d+)" %) ,,)))


(loop [stack input
       x 0
       d 0]
  (if (empty? stack)
    (println (* x d))
    (let [dir ((first stack) 1)
          a (Integer/parseInt ((first stack) 2))]
      (recur
           (rest stack)
             (if (= "forward" dir)
               (+ x a)
               x)
             (case dir
               "up" (- d a)
               "down" (+ d a)
               d)))))

