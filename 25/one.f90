      program one
      implicit none
      integer, parameter               :: n = 1080
      integer (kind=4)                 :: i, j, k
      integer (kind=4), dimension(n,5) :: x

      open(10,file="scan.dat")
      do i = 1, n
       read(10,*,err=100,end=100) x(i,1), x(i,2), x(i,3), x(i,4) 
      end do
100   close(10)


      stop
      end program one


      subroutine finder ( n, x )
      implicit none
      integer n
      integer x(n,5)

      return
      end subroutine finder
