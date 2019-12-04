!! *** Advent of Code 2019 Day 04

!!
!! subroutines and functions
!!
 module subs
  implicit none
  contains

  !!
  !! puzzle ruleset
  !!

  !!  parse integer into a char arraay
  function as_char_array ( as_integer ) result ( ca )
    character(6)        :: ca
    integer, intent(in) :: as_integer
    character           :: c
    integer             :: i, j, n, current_modulus

    n = as_integer
    j = 0
    do i = 5, 0, -1
      current_modulus = mod( n, int( 10**i ) )
      write( c, '(I1)' ) ( n - current_modulus )/10**i
      n = current_modulus
      j = j + 1
      ca(j:j) = c
    end do

    return
  end function as_char_array


  !!  test for increasing digit values
  recursive function is_increasing ( n ) result ( predicate )
    integer, intent(in)        :: n
    logical                    :: predicate
    character(:), allocatable  :: chars
    integer                    :: i, j, num_chars, a, b

    chars = as_char_array( n )
    num_chars = len( chars )

    if ( num_chars .eq. 1 ) then
      predicate = .true.
    else
      do i = 1, num_chars
        write(6,*) chars(i:i)
        read( chars(i:i), '(I1)' ) a
        !write(6,*) " a: ", a
        do j = i+1, num_chars-i
          read( chars(j:j), '(I1)' ) b
          !write(6,*) " b: ", b
          if ( a .gt. b ) then
            predicate = .false.
          else
            predicate = is_increasing( mod( n, int( 10**i ) ) )
          endif
        enddo
      enddo
    endif

    return
  end function is_increasing


  !!  test for repetitions as per rules
  recursive function has_repeat ( n ) result ( predicate )
    integer, intent(in) :: n
    logical             :: predicate

    predicate = .false.
    return
  end function has_repeat
 end module subs

!!
!! main program
!!
 program main
  use subs
  implicit none
  integer :: upper_bound, lower_bound

  ! read puzzle input --- always six-digit integers
  open( 10, file="puzzle.txt")
  read( 10, '(I6,1X,I6)' ) lower_bound, upper_bound
  close( 10 )

  write(6,'(/,A,I0,A,I0,A,/)') ' Using ', lower_bound, ' and ', upper_bound, ' as bounds.'

  write(6,'(/,A,/)') ' as_char_array: '//as_char_array( lower_bound )

  write(6,*) is_increasing( lower_bound )

  stop
 end program main

