
 module types
  implicit none
  integer, parameter :: maxlines = 8192
  integer, parameter ::   maxlen = 80
  character (len=*), parameter :: alpha = "abcdefghijklmnopqrstuvwxyz"
 end module types

 module subs
  use types
  implicit none
  contains

  subroutine reader ( fp, nlines, buf )
   integer, intent(in)                          :: fp
   integer, intent(out)                         :: nlines
   character (len=*), dimension(*), intent(out) :: buf
   integer :: i

   nlines = 0
   do i = 1, maxlines
    read( fp, *, err=100, end=200 ) buf(i)
    nlines = nlines + 1
   end do

   100 write( 6, * ) ' file read error'
   200 continue
   return
  end subroutine reader

  function freqs ( col ) result ( c )
   character (len=*), intent(in) :: col
   character :: c
   integer :: i, j, nj, jbest, nbest

   nbest = 26 
   do j = 1, len( alpha )
    nj = 0
    do i = 1, len( col )
     if ( col(i:i) .eq. alpha(j:j) ) nj = nj + 1
    end do
    if ( nj .lt. nbest .and. nj .gt. 0 ) then
     nbest = nj
     jbest = j
    end if
   end do

   c = alpha( jbest:jbest )

   return
  end function freqs

  subroutine decode ( cols, message )
   character (len=*), dimension(*), intent(in) :: cols
   character (len=*), intent(out) :: message
   integer :: i

   do i = 1, len( message )
    message(i:i) = freqs( cols(i) )
   end do

   return
  end subroutine decode
 end module subs

!!
!!  main program
!!
 program one
  use types
  use subs
  implicit none

  character (len=maxlen), dimension(:), allocatable :: buffer
  character      (len=:), dimension(:), allocatable :: input
  character      (len=:), dimension(:), allocatable :: cols
  character      (len=:), allocatable :: message
  integer :: i, j, fp=10, n0, L0

  allocate( buffer( maxlines ) )
  open( fp, file = "puzzle.txt" )
  call reader( fp, n0, buffer )
  close( fp )
  if ( n0 .lt. 2 ) goto 300

  L0 = len( trim( buffer(1) ) )
  !! https://software.intel.com/en-us/forums/intel-fortran-compiler/topic/287349
  write( 6,* ) " read ", n0, " lines of ", L0, " characters"
  allocate( character (len=L0) :: input( n0 ) )
  allocate( character (len=n0) ::  cols( L0 ) )
  do i = 1, n0
   input(i) = trim( buffer(i) )
   do j = 1, L0
    cols(j)(i:i) = trim( input(i)(j:j) )
   end do
  end do
  deallocate( buffer )

  allocate( character (len=L0) :: message )
  call decode( cols, message )

  write(6,*) message

  deallocate( input )
  deallocate( cols )
  deallocate( message )
  call exit( 0 )
  300 call exit ( 1 )
 end program one
