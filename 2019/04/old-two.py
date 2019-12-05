#!/usr/bin/env python3

##
##  subprograms
##

#  recursive predicate for checking for sequential increase
#   is there a more obvious arithmetic test ??
def is_increasing ( string_of_digits ):
    if len( string_of_digits ) == 1:
        return True
    for i in range( len( string_of_digits ) ):
        for j in range( i+1, len( string_of_digits )-i ):
            if int(string_of_digits[i]) > int(string_of_digits[j]):
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
    elif len( string_of_digits ) == 2:            # work around range(0) have a len of 2
            return has_repeats( "Q", repeated_digits_found )
    else:
        for i in range( len( string_of_digits )-1 ):
            if int( string_of_digits[i] ) == int( string_of_digits[i+1] ):
                j = int( string_of_digits[i] )    # use its value for indexing
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

lower_bound = int( puzzle[:6] )
upper_bound = int( puzzle[7:] )

possibilities = rules( lower_bound, upper_bound )

print( possibilities )
print( "\n number of possible passwords: %d\n\n" % len( possibilities ) )

# 175 is too low
# 303 is wrong

