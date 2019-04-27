import sys
def printf(format, *args):
    sys.stdout.write(format % args)

# constants
class C():
    def pi():
        return 3.1415926535897932384626433832795028841971
    def alpha():
        return "abcdefghijklmnopqrstuvwxyz"

def counter ( ch, string ):
    return sum( [ x is ch for x in string ] )

def sumcheck ( x, y ):
    for i in range( len(y)-1 ):
      if ( x.count(y[-1]) > 0 and
           ( ( x.count(y[i]) > x.count(y[i+1]) ) or
             ( ( x.count(y[i]) == x.count(y[i+1]) ) and
               ( C.alpha().index(y[i]) > C.alpha().index(y[i+1]) )
             )
           ) ): continue
      else: return False
    return True

# poor man's struct
#  The following mutable lists will be the members
#  of the immutable tuple that follows; thus, there is
#  no single element that is a single record: their 
#  common indeces are their only link to each other.
listing = []
sector_id = []
encrypted = []
decrypted = []
checksum = []
is_real = []

registry = (
   listing,    # 0
   sector_id,  # 1
   encrypted,  # 2
   decrypted,  # 3
   checksum,   # 4
   is_real     # 5
   )

# https://docs.python.org/2/faq/design.html#why-can-t-i-use-an-assignment-in-an-expression
n = 0
with open("input.txt") as fo:
 while True:
  line = fo.readline()
  if not line: break
  listing.append(line)
  n = n + 1
fo.close()

#[ print(x) for x in registry[0] ]
#print( f" {n:d} lines in file\n" )

[ checksum.append( x[ x.index('[')+1 : x.index(']') ] ) for x in listing ]
[ encrypted.append( x[ :x.index('[')-3 ] ) for x in listing ]
[ is_real.append( sumcheck(encrypted[i],checksum[i]) ) for i in range(n) ]
for i in range(n):
    s = listing[i]
    nums = [ ch.isdigit() for ch in s ]
    sub = ""
    for j in range(len(s)):
        if ( nums[j] ):
            sub = sub + s[j]
    sector_id.append( int( sub ) )
    print( encrypted[i], sector_id[i] )
print( sum( [ sector_id[i] for i in range(n) if is_real[i] ] ) )

#n = 15
#print ( f" the {n:2d}th letter of alpha() is {C.alpha()[n-1]!r}\n" )
#printf ( " the %dth letter of alpha() is %s\n", n, C.alpha()[n-1] )
#print ( f" the constant has the value {C.pi():7.5}\n" )

