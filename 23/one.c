#include<stdio.h>
#include<stdlib.h>
#include<stdint.h>

#define size_of_buffer 80
#define maxbots 1000

typedef struct {
 int64_t x, y, z, r;
} bot;

int main(void) {
 FILE* f;
 int i, j, k, Nbots, imaxr, counter=0;
 int64_t maxr=(int64_t)0;
 bot bots[maxbots];

 f = fopen("data.txt","r");
 char buffer[size_of_buffer]="\0";
 i = 0;
 while ( fgets(buffer, size_of_buffer, f) != NULL ) {
  sscanf(buffer, "pos=<%lld,%lld,%lld>, r=%lld\n",&bots[i].x,&bots[i].y,&bots[i].z,&bots[i].r);
//  printf(" bot at (%d,%d,%d) with range %d\n", bots[i].x,bots[i].y,bots[i].z,bots[i].r);
  if ( bots[i].r > maxr ) {
   imaxr = i;
   maxr = bots[i].r;
  }
  i++;
 }
 Nbots = i;

 printf("\n bot with max range is at (%d,%d,%d), has index %d, and range %d\n\n",
		 bots[i].x,bots[i].y,bots[i].z,imaxr,maxr);

 for ( i=0; i<Nbots; i++ ) {
 }
 // subtract 1 for itself

 return 0;
}
