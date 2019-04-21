      module subs
      implicit none

      integer, parameter :: nmax = 512

      contains

      subroutine update ( n, x )
      implicit none
      integer :: n
      integer, dimension(nmax,4) :: x
      integer :: i

      do i = 1, n
       x(i,1) = x(i,1) + x(i,3)
       x(i,2) = x(i,2) + x(i,4)
      end do

      return
      end subroutine update

      end module subs




      program one
      use subs
      implicit none
      integer :: i, j, n, pgbeg
      integer, dimension(nmax,4) :: stars
      real :: xmin=+1E8, xmax=-1E8, ymin=-1E8, ymax=+1E8

      n = 0
      open(10,file="scan.txt")
      do i = 1, nmax
       read(10,100,err=200,end=200) &
     &   stars(i,1), stars(i,2), stars(i,3), stars(i,4) 
!100    format(10X,I2,2X,I2,12X,I2,2X,I2,1X)
100    format(10X,I6,2X,I6,12X,I2,2X,I2,1X)
       n = n + 1
      end do
200   close(10)

      if ( pgbeg(0,'/XWINDOW',1,1) .ne. 1 ) stop
!      if ( pgbeg(0,'out.ps/CPS',1,1) .ne. 1 ) stop
      call pgsvp(  0.05, 0.95, 0.05, 0.95)
      call pgask ( .true. )
      do i = 1, 1000000
!       if ( i .gt. 10000 .and. mod(i,10) .eq. 0 ) then
       if ( i .gt. 10300 ) then
       write(6,*) i
       xmin=+1E8
       xmax=-1E8
       ymin=-1E8
       ymax=+1E8
       do j = 1, n
        if ( real(stars(j,1)) .lt. xmin ) xmin = real(stars(j,1))
        if ( real(stars(j,1)) .gt. xmax ) xmax = real(stars(j,1))
        if ( real(stars(j,2)) .lt. ymax ) ymax = real(stars(j,2))
        if ( real(stars(j,2)) .gt. ymin ) ymin = real(stars(j,2))
       end do
       call pgslw(1)
       call pgswin( xmin  , xmax  , ymin   , ymax    )
       call pgsci(3)
       do j = 1, n
        call pgpt1(real(stars(j,1)),real(stars(j,2)),12)
       end do
       call pgpage
       end if
       call update(n,stars)
      end do
      call pgend

      stop
      end program one
