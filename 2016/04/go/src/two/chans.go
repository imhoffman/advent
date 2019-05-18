package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strings"
	"strconv"
)

//  constants
const MAX_LINES int = 16384
const ALPHA  string = "abcdefghijklmnopqrstuvwxyz"
const NUMS   string = "0123456789"

//  file read
//   https://golang.org/pkg/bufio/#Scanner
//   https://stackoverflow.com/questions/8757389/reading-file-line-by-line-in-go
//  OMG!  from
//   https://golang.org/doc/effective_go.html#data
//   "Note that, unlike in C, it's perfectly OK to return the address of a local variable; the storage associated with the variable survives after the function returns."
func reader ( filename string ) []string {
    var i int = 0
    temp := make( []string, MAX_LINES )

    f, err := os.Open(filename)
    if err != nil { log.Fatal(err) }
    defer f.Close()
    s := bufio.NewScanner(f)
    for s.Scan() {
      temp[i] = s.Text()
      i++
    }
    if err := s.Err(); err != nil { log.Fatal(err) }

    //  https://blog.golang.org/go-slices-usage-and-internals
    registry := make( []string, i )   // allocate final array now that Nrooms is known
    copy( registry, temp )            // GC should find temp soon enough
    return registry
}


// sector id
func get_id ( s string ) int {
    id, _ := strconv.Atoi( s[ strings.IndexAny(s,NUMS):strings.LastIndexAny(s,NUMS)+1 ] )
    return id
}

// checksum ruleset
func sumcheck ( s string ) bool {
    var (
	    checksum  string = s[ strings.Index(s,"[")+1:strings.Index(s,"]") ]
	    encrypted string = s[ :strings.IndexAny(s,NUMS)-1 ]
	    done        bool = false
        )

    for pos, char := range checksum {
	if ( !done && ( pos < len(checksum)-1 ) ) {
	     nthis  := strings.Count( encrypted, string(char) )
	     nnext  := strings.Count( encrypted, string(checksum[pos+1]) )
	     iAthis := strings.Index( ALPHA, string(char) )
	     iAnext := strings.Index( ALPHA, string(checksum[pos+1]) )
             if ( ( nthis < 1 ) || (nnext < 1 ) ) { done = true; break }
	     if ( ( nthis > nnext ) || ( (nthis == nnext) && (iAthis < iAnext) ) ) {
		     continue
	     } else { done = true; break }    // is 'return !done' from here idiomatic ?
        }
    }
    return !done
}


//  channelized distributable function
func checker ( s string, c chan int ) {
    if ( sumcheck (s) ) {
      c <- get_id(s)
    } else {
      c <- 0
    }
    // this is not working
    //if len(c) == cap(c) { close(c) }
    // consult
    //   https://tour.golang.org/concurrency/4
    //   https://stackoverflow.com/questions/25657207/golang-how-to-know-a-buffered-channel-is-full
    //   --- or maybe an intemediary ring buffer or handler
    return
}


//  main program
func main () {

    r := reader("input.txt")
    d := make( chan int )
    //d := make( chan int, len(r) )

    total := 0
    for _, s := range r {
       go checker( s, d )
       total = total + <-d
    }
    //for len(d) != cap(d) { for i := range d { total = total + i } }

    fmt.Printf("\n read %d lines\n", len(r) )
    fmt.Printf("\n sum of real sector id's is %d\n\n", total)

}
