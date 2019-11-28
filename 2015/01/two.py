#
# main program
#
listing = []

# read file
with open("puzzle.txt") as fo:
 while True:
  line = fo.readline()
  if not line: break
  listing.append(line)

puzzle = listing[0]      # clunky way to read one line

index = 1                # as per the ruleset
floor = 0                # as per the ruleset
for c in puzzle:
    if c == '(':
        floor = floor + 1
    elif c == ')':
        floor = floor - 1
    else:
        print( "\n problem reading input\n" )
    if floor == -1:
        print( "\n first encountering floor -1 at instruction %d\n" % index )
        break
    index = index + 1

