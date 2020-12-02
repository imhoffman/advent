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
  //  the address to be used for the tokens
  //   in the read loop
  char* token = NULL;

  //  the array that will hold the string as
  //   it is read in from the file
  char buffer[MAX_LINE_LENGTH*MAX_LINES] = { '\0' };

  //  the array that will hold the integer
  //   from each line as it is parsed
  int  input[MAX_LINES] = { 0 };

  //
  //  file I/O
  //
  FILE *f = fopen( "input.txt", "r" );
  fread( buffer, (size_t)MAX_LINE_LENGTH*MAX_LINES, (size_t)1, f );
  fclose( f );

  //  keep getting lines as long as they
  //   are available from the buffer
  int count;
  char* str;  //  the re-rentry semafore, as per the `man` example
  for ( count=0, str=buffer ; ; count++, str=NULL ) {
    token = strtok( str, "\n" );
    if ( token == NULL )
      break;
    sscanf( token, "%d", &input[count] );
  }

  //  ruleset
  for( int i=0; i<count-1; i++ )
    for( int j=i; j<count; j++ ) {
      const int a = input[i];
      const int b = input[j];
      if ( 2020 == a + b ) {
       	fprintf( stdout, "answer: %d\n", a*b );
	//  break both loops simultaneously
	goto found_it;
      }
    }

  found_it: ;

  return 0;
}




