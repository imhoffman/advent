 module types
  implicit none
  integer, parameter :: max_lines = 16384
  integer, parameter :: max_checksum = 8
  integer, parameter :: strlen = 80
  character (len=26) :: alpha="abcdefghijklmnopqrstuvwxyz"
  character (len=10) :: numer="0123456789"
  ! make a struct of id, value, and checksum
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
 end module subs

 program main
  use types
  use subs
  implicit none
  character (len=strlen), dimension(max_lines) :: temp
  character (len=strlen), dimension(:), allocatable :: rooms
  integer,                dimension(:), allocatable :: val
  character (len=max_checksum) :: checksum
  integer :: fileunit=10, total, Nrooms, nthis, nnext, nlast
  integer :: i, j, k, m1, m2, m3, mm

  open(fileunit,file="input.txt")
  call reader ( fileunit, Nrooms, temp )
  close(fileunit)
  allocate ( rooms(Nrooms), val(Nrooms) ) 

  do i = 1, Nrooms
   !rooms(i) = ''        ! initialize the blank here if trimming within this loop
   rooms(i) = temp(i)
   !rooms(i) = trim( rooms(i) )
  end do

  do i = 1, Nrooms
   m1 = scan( rooms(i), "[" )
   m2 = scan( rooms(i), "]" )

   m3 = 1024
   do j = 1, len(numer)
    mm = scan( rooms(i)(:m1-1), numer(j:j) )
    if ( mm .lt. m3  .and.  mm .gt. 0 ) m3 = mm
   end do
   if ( m3 .gt. 0 ) read( rooms(i)(m3:m1-1), * ) val(i)

   checksum = rooms(i)(m1+1:m2-1)
   !write(6,'(4A)')    ' checksum of ',rooms(i)(:m2),' is ',checksum
   !write(6,'(3A,I3)') '    value of ',rooms(i)(:m2),' is ',val(i)
   do k = 1, m2-m1-2
     nthis = counter(0,       checksum(k:k)       ,rooms(i)(:m1-1))
     nnext = counter(0,     checksum(k+1:k+1)     ,rooms(i)(:m1-1))
     nlast = counter(0, checksum(m2-m1-1:m2-m1-1) ,rooms(i)(:m1-1))
     if ( &
         &         nlast .gt. 0 &
         & .and. ( nthis .gt. nnext &
         &        .or. ( &
         &              nthis .eq. nnext &
         &  .and. ( scan( alpha, checksum(k:k) ) .lt. scan( alpha, checksum(k+1:k+1) ) ) &
         & ) ) ) then
       continue
     else
       goto 420
     end if
   end do
   total = total + val(i)
   420 continue
  end do

  write(6,'(A,I0)') ' total = ',total

  deallocate ( rooms, val )
  stop
 end program main

