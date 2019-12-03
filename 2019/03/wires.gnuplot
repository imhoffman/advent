# http://ayapin-film.sakura.ne.jp/Gnuplot/Docs/ps_guide.pdf
set samples 10000
set terminal png enhanced size 1024,768 crop font "Helvetica, 24" linewidth 2
set output 'gnuplot.png'
set key inside top left
set xzeroaxis
set yzeroaxis
set xlabel 'horizontal, {/:Italic x}'
set ylabel 'vertical, {/:Italic y}'
set size ratio -1
plot 'plot1.dat' with lines title 'wire 1', 'plot2.dat' with lines title 'wire 2'
