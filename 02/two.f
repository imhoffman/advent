      program two
      implicit none
      integer label_len, max_lines
      parameter ( label_len = 26, max_lines = 250 )

      character (len=label_len), dimension(max_lines) :: s
      integer i

      open(10,file='scan.txt',status='old')
      do i = 1, max_lines
       read(10,*,end=100) s(i)
      end do
100   close(10)

      call compid(s)

      stop
      end program two
!
!
!
!
      subroutine compid(a)
      implicit none
      character (len=*), dimension(*) :: a
      integer i, j
!      do i = 1,   ! use scan by going letter by letter though a
!      comparison; keep the return so that the index can be used to
!      remove the letter later; the comparison will be the scan of one
!      entire string with each successive letter of the next string
      write(6,*) sizeof(a), sizeof(a(1))  ! how to get size of an array of strings?
      return
      end subroutine compid
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
