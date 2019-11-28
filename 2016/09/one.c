// *** Advent of Code 2016 Day 09
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


//
// main program
//
int main( int argc, char *argv[] ) {
  FILE* fp;
  int nchars;

  //  this would work---instead of the following two lines---but
  //  only this way can be immediately freed
  //char temp[MAXCHARS] = { '\0' };
  
  char *temp;
  temp =(char *) malloc( MAXCHARS * sizeof( char ) );

  fp = fopen("puzzle.txt","r");
  reader( fp, &nchars, temp);
  fclose(fp);

  char *compressed_string = malloc( (nchars+1) * sizeof( char ) );
  strncpy( compressed_string, temp, nchars );
  free( temp );     // free the huge temporary file-read buffer
  compressed_string[nchars+1] = '\0';    // in this needed ?

  printf( "\n %s\n", compressed_string );

  return 0;
}
