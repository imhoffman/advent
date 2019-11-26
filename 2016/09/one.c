// *** Advent of Code 2016 Day 09
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<sys/stat.h>
//  the stat library will only work on POSIX; perhaps fseek to SEEK_END elsewhere


//
// main program
//
int main( int argc, char *argv[] ) {
 FILE* fp;
 int compressed_length;

 // https://stackoverflow.com/questions/238603/how-can-i-get-a-files-size-in-c
 struct stat st;
 stat( "puzzle.txt", &st );
 compressed_length = st.st_size;

 char *compressed = malloc( (compressed_length+1) * sizeof( char ) );

 fp = fopen("puzzle.txt","r");
 fgets( compressed, compressed_length, fp );
 fclose(fp);
 compressed[compressed_length+1] = '\0';

 printf( "\n %s\n", compressed );

 free( compressed );
 return 0;
}
