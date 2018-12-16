#include<stdio.h>
#include<stdlib.h>

typedef struct {
 int R0,R1,R2,R3;
} reg;

void addr (int* A, int* B, int* C) {
	*C = *A + *B;
	return;
}

void addi (int* A, int B, int* C) {
	*C = *A + B;
	return;
}

int main (void) {
 reg R;

 R.R0 = 1;
 R.R1 = 2;
 R.R2 = 3;
 R.R3 = 4;

 printf("\n before addr: [ %2d, %2d, %2d, %2d ]\n\n", R.R0, R.R1, R.R2, R.R3);

 addr ( &(R.R0), &(R.R1), &(R.R2) );

 printf("\n  after addr: [ %2d, %2d, %2d, %2d ]\n\n", R.R0, R.R1, R.R2, R.R3);

 return 0;
}
