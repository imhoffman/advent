#include <stdio.h>
#include <stdlib.h>
#include <string.h>

//  for simplicity, use these values to allocate
//   more space than is needed
#define MAX_LINE_LENGTH 16
#define MAX_LINES       1024

int
main ( void )
{
  //  the array that will hold the string as
  //   it is read in from the file
  char buffer[MAX_LINE_LENGTH] = { '\0' };

  //  the array that will hold the integer
  //   from each line as it is parsed
  int  input[MAX_LINES] = { 0 };

  //  a running tally that will tell us how
  //   many lines were actually in the file
  //   and how long our loops must actually be
  int  count;

  //
  //  file I/O
  //
  FILE *f = fopen( "input.txt", "r" );

  //  initialize the running tally
  count = 0;

  //  keep getting lines as long as they
  //   are available from the file
  while ( fgets(buffer, MAX_LINE_LENGTH, f) != NULL ) {
    //  read the contents of the line as
    //   a formatted integer
    sscanf( buffer, "%d\n", &input[count] );
    count++;
  }

  fclose( f );
  //
  //  end file I/O
  //


  //  ruleset
  for( int i=0; i<count-2; i++ )
    for( int j=i; j<count-1; j++ )
      for( int k=j; k<count; k++ ) {
        const int a = input[i];
        const int b = input[j];
        const int c = input[k];
        if ( 2020 == a + b + c) {
	  fprintf( stdout, "answer: %d\n", a*b*c );
	  //  break both loops simultaneously
	  goto found_it;
       	}
      }

  found_it: ;

  return 0;
}




