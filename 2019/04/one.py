#!/usr/bin/env python3

##
##  subprograms
##

def combo_maker ( lower_bound, upper_bound ):
    length_of_input = len( puzzle_input )
    for i in range( length_of_input )



def rules ( puzzle_input, init_index, list_of_possibilities ):
    s = puzzle_input[:]
    if len(s) - init_index < 6:
        return list_of_possibilities
    possibility = []
    for i in range( init_index, len(s) ):
        possibility.append( s[i] )
        for j in range( i, len(s)-i ):
            if s[i] <= s[j]:
                possibility.append( s[j] )







##
##  main program
##

with open("puzzle.txt") as fo:
  puzzle = fo.readline().strip()
#fo.close()

print( " read %d characters from input file\n" % ( len(puzzle) ) )

lower_bound = int( puzzle[:5] )
upper_bound = int( puzzle[7:] )


