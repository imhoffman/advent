
(require '[clojure.string :as str])

(defn differencer [list-of-strings]
  (let [flattened-master-string-of-all-chars (apply str list-of-strings)]
    (- (+ 
         (count flattened-master-string-of-all-chars)
         (* 2 (count list-of-strings)))   ;; the removed "'s
       (loop [stack flattened-master-string-of-all-chars
              accum 0]
         (if (empty? stack)
           accum
           (if (= (first stack) \\)
             (case (second stack)
               \" (recur (nthrest stack 2) (inc accum))
               \\ (recur (nthrest stack 2) (inc accum))
               \x (recur (nthrest stack 4) (inc accum))
                  ;; can't happen: "The only escape sequences used..."
                  (recur (rest stack) accum))
             (recur (rest stack) (inc accum))))))))


(def input-list-of-data-entries
  (with-open [f (clojure.java.io/reader "puzzle.txt")]
    (reduce conj () (map #(subs % 1 (dec(count %))) (line-seq f)))))

;(println " read" (count input-list-of-data-entries) "lines")

(println " part one:" (differencer input-list-of-data-entries))

;; 4978 is too high
;; 4977 is too high

