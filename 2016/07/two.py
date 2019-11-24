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
    return p


def ssl ( q ):
    outs = parse_outsides( q, [] )
    ins  = parse_insides( q, [] )
    for s in outs:
        for i in range( len(s) ):
            if i + 2 < len(s) and s[i] == s[i+2] and s[i+1] != s[i]:
                bab = s[i+1] + s[i] + s[i+1]
                for r in ins:
                    for j in range( len(r) ):
                        if j + 2 < len(r) and r.find( bab, j ) != -1:
                            #print( " outs:", s, " ins:", r, " bab:", bab )
                            return True
    return False

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

print( " number of SSL listings: %d\n" %
        sum( [ 1 if ssl( s ) else 0 for s in listing ] ) )

#[ print( s, "\n", parse_insides( s, []), "\n", parse_outsides( s, [] ), "\n" ) for s in listing ]
#print( [ s for s in listing if ssl( parse_outsides( s, [] ), parse_insides( s, [] ) ) ] )

# 338 is too high
# 247 is too low
