#!/usr/bin/python3
# v >= 3.6 for f-strings

#
#  constants
#
class C:
    ALPHA = "abcdefghijklmnopqrstuvwxyz"

#
#  typedefs
#
class listing:
  def __init__(self, s):
    self.s = s

  def checksum ( self ) :
    x = self.s
    return x[ x.index('[')+1 : x.index(']') ]

  def encrypted ( self ) :
    x = self.s
    return x[ :x.index('[')-3 ] 

  def sumcheck ( self ):
      a = self.encrypted()
      b = self.checksum()
      counts = [ a.count(ch) for ch in b ]
      if 0 in counts: return False
      for k in range( len(counts)-1 ):
          if ( ( counts[k] > counts[k+1] )
                  or
                  ( ( counts[k] == counts[k+1] )
                      and
                      ( ( C.ALPHA.index( b[k] ) < C.ALPHA.index( b[k+1] ) ) )
                  ) 
              ): continue
          else: return False
      return True

  def sector_id ( self ) :
    nums = [ ch.isdigit() for ch in self.s ]
    sub = ""
    for j in range(len(self.s)):
        if ( nums[j] ):
            sub = sub + self.s[j]
    return int( sub )


#
#  subprograms
#
#   decryption ruleset
def caesar ( r ):     # accepts a two-item list
    s = ""
    rotate = r[0] % len( C.ALPHA )
    for i in range( len( r[1] ) ):
        if r[1][i] == '-': s = s + ' '
        else:
            m = C.ALPHA.index(r[1][i])
            if m + rotate < len( C.ALPHA ): s = s + C.ALPHA[m+rotate]
            else: s = s + C.ALPHA[ m+rotate-len(C.ALPHA) ] 
    return s

#
# main program
#
registry = []

# read file and populate registry with listing objects
n = 0
with open("input.txt") as fo:
 while True:
  line = fo.readline()
  if not line: break
  registry.append( listing(line) )

# output answers
[ print(
    x.sector_id(),
    caesar( [ x.sector_id(), x.encrypted() ] ) )
    for x in registry if x.sumcheck() ]

print( f"\n total of real sector ids = {sum( [ x.sector_id() for x in registry if x.sumcheck() ] ):d}\n" )

