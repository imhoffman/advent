#include<stdio.h>
#include<stdlib.h>

typedef struct {
 int R[4];
} reg;

void addr (reg* R) { (*R).R[(*R).R[2]] = (*R).R[(*R).R[0]] + (*R).R[(*R).R[1]]; return; }
void addi (reg* R) { (*R).R[(*R).R[2]] = (*R).R[(*R).R[0]] + (*R).R[1]; return; }
void mulr (reg* R) { (*R).R[(*R).R[2]] = (*R).R[(*R).R[0]] * (*R).R[(*R).R[1]]; return; }
void muli (reg* R) { (*R).R[(*R).R[2]] = (*R).R[(*R).R[0]] * (*R).R[1]; return; }
void banr (reg* R) { (*R).R[(*R).R[2]] = (*R).R[(*R).R[0]] & (*R).R[(*R).R[1]]; return; }
void bani (reg* R) { (*R).R[(*R).R[2]] = (*R).R[(*R).R[0]] & (*R).R[1]; return; }
void borr (reg* R) { (*R).R[(*R).R[2]] = (*R).R[(*R).R[0]] | (*R).R[(*R).R[1]]; return; }
void bori (reg* R) { (*R).R[(*R).R[2]] = (*R).R[(*R).R[0]] | (*R).R[1]; return; }
void setr (reg* R) { (*R).R[(*R).R[2]] = (*R).R[(*R).R[0]]; return; }
void seti (reg* R) { (*R).R[(*R).R[2]] = (*R).R[0]; return; }
void gtir (reg* R) {
 if ( (*R).R[0] > (*R).R[(*R).R[1]] ) { (*R).R[(*R).R[2]] = 1; return; }
 else { (*R).R[(*R).R[2]] = 0; return; } }
void gtri (reg* R) {
 if ( (*R).R[(*R).R[0]] > (*R).R[1] ) { (*R).R[(*R).R[2]] = 1; return; }
 else { (*R).R[(*R).R[2]] = 0; return; } }
void gtrr (reg* R) {
 if ( (*R).R[(*R).R[0]] > (*R).R[(*R).R[1]] ) { (*R).R[(*R).R[2]] = 1; return; }
 else { (*R).R[(*R).R[2]] = 0; return; } }
void eqir (reg* R) {
 if ( (*R).R[0] == (*R).R[(*R).R[1]] ) { (*R).R[(*R).R[2]] = 1; return; }
 else { (*R).R[(*R).R[2]] = 0; return; } }
void eqri (reg* R) {
 if ( (*R).R[(*R).R[0]] == (*R).R[1] ) { (*R).R[(*R).R[2]] = 1; return; }
 else { (*R).R[(*R).R[2]] = 0; return; } }
void eqrr (reg* R) {
 if ( (*R).R[(*R).R[0]] == (*R).R[(*R).R[1]] ) { (*R).R[(*R).R[2]] = 1; return; }
 else { (*R).R[(*R).R[2]] = 0; return; } }

int main (void) {
 reg R;

// R.R0 = 0x1100;
// R.R1 = 0x0101;
// R.R2 = 0x0011;
// R.R3 = 0x0100;
 R.R[0] =  0; R.R[1] =  7; R.R[2] =  3; R.R[3] =  1;

 printf("\n before addi: [ %2d, %2d, %2d, %2d ]\n", R.R[0], R.R[1], R.R[2], R.R[3]);
 addi ( &R );
 printf("\n  after addi: [ %2d, %2d, %2d, %2d ]\n\n", R.R[0], R.R[1], R.R[2], R.R[3]);

 R.R[0] =  3; R.R[1] =  2; R.R[2] =  1; R.R[3] =  1;
 printf("\n before mulr: [ %2d, %2d, %2d, %2d ]\n", R.R[0], R.R[1], R.R[2], R.R[3]);
 mulr ( &R );
 printf("\n  after mulr: [ %2d, %2d, %2d, %2d ]\n\n", R.R[0], R.R[1], R.R[2], R.R[3]);

 R.R[0] =  3; R.R[1] =  2; R.R[2] =  1; R.R[3] =  1;
 printf("\n before addi: [ %2d, %2d, %2d, %2d ]\n", R.R[0], R.R[1], R.R[2], R.R[3]);
 addi ( &R );
 printf("\n  after addi: [ %2d, %2d, %2d, %2d ]\n\n", R.R[0], R.R[1], R.R[2], R.R[3]);

 R.R[0] =  3; R.R[1] =  2; R.R[2] =  1; R.R[3] =  1;
 printf("\n before seti: [ %2d, %2d, %2d, %2d ]\n", R.R[0], R.R[1], R.R[2], R.R[3]);
 seti ( &R );
 printf("\n  after seti: [ %2d, %2d, %2d, %2d ]\n\n", R.R[0], R.R[1], R.R[2], R.R[3]);

 return 0;
}
