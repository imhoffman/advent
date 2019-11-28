// *** Advent of Code 2015 Day 01
#include<stdio.h>
#include<stdlib.h>
#include<string.h>

#define MAXCHARS 65536

//  file-reader subroutine to determine dynamic lengths
void reader( FILE *f, int *n, char file_contents[] ) {
  char buffer[MAXCHARS] = { '\0' };
  int num_lines = 0;
  char *qnull;
  size_t file_length;

  while ( fgets( buffer, MAXCHARS, f ) != NULL ) {
    strncpy( file_contents, buffer, MAXCHARS );
    num_lines = num_lines + 1;
  }

  if ( num_lines > 1 ) {
	  printf( "\n PROBLEM: more than one line in file\n" );
	  *n = -1;
	  return;
  }

  qnull = strchr( file_contents, '\0' );
  file_length = qnull - &file_contents[0];   // difference of size_t's
  *n =(int) file_length;

  return;
}


//  recursive function to count occurences of ch in str
int counter ( const char ch, const char str[], const int accum ) {
  char* rem;

  if ( str[0] == '\0' ) { return accum; }

  rem = strchr( str, ch );
  if ( rem != NULL ) { return counter( ch, rem+1, accum+1 ); }
  //  rem+1 is pointer arith.;  accum+1 is integer arith.

  return accum;
}



//
// main program
//
int main( int argc, char *argv[] ) {
  FILE* fp;
  int nchars;

  char *temp;
  temp =(char *) malloc( MAXCHARS * sizeof( char ) );

  fp = fopen("puzzle.txt","r");
  reader( fp, &nchars, temp);
  fclose(fp);

  char *directions = malloc( (nchars+1) * sizeof( char ) );
  strncpy( directions, temp, nchars );
  free( temp );     // free the huge temporary file-read buffer
  directions[nchars+1] = '\0';    // in this needed ?

  printf( "\n finished on floor %d\n\n", 
	  counter( '(', directions, 0 ) - counter( ')', directions, 0 ) );

  return 0;
}
