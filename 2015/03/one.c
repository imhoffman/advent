#include<stdio.h>
#include<stdlib.h>
#include<string.h>

#define MAXMOVES 1048576


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

  printf( "\n number of moves read from file: %d\n\n", number_of_moves );

  return 0;
}
