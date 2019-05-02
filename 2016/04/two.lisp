(defun range (min max &optional (step 1))
  (when (<= min max)
    (cons min (range (+ min step) max step))))

(print (range 0 8))

(defun counter (ch str)
  (let ((i 0))
  (loop for j from 0 to (- (length str) 1) do
    (if (string= ch (char str j)) (setf i (+ i 1))))
  (format t "~&~% '~a' has ~d '~a's~%~%" str i ch)
  (values))
)
;;(print i)     ;; fails because outside of let

(counter "e" "the change has come she's under my thumb")

;; Does my read loop involve full traversal ?
;;  https://stackoverflow.com/questions/13359025/adding-to-the-end-of-list-in-lisp
;; should the push instead be a lambda that acts like the range function above?
(setf registry (list))
(let ((fp (open "input.txt" :if-does-not-exist nil)))
   (when fp
      (loop for line = (read-line fp nil)
      
      while line do (push line registry))
      (close fp)
   )
)
(setf registry (reverse registry))

(loop for s in registry do
  (format t "~& ~a~%" s))
