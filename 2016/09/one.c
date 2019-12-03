// *** Advent of Code 2016 Day 09
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<stdbool.h>

#define MAXCHARS 65536
#define MAX_TOKEN_LENGTH 80

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
// tokenizer to parse "marker" directives into an uncompressed output
void tokenizer ( const char compressed_input[], char tokens[][MAX_TOKEN_LENGTH] ) {
  int i=0, j=0, num_chars_to_repeat, num_repeats;
  char buffer[80];
  bool header = true;    // true if reading a marker; false if reading content

  sscanf( compressed_input, "(%dx%d)", &num_chars_to_repeat, &num_repeats ); // no error check
  sscanf( compressed_input, "%*c",     num_chars_to_repeat, &buffer );
  sprintf( &uncompressed_output + stream_location, "%s"
    
    



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
  //printf( "\n%s\n", compressed_string );

  //
  //  puzzle solution
  //
  //   allocate array of compression markers
  char (*tokens)[MAX_TOKEN_LENGTH] = malloc( (nchars+1) * sizeof( char ) * MAX_TOKEN_LENGTH );
  //       why does casting this to char* not compile ??
  tokenizer( compressed_string, tokens );

  free( tokens );

  return 0;
}
