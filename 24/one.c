#include<stdio.h>
#include<stdlib.h>
#include<stdbool.h>
#include<limits.h>

#define Ngroups    20

#define radiation   0
#define bludgeoning 1
#define fire        2
#define slashing    3
#define cold        4

#define army0       0
#define army1       1

typedef struct {
 int army, units, adam, atype, init, hp, weak, imm, order, attacking, epow;
 bool ranked;
} group;

void data_entry ( group* armies );

// for ordering array to survive ranking algorith
void arrange_init ( group* b, bool bottom ) {
 group temp;
 bool changes=false;
 int i, j, N=Ngroups;

 for ( i=0; i<N; i++ ) {
  for ( j=0; j<N; j++ ) {
   if ( i < j && b[i].epow == b[j].epow && b[i].init < b[j].init ) {
    temp = b[i]; b[i] = b[j]; b[j] = temp; changes=true;
   } } }
 if ( changes || bottom ) { arrange_init ( b, false ); }
 return;
}

// assign selection order
void power_rank ( group* b ) {
 int N=Ngroups, i, turn, higher=INT_MAX, current=0, iturn=0;
 for ( turn=0; turn<N; turn++ ) {
  for ( i=0; i<N; i++ ) {
   if ( b[i].epow >= current && b[i].epow <= higher && !b[i].ranked ) { current = b[i].epow; iturn = i; }
  } higher = current; current = 0; b[iturn].order = turn; b[iturn].ranked = true;
 }
 return;
}

// for determining max-damage foe
void select_foe ( group* b, bool bottom ) {
 bool changes=false;
 int i, j, N=Ngroups;

 for ( i=0; i<N; i++ ) {
  for ( j=0; j<N; j++ ) {
   if ( i < j && b[i].epow == b[j].epow && b[i].init < b[j].init ) {
    changes=true;
   } } }
 if ( changes || bottom ) { arrange_init ( b, false ); }
 return;
}

void target_selection ( group* a ) {
 int N=Ngroups;
 int i, j, k;

// load in effective powers for this round's ranking
 for ( i=0; i<N; i++ ) {
  a[i].epow = a[i].units * a[i].adam; a[i].ranked = false;
 }

 arrange_init(a,true);
 power_rank(a);

 return;
}

int main (void) {
 FILE* f;
 int i, j, k, n;

 group armies[Ngroups];
 data_entry( armies );

 n = 12;
 printf(" group %2d is in army %d and has weaknesses 0x%04x and immunities 0x%04x\n",n,armies[n].army,armies[n].weak,armies[n].imm);
 n = 07;
 printf(" group %2d is in army %d and has weaknesses 0x%04x and immunities 0x%04x\n",n,armies[n].army,armies[n].weak,armies[n].imm);

 target_selection( armies );
 for ( i=0; i<Ngroups; i++ ) {
  printf(" group %2d is in army %d, has effective power %d, and is attacking with rank %2d\n",i,armies[i].army,armies[i].epow,armies[i].order);
 }

 return 0;
}

