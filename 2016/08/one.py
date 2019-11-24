#!/usr/bin/env python3


class screen:
  def __init__(self, s):
    self.s = s

  def command ( self ):
    x = self.s
    print( x.split(sep=' ')[0] )



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

[ screen(s).command() for s in listing ]

