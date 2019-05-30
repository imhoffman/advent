Sample Solutions
================

I assigned this puzzle as the entrance exam to a programming course.
For the students' sake&mdash;and in the wonderful spirit of learning to code through Advent of Code&mdash;I am posting solutions in as many languages as I can.

I was new to this puzzle when I assigned it.
I wrote my initial solution in fortran.
([Part one](one.f95) on a two-hour flight, [part two](two.f95) on the connecting flight!)
So, there are separate (and very flexible) part one and part two solutions in fortran.
Once I knew where the problem was headed, I started writing increasingly streamlined solutions in other languages.

## go

Perhaps my favorite nugget so far is learning concurrency in go so that I could [farm out each entry on the puzzle input to its own concurrent routine](go/src/two/two.go)&mdash;then watch them come back in a different order on each run.
I also [learned a lot about race conditions](go/src/two).

## python

I wrote two solutions in python: [one in an imperative style](two.py) and [one centered on objects](two-classes.py).
FWIW, here are their performances:

```bash
$ sudo perf stat -r 128 -d python3.7 two.py > /dev/null 

 Performance counter stats for 'python3.7 two.py' (128 runs):

         40.867228      task-clock (msec)         #    0.996 CPUs utilized            ( +-  0.89% )
                 0      context-switches          #    0.002 K/sec                    ( +- 30.02% )
                 0      cpu-migrations            #    0.000 K/sec                  
             1,132      page-faults               #    0.028 M/sec                    ( +-  0.05% )
       163,340,948      cycles                    #    3.997 GHz                      ( +-  0.88% )  (54.82%)
       387,032,692      instructions              #    2.37  insn per cycle           ( +-  0.29% )  (73.78%)
        73,507,897      branches                  # 1798.700 M/sec                    ( +-  0.30% )  (76.92%)
           910,982      branch-misses             #    1.24% of all branches          ( +-  0.22% )  (79.59%)
        96,223,707      L1-dcache-loads           # 2354.544 M/sec                    ( +-  0.11% )  (80.43%)
         1,857,456      L1-dcache-load-misses     #    1.93% of all L1-dcache hits    ( +-  0.25% )  (80.43%)
           147,950      LLC-loads                 #    3.620 M/sec                    ( +-  6.17% )  (42.66%)
             1,476      LLC-load-misses           #    1.00% of all LL-cache hits     ( +- 14.34% )  (39.98%)

       0.041049386 seconds time elapsed                                          ( +-  0.89% )
```

```bash
$ sudo perf stat -r 128 -d python3.7 two-classes.py > /dev/null 

 Performance counter stats for 'python3.7 two-classes.py' (128 runs):

         25.159923      task-clock (msec)         #    0.993 CPUs utilized            ( +-  0.46% )
                 0      context-switches          #    0.001 K/sec                    ( +- 57.28% )
                 0      cpu-migrations            #    0.000 K/sec                  
             1,094      page-faults               #    0.043 M/sec                    ( +-  0.01% )
       100,362,574      cycles                    #    3.989 GHz                      ( +-  0.46% )  (52.31%)
       213,051,485      instructions              #    2.12  insn per cycle           ( +-  0.44% )  (68.21%)
        38,693,789      branches                  # 1537.914 M/sec                    ( +-  0.55% )  (68.21%)
           871,862      branch-misses             #    2.25% of all branches          ( +-  0.18% )  (68.21%)
        49,742,811      L1-dcache-loads           # 1977.065 M/sec                    ( +-  0.37% )  (72.75%)
         1,487,509      L1-dcache-load-misses     #    2.99% of all L1-dcache hits    ( +-  0.51% )  (87.47%)
           200,595      LLC-loads                 #    7.973 M/sec                    ( +-  1.30% )  (59.03%)
             4,042      LLC-load-misses           #    2.02% of all LL-cache hits     ( +-  4.27% )  (44.32%)

       0.025334876 seconds time elapsed                                          ( +-  0.46% )
```

