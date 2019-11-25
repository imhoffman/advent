#!/usr/bin/env python3


class screen:
  def __init__(self, width, length):
    self.width = width
    self.length = length
    self.state = [ [ 0 for i in range(self.width) ] for j in range(self.length) ]

  def command ( self, r ):
    d = self.state
    print( r.split(sep=' ')[0] )
    return

  def test_point ( self, x0, y0 ):
    d = self.state
    d[y0][x0] = '#'
    return

  def render ( self ):
    length = self.length
    width = self.width
    state = self.state
    print()
    for i in range( length ):
      [ print( '.', end='', flush=True ) if state[i][j] == 0 else
        print( '#', end='', flush=True )
        for j in range( width ) ]
      print()
    print()



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

# instantiate a screen object with an internally-tracked state
display = screen( 50, 6 )


display.render()
display.test_point( 26, 4 )
display.render()

