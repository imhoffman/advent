;; NOT T J       ; load true into J
;; AND D J       ; J is true if D is true
;; WALK

;  this won't jump if there is nowhere to land at D
;  ...but I'll walk into a hole at A B or C



(defn droid [A B C D T J]
  (and D (not T))     ; D is a tile
  )


(def A true)
(def B true)
(def C true)
(def D false)

(println
 "\n     A:" A
 "\n     B:" B
 "\n     C:" C
 "\n     D:" D
 "\n Jump?:" (droid A B C D false false))

