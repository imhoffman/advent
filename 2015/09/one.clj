
(require '[clojure.string :as str])

(def distances-dict
  ((fn [filename]
     (->> filename
          slurp
          (#(str/split % #"[\r\n|\n|\r]"))
          (map #(str/split % #"\s"))
          (#(loop [e %, d {}]
              (if (empty? e)
                d
                (recur
                  (rest e)
                  (let [s (first e)]
                    (assoc d #{(s 0) (s 2)} (s 4)))))))
          ))
     "puzzle.txt"))


(doseq [kv distances-dict]
  (println (key kv) (val kv)))

