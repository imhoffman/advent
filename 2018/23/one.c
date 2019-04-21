#include<stdio.h>
#include<stdlib.h>
#include<stdint.h>
#include<stdbool.h>
#include<math.h>

#define size_of_buffer 80
#define maxbots 1024

typedef struct {
 int64_t x, y, z, r;
} bot;

bool near ( bot I, bot F ) {
 if ( abs(I.x-F.x)+abs(I.y-F.y)+abs(I.z-F.z) <= F.r ) { return true; }
 else { return false; }
}

int main(void) {
 FILE* f;
 int i, j, k, Nbots, imaxr, counter=0;
 int64_t maxr=(int64_t)0;
 bot bots[maxbots];

 f = fopen("data.txt","r");
 char buffer[size_of_buffer]="\0";
 i = 0;
 while ( fgets(buffer, size_of_buffer, f) != NULL ) {
  sscanf(buffer, "pos=<%ld,%ld,%ld>, r=%ld\n",&bots[i].x,&bots[i].y,&bots[i].z,&bots[i].r);
//  printf(" bot at (%ld,%ld,%ld) with range %ld\n", bots[i].x,bots[i].y,bots[i].z,bots[i].r);
  if ( bots[i].r > maxr ) {
   imaxr = i;
   maxr = bots[i].r;
  }
  i++;
 }
 fclose(f);
 Nbots = i;

 printf("\n bot with max range is at (%d,%d,%d), has index %d, and range %d\n\n",
		 bots[imaxr].x,bots[imaxr].y,bots[imaxr].z,imaxr,maxr);

 counter=0;
 for ( i=0; i<Nbots; i++ ) {
  if ( near( bots[i], bots[imaxr] ) ) {
//   printf("\n found a close one with index %d\n", i);
   counter++;
  }
 }

 // don't subtract 1 for itself !
 printf("\n number of bots within range: %d\n\n",counter);

 return 0;
}
