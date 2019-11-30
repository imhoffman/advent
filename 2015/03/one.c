#include<stdio.h>
#include<stdlib.h>
#include<string.h>

#define MAXMOVES 1048576

void mover ( const char current_move, int *x, int *y ) {
  switch ( current_move ) {
	  case '>': *x = *x + 1; break;
	  case '^': *y = *y + 1; break;
	  case '<': *x = *x - 1; break;
	  case 'v': *y = *y - 1; break;
	  default: printf( "\n :(\n" );
  }
  return;
}


int main ( void ) {
  char buffer[MAXMOVES] = { '\0' };
  FILE *fp;
  char *address_of_null;
  size_t difference_of_memory_locations;
  int number_of_moves;

  fp = fopen( "input.txt", "r" );
  if ( fgets( buffer, MAXMOVES, fp ) != NULL ) {
    address_of_null = strchr( buffer, '\0' );
    difference_of_memory_locations = address_of_null - &buffer[0];
    number_of_moves =(int) difference_of_memory_locations;
  } else {
    printf( " :(\n " );
    return 1;
  }
  fclose(fp);

  //char moves[8193] = { '\0' };
  //char moves[number_of_moves+1] = { '\0' };
  //strncpy( moves, buffer, number_of_moves );

  int x=0, y=0;    // maybe an ordered-pair struct ?
  for ( int i = 0; i < number_of_moves; i++ ) {
    mover( buffer[i], &x, &y );
    printf( " move: %c, x: %d, y: %d\n", buffer[i], x, y );
  }

  //printf( "\n number of moves read from file: %d\n\n", number_of_moves );

  return 0;
}
