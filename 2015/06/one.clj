
(require '[clojure.string :as str])

(def initial-screen
  (let [dim 1000]
    (loop [i     0
           array (transient [])]
      (if (= i dim)
        (persistent! array)
        (recur (inc i) (conj! array (into [] (repeat dim 0))))))))


;;  input `s` is a single instruction line from the input file
;;  return is a vector of
;;   [operation [x_init,y_init] [x_final,y_final]]
;;   for subsequent destructuring
;;  splitting the entire string, only to use `first` to get op-str
;;   ...how to do it better? subs of an index-of of a regex ??
(defn parser [s]
  (let [op-str          (first (str/split s #"\d+"))
        [xi,yi,_,xf,yf] (vec (str/split (subs s (count op-str)) #"[,\s+]"))
        long            #(Long/parseLong %)]      ;; overload within scope
    (vector
      (case (last (str/trim op-str))  ;; the final letter of the instructions is unique
        \n :turn-on
        \f :turn-off
        \e :toggle)
      [(long xi) (long yi)]
      [(long xf) (long yf)])))


;;  dictionary of functions with which to map as per instruction
;;   map will return a list ... so must `into []` on the other end
(def ops
  {:toggle   #(if (= % 1) 0 1)
   :turn-on  (fn [x] 1)
   :turn-off (fn [x] 0)})



(defn run-screen [filename]
  (loop [instr-list (str/split-lines (slurp filename))
         screen     initial-screen]
    (if (empty? instr-list)
      screen
      (let [instr                (first instr-list)
            [op [xi,yi] [xf,yf]] (parser instr)]
        (println instr op xi yi xf yf)
        (recur (rest instr-list) screen)))))







