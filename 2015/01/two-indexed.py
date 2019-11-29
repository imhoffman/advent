
#  input file has only one line; file is closed by the `with` ending
#  `directions` will be one, long, iterable string of chars
with open( "input.txt" ) as f:
    directions = f.read()

#  puzzle rules state these as the starting points
floor = 0
i = 1

#  must address the char array as per Python zero-indexing
while floor != -1:
    if directions[i-1] == '(':
        floor += 1
        i += 1
    elif directions[i-1] == ')':
        floor -= 1
        i += 1

#  once floor -1 is reached, i is incremented one excessive time
direction_number = i - 1

print( "\n we reached the basement on direction number %d\n" % direction_number )


