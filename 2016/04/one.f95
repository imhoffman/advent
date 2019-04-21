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

 module subs
  use types
  implicit none
  contains

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

  subroutine totaller ( g )
   type(record), dimension(:), intent(in) :: g
   integer :: n

   n = size( g )

   return
  end subroutine totaller

 end module subs

 program main
  use types
  use subs
  implicit none
  character (len=strlen), dimension(:), allocatable :: temp
  type(record), dimension(:), allocatable :: registry
  character (len=max_checksum) :: checksum
  integer :: fileunit=10, Nrooms, nthis, nnext, nlast
  integer (kind=iw) :: total=0
  integer :: i, j, k, m1, m2, m3, mm

  allocate( temp(max_lines) )
  open(fileunit,file="input.txt")
  call reader ( fileunit, Nrooms, temp )
  close(fileunit)
  allocate ( registry(Nrooms) )

  !call totaller( registry )
  do i = 1, Nrooms
   !registry(i)%listing = ''
   registry(i)%listing = temp(i)
   !registry(i)%listing = trim( registry(i)%listing )
  end do
  deallocate( temp )

  do i = 1, Nrooms
   m1 = scan( registry(i)%listing, "[" )
   m2 = scan( registry(i)%listing, "]" )

   ! poor man's is_number...
   m3 = 1024
   do j = 1, len(numer)
    mm = scan( registry(i)%listing(:m1-1), numer(j:j) )
    if ( mm .lt. m3  .and.  mm .gt. 0 ) m3 = mm
   end do
   ! store value
   if ( m3 .gt. 0 ) read( registry(i)%listing(m3:m1-1), * ) registry(i)%val

   registry(i)%checksum = registry(i)%listing(m1+1:m2-1)
   do k = 1, m2-m1-2
     nthis = counter(0,       registry(i)%checksum(k:k)       ,registry(i)%listing(:m1-1))
     nnext = counter(0,     registry(i)%checksum(k+1:k+1)     ,registry(i)%listing(:m1-1))
     nlast = counter(0, registry(i)%checksum(m2-m1-1:m2-m1-1) ,registry(i)%listing(:m1-1))
     ! rule set
     if ( &
         &         nlast .gt. 0 &
         & .and. ( nthis .gt. nnext &
         &        .or. ( &
         &              nthis .eq. nnext &
         &  .and. ( scan( alpha, registry(i)%checksum(k:k) ) &
         &             .lt. &
         &          scan( alpha, registry(i)%checksum(k+1:k+1) ) ) &
         & ) ) ) then
       continue
     else
       goto 420
     end if
   end do
   total = total + registry(i)%val
   420 continue
  end do

  write(6,'(A,I0)') ' total = ', total

  deallocate ( registry )
  stop
 end program main

