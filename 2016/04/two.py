# https://stackoverflow.com/questions/2682745/how-do-i-create-a-constant-in-python/2682752
#   "Just don't change it"
import sys
def printf(format, *args):
    sys.stdout.write(format % args)

class C():
    def pi():
        return 3.14159
    def alpha():
        return "abcdefghijklmnopqrstuvwxyz"


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
   listing,
   sector_id,
   encrypted,
   decrypted,
   checksum,
   is_real
   )

n = 15
print ( f" the {n:2d}th letter of alpha() is {C.alpha()[n-1]!r}\n" )
printf ( " the %dth letter of alpha() is %s\n", n, C.alpha()[n-1] )
print ( f" the constant has the value {C.pi():7.5}\n" )

