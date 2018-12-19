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

! use scratch files rather than strings
!      open(21,status='scratch')    ! initial state
      open(21,file='junk.txt')    ! initial state
      open(31,status='scratch')    ! final state
      open(41,status='scratch')    ! empty state

      do i = 1, ngen
       call fseek(21,i,0,0)
       write(21,'(A1)') '.'
!       write(21,'(A1)',advance='no') '.'
      end do
       call fseek(21,ngen,0,0)
      write(21,fmt2) trim(state0)
!      write(21,fmt2,advance='no') trim(state0)
      do i = 1, ngen+2
       call fseek(21,ngen+npot0+i,0,0)
       write(21,'(A1)') '.'
!       write(21,'(A1)',advance='no') '.'
      end do
      rewind(21)

      do i = 0, npots-1
       call fseek(41,i,0,0)
       write(41,'(A1)') '.'
      end do
      write(6,*) ' is rewind the problem?'
      rewind(41)
      write(6,*) ' is rewind the problem?'

      do i = 2, ngen
       do jj = 0, npots-1      ! 0 b/c offsets
        call fseek(31,jj,0,0)
        call fseek(41,jj,0,0)
        read(41, '(A1)', advance='no') pot
        write(31,'(A1)', advance='no') pot
       end do
       rewind(41)
       do j = 3, npots-m
        call fseek(21,j-3,0,0)
        read(21,'(A5)',advance='no') nigh
        do k = 1, nrule
         if ( nigh .eq. grow(k,1) ) then
          call fseek(31,j,0,0)
          write(31,'(A1)',advance='no') grow(k,2)
         end if
        end do
       end do
       do jj = 0, npots-1
        call fseek(21,jj,0,0)
        call fseek(31,jj,0,0)
        read(31, '(A1)', advance='no') pot
        write(21,'(A1)', advance='no') pot
       end do
       rewind(21)
       rewind(31)
      end do
      close(21)
      close(41)

      total = 0
      do i = 0, npots-1
       call fseek(31,i,0,0)
       read(31,'(A1)',advance='no') pot
       if ( pot .eq. '#' ) then
        total = total + ival(i)
       end if
      end do
      close(31)
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
