#!/usr/bin/env python3

##
##  subprograms
##

def rules ( puzzle_input, init_index, list_of_possibilities ):
    s = puzzle_input[:]
    if len(s) - init_index < 6:
        return list_of_possibilities
    possibility = []
    for i in range( init_index, len(s) ):
        possibility.append( int(s[i]) )
        for j in range( i, len(s)-i ):
            if int(s[i]) <= int(s[j]):
                possibility.append( int(s[j]) )







##
##  main program
##

with open("puzzle.txt") as fo:
  puzzle = fo.readline().strip()
#fo.close()

print( " read %d characters from input file\n" % ( len(puzzle) ) )

numerical_input = [ int( c ) for c in puzzle if c != '-' ]

answer = rules( numerical_input, 0, [] )

