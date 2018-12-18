      program one
      implicit none
      integer npot0, npots, ngen, m
      parameter ( npot0 = 99, ngen = 20, m = 5, npots = npot0+2*ngen )
!      parameter ( npots = npot0+2*ngen )

      integer i, n, ival
      character (len=npots), dimension(ngen)   :: state
      character (len=m),     dimension(ngen,2) :: grow
      character junk*(15), fill*(9)

      write(fill,'(A6,I2,A1)') "(A15,A",npot0,")"      ! change for three-digit number
      open(10,file="input.txt",status="old")
      read(10,fill) junk, state(1)
      state(1) = '....................'
     $  //trim(state(1))//
     $  '....................' 
      write(6,*) state(1)

      read(10,*)

      do i = 1, ngen
       read(10,'(A5,A4,A1)') grow(i,1), junk, grow(i,2)
      end do
      n = 5
      write(6,*) grow(n,1), ' ', grow(n,2)

!      do i = 1, npot+2*ngen

      close(10)

      stop
      end program one
!
!
      function ival(i)
      integer i
      ival = i - 23
      return
      end function ival
