
 program day02
   implicit none
   integer   :: i1, i2, puzzle_count, j1, j2, n
   character :: c, s*32, line*64
   logical   :: c1, c2

   open( unit=10, file='puzzle.txt', action='read' )

   !!  check for validity during I/O loop
   puzzle_count = 0
   do while ( .true. )

     read( 10, '(A64)', end=200 ) line

     j1 = index( line, '-' )
     read( line(1:j1-1),    * ) i1

     j2 = index( line, ' ' )
     read( line(j1+1:j2-1), * ) i2

     j1 = index( line, ':' )
     read( line(j2+1:j1-1), * ) c

     read( line(j1+2:),     * ) s

     !!  ruleset
     c1 = s(i1:i1) .eq. c
     c2 = s(i2:i2) .eq. c
     if ( c1 .neqv. c2 ) puzzle_count = puzzle_count + 1
     !if ( (c1.or.c2) .and. .not.(c1.and.c2) ) puzzle_count = puzzle_count + 1

   enddo

   200 continue
   close( 10 )
   write( 6, '(A,I0)' ) ' number of valid passwords: ', puzzle_count

   call exit(0)
 end program day02

