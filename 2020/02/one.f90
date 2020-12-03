
 module subs
   implicit none
   contains
   pure function occur( s, c ) result ( n )
     character (*), intent(in) :: s
     character, intent(in)     :: c
     integer                   :: n
     integer                   :: k, i

     n = 0
     k = 1
     do while ( .true. )
       i = index( s(k:), c )
       if ( i .eq. 0 ) exit
       k = k + i
       n = n + 1
     enddo

     return
   end function occur
 end module subs


 program day02
   use subs
   implicit none
   integer   :: nmin, nmax, puzzle_count, j1, j2, n
   character :: c, s*32, line*64

   open( unit=10, file='puzzle.txt', action='read' )

   !!  check for validity during I/O loop
   puzzle_count = 0
   do while ( .true. )

     read( 10, '(A64)', end=200 ) line

     j1 = index( line, '-' )
     read( line(1:j1-1),    * ) nmin

     j2 = index( line, ' ' )
     read( line(j1+1:j2-1), * ) nmax

     j1 = index( line, ':' )
     read( line(j2+1:j1-1), * ) c

     read( line(j1+2:),     * ) s

     !!  ruleset
     n = occur( s, c )
     if ( n .ge. nmin  .and.  n .le. nmax ) puzzle_count = puzzle_count + 1

   enddo

   200 continue
   close( 10 )
   write( 6, '(A,I0)' ) ' number of valid passwords: ', puzzle_count

   call exit(0)
 end program day02

