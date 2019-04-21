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
    integer (kind=iw)            :: id
    character (len=strlen)       :: encrypted
    character (len=strlen)       :: decrypted
    character (len=max_checksum) :: checksum
    logical                      :: is_real
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
  subroutine decrypter ( g )
   type(record), dimension(:) :: g
   character (len=max_checksum) :: checksum
   integer :: i, j, k, m1, m2, m3, mm, nthis, nnext, nlast
   integer :: m4, rotate

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
       m4 = scan( g(i)%encrypted, ' ' ) - 1
       rotate = mod( g(i)%id, len(alpha) )
       if ( len(alpha) - scan( alpha, g(i)%encrypted(j:j) )  .gt.  rotate ) then
         write(6,'(A,I4)') ' wrapping around for ', i
       end if
       do j = 1, m4
         continue
       end do
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

