      program one
      implicit none
      integer label_len, max_lines
      parameter ( label_len = 26, max_lines = 1024 )

!      character (len=label_len), dimension(max_lines) ::  sarray
      character (len=label_len) :: s
      integer n, n2, n3
      n2 = 0
      n3 = 0

      n = 1
      open(10,file='scan.txt',status='old')
      do while ( .true. )
       read(10,*,end=100) s
!       write(6,*) s
       call counter(trim(s),n2,n3)
       n = n + 1
      end do
100   close(10)

!      s(1) = "banana"

!      write(6,*) s(2:)

      write(6,200) ' n2 = ',n2,' n3 = ',n3,' checksum = ',n2*n3
200   format(A6,I4,A6,I4,A12,I8)

      stop
      end program one
!
!
!
!
      subroutine counter(a, n2, n3)
      implicit none
      character a*(*)
      integer n2, n3
      integer i, j, occ
      character s*(1), done*(26)
      logical twos, threes
!      character alpha*26
!      data alpha / 'abcdefghijklmnopqrstuvwxyz' /
      done= ''
      twos = .true.
      threes = .true.
      do i = 1, len(a)
       occ = 0
       s = a(i:i)
!        write(6,*) ' done=',done, ' s=',s, scan(done,s)
       if ( scan(done,s) .gt. 0 ) goto 90
       do j = i, len(a)
!        write(6,*) ' a=',a, ' s=',s, ' a(j)=', a(j:j), ' occ=',occ
        if ( s .eq. a(j:j) ) occ = occ + 1
       end do
       if ( occ .eq. 2 .and. twos ) then
        n2 = n2 + 1
        twos = .false.
       else if ( occ .eq. 3 .and. threes ) then
        n3 = n3 + 1
        threes = .false.
       endif
       done = trim(done)//s
!       write(6,*) done
 90    continue
      end do
      goto 120
100   write(6,*) s, i, 'non-alphabetic character'
110   goto 400
120   continue

400   return
      end subroutine counter
