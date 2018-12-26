      module subs
      implicit none

      integer, parameter :: nmax = 1080

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
      recursive subroutine finder ( n, x, k, c, new )
      integer (kind=4)                    :: c, k, n
      integer (kind=4), dimension(nmax,5) :: x
      logical                             :: new
!      logical, external                   :: nearby
      integer (kind=4)                    :: i, j

      if (new) then
       x(k,5) = c
      else
       c = x(k,5)
      end if

      do i = 1, n
        if ( x(i,5).ne.x(k,5) .and. nearby( x(k,:), x(i,:)) ) then

!        write(6,'(A16,I3,A9,I3,A18,I3)')&
!     &  '   matching star',k,' and star',i,'  in constellation',c

          x(i,5) = x(k,5)
!          write(6,*) 'calling finder on existing constellation',c
          call finder(n,x,i,x(k,5),.false.)
        end if
      end do

      if ( .not. new ) return

      do i = 1, n
        if ( x(i,5).eq.-1 ) then
          c = c + 1
!          write(6,*) 'calling finder on new constellation',c
          call finder(n,x,i,c,.true.)
        end if
      end do

      return
      end subroutine finder

      end module subs


! main
      program two
      use subs
      implicit none
      integer (kind=4)                    :: i, j, k, c, filelen
      integer (kind=4), dimension(nmax,5) :: x
!      integer (kind=4), allocatable, dimension(:,5) :: x

      open(10,file="scan.dat")
      filelen = 0
      do i = 1, nmax
       read(10,*,err=100,end=100) x(i,1), x(i,2), x(i,3), x(i,4)
       filelen = filelen + 1
       x(i,5) = -1
      end do
100   close(10)

      c = 1
      call finder(filelen,x,1,c,.true.)
      write(6,'(A29,I4)') " number of constellations is ",c

      stop
      end program two
