(define registry (list))

(define file (open-input-file "input.txt"))
(do ((line (read-line file) (read-line file))) ((eof-object? line))
        (set! registry (append registry (list line))))

(for-each
 (lambda (s) (display s) (newline)) registry)


