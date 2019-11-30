#include<stdio.h>
#include<stdlib.h>
#include<string.h>

#define MAX_LINE_LENGTH       100
#define MAX_LINES_IN_FILE     30000

//
//  subroutine for determining length of file and longest single line
//  the file read is held in a large temporary array that is free'ed
//  in main once an appropriately-sized array is allocated and populated
void reader ( FILE* f, int* n, int* maxlen, char lines[][MAX_LINE_LENGTH] ) {
  char buffer[MAX_LINE_LENGTH];   // fgets null-terminates
  char *qnull;
  size_t maxtemp;

  *maxlen=0;
  *n = 0;
  while ( fgets(buffer, MAX_LINE_LENGTH, f) != NULL ) {
    strncpy( lines[*n], buffer, MAX_LINE_LENGTH );
    qnull = strchr( buffer, '\0' );  // no need to memset buffer to all \0 every loop
    maxtemp = qnull - &buffer[0];    // arithmetic among memory addresses
    if ( (int)maxtemp > *maxlen ) { *maxlen =(int) maxtemp; }
    (*n)++;
  }
  return;
}


//  subroutine to parse box dimensions and needed ribbon from input strings
int box_dimension_parser ( const char input[] ) {
  int smallest_side_length, x, y, z;

  sscanf( input, "%dx%dx%d", &x, &y, &z );

  if ( x*y <= x*z  &&  x*y <= y*z ) { smallest_side_length = x+y; }
  if ( x*z <= x*y  &&  x*z <= y*z ) { smallest_side_length = x+z; }
  if ( z*y <= x*z  &&  z*y <= y*x ) { smallest_side_length = z+y; }

  return x*y*z + 2*smallest_side_length;
}

//
// main program
//
int main( int argc, char *argv[] ) {
 FILE* fp;
 int number_of_lines, longest_line, total;
 char (*temp)[MAX_LINE_LENGTH]
	 = malloc( MAX_LINES_IN_FILE * sizeof( char ) * MAX_LINE_LENGTH );
         //  do we need to cast from (void *) to (char *) ?

 //  file I/O and memory allocation of memory input
 fp = fopen("puzzle.txt","r");
 reader( fp, &number_of_lines, &longest_line, temp );  // n and longest_line are known upon return
 fclose(fp);        // done with file...only read the file once

 printf( "\n read %d lines from the input file\n", number_of_lines );
 printf( "\n the longest line is %d chars long\n\n", longest_line );

 char input[number_of_lines][longest_line+1];   // declare an array of appropriate length and width

 //  populate `input` with puzzle data for use going forward
 for( int i=0; i < number_of_lines; i++ ) {
  strncpy( input[i], temp[i], longest_line+1 );
 }
 free( temp );  // keeper now has the data...free the big temp array from the file read
 //  end of file I/O

 total = 0; 
 for ( int i=0; i < number_of_lines; i++ ) {
   total += box_dimension_parser( input[i] );
 }
 printf( "\n total ribbon needed: %d feet\n\n", total );

 return 0;
}

