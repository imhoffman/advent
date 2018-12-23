      program one
      implicit none
      integer n
      parameter ( n = 1024 )
      integer (kind=4)                 :: i, j, k
      integer (kind=8), dimension(n,4) :: bots
      character buffer*(80)

      open(11,file="data.txt")
      i = 0
      do while ( i < n )
       i = i + 1
       read(11,'(A80)',end=200) buffer
       write(6,*) buffer
!       read(buffer,100,end=200) bots(i,1),bots(i,2),bots(i,3),bots(i,1)
      end do
200   continue

      stop
      end program one
