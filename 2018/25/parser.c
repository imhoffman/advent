#include<stdio.h>

int main(void) {
 FILE *fin, *fout;
 char buffer[32]="\0";
 int x, y, z, t;
 fin = fopen("scan.txt","r");
 fout= fopen("scan.dat","w");

 while ( fgets(buffer, 24, fin) != NULL ) {
  sscanf(buffer,"%d,%d,%d,%d\n",&x,&y,&z,&t);
  fprintf(fout, "%d %d %d %d\n",x,y,z,t);
 }

 fclose(fin); fclose(fout);

 return 0;
}
