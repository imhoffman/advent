
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
          ;(println (first rules) p)
          (recur
            (rest rules)
            (if (= 2 (count p))
              (conj d {:dest (second p)})
              (conj d {:attr (p 0), :op (p 1), :val (Integer/parseInt (p 2)), :dest (p 3)}))))))))

(def rules (->> (first input)
                (#(str/split % #"\n") ,,)
                (map #(parse-rules %) ,,)
                (into {} ,,)))

;(prn rules)


(defn parse-part [bracketed]
  (let [s (second (re-matches #"\{(.*)\}" bracketed))
        v (mapv #(Integer/parseInt %) (re-seq #"\d+" s))]
    {"x" (v 0), "m" (v 1), "a" (v 2), "s" (v 3)}))

(def parts (->> (second input)
                (#(str/split % #"\n") ,,)
                (map #(parse-part %) ,,)))
;(prn parts)


(defn accept? [part & rule]
  (let [k (or rule "in")
        r (or (re-matches #"[AR]" k) (rules k))]
    (cond
      (= r "R") false
      (= r "A") true
      :default
      (recur part
             (loop [rv r]
               (letfn [(f [a b] (if (= "<" ((rv 0) :op)) (< a b) (> a b)))]
                 (cond
                   (= 1 (count rv)) ((rv 0) :dest)
                   (f (part ((rv 0) :attr)) ((rv 0) :val)) ((rv 0) :dest)
                   :default (recur (vec (rest rv))))))))))



(println
  (apply +
         (for [part parts]
           (if (accept? part)
             (apply + (vals part))
             0))))




