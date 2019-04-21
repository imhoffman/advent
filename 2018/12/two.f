      program one
      implicit none
      integer*8 npot0, npots, ngen, nrule, m
      parameter ( npot0 = 99, ngen = 21_8, m = 5, nrule = 32,
     $            npots = npot0+2*ngen+2 )

      integer*8 i, j, k, jj, n, ival, total
      character state0*(npot0)
      character (len=m),     dimension(nrule,2) :: grow
      character junk*(15), fmt1*(9), fmt2*(5), pot*(1), nigh*(5)

      write(fmt1,'(A6,I2,A1)') "(A15,A",npot0,")"      ! change for three-digit number
      write(fmt2,'(A2,I2,A1)') "(A",npot0,")"          ! change for three-digit number
      open(10,file="input.txt",status="old")
      read(10,fmt1) junk, state0

      read(10,*)     ! blank line after initial state

      do i = 1, nrule
       read(10,'(A5,A4,A1)') grow(i,1), junk, grow(i,2)
      end do
      close(10)

      do i = 0, npots-1
      end do

      do i = 2, ngen
       do jj = 0, npots-1      ! 0 b/c offsets
       end do
       do j = 3, npots-m
        do k = 1, nrule
        end do
       end do
       do jj = 0, npots-1
       end do
      end do

      total = 0
      do i = 0, npots-1
      end do
      write(6,*) 'sum of plant indices:', total

      stop
      end program one
!
!
      function ival(i)
      implicit none
      integer*8 i, ival
      ival = i - 21
      return
      end function ival
