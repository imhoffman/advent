# v >= 3.6 for f-strings

# C printf
import sys
def printf(format, *args):
    sys.stdout.write(format % args)

# python does not have constants --- "Just don't change it."
# https://stackoverflow.com/questions/2682745/how-do-i-create-a-constant-in-python/2682752
class C():
    def alpha():
        return "abcdefghijklmnopqrstuvwxyz"

# not used, but useful for comparison to fortran and C
# the python built-in .count is likely recursive
def counter ( ch, string ):
    return sum( [ x is ch for x in string ] )

# checksum ruleset
def sumcheck ( x, y ):
    counts = [ x.count(ch) for ch in y ]
    if 0 in counts: return False
    for k in range( len(counts)-1 ):
        if ( ( counts[k] > counts[k+1] )
                or
                ( ( counts[k] == counts[k+1] )
                    and
                    ( ( C.alpha().index(y[k]) < C.alpha().index(y[k+1]) ) )
                ) 
            ): continue
        else: return False
    return True

def decrypter ( r ):
    s = ""
    rotate = r[0] % len( C.alpha() )
    for i in range( len( r[1] ) ):
        if r[1][i] == '-': s = s + ' '
        else:
            if C.alpha().index(r[1][i]) + rotate < len( C.alpha() ): s = s + C.alpha()[rotate]
            else: s = s + C.alpha()[ rotate-len(C.alpha()) ] 
    return s

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

[ decrypted.append( decrypter([sector_id[i],encrypted[i]]) ) for i in range( len(listing) ) ]
[ print( sector_id[i], decrypted[i] ) for i in range(n) ]
print( f" total of real sector ids = {sum( [ sector_id[i] for i in range(n) if is_real[i] ] ):d}" )

#n = 15
#print ( f" the {n:2d}th letter of alpha() is {C.alpha()[n-1]!r}\n" )
#printf ( " the %dth letter of alpha() is %s\n", n, C.alpha()[n-1] )
#print ( f" the constant has the value {C.pi():7.5}\n" )

