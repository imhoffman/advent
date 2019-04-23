// *** Advent of Code 2016 Day 04
#include<stdio.h>
#include<math.h>

const int iw = 32;
const int max_lines = 16384;
const int max_checksum = 8;
const int strlen = 80;
const char[] alpha="abcdefghijklmnopqrstuvwxyz";
const char[] numer="0123456789";

typedef struct {
  char[strlen]        listing;
  int                      id:iw;
  char[strlen]      encrypted;
  char[strlen]      decrypted;
  char[max_checksum] checksum;
  bool                is_real;
} record;

void reader ( FILE* f, int* n, char* lines ) {
  char[strlen] buffer = "\0";

  *n = 0;
  while ( fgets(buffer, strlen, f) != NULL ) {
    lines[*n] = buffer;
    *n++;
  }
}


  !! letter-occurence counter
  recursive function counter ( n, ch, str ) result ( k )
   integer, intent(in)             :: n       ! the running count when called
   character ( len=1 ), intent(in) :: ch
   character ( len=* ), intent(in) :: str
   integer                         :: k, m=-1

   k = 0

   if ( len(str) .eq. 0 ) return

   m = scan( str, ch ) 
   if ( m .gt. 0 ) then
    k = 1 + counter( n+1, ch, str(m+1:) )
   end if

   return
  end function counter

  !! puzzle ruleset
  subroutine decrypter ( g )
   type(record), dimension(:) :: g
   character (len=max_checksum) :: checksum
   integer :: i, j, k, m1, m2, m3, mm, nthis, nnext, nlast
   integer :: m4, rotate, oldch, newch

   do i = 1, size( g )
     g(i)%is_real = .false.
     m1 = scan( g(i)%listing, "[" )
     m2 = scan( g(i)%listing, "]" )

     ! poor man's is_number...
     m3 = 1024
     do j = 1, len(numer)
       mm = scan( g(i)%listing(:m1-1), numer(j:j) )
       if ( mm .lt. m3  .and.  mm .gt. 0 ) m3 = mm
     end do
     ! store sector id
     if ( m3 .gt. 0 ) read( g(i)%listing(m3:m1-1), * ) g(i)%id
     ! store encrypted prefix
     g(i)%encrypted = trim( g(i)%listing(:m3-1) )
     ! store checksum
     g(i)%checksum = g(i)%listing(m1+1:m2-1)

     ! determine decoys
     do k = 1, m2-m1-2
       nthis = counter(0,       g(i)%checksum(k:k)       ,g(i)%listing(:m1-1))
       nnext = counter(0,     g(i)%checksum(k+1:k+1)     ,g(i)%listing(:m1-1))
       nlast = counter(0, g(i)%checksum(m2-m1-1:m2-m1-1) ,g(i)%listing(:m1-1))
       ! checksum rule set
       if ( &
           &         nlast .gt. 0 &
           & .and. ( nthis .gt. nnext &
           &        .or. ( &
           &              nthis .eq. nnext &
           &  .and. ( scan( alpha, g(i)%checksum(k:k) ) &
           &             .lt. &
           &          scan( alpha, g(i)%checksum(k+1:k+1) ) ) &
           & ) ) ) then
         continue
       else
         goto 420
       end if
     end do
     g(i)%is_real = .true.
     !write(6,'(I5,A)') i, ' is a real room!'
     420 continue

     if ( g(i)%is_real ) then
       m4 = scan( g(i)%encrypted, ' ' ) - 2
       rotate = mod( g(i)%id, len(alpha) )
       ! rule set
       do j = 1, m4
         if ( g(i)%encrypted(j:j) .eq. '-' ) then
           g(i)%decrypted(j:j) = ' '
         else
           oldch = scan( alpha, g(i)%encrypted(j:j) ) 
           if ( rotate + oldch  .gt.  len(alpha) ) then
             newch = oldch + rotate - len(alpha) 
           else
             newch = oldch + rotate
           end if
           g(i)%decrypted(j:j) = alpha( newch:newch )
         end if
       end do
       write(6,'(I5,2X,A)') g(i)%id, g(i)%decrypted(:m4)
     end if
    end do

   return
  end subroutine decrypter
 end module subs

!!
!! main program
!!
 program main
  use types
  use subs
  implicit none
  character (len=strlen), dimension(:), allocatable :: temp
  type(record), dimension(:), allocatable :: registry
  integer :: fileunit=10, Nrooms
  integer :: i

  ! read external file into temporary array
  allocate( temp(max_lines) )
  open(fileunit,file="input.txt")
  call reader ( fileunit, Nrooms, temp )
  close(fileunit)

  ! populate registry and free reading buffer
  allocate ( registry(Nrooms) )
  do i = 1, Nrooms
   registry(i)%listing = temp(i)
  end do
  deallocate( temp )

  ! decrypt as per puzzle rules
  call decrypter( registry )

  deallocate ( registry )
  stop
 end program main

