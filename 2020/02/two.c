#include <stdio.h>
#include <stdlib.h>
#include <string.h>

//  input file properties
#define MAX_LINE_LENGTH 32
#define MAX_LINES       2048

int
main ( void )
{
  char* token = NULL;
  //  buffer for a one-time file read
  char buffer[MAX_LINE_LENGTH*MAX_LINES] = { '\0' };
  //  array of pointers to each input line
  char* file_line_array[MAX_LINES] = { NULL };

  //  file I/O
  FILE *f = fopen( "puzzle.txt", "r" );
  fread( buffer, (size_t)MAX_LINE_LENGTH*MAX_LINES, (size_t)1, f );
  fclose( f );

  //  parse the file buffer into lines
  int line_count;
  char* str;
  for( line_count=0, str=buffer ; ; line_count++, str=NULL ) {
    token = strtok( str, "\n" );
    if ( token == NULL )
      break;
    file_line_array[line_count] = token;
  }

  //  parse the puzzle info from each line and
  //   apply the ruleset, incrementing the
  //   puzzle tally on success
  int puzzle_count = 0;
  int i1, i2;
  char  c;
  char* s;
  for( int i=0; i<line_count; i++ ) {
    str = file_line_array[i];

    token = strtok( str, "-" );
    sscanf( token, "%d", &i1 );

    token = strtok( NULL, " " );
    sscanf( token, "%d", &i2 );

    token = strtok( NULL, ":" );
    sscanf( token, "%c", &c );

    s = token+3;

    // 1-indexing
    i1--;
    i2--;
    //  ruleset
    _Bool first  = s[i1]==c;
    _Bool second = s[i2]==c;
    if ( ( first || second ) && !( first && second ) ) puzzle_count++;
  }

  fprintf( stdout, " number of valid passwords: %d\n", puzzle_count );

  return 0;
}


