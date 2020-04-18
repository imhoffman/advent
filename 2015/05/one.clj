
;(require 'clojure.set)
(require '[clojure.set    :as set])
(require '[clojure.string :as str])

(defn has-three-vowels? [s]
  (> (count (set/intersection
              #{\a \e \i \o \u}
              (set (keys (frequencies s)))))
     2))



(defn free-of-bad-pairs? [s]
  (if (some true? (for [pair '("ab" "cd" "pq" "xy")]
                    (str/includes? s pair)))
    false
    true))    ;; `some` returns the value, not the logical



