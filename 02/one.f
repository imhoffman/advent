      program one
      implicit none
      integer label_len
      parameter ( label_len = 6 )
!
      character s*(label_len)
      integer n2, n3
!
      s = "banana"
!
      write(6,*) s(2:)
!
      write(6,*) verify("fortran", "r")
      write(6,*) scan("fortran", "r")
!
      call counter(s,n2,n3)
!
      stop
      end program one
!
!
      subroutine counter (a, n2, n3)
      implicit none
      character a*(*)
      integer n2, n3
      integer i, n
      character alpha*26, s*(1)
!
      data alpha / "abcdefghijklmnopqrstuvwxyz" /
!
      do i = 1, len("fortran")
       s = a(i)
       if ( verify(alpha,s) .ne. 1 ) goto 100
      end do
      goto 120
100   write(6,*) 'non-alphabetic character'
110   goto 400
120   continue
!
400   return
      end subroutine counter
