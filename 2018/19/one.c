#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<stdbool.h>
#include<stdint.h>
#include<inttypes.h>

#define MAX_PROG_LINES 128

typedef struct {
 int R[6];
} reg;

void addr (reg* R, int A, int B, int C) { (*R).R[C] = (*R).R[A] + (*R).R[B]; return; }
void addi (reg* R, int A, int B, int C) { (*R).R[C] = (*R).R[A] + B; return; }
void mulr (reg* R, int A, int B, int C) { (*R).R[C] = (*R).R[A] * (*R).R[B]; return; }
void muli (reg* R, int A, int B, int C) { (*R).R[C] = (*R).R[A] * B; return; }
void banr (reg* R, int A, int B, int C) { (*R).R[C] = (*R).R[A] & (*R).R[B]; return; }
void bani (reg* R, int A, int B, int C) { (*R).R[C] = (*R).R[A] & B; return; }
void borr (reg* R, int A, int B, int C) { (*R).R[C] = (*R).R[A] | (*R).R[B]; return; }
void bori (reg* R, int A, int B, int C) { (*R).R[C] = (*R).R[A] | B; return; }
void setr (reg* R, int A, int B, int C) { (*R).R[C] = (*R).R[A]; return; }
void seti (reg* R, int A, int B, int C) { (*R).R[C] = A; return; }
void gtir (reg* R, int A, int B, int C) {
 if ( A > (*R).R[B] ) { (*R).R[C] = 1; return; }
 else { (*R).R[C] = 0; return; } }
void gtri (reg* R, int A, int B, int C) {
 if ( (*R).R[A] > B ) { (*R).R[C] = 1; return; }
 else { (*R).R[C] = 0; return; } }
void gtrr (reg* R, int A, int B, int C) {
 if ( (*R).R[A] > (*R).R[B] ) { (*R).R[C] = 1; return; }
 else { (*R).R[C] = 0; return; } }
void eqir (reg* R, int A, int B, int C) {
 if ( A == (*R).R[B] ) { (*R).R[C] = 1; return; }
 else { (*R).R[C] = 0; return; } }
void eqri (reg* R, int A, int B, int C) {
 if ( (*R).R[A] == B ) { (*R).R[C] = 1; return; }
 else { (*R).R[C] = 0; return; } }
void eqrr (reg* R, int A, int B, int C) {
 if ( (*R).R[A] == (*R).R[B] ) { (*R).R[C] = 1; return; }
 else { (*R).R[C] = 0; return; } }

typedef struct {
 char name[16][5];
 void (*instr[16])(reg* R, int A, int B, int C);
} mnemonics;

typedef struct {
 char mn[5];
 int A, B, C;
} prog_line;

typedef struct {
 int ipreg;
 prog_line line[1];    // struct hack; only address within malloc
} prog;
 

int main (void) {
 FILE *f;
 prog* p = malloc(sizeof(int)+MAX_PROG_LINES*sizeof(prog_line));
 reg R, Ri, Rf;
 mnemonics m;
 int A, B, C, n=0, i, j, k, Nlines;

 // populate struct
 const char names[][5] = {
  "addi", "bani", "gtir", "borr", "eqrr", "bori", "gtrr", "setr",
  "muli", "seti", "banr", "gtri", "eqir", "eqri", "addr", "mulr"
 };
 const void (*instrs[]) = {
  &addi, &bani, &gtir, &borr, &eqrr, &bori, &gtrr, &setr,
  &muli, &seti, &banr, &gtri, &eqir, &eqri, &addr, &mulr
 };
 memcpy(&(m.name), &names, sizeof(m.name));
 memcpy(&(m.instr), &instrs, sizeof(m.instr));

 char buffer[24]="\0", opinstr[5], junk[32];
 f = fopen("input.txt","r");

 fgets(buffer, 24, f);                        // read header line
 sscanf(buffer, "%s %d\n", &junk, &(p->ipreg) );
 printf(" instruction pointer register: %d\n",(*p).ipreg);

 k = 0;
 while ( fgets(buffer, 24, f) != NULL ) {     // read lines of program
  sscanf(buffer, "%s %d %d %d\n",
		  &(p->line[k].mn), &(p->line[k].A), &(p->line[k].B), &(p->line[k].C));
  k++;
 }
 fclose(f);
 Nlines = k;
 printf(" Nlines = %2d\n",Nlines);

 for (i=0;i<6;i++) { R.R[i]=0; }     // initialize register
 k=0;
 while ( true ) {
//  printf(" ip = %2d\n",R.R[(*p).ipreg]);
  k = R.R[(*p).ipreg];
  for ( i=0; i<16; i++ ) {
   if ( !strcmp(m.name[i], (*p).line[k].mn ) ) {
//    printf(" ["); for (j=0;j<6;j++) { printf(" %d,", R.R[j]); } printf("]\n");
//    printf(" executing %s %d %d %d\n",m.name[i],(*p).line[k].A,(*p).line[k].B,(*p).line[k].C);
    m.instr[i] ( &R, (*p).line[k].A, (*p).line[k].B, (*p).line[k].C);
//    printf(" ["); for (j=0;j<6;j++) { printf(" %d,", R.R[j]); } printf("]\n");
   }
  }
  // does "instruction pointer is incremented" mean "stored in the ip register?"
  if ( R.R[(*p).ipreg]+1 > Nlines-1 ) { break; }
  R.R[(*p).ipreg]++;
 }

 n = 0;
 printf("\n\n register %d at the end of the program has value %d\n\n",n,R.R[n]);

 return 0;
}
