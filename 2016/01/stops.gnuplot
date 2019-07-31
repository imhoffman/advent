# http://ayapin-film.sakura.ne.jp/Gnuplot/Docs/ps_guide.pdf
set samples 10000
set terminal png enhanced size 1024,768 crop font "Helvetica, 24" linewidth 2
set output '~/Downloads/gnuplot.png'
set key inside top left
set xzeroaxis
set yzeroaxis
set xlabel 'longitude, {/:Italic x}'
set ylabel 'latitude, {/:Italic y}'
set size ratio -1
plot 'stops.dat' with lines title '2016 Day 01'
