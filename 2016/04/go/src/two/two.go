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
const max_lines int = 16384
const ALPHA  string = "abcdefghijklmnopqrstuvwxyz"
const NUMS   string = "0123456789"

//  file read
//   https://golang.org/pkg/bufio/#Scanner
//   https://stackoverflow.com/questions/8757389/reading-file-line-by-line-in-go
//  OMG!  from
//   https://golang.org/doc/effective_go.html#data
//   "Note that, unlike in C, it's perfectly OK to return the address of a local variable; the storage associated with the variable survives after the function returns."
func reader () []string {
    var i int = 0
    temp := make( []string, max_lines )

    f, err := os.Open("input.txt")
    if err != nil { log.Fatal(err) }
    defer f.Close()
    s := bufio.NewScanner(f)
    for s.Scan() {
      temp[i] = s.Text()
      i++
    }
    if err := s.Err(); err != nil { log.Fatal(err) }

    //  https://blog.golang.org/go-slices-usage-and-internals
    registry := make( []string, i )
    copy( registry, temp )
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
	    encrypted string = s[ :strings.Index(s,"[") ]
	    done        bool = false
        )

    nlast := strings.Count( encrypted, checksum[len(checksum):] )
    for pos, char := range checksum {
	if ( !done && ( nlast > 0 ) && ( pos < len(checksum)-1 ) ) {
	     nthis  := strings.Count( encrypted, string(char) )
	     nnext  := strings.Count( encrypted, string(checksum[pos+1]) )
	     iAthis := strings.Index( ALPHA, string(char) )
	     iAnext := strings.Index( ALPHA, string(checksum[pos+1]) )
	     if ( ( nthis > nnext ) || ( (nthis == nnext) && (iAthis < iAnext) ) ) {
		     continue
	     } else {
		     done = true
		     break
		     //return !done
	     }
        }
    }
    return !done
}


//  main program
func main () {

    r := reader()

    total := 0
    for _, s := range r {
       if ( sumcheck(s) ) {
	 id := get_id(s)
	 total = total + id
	 fmt.Printf(" %d %v checks out\n", id, s)
       }
    }

    fmt.Printf("\n read %d lines\n\n", len(r) )
    fmt.Printf("\n sum of real sector id's is %d\n\n", total)

}
