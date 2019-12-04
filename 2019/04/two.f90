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
    integer, intent(in) :: n
    logical             :: predicate

    predicate = .false.
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

  stop
 end program main

