program main

 integer :: a
 character(:), allocatable :: s

 s = "412"

 read( s(2:2), '(I1)' ) a

 write(6,*) a

end program main
