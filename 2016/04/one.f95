!! *** Advent of Code 2016 Day 04
!!
!! typedefs and parameters
!!
 module types
  use iso_fortran_env
  implicit none
  integer, parameter :: iw = int64
  integer, parameter :: max_lines = 16384
  integer, parameter :: max_checksum = 8
  integer, parameter :: strlen = 80
  character (len=26) :: alpha="abcdefghijklmnopqrstuvwxyz"
  character (len=10) :: numer="0123456789"
  type record
    character (len=strlen)       :: listing
    integer (kind=iw)            :: val
    character (len=max_checksum) :: checksum
  end type record
 end module types

!!
!! subroutines and functions
!!
 module subs
  use types
  implicit none
  contains

  !! read external file and count lines
  subroutine reader ( fp, n, lines )
   implicit none
   integer, intent(in) :: fp
   integer             :: n
   character (len=*), dimension(*) :: lines
   integer             :: i

   n = 0
   do i = 1, max_lines
    read(fp,*,err=100,end=200) lines(i)
    n = n + 1
   end do
   goto 200
   100 write(6,*) ' error reading file'
   200 continue
   return
  end subroutine reader

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
  subroutine totaller ( g, x )
   type(record), dimension(:) :: g
   integer (kind=iw) :: x
   character (len=max_checksum) :: checksum
   integer :: i, j, k, m1, m2, m3, mm, nthis, nnext, nlast

   do i = 1, size( g )
     m1 = scan( g(i)%listing, "[" )
     m2 = scan( g(i)%listing, "]" )

     ! poor man's is_number...
     m3 = 1024
     do j = 1, len(numer)
       mm = scan( g(i)%listing(:m1-1), numer(j:j) )
       if ( mm .lt. m3  .and.  mm .gt. 0 ) m3 = mm
     end do
     ! store value
     if ( m3 .gt. 0 ) read( g(i)%listing(m3:m1-1), * ) g(i)%val

     g(i)%checksum = g(i)%listing(m1+1:m2-1)
     do k = 1, m2-m1-2
       nthis = counter(0,       g(i)%checksum(k:k)       ,g(i)%listing(:m1-1))
       nnext = counter(0,     g(i)%checksum(k+1:k+1)     ,g(i)%listing(:m1-1))
       nlast = counter(0, g(i)%checksum(m2-m1-1:m2-m1-1) ,g(i)%listing(:m1-1))
       ! rule set
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
     x = x + g(i)%val
     420 continue
    end do

   return
  end subroutine totaller
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
  integer (kind=iw) :: total=0
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

  ! compute sum as per puzzle rules
  total = 0
  call totaller( registry, total )
  write(6,'(A,I0)') ' total = ', total

  deallocate ( registry )
  stop
 end program main

