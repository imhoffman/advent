GO experiments
==============

## environment and path

Unless using simply `go run two.go` at the bash prompt, the directory structure above this Readme is mandated, as described [here](https://golang.org/doc/install) and [here](https://golang.org/doc/code.html).
The `GOPATH` variable needs to be set.
From this directory, it is
```
$ cd ../..
$ export GOPATH=`pwd`
```
Once set, the following will work
```
$ go build
$ ./two
```
where `two` is an enormous static executable.
This is the executable that I use in the benchmarking below.
Usually, I simply use `run`.

BTW, I'm breaking the rules by symlinking the puzzle input file.

## concurrent channels

The code in `chans.go` uses concurrency.

My goal is to get the many different operations (e.g., `sumcheck`) on the input list to run asynchronously on the channel `d := make( chan int, len(r) )`.
However, my simple solution of waiting for `len(d) == cap(d)` does not work.
(I eventually figured it out! Keep reading!)
For the synchronous case (that is, blocking each routine in a queue for a single channel `d := make( chan int )`, which is basically a really tricked-out for loop), it performs as follows:

```
 Performance counter stats for './two' (128 runs):

          1.408241      task-clock (msec)         #    1.045 CPUs utilized            ( +-  0.36% )
                38      context-switches          #    0.027 M/sec                    ( +-  1.22% )
                 2      cpu-migrations            #    0.001 M/sec                    ( +-  7.13% )
               206      page-faults               #    0.147 M/sec                    ( +-  0.25% )
         4,573,914      cycles                    #    3.248 GHz                      ( +-  1.21% )  (87.64%)
         6,908,291      instructions              #    1.51  insn per cycle           ( +-  0.10% )
         1,410,601      branches                  # 1001.676 M/sec                    ( +-  0.09% )
            18,243      branch-misses             #    1.29% of all branches          ( +-  0.33% )
         1,953,974      L1-dcache-loads           # 1387.528 M/sec                    ( +-  0.12% )
            51,125      L1-dcache-load-misses     #    2.62% of all L1-dcache hits    ( +-  0.41% )
     <not counted>      LLC-loads                                                     (0.00%)
     <not counted>      LLC-load-misses                                               (0.00%)

       0.001347552 seconds time elapsed                                          ( +-  0.27% )
```

I figured out asynchronous channelling! WaitGroups!
FWIW, here is the asynchronous performance of `chans.go` for the same number of context switches on the same linux box (Intel(R) Core(TM) i7-7700 CPU @ 3.60GHz).

```
 Performance counter stats for './two' (128 runs):

          2.469306      task-clock (msec)         #    1.950 CPUs utilized            ( +-  1.24% )
                38      context-switches          #    0.015 M/sec                    ( +-  1.87% )
                 5      cpu-migrations            #    0.002 M/sec                    ( +-  4.95% )
               466      page-faults               #    0.189 M/sec                    ( +-  2.02% )
         9,097,435      cycles                    #    3.684 GHz                      ( +-  1.69% )  (88.87%)
         8,591,258      instructions              #    0.94  insn per cycle           ( +-  0.59% )
         1,750,957      branches                  #  709.089 M/sec                    ( +-  0.59% )
            19,457      branch-misses             #    1.11% of all branches          ( +-  0.82% )
         2,411,292      L1-dcache-loads           #  976.506 M/sec                    ( +-  0.66% )
           140,939      L1-dcache-load-misses     #    5.84% of all L1-dcache hits    ( +-  0.73% )
     <not counted>      LLC-loads                                                     (0.00%)
     <not counted>      LLC-load-misses                                               (0.00%)

       0.001266223 seconds time elapsed                                          ( +-  1.26% )
```

