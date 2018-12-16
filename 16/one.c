#include<stdio.h>
#include<stdlib.h>
#include<string.h>

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
 reg R;
 mnemonics m;
 int n;

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

 R.R[0] =  3; R.R[1] =  2; R.R[2] =  1; R.R[3] =  1;
 n = 2;
 printf("\n before %s 2 1 2: [ %2d, %2d, %2d, %2d ]\n", m.name[n], R.R[0], R.R[1], R.R[2], R.R[3]);
 m.instr[n] ( &R, 2, 1, 2);
 printf("\n  after %s 2 1 2: [ %2d, %2d, %2d, %2d ]\n\n", m.name[n], R.R[0], R.R[1], R.R[2], R.R[3]);

 R.R[0] =  3; R.R[1] =  2; R.R[2] =  1; R.R[3] =  1;
 n = 1;
 printf("\n before %s 2 1 2: [ %2d, %2d, %2d, %2d ]\n", m.name[n], R.R[0], R.R[1], R.R[2], R.R[3]);
 m.instr[n] ( &R, 2, 1, 2);
 printf("\n  after %s 2 1 2: [ %2d, %2d, %2d, %2d ]\n\n", m.name[n], R.R[0], R.R[1], R.R[2], R.R[3]);

 R.R[0] =  3; R.R[1] =  2; R.R[2] =  1; R.R[3] =  1;
 n = 9;
 printf("\n before %s 2 1 2: [ %2d, %2d, %2d, %2d ]\n", m.name[n], R.R[0], R.R[1], R.R[2], R.R[3]);
 m.instr[n] ( &R, 2, 1, 2);
 printf("\n  after %s 2 1 2: [ %2d, %2d, %2d, %2d ]\n\n", m.name[n], R.R[0], R.R[1], R.R[2], R.R[3]);

 R.R[0] =  3; R.R[1] =  2; R.R[2] =  1; R.R[3] =  1;
 n = 6;
 printf("\n before %s 2 1 2: [ %2d, %2d, %2d, %2d ]\n", m.name[n], R.R[0], R.R[1], R.R[2], R.R[3]);
 m.instr[n] ( &R, 2, 1, 2);
 printf("\n  after %s 2 1 2: [ %2d, %2d, %2d, %2d ]\n\n", m.name[n], R.R[0], R.R[1], R.R[2], R.R[3]);

 return 0;
}
