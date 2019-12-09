(require '[clojure.string :as str])

(def sep #"[)]")     ;; my vim highlighter is reading the guts of the regex!

;;  the input list is called `the orbit map` on advent
;;   produces a vector of barycenter--satellite vector pairs
(defn parse-orbits [orbit-map] 
  (vec (for [listing orbit-map]
         (str/split listing sep))))

(defn bary [pair] (first pair))

(defn satl [pair] (last pair))

;;  for determining direct orbits
;;   call initially with dict as an empty hash map (i.e., dictionary)
;;
;;   do I need to pass the empty dict?  or can I return a `let`ed dict?
(defn direct-orbit-dict [pairs dict]
  (let [a (bary (first pairs))]
    (if (empty? a)
      dict
      (recur (rest pairs)
             (assoc dict a (if (contains? dict a) (inc (get dict a)) 1)))
      )))


;;  find the intersection of the barycenter set and the satellite set ...




;;
;;  main program
;;
;;   file I/O
;;   https://clojuredocs.org/clojure.core/line-seq
(def input
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj () (line-seq f))))
(println "Read" (count input) "lines.")

;;  load up vector of pairs for processing going forward
(def pairs (parse-orbits input))


(println " Number of direct orbits in map:" (apply + (vals (direct-orbit-dict pairs {}))))

