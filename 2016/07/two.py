#
# main program
#
listing = []
n = 0
with open("puzzle.txt") as fo:
 while True:
  line = fo.readline()
  if not line: break
  listing.append(line)
  n = n + 1
#fo.close()

print( " read %d lines from input file\n" % (n) )

[ print( s ) for s in listing ]

