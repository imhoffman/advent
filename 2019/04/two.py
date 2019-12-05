#!/usr/bin/env python3

##
##  subprograms
##

#  recursive predicate for checking for sequential increase
#   is there a more obvious arithmetic test? yes ... see two.f90
def is_increasing ( string_of_digits ):
    if len( string_of_digits ) == 1:
        return True
    i = 0
    for a in string_of_digits:
        i += 1
        for b in string_of_digits[i:]:
            if int(a) > int(b):
                return False
            else:
                return is_increasing( string_of_digits[1:] )


#  recursive predicate for the "repeated" rule
#   repeated_digits_found is an array of each digit's repeat count
def has_repeats ( string_of_digits, repeated_digits_found ):
    if len( string_of_digits ) == 1:              # base case
        if any( [ x == 1 for x in repeated_digits_found ] ):
            return True                           # at least one single-repeat
        else:
            return False
    else:
        a, b = [ d[0] for d in [ string_of_digits[:-1], string_of_digits[1:] ] ]
        if int( a ) == int( b ):
            j = int( a )                          # use its value for indexing
            repeated_digits_found[j] += 1
        # recur with remainder of string
        return has_repeats( string_of_digits[1:], repeated_digits_found )
    #return False                                 # should never get here


def rules ( lower_bound, upper_bound ):
    list_of_possibilities = []
    for i in range( lower_bound, upper_bound ):
        candidate_digits = str( i )
        if not is_increasing( candidate_digits ):
            continue
        if not has_repeats( candidate_digits, [0]*10 ):
            continue
        list_of_possibilities.append( i )
    return list_of_possibilities


##
##  main program
##
with open("puzzle.txt") as fo:
  puzzle = fo.readline().strip()

print( "\n read %d characters from input file\n" % ( len(puzzle) ) )

lower_bound, upper_bound = map( int, puzzle.split('-') )

possibilities = rules( lower_bound, upper_bound )

#print( possibilities )
print( "\n number of possible passwords: %d\n\n" % len( possibilities ) )

