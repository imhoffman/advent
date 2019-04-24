// *** Advent of Code 2016 Day 04
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<stdbool.h>
#include<stddef.h>

#define iw            32
#define lenstr        80
#define max_lines     16384
#define max_checksum  8
const char alpha[]="abcdefghijklmnopqrstuvwxyz";
const char numer[]="0123456789";

typedef struct {
  char        listing[lenstr];
  int              id:iw;
  char      encrypted[lenstr];
  char      decrypted[lenstr];
  char checksum[max_checksum];
  bool                is_real;
} record;

void reader ( FILE* f, int* n, char lines[][lenstr] ) {
  char buffer[lenstr] = {'\0'};

  *n = 0;
  while ( fgets(buffer, lenstr, f) != NULL ) {
    //printf("read line %d as %s\n", *n, buffer);
    strcpy( lines[*n], buffer );
    (*n)++;
  }
  //printf("\n\n The %dth line in the file is %s\n\n",3,&lines[3-1][0]);
  return;
}

int counter ( int n, const char ch, char str[] ) {
  char* rem;

  if ( str[0] == '\0' ) { return 0; }

  rem = strchr( str, ch );

  if ( rem != NULL ) { return 1 + counter( n+1, ch, rem+1 ); }

  return 0;
}

// fortran scan
int scan ( const char str[], const char ch ) {
 return (int)( strchr(str,ch) - &str[0] );     // cast from size_t
}

void decrypter ( record g[], size_t N ) {
  int i, j, k, m1, m2, m3, mm, nthis, nnext, nlast, m4, rotate, oldch, newch;
  char checksum[max_checksum];

  //printf(" passed struct array has %zu elements\n\n", N );
  //printf(" %s has %d %c's\n\n", g[99].listing, counter(0,'t',g[99].listing), 't');
  //printf(" the index of the first 't' is %zu\n", strchr(g[99].listing,'t') - &g[99].listing[0]); 
  //printf(" a 'scan' for 't' returns %d\n", scan(g[99].listing,'t'));

  for( i=0; i<N; i++ ) {
   g[i].is_real = false;
  }

  return;
}
/*
  subroutine decrypter ( g )

   do i = 1, size( g )
     g(i)%is_real = .false.
     m1 = scan( g(i)%listing, "[" )
     m2 = scan( g(i)%listing, "]" )

     ! poor man's is_number...
     m3 = 1024
     do j = 1, len(numer)
       mm = scan( g(i)%listing(:m1-1), numer(j:j) )
       if ( mm .lt. m3  .and.  mm .gt. 0 ) m3 = mm
     end do
     ! store sector id
     if ( m3 .gt. 0 ) read( g(i)%listing(m3:m1-1), * ) g(i)%id
     ! store encrypted prefix
     g(i)%encrypted = trim( g(i)%listing(:m3-1) )
     ! store checksum
     g(i)%checksum = g(i)%listing(m1+1:m2-1)

     ! determine decoys
     do k = 1, m2-m1-2
       nthis = counter(0,       g(i)%checksum(k:k)       ,g(i)%listing(:m1-1))
       nnext = counter(0,     g(i)%checksum(k+1:k+1)     ,g(i)%listing(:m1-1))
       nlast = counter(0, g(i)%checksum(m2-m1-1:m2-m1-1) ,g(i)%listing(:m1-1))
       ! checksum rule set
       if ( &
           &         nlast .gt. 0 &
           & .and. ( nthis .gt. nnext &
           &        .or. ( &
           &              nthis .eq. nnext &
           &  .and. ( scan( alpha, g(i)%checksum(k:k) ) &
           &             .lt. &
           &          scan( alpha, g(i)%checksum(k+1:k+1) ) ) &
           & ) ) ) then
         continue
       else
         goto 420
       end if
     end do
     g(i)%is_real = .true.
     !write(6,'(I5,A)') i, ' is a real room!'
     420 continue

     if ( g(i)%is_real ) then
       m4 = scan( g(i)%encrypted, ' ' ) - 2
       rotate = mod( g(i)%id, len(alpha) )
       ! rule set
       do j = 1, m4
         if ( g(i)%encrypted(j:j) .eq. '-' ) then
           g(i)%decrypted(j:j) = ' '
         else
           oldch = scan( alpha, g(i)%encrypted(j:j) ) 
           if ( rotate + oldch  .gt.  len(alpha) ) then
             newch = oldch + rotate - len(alpha) 
           else
             newch = oldch + rotate
           end if
           g(i)%decrypted(j:j) = alpha( newch:newch )
         end if
       end do
       write(6,'(I5,2X,A)') g(i)%id, g(i)%decrypted(:m4)
     end if
    end do

   return
  end subroutine decrypter
 end module subs
 */

int main( int argc, char *argv[] ) {
 FILE* fp;
 int i, n;
 // how to malloc an array of strings ... pretty
 char (*temp)[lenstr] = malloc( max_lines * sizeof( *temp ) );

 fp = fopen("input.txt","r");
 reader( fp, &n, temp );
 fclose(fp);
 printf(" file has %d lines\n",n);

 record registry[n];
 for( i=0; i<n; i++ ) {
  memset(registry[i].listing, '\0', sizeof( registry[i].listing ) );
  strcpy( registry[i].listing, temp[i] );
 }
 free( temp );

 decrypter( registry , sizeof(registry)/sizeof(registry[0]) );  // must pass array size from calling unit

 return 0;
}
/*

  ! decrypt as per puzzle rules
  call decrypter( registry )

  deallocate ( registry )
  stop
 end program main
*/
