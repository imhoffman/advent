
(require '[clojure.string :as str])
(require '[clojure.set :as set])


(def input
  (->> "puzzle.txt"
       slurp
       (#(str/split % #"\n") ,,)
       (map #(re-matches #"(\w+) (\w+) (\w+) (\w+) (\w+) (\w+) (\w+) (\w+) (\w+) (\w+) \| (\w+) (\w+) (\w+) (\w+)" %) ,,)
       (map rest)
       (map #(vector (vec (take 10 %)) (vec (nthrest % 10))) ,,)))


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
   })


(defn deduce [line]
  (let [ins (first line)
        outs (second line)]
    (loop [wires (into #{} (apply str ins))
           pins (reduce #(assoc %1 %2 nil) {} [:A :B :C :D :E :F :G])]
      (cond

        (empty? wires)
        (Integer/parseInt
          (apply str
                 (reduce
                   (fn [a b]
                     (conj a
                           (seven-seg-dict (reduce #(conj %1 ((set/map-invert pins) %2)) #{} b))))
                   (vector)
                   outs))
          10)

        (nil? (pins :A))
        (let [A-wire
              (set/difference
                (apply set (filter #(= 3 (count %)) ins))        ;; digit 7
                (apply set (filter #(= 2 (count %)) ins)))]      ;; digit 1
          (recur (set/difference wires A-wire)
                 (assoc pins :A (first A-wire))))

        (nil? (pins :B))
        (let [B-wire       ;; of 6, 9, and 0, only 6 doesn't overlay 1
              ;(first (filter #(set/subset? (apply set (filter #(= 2 (count %)) ins)) %)
              ;               (map set (filter #(= 6 (count %)) ins))))]
              (first (remove empty?
                             (map (fn [s] (set/difference
                                            (apply set (filter #(= 2 (count %)) ins)) s))
                                  (map set (filter #(= 6 (count %)) ins)))))]
          (recur (set/difference wires B-wire)
                 (assoc pins :B (first B-wire))))

        (nil? (pins :C))
        (let [C-wire (first (remove #(= (pins :B) %) (first (filter #(= 2 (count %)) ins))))]
          (recur (set/difference wires (set (list C-wire)))
                 (assoc pins :C C-wire)))

        (nil? (pins :F))
        (let [F-wire (set/difference
                       (apply set (filter #(= 4 (count %)) ins))    ;; digit 4
                       (first (remove #(contains? % (pins :C))      ;; digit 2
                                      (map set (filter #(= 5 (count %)) ins))))
                       (set (list (pins :B) (pins :C))))]           ;; digit 1
          (recur (set/difference wires F-wire)
                 (assoc pins :F (first F-wire))))

        (nil? (pins :G))
        (let [G-wire (set/difference
                       (apply set (filter #(= 4 (count %)) ins))    ;; digit 4
                       (set (list (pins :B) (pins :C) (pins :F))))] 
          (recur (set/difference wires G-wire)
                 (assoc pins :G (first G-wire))))

        (nil? (pins :D))
        (let [D-wire (set/difference
                       (apply set/intersection
                         (map set (filter #(= 5 (count %)) ins)))
                       (set (for [p (list :A :B :C :F :G)] (pins p))))]
          (recur (set/difference wires D-wire)
                 (assoc pins :D (first D-wire))))

        ;(nil? (pins :E))
        :else
        (let [E-wire (set/difference
                       (set "abcdefg")
                       (set (for [p (list :A :B :C :D :F :G)] (pins p))))]
          (recur (set/difference wires E-wire)
                 (assoc pins :E (first E-wire))))

        ))))





(println (apply + (for [line input] (deduce line))))



