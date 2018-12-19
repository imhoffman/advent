      program one
      implicit none
      integer npot0, npots, ngen, nrule, m
      parameter ( npot0 = 99, ngen = 20, m = 5, nrule = 32,
     $            npots = npot0+2*ngen )

      integer i, j, k, n, ival, total
      character (len=npots), dimension(ngen)    :: state
      character (len=m),     dimension(nrule,2) :: grow
      character junk*(15), fill*(9), current*(npots), empty*(npots)

      write(fill,'(A6,I2,A1)') "(A15,A",npot0,")"      ! change for three-digit number
      open(10,file="input.txt",status="old")
      read(10,fill) junk, state(1)
      state(1) = '....................'
     $  //trim(state(1))//
     $  '....................' 
!      write(6,*) state(1)

      read(10,*)     ! blank line after initial state

      do i = 1, nrule
       read(10,'(A5,A4,A1)') grow(i,1), junk, grow(i,2)
      end do
!      n = 5
!      write(6,*) grow(n,1), ' ', grow(n,2)

      do i = 1, npots
       empty(i:i) = "."
      end do

      do i = 2, ngen
       current = empty
       do j = 3, npots-m
        do k = 1, nrule
         if ( state(i-1)(j-2:j-2+m-1) .eq. grow(k,1) ) then
          current(j:j) = grow(k,2)
         end if
        end do
       end do
       state(i) = current
!       write(6,*) state(i)
      end do
      close(10)

      total = 0
      do i = 1, npots
       if ( current(i:i) .eq. '#' ) then
        total = total + ival(i)
       end if
      end do
      write(6,*) 'sum of plant indices:', total
      ! 2656 too low

      stop
      end program one
!
!
      function ival(i)
      integer i
      ival = i - 23
      return
      end function ival
