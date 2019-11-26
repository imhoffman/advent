#!/usr/bin/env python3

def rotater ( v, n ):
    temp = v[:]             # cannot do temp = v  OMG
    for i in range( len(v) ):
        m = i + n
        m = m % len(v)
        v[m] = temp[i]
    return v


class screen:
  def __init__(self, width, length):
    self.width = width
    self.length = length
    self.state = [ [ 0 for i in range(self.width) ] for j in range(self.length) ]

  def command ( self, r ):
    d = self.state
    #print( r )
    if r.split(sep=' ')[0] == 'rect':
        x,y = [ int(n) for n in r.split(sep=' ')[1].split(sep='x') ]
        for i in range(x):
            for j in range(y):
                d[j][i] = 1
    elif r.split(sep=' ')[0] == 'rotate':
        e = r.find('=')+1
        if r.split(sep=' ')[1] == 'row':
            y = int( r[ e:r.find(' ',e) ] )
            n = int( r[ r.find('by')+2: ] )
            row = d[:][y]
            d[:][y] = rotater( row, n )
        elif r.split(sep=' ')[1] == 'column':
            x = int( r[ e:r.find(' ',e) ] )
            m = int( r[ r.find('by')+2: ] )
            col = [ d[k][x] for k in range( len(d) ) ]
            newcol = rotater( col, m )[:]
            for k in range( len(col) ):
                d[k][x] = newcol[k]
        else:
            print( " problem reading rotate command\n" )
    else:
        print( " command unknown\n" )
    return


  def render ( self ):
    length = self.length
    width = self.width
    state = self.state
    total = 0
    print()
    for i in range( length ):
        for j in range( width ):
            if state[i][j] == 0 :
                print( '.', end='', flush=True )
            else:
                print( '#', end='', flush=True )
                total = total + 1
        print()
    print( "   %d" % total )


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
#display = screen( 7, 3 )     # for the example
display = screen( 50, 6 )

for s in listing:
    display.command( s )
    display.render()

