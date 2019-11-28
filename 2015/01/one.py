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

floor = 0                # as per the ruleset
for c in puzzle:
    if c == '(':
        floor = floor + 1
    elif c == ')':
        floor = floor - 1
    else:
        print( "\n problem reading input\n" )

print( "\n floor at the end: %d\n" % floor )

