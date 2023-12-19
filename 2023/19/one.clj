
(require '[clojure.string :as str])
(require '[clojure.set :as set])
(require '[clojure.core.reducers :as r])


(def input (->> "puzzle.txt"
                slurp
                (#(str/split % #"\n\n") ,,)))


(defn parse-rules [s]
  (let [v (rest (re-matches #"([a-z]+)\{(.*)\}" s))
        k (first v)
        rs (str/split (second v) #",")]
    (loop [rules rs
           d (vector)]
      (if (empty? rules)
        (vector k d)
        (let [p (or (re-matches #"([^\<\>]+)" (first rules))
                    (vec (rest (re-matches #"([xmas])([\<\>])(\d+):([a-zAR]+)" (first rules)))))]
          (println (first rules) p)
          (recur
            (rest rules)
            (if (= 2 (count p))
              (conj d {:dest (second p)})
              (conj d {:attr (p 0), :op (p 1), :val (Integer/parseInt (p 2)), :dest (p 3)}))))))))

(def rules (->> (first input)
                (#(str/split % #"\n") ,,)
                (map #(parse-rules %) ,,)
                (into {} ,,)))

(prn rules)


(defn parse-part [bracketed]
  (let [s (second (re-matches #"\{(.*)\}" bracketed))
        v (mapv #(Integer/parseInt %) (re-seq #"\d+" s))]
    {"x" (v 0), "m" (v 1), "a" (v 2), "s" (v 3)}))

(def parts (->> (second input)
                (#(str/split % #"\n") ,,)
                (map #(parse-part %) ,,)))
(prn parts)



