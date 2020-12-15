
skip_indexOf_lines = 2
skip_dictionary_lines = 7

a1 = 1.0
e1 = 2.0
a2 = 1.0
e2 = 1.0

pow1n(x) = a1*x**e1
pow2n(x) = a2*x**e2
#logn(x) = a2*log(x)**e2

FIT_LIMIT = 1e-07
set fit quiet
set fit errorvariables

set datafile missing "NA"
stats "times.txt" every ::skip_indexOf_lines using 2 nooutput

fit pow1n(x) "times.txt" every ::skip_indexOf_lines using 1:2:($3/$2) via a1,e1
fit pow2n(x) "times.txt" every ::skip_dictionary_lines using 1:4:($5/$4) via a2,e2

set terminal pngcairo dashed enhanced size 1024,768 crop font "Helvetica, 18" linewidth 2
set output "plot.png"
set size 0.8,1.0

set yrange [0:1.1*STATS_max]
set logscale x
set xlabel "number of turns (log_{10})"
set ylabel "execution time (sec)"
plot "times.txt" every ::skip_indexOf_lines using 1:2:3 with yerrorbars title "indexOf", \
pow1n(x) with lines title sprintf("{/:Italic n}^{%4.2f+\-%4.2f}", e1, e1_err), \
"times.txt" every ::skip_dictionary_lines using 1:4:5 with yerrorbars title "dictionary", \
pow2n(x) with lines title sprintf("{/:Italic n}^{%4.2f+\-%4.2f}", e2, e2_err)