void data_entry ( group* armies ) {
 int n;

 n = 0; armies[n].army=army0;
 armies[n].units=479; armies[n].hp=3393; armies[n].init=8; armies[n].adam=66;
 armies[n].atype=0; armies[n].atype |= (1<<cold);
 armies[n].weak=0; armies[n].weak |= (1<<radiation);
 armies[n].imm=0;

 n = 1; armies[n].army=army0;
 armies[n].units=2202; armies[n].hp=4950; armies[n].init=20; armies[n].adam=18;
// armies[n].units=2202; armies[n].hp=4950; armies[n].init=2; armies[n].adam=18;
 armies[n].atype=0; armies[n].atype |= (1<<cold);
 armies[n].weak=0; armies[n].weak |= (1<<fire);
 armies[n].imm=0; armies[n].imm |= (1<<slashing);

 n = 2; armies[n].army=army0;
 armies[n].units=8132; armies[n].hp=9680; armies[n].init=7; armies[n].adam=9;
 armies[n].atype=0; armies[n].atype |= (1<<radiation);
 armies[n].weak=0; armies[n].weak |= (1<<bludgeoning); armies[n].weak |= (1<<fire);
 armies[n].imm=0; armies[n].imm |= (1<<slashing);

 n = 3; armies[n].army=army0;
 armies[n].units=389; armies[n].hp=13983; armies[n].init=13; armies[n].adam=256;
 armies[n].atype=0; armies[n].atype |= (1<<cold);
 armies[n].weak=0;
 armies[n].imm=0; armies[n].imm |= (1<<bludgeoning);

 n = 4; armies[n].army=army0;
 armies[n].units=1827; armies[n].hp=5107; armies[n].init=18; armies[n].adam=24;
 armies[n].atype=0; armies[n].atype |= (1<<slashing);
 armies[n].weak=0;
 armies[n].imm=0;

 n = 5; armies[n].army=army0;
 armies[n].units=7109; armies[n].hp=2261; armies[n].init=16; armies[n].adam=3;
 armies[n].atype=0; armies[n].atype |= (1<<fire);
 armies[n].weak=0;
 armies[n].imm=0; armies[n].imm |= (1<<radiation); armies[n].imm |= (1<<slashing); armies[n].imm |= (1<<cold);

 n = 6; armies[n].army=army0;
 armies[n].units=4736; armies[n].hp=8421; armies[n].init=3; armies[n].adam=17;
 armies[n].atype=0; armies[n].atype |= (1<<slashing);
 armies[n].weak=0; armies[n].weak |= (1<<cold);
 armies[n].imm=0;

 n = 7; armies[n].army=army0;
 armies[n].units=491; armies[n].hp=3518; armies[n].init=1; armies[n].adam=65;
 armies[n].atype=0; armies[n].atype |= (1<<radiation);
 armies[n].weak=0; armies[n].weak |= (1<<cold);
 armies[n].imm=0; armies[n].imm |= (1<<bludgeoning); armies[n].imm |= (1<<fire);

 n = 8; armies[n].army=army0;
 armies[n].units=2309; armies[n].hp=7353; armies[n].init=20; armies[n].adam=31;
 armies[n].atype=0; armies[n].atype |= (1<<bludgeoning);
 armies[n].weak=0;
 armies[n].imm=0; armies[n].imm |= (1<<radiation);

 n = 9; armies[n].army=army0;
 armies[n].units=411; armies[n].hp=6375; armies[n].init=14; armies[n].adam=151;
 armies[n].atype=0; armies[n].atype |= (1<<bludgeoning);
 armies[n].weak=0; armies[n].weak |= (1<<cold); armies[n].weak |= (1<<fire);
 armies[n].imm=0; armies[n].imm |= (1<<slashing);

 n = 10; armies[n].army=army1;
 armies[n].units=148; armies[n].hp=31914; armies[n].init=4; armies[n].adam=416;
 armies[n].atype=0; armies[n].atype |= (1<<cold);
 armies[n].weak=0; armies[n].weak |= (1<<bludgeoning);
 armies[n].imm=0; armies[n].imm |= (1<<radiation); armies[n].imm |= (1<<cold); armies[n].imm |= (1<<fire);

 n = 11; armies[n].army=army1;
 armies[n].units=864; armies[n].hp=38189; armies[n].init=6; armies[n].adam=72;
 armies[n].atype=0; armies[n].atype |= (1<<slashing);
 armies[n].weak=0;
 armies[n].imm=0;

 n = 12; armies[n].army=army1;
 armies[n].units=2981; armies[n].hp=7774; armies[n].init=15; armies[n].adam=4;
 armies[n].atype=0; armies[n].atype |= (1<<fire);
 armies[n].weak=0;
 armies[n].imm=0; armies[n].imm |= (1<<cold); armies[n].imm |= (1<<bludgeoning);

 n = 13; armies[n].army=army1;
 armies[n].units=5259; armies[n].hp=22892; armies[n].init=5; armies[n].adam=8;
 armies[n].atype=0; armies[n].atype |= (1<<fire);
 armies[n].weak=0;
 armies[n].imm=0;

 n = 14; armies[n].army=army1;
 armies[n].units=318; armies[n].hp=16979; armies[n].init=9; armies[n].adam=106;
 armies[n].atype=0; armies[n].atype |= (1<<bludgeoning);
 armies[n].weak=0; armies[n].weak |= (1<<fire);
 armies[n].imm=0;

 n = 15; armies[n].army=army1;
 armies[n].units=5017; armies[n].hp=32175; armies[n].init=17; armies[n].adam=11;
 armies[n].atype=0; armies[n].atype |= (1<<bludgeoning);
 armies[n].weak=0; armies[n].weak |= (1<<slashing);
 armies[n].imm=0; armies[n].imm |= (1<<radiation);

 n = 16; armies[n].army=army1;
 armies[n].units=2202; armies[n].hp=4950; armies[n].init=10; armies[n].adam=18;
// armies[n].units=4308; armies[n].hp=14994; armies[n].init=10; armies[n].adam=5;
 armies[n].atype=0; armies[n].atype |= (1<<fire);
 armies[n].weak=0; armies[n].weak |= (1<<slashing);
 armies[n].imm=0; armies[n].imm |= (1<<fire); armies[n].imm |= (1<<cold);

 n = 17; armies[n].army=army1;
 armies[n].units=208; armies[n].hp=14322; armies[n].init=19; armies[n].adam=133;
 armies[n].atype=0; armies[n].atype |= (1<<cold);
 armies[n].weak=0; armies[n].weak |= (1<<radiation);
 armies[n].imm=0;

 n = 18; armies[n].army=army1;
 armies[n].units=3999; armies[n].hp=48994; armies[n].init=11; armies[n].adam=20;
 armies[n].atype=0; armies[n].atype |= (1<<cold);
 armies[n].weak=0; armies[n].weak |= (1<<slashing); armies[n].weak |= (1<<cold);
 armies[n].imm=0;

 n = 19; armies[n].army=army1;
 armies[n].units=1922; armies[n].hp=34406; armies[n].init=12; armies[n].adam=35;
 armies[n].atype=0; armies[n].atype |= (1<<slashing);
 armies[n].weak=0; armies[n].weak |= (1<<slashing);
 armies[n].imm=0;

 return;
}
