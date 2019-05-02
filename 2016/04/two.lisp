(defun range (min max &optional (step 1))
  (when (<= min max)
    (cons min (range (+ min step) max step))))

(defun counter (ch str)
  (let ((i 0))
  (loop for j from 0 to (- (length str) 1) do
    (if (string= ch (char str j)) (setf i (+ i 1))))
  (format t "~&~% '~a' has ~d '~a's~%~%" str i ch)
  (values))
)
;;(print i)     ;; fails because outside of let

(counter "e" "the change has come she's under my thumb")

;; figure out file i/o
;;  http://www.gigamonkeys.com/book/files-and-file-io.html
;; use recursive cons to add lines to a list ?
