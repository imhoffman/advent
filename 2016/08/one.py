#!/usr/bin/env python3


class screen:
  def __init__(self, width, length):
    self.width = width
    self.length = length
    self.state = [ [ 0 for i in range(self.width) ] for j in range(self.length) ]

  def command ( self, r ):
    d = self.state
    print( r )
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
            #print( " rotate row command encountered: row %d by %d\n" % (y, n) )
            temp = d[y][-n:]
            for i in range( len(d[0])-n-1 ):
                d[y][i+1] = d[y][i]
            # something like d[y][0] = temp
        elif r.split(sep=' ')[1] == 'column':
            x = int( r[ e:r.find(' ',e) ] )
            m = int( r[ r.find('by')+2: ] )
            #print( " rotate column command encountered: column %d by %d\n" % (x, m) )
            temp = d[-m:][x]
            for j in range( len(d)-m-1 ):
                d[j+m][x] = d[j][x]
            #something like d[0][x] = temp
        else:
            print( " problem reading rotate command\n" )
    else:
        print( " command unknown\n" )
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
display = screen( 7, 3 )
#display = screen( 50, 6 )

for s in listing:
    display.command( s )
    display.render()

