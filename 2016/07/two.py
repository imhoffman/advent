#!/usr/bin/env python3

##
##  subprograms
##

def parse_outsides ( s, p ):
    if ']' in s:
        n = s.find('[')
        p.append( s[ 0:n ] )        # this cannot go inside the argument list
        parse_outsides( s[ s.find(']')+1: ], p )
    else:
        p.append( s )
    return p


def parse_insides ( s, p ):
    if ']' in s:
        n = s.find('[')
        m = s.find(']')
        p.append( s[ n+1:m ] )
        parse_insides( s[ m+1: ], p )
    else:
        p.append( s )
    return p


##
##  main program
##
listing = []
n = 0
with open("puzzle.txt") as fo:
 while True:
  line = fo.readline()
  if not line: break
  listing.append( line.rstrip() )
  n = n + 1
#fo.close()

print( " read %d lines from input file\n" % (n) )

[ print( s, "outsides:", parse_outsides( s, [] ), " insides:", parse_insides( s, [] ) ) for s in listing ] 


