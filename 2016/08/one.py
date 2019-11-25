#!/usr/bin/env python3


class screen:
  def __init__(self, state):
    self.state = state

    # the instantiation should accept 50,6 and
    # the object should keep track of its own state thereafter...

  def command ( self, r ):
    x = self.state
    print( r.split(sep=' ')[0] )
    x[2][9] = 1
    return x

  def render ( self ):
    x = self.state
    print()
    for i in range( len(x) ):
      [ print( '.', end='', flush=True ) if x[i][j] == 0 else
        print( '#', end='', flush=True )
        for j in range( len(x[0])) ]
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

state = [ [ 0 for i in range(50) ] for j in range(6) ]

state = screen(state).command( listing[3] )

screen(state).render()

#print( screen(state).command( listing[3] ) )
#[ screen(state).command(s) for s in listing ]

