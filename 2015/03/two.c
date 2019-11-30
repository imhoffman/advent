#include<stdio.h>
#include<string.h>
#include<stdbool.h>

#define MAXMOVES 16384

// subroutine for updating the current location
void mover ( const char current_move, int *x, int *y ) {
  switch ( current_move ) {
    case '>': *x = *x + 1; break;
    case '^': *y = *y + 1; break;
    case '<': *x = *x - 1; break;
    case 'v': *y = *y - 1; break;
    default: printf( "\n :(\n" );   // error checker...should not happen
  }
  return;
}

//
//  main program
//
int main ( void ) {
  char buffer[MAXMOVES];    // fgets will terminate with a null
  FILE *fp;
  char *address_of_null;
  size_t difference_of_memory_locations;
  int x, y, number_of_moves, number_of_houses_visited;
  bool match;
  int houses[MAXMOVES][3] = { -9000 };  //  2D array: x, y, number_of_presents
                                        //  initialize with negative presents
					//  for identification later

  //  file I/O
  fp = fopen( "input.txt", "r" );
  if ( fgets( buffer, MAXMOVES, fp ) != NULL ) {
    address_of_null = strchr( buffer, '\0' );
    difference_of_memory_locations = address_of_null - &buffer[0];
    number_of_moves =(int) difference_of_memory_locations;
  } else {
    printf( " :(\n " );    // error checking
    return 1;
  }
  fclose(fp);

  //  puzzle solution
  //   start by delivering two presents to the first house
  x = 0;  y = 0;
  houses[0][2] = 2;
  houses[0][0] = x;
  houses[0][1] = y;
  //   loop over moves
  for ( int i = 1; i < number_of_moves; i++ ) {
	//  loop with i += 2 with a second mover on i-1+1 ?
	//  or a duplicate round of the j search for the other i's
    mover( buffer[i-1], &x, &y );     // update location

    //  look for a match to a previous visit
    match = false;
    for ( int j = 0; j < i; j++ ) { //  NB: j does not map to i
      if ( houses[j][0] == x  &&  houses[j][1] == y ) {
        houses[j][2] += 1;          // give them one more present
	match = true;
        break;
      }
    }
    if ( !match ) {           // must be the first visit
       houses[i][2] = 1;      // give them their first present
       houses[i][0] = x; houses[i][1] = y;
    }
  }

  //  loop over houses and count the ones without the negative initializer
  number_of_houses_visited = 0;
  for ( int i = 0; i < number_of_moves; i++ ) {
    if ( houses[i][2] > 0 ) { number_of_houses_visited += 1; }
  }

  printf( "\n number of houses visited: %d\n\n", number_of_houses_visited );

  return 0;
}
