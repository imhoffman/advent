#include<stdio.h>
#include<stdlib.h>
#include<stdbool.h>

#define MAX_NODES 128
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
 node* p = (node *)malloc(
		 (m+2)*sizeof(short)+sizeof(int*)+sizeof(long int)
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

void push (unsigned int on, int* q) {
 int i=0;
 while ( q[i] != -1 ) { i++; }
 q[i] = on;
 printf("\n node %2d pushed to the queue\n", q[i]);
 return;
}
 
unsigned int pop (int* q) {
 int i=0;
 unsigned int off;
 while ( q[i] != -1 ) { i++; }
 off = q[i-1];
 q[i-1] = -1;
 printf("\n node %2d popped off the queue\n", off);
 return (unsigned int) off;
}


// recursive branch walker
void tree_to_structs (FILE* f, int* q, bool bottom) {
 unsigned short i, j, n, m, burn, k=0, l;
 unsigned int current_fill;

 long int loc = ftell(f);
 fscanf(f, "%hd %hd", &n, &m);
 pb[inode] = alloc_node(n,m,loc,inode,!bottom); push(inode, q); inode++;

 for ( i=0; i<n; i++ ) {
   tree_to_structs(f, q, false);
 }

 current_fill = pop(q);
 while ( current_fill != (*pb[k]).reg ) { k++; }
 for ( l=0; l < (*pb[k]).nmeta; l++ ) {
   fscanf(f, "%hd", &(pb[k]->meta[l]) );
   printf(" filling a %2d\n", (*pb[k]).meta[l]);
 }

 return;
}


void metaop (void) {
 int answer=0;
 unsigned short i, j;
 for ( j=0; j<inode; j++ ) {
  for ( i=0; i<(*pb[j]).nmeta; i++ ) {
   printf(" node %2d: meta[%1d] = %hd\n", j, i, (*pb[j]).meta[i]);
   answer = answer + (*pb[j]).meta[i];
  }
 }
 printf(" sum of metadata: %d\n", answer);
 return;

}


int main(void) {
 FILE* f;
 int queue[MAX_NODES] = { [0 ... MAX_NODES-1] = -1 };

 f = fopen("license.txt", "r");

 tree_to_structs(f, queue, true);
 fclose(f);

 metaop();

 return 0;
}
