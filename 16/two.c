#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<stdbool.h>
#include<stdint.h>
#include<inttypes.h>

typedef struct {
 int R[4];
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
 

int main (void) {
 FILE *f;
 reg R, Ri, Rf;
 mnemonics m;
 int opcode, A, B, C, n=0, i, j, k;
 int codes[16] = {0};
 bool loop;

 // populate struct
 const char names[][5] = {
  "addr", "addi", "mulr", "muli", "banr", "bani", "borr", "bori",
  "setr", "seti", "gtir", "gtri", "gtrr", "eqir", "eqri", "eqrr"
 };
 const void (*instrs[]) = {
  &addr, &addi, &mulr, &muli, &banr, &bani, &borr, &bori,
  &setr, &seti, &gtir, &gtri, &gtrr, &eqir, &eqri, &eqrr
 };
 memcpy(&(m.name), &names, sizeof(m.name));
 memcpy(&(m.instr), &instrs, sizeof(m.instr));

 char buffer[24]="\0", junk[11];
 f = fopen("input.txt","r");
 for ( i=0; i<16; i++ ) {   //  opcode i
  j = 0;
  fseek(f,0L,SEEK_SET);
  while ( j<16 ) {          //  operation j
   if ( fgets(buffer, 24, f) == NULL ) { j++; fseek(f,0L,SEEK_SET); }
   sscanf(buffer, "%9s %1s%1d,%2d,%2d,%2d]\n", junk,junk,&(Ri.R[0]),&(Ri.R[1]),&(Ri.R[2]),&(Ri.R[3]));
   if ( fgets(buffer, 24, f) == NULL ) { j++; fseek(f,0L,SEEK_SET); }
   sscanf(buffer, "%d %d %d %d\n", &opcode, &A, &B, &C);
   if ( fgets(buffer, 24, f) == NULL ) { j++; fseek(f,0L,SEEK_SET); }
   sscanf(buffer, "%9s %1s%1d,%2d,%2d,%2d]\n", junk,junk,&(Rf.R[0]),&(Rf.R[1]),&(Rf.R[2]),&(Rf.R[3]));
   if ( fgets(buffer, 24, f) == NULL ) { j++; fseek(f,0L,SEEK_SET); }
   if ( j > 15 ) { break; }
   if ( opcode == i ) {
    R = Ri;
   // printf(" attempting %s %d %d %d\n", m.name[j], A, B, C);
    m.instr[j] ( &R, A, B, C);
    if ( Rf.R[0]!=R.R[0] || Rf.R[1]!=R.R[1] || Rf.R[2]!=R.R[2] || Rf.R[3]!=R.R[3] ) {
     codes[i] |= ( 1 << j );
     j++;
     fseek(f,0L,SEEK_SET);
    }
   }
  }
 }
 fclose(f);

 for ( i=0; i<16; i++ ) {
  printf(" i: %2d, 0x%04x\n", i, codes[i]);
 }

 for ( i=0; i<16; i++ ) {
  printf("\n opcode %2d could be: ", i);
  for ( j=0; j<16; j++ ) {
   if ( !(codes[i] & ( 1 << j )) ) { printf(" %s", m.name[j]); }
  }
 }
 printf("\n\n");
 

 return 0;
}
