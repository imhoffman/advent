
skip_moves_lines = 0
skip_cups_lines = 5

a1 = 1.0e-05
e1 = 0.8
a2 = 1.0e-05
e2 = 1.0

pow1n(x) = a1*x**e1
pow2n(x) = a2*x**e2

FIT_LIMIT = 1e-07
set fit quiet
set fit errorvariables

set datafile missing "NA"
stats "times.txt" every ::skip_moves_lines using 3 nooutput name "Y" 
stats "times.txt" every ::skip_moves_lines using 1 nooutput name "X"

fit pow1n(x) "times.txt" every ::skip_moves_lines::skip_moves_lines+5 using 1:3:($4/$3) via a1,e1
fit pow2n(x) "times.txt" every ::skip_cups_lines using 2:3:($4/$3) via a2,e2

set terminal pngcairo dashed enhanced size 1024,768 crop font "Helvetica, 18" linewidth 2
set output "plot.png"
set size 1.0,0.75

set key inside left top
set xrange [0.9*X_min:3.0*X_max]
set yrange [0:1.1*Y_max]
set logscale x
set xlabel "number of moves/cups (log_{10})"
set ylabel "execution time (sec)"
plot "times.txt" every ::skip_moves_lines::skip_moves_lines+5 using 1:3:4 with yerrorbars title "9 cups, vary moves", \
pow1n(x) with lines title sprintf("{/:Italic n}^{%4.2f+\-%4.2f}", e1, e1_err), \
"times.txt" every ::skip_cups_lines using 2:3:4 with yerrorbars title "100 moves, vary cups", \
pow2n(x) with lines title sprintf("{/:Italic n}^{%4.2f+\-%4.2f}", e2, e2_err)

