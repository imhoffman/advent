      module subs
      implicit none
      contains

! nearby
      logical function nearby ( s1, s2 )
      integer (kind=4), dimension(5) :: s1, s2

      if ( abs(s1(1)-s2(1)) + abs(s1(2)-s2(2)) + &
     &      abs(s1(3)-s2(3)) + abs(s1(4)-s2(4)) .le. 3 ) then
       nearby = .true.
      else
       nearby = .false.
      end if

      return
      end function nearby

! finder
      recursive subroutine finder ( n, x )
      integer (kind=4)                 :: n
      integer (kind=4), dimension(n,5) :: x
!      logical, external                :: nearby
      integer (kind=4)                 :: i, j, c

      c = 0
      do i = 1, n
       if ( x(i,5) .ne. -1 ) then
        do j = 1, n
         if ( i.ne.j .and. x(j,5).eq.-1 .and. nearby(x(i,:), x(j,:)) ) then
          x(j,5) = x(i,5)
          write(6,*) 'got in here'
         end if
        end do
       end if
      end do

      return
      end subroutine finder

      end module subs


! main
      program one
      use subs
      implicit none
      integer, parameter               :: n = 1080
      integer (kind=4)                 :: i, j, k, c
      integer (kind=4), dimension(n,5) :: x

      open(10,file="scan.dat")
      do i = 1, n
       read(10,*,err=100,end=100) x(i,1), x(i,2), x(i,3), x(i,4)
       x(i,5) = -1
      end do
100   close(10)
      x(1,5) = 1

      call finder(n,x)

      stop
      end program one
