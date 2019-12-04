#!/usr/bin/env python3

##
##  subprograms
##

def is_increasing ( string_of_digits ):
    if len( string_of_digits ) == 1:
        return True
    for i in range( len( string_of_digits ) ):
        for j in range( i+1, len( string_of_digits )-i ):
            if int(string_of_digits[i]) > int(string_of_digits[j]):
                return False
            else:
                return is_increasing( string_of_digits[1:] )


def has_repeats ( string_of_digits, number_found ):
    if len( string_of_digits ) == 1:
        if any( [ x == 1 for x in number_found ] ):
            return True
        else:
            return False
    for i in range( len( string_of_digits )-1 ):
        if int( string_of_digits[i] ) == int( string_of_digits[i+1] ):
            j = int( string_of_digits[i] )
            number_found[j] += 1
            return has_repeats( string_of_digits[1:], number_found )
    #return False      # should never get here


def rules ( lower_bound, upper_bound ):
    list_of_possibilities = []
    for i in range( lower_bound, upper_bound ):
        candidate_digits = str( i )
        if not is_increasing( candidate_digits ):
            continue
        if not has_repeats( candidate_digits, [0,0,0,0,0,0,0,0,0,0] ):
            continue
        list_of_possibilities.append( i )
    return list_of_possibilities


##
##  main program
##

with open("puzzle.txt") as fo:
  puzzle = fo.readline().strip()

print( "\n read %d characters from input file\n" % ( len(puzzle) ) )

lower_bound = int( puzzle[:6] )
upper_bound = int( puzzle[7:] )

possibilities = rules( lower_bound, upper_bound )

print( possibilities )
print( "\n number of possible passwords: %d\n\n" % len( possibilities ) )

# 175 is too low

