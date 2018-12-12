#include<stdio.h>
#include<stdlib.h>
#include<stdbool.h>
#pragma pack(1)

#define MAX_NODES 2048
typedef struct {
 short nchild, nmeta;
 int* addr;
 long int tell;
 unsigned int reg;
 bool is_child;
 short meta[1];   // fake size; must be packed last; intentionally overrun addresses within the malloc
} node;

static node* pb[MAX_NODES];  // irregular struct sizes, so array their addresses
static unsigned int inode=0; // should get this into main and pointed at from tree_

node* alloc_node (short n, short m, long int loc, unsigned int registry, bool is_child) {
// node* p = (node *)malloc(
 node* p = malloc(
		 (m+2)*sizeof(short)+sizeof(node*)+sizeof(long int)
		 +sizeof(unsigned int)+sizeof(bool)
		 );
 (*p).nchild = n;
 (*p).nmeta = m;
 (*p).addr = p;		// compilers may complain about this, but it can't fail
 (*p).tell = loc;
 (*p).reg = registry;
 (*p).is_child = is_child;
// printf(" node %2d is a child: %s\n", registry, is_child ? "true" : "false");
 return p;
}

void free_nodes (void) {
 unsigned short i, j;
 for ( j=0; j<inode; j++ ) {
  free( (*pb[j]).addr );
 }
 return;
}

void push (unsigned int on, int* q) {
 int i=0;
 while ( q[i] != -1 ) { i++; }
 q[i] = on;
// printf("\n node %2d pushed to the queue\n", q[i]);
 return;
}
 
unsigned int pop (int* q) {
 int i=0;
 unsigned int off;
 while ( q[i] != -1 ) { i++; }
 off = q[i-1];
 q[i-1] = -1;
// printf("\n node %2d popped off the queue\n", off);
 return (unsigned int) off;
}


// recursive branch walker
void tree_to_structs (FILE* f, int* q, bool bottom) {
 unsigned short i, j, n, m, burn, k=0, l;
 unsigned int current_fill;

 long int loc = ftell(f);
 fscanf(f, "%hd %hd", &n, &m);
 pb[inode] = alloc_node(n,m,loc,inode,!bottom);
 push(inode, q);
 inode++;

 for ( i=0; i<n; i++ ) {
   tree_to_structs(f, q, false);
 }

 current_fill = pop(q);
 while ( current_fill != (*pb[k]).reg ) { k++; }
 for ( l=0; l < (*pb[k]).nmeta; l++ ) {
   fscanf(f, "%hd", &(pb[k]->meta[l]) );
 }

 return;
}

int value_of_node (int nnode, int* answer, int* q, bool bottom) {
 unsigned int i, j, k, n, m, offset;
 int return_val;
 bool trick = true;

 // if no children
 if ( (*pb[nnode]).nchild == 0 ) {
  for ( i=0; i<(*pb[nnode]).nmeta; i++ ) {
   *answer = *answer + (*pb[nnode]).meta[i];
  }
  printf("\n no children return of %d\n", *answer);
  return_val = *answer;
  *answer = 0;
  return return_val;
 }

 // the case of a nonsense child reference
 for ( i=0; i<(*pb[nnode]).nmeta; i++ ) {
  if ( (*pb[nnode]).meta[i] <= (*pb[nnode]).nchild ) { trick = false; }
 }
 if ( trick ) {
  *answer = 0;
  printf("\n       trick return of 0\n");
  return *answer;
 }

 // looping through meta-referred children
 for ( i=0; i<(*pb[nnode]).nmeta; i++ ) {
  // value of meta[i]th child
  // find node number of next child; the node must have children if at this point, so j=1
  offset = 0;
  for ( m=0; m<(*pb[nnode]).meta[i]; m++ ) {
    for ( j=1; j<(*pb[nnode]).nchild+1; j++ ) {
     offset = offset + (*pb[nnode+j]).nchild;
    }
  }
  printf(" \n answer before recursive call %d\n", *answer);
  *answer = *answer + value_of_node( nnode+offset, answer, q, false ); // can't use or zero the answer pointer here !
  printf(" \n  answer after recursive call %d\n", *answer);
 }

 return_val = *answer;
// if ( bottom ) { *answer = 0; }
 *answer = 0;
 return return_val;
}


int main(void) {
 FILE* f;
 int queue[MAX_NODES] = { [0 ... MAX_NODES-1] = -1 };
 int n, value=0;

 f = fopen("license.txt", "r");
 tree_to_structs(f, queue, true);
 fclose(f);

 n = 0;
 printf("\n the value of node %4d is %2d\n\n", n, value_of_node(n, &value, queue, true) );

 free_nodes();

 return 0;
}
