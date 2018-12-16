#include<stdio.h>
#include<stdlib.h>

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

int main (void) {
 reg R;
 void (*instr[16])(reg* R, int A, int B, int C) = {
  &addr, &addi, &mulr, &muli, &banr, &bani, &borr, &bori,
  &setr, &seti, &gtir, &gtri, &gtrr, &eqir, &eqri, &eqrr
 };

 R.R[0] =  3; R.R[1] =  2; R.R[2] =  1; R.R[3] =  1;
 printf("\n before mulr 2 1 2: [ %2d, %2d, %2d, %2d ]\n", R.R[0], R.R[1], R.R[2], R.R[3]);
 instr[2] ( &R, 2, 1, 2);
 printf("\n  after mulr 2 1 2: [ %2d, %2d, %2d, %2d ]\n\n", R.R[0], R.R[1], R.R[2], R.R[3]);

 R.R[0] =  3; R.R[1] =  2; R.R[2] =  1; R.R[3] =  1;
 printf("\n before addi 2 1 2: [ %2d, %2d, %2d, %2d ]\n", R.R[0], R.R[1], R.R[2], R.R[3]);
 instr[1] ( &R, 2, 1, 2 );
 printf("\n  after addi 2 1 2: [ %2d, %2d, %2d, %2d ]\n\n", R.R[0], R.R[1], R.R[2], R.R[3]);

 R.R[0] =  3; R.R[1] =  2; R.R[2] =  1; R.R[3] =  1;
 printf("\n before seti 2 1 2: [ %2d, %2d, %2d, %2d ]\n", R.R[0], R.R[1], R.R[2], R.R[3]);
 instr[9] ( &R, 2, 1, 2 );
 printf("\n  after seti 2 1 2: [ %2d, %2d, %2d, %2d ]\n\n", R.R[0], R.R[1], R.R[2], R.R[3]);

 return 0;
}
