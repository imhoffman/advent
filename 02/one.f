      program one
      implicit none
      integer label_len
      parameter ( label_len = 6 )

      character s*(label_len)
      integer n2, n3
      n2 = 0
      n3 = 0

      s = "banana"

!      write(6,*) s(2:)

      call counter(s,n2,n3)
      write(6,200) ' n2 = ',n2,' n3 = ',n3,' checksum = ',n2*n3
200   format(A6,I3,A6,I3,A12,I3)

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
      logical twos, checked
      integer i, j, occ
      character s*(1), done*(6)         ! should match label_len
!      character alpha*26
!      data alpha / 'abcdefghijklmnopqrstuvwxyz' /
      done= ''
      do i = 1, len(a)
       occ = 0
       s = a(i:i)
       if ( scan(done,s) .gt. 0 ) goto 90
       do j = i, len(a)
        if ( s .eq. a(j:j) ) occ = occ + 1
        if ( s .eq. a(j:j) ) write(6,*) s, a(j:j)
       end do
       if ( occ .eq. 2 ) n2 = n2 + 1
       if ( occ .eq. 3 ) n3 = n3 + 1
       done = trim(done)//s
       write(6,*) done
 90    continue
      end do
      goto 120
100   write(6,*) s, i, 'non-alphabetic character'
110   goto 400
120   continue

400   return
      end subroutine counter
