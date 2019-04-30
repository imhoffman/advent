// *** Advent of Code 2016 Day 04
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<stdbool.h>
#include<stddef.h>
#include<ctype.h>

#define lenstr        80
#define max_lines     16384
#define max_checksum  8
const char alpha[]="abcdefghijklmnopqrstuvwxyz";

typedef struct {
  char        listing[lenstr];
  int              id;
  char      encrypted[lenstr];
  char      decrypted[lenstr];
  char       checksum[max_checksum];
  bool         is_real;
} record;

void reader ( FILE* f, int* n, char lines[][lenstr] ) {
  char buffer[lenstr] = {'\0'};

  *n = 0;
  while ( fgets(buffer, lenstr, f) != NULL ) {
    strcpy( lines[*n], buffer );
    (*n)++;
  }
  return;
}

int counter ( const char ch, char str[] ) {
  char* rem;

  if ( str[0] == '\0' ) { return 0; }

  rem = strchr( str, ch );
  if ( rem != NULL ) { return 1 + counter( ch, rem+1 ); }

  return 0;
}

// cypher rules
char caesar ( const int orig, const int rotate ) {
  int new;
  if ( orig + rotate > strlen(alpha) - 1 ) { 
    new = orig + rotate - strlen(alpha) ;
  } else {
    new = orig + rotate;
  }
  return alpha[new];
}

// fortran scan
int scan ( const char str[], const char ch ) {
 return (int)( strchr(str,ch) - &str[0] );     // cast from size_t
}

void decrypter ( record g[], size_t N ) {
  int i, j, k, m1, m2, m3, mm, nthis, nnext, nlast, m4;
  long total=0L;
  bool stop;

  //printf(" passed struct array has %zu elements\n\n", N );

  for( i=0; i<(int)N; i++ ) {
   g[i].is_real = false;
   m1 = scan( g[i].listing, '[' );
   m2 = scan( g[i].listing, ']' );
   for ( j=0; j<m1; j++ ) { 
     if ( isdigit( g[i].listing[j] ) ) { m3 = j; break; }
   }
   sscanf( memchr(&g[i].listing[m3],g[i].listing[m3],(size_t)(m1-m3)), "%d", &(g[i].id) );
   strncpy( &(g[i].encrypted[0]), &(g[i].listing[0]), m3 );
   //strncpy( g[i].encrypted, g[i].listing, m3 ); // same as above
   strncpy( &(g[i].checksum[0]), &(g[i].listing[m1+1]), m2-m1-1 );

   stop = false;
   for ( k=0; k<m2-m1-2 && !stop; k++ ) {
     nthis = counter(    g[i].checksum[k]     ,g[i].encrypted);
     nnext = counter(   g[i].checksum[k+1]    ,g[i].encrypted);
     nlast = counter( g[i].checksum[m2-m1-2]  ,g[i].encrypted);
     if (  nlast > 0
        && (  nthis > nnext
           || ( nthis == nnext
               &&  scan(alpha,g[i].checksum[k])<scan(alpha,g[i].checksum[k+1]) ) ) )
      { g[i].is_real = true; } else { g[i].is_real = false; stop = true; }
   }
   if ( g[i].is_real ) {
     total =(long) total + g[i].id;
     m4 = scan( g[i].encrypted, '\0' ) - 1;
     for ( j=0; j<m4; j++ ) {
       if ( g[i].encrypted[j] == '-' ) { g[i].decrypted[j] = ' '; }
       else {
         g[i].decrypted[j] = caesar( scan( alpha, g[i].encrypted[j] ), g[i].id % strlen(alpha) );
       }
     }
     printf("%5d  %s\n", g[i].id, g[i].decrypted);
   }
  }
  printf(" total of real sector ids = %ld\n", total);
  return;
}

//
// main program
//
int main( int argc, char *argv[] ) {
 FILE* fp;
 int i, n;
 // how to malloc an array of strings ... pretty
 char (*temp)[lenstr] = malloc( max_lines * sizeof( *temp ) );

 fp = fopen("input.txt","r");
 reader( fp, &n, temp );
 fclose(fp);

 record registry[n];
 for( i=0; i<n; i++ ) {
  memset(registry[i].listing,   '\0', sizeof( registry[i].listing ) );
  memset(registry[i].encrypted, '\0', sizeof( registry[i].encrypted ) );
  memset(registry[i].decrypted, '\0', sizeof( registry[i].decrypted ) );
  memset(registry[i].checksum,  '\0', sizeof( registry[i].checksum ) );
  strcpy( registry[i].listing, temp[i] );
 }
 free( temp );

 // must pass array size from calling unit
 decrypter( registry , sizeof(registry)/sizeof(registry[0]) );

 return 0;
}
