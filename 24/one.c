#include<stdio.h>
#include<stdlib.h>
#include<stdint.h>
#include<stdbool.h>

#define radiation   0
#define bludgeoning 1
#define fire        2
#define slashing    3
#define cold        4

typedef struct {
 int units, adam, atype, init, hp, weak, imm;
} group;

int main (void) {
 FILE* f;
 int i, j, k, n;

 group immune[10];
 group infect[10];

 n = 0;
 immune[n].units=479; immune[n].hp=3393; immune[n].init=8; immune[n].adam=66;
 immune[n].atype=0; immune[n].atype |= (1<<cold);
 immune[n].weak=0; immune[n].weak |= (1<<radiation);
 immune[n].imm=0;

 n = 1;
 immune[n].units=2202; immune[n].hp=4950; immune[n].init=2; immune[n].adam=18;
 immune[n].atype=0; immune[n].atype |= (1<<cold);
 immune[n].weak=0; immune[n].weak |= (1<<fire);
 immune[n].imm=0; immune[n].imm |= (1<<slashing);

 n = 2;
 immune[n].units=8132; immune[n].hp=9680; immune[n].init=7; immune[n].adam=9;
 immune[n].atype=0; immune[n].atype |= (1<<radiation);
 immune[n].weak=0; immune[n].weak |= (1<<bludgeoning); immune[n].weak |= (1<<fire);
 immune[n].imm=0; immune[n].imm |= (1<<slashing);

 n = 3;
 immune[n].units=389; immune[n].hp=13983; immune[n].init=13; immune[n].adam=256;
 immune[n].atype=0; immune[n].atype |= (1<<cold);
 immune[n].weak=0;
 immune[n].imm=0; immune[n].imm |= (1<<bludgeoning);

 n = 4;
 immune[n].units=1827; immune[n].hp=5107; immune[n].init=18; immune[n].adam=24;
 immune[n].atype=0; immune[n].atype |= (1<<slashing);
 immune[n].weak=0;
 immune[n].imm=0;

 n = 5;
 immune[n].units=7109; immune[n].hp=2261; immune[n].init=16; immune[n].adam=3;
 immune[n].atype=0; immune[n].atype |= (1<<fire);
 immune[n].weak=0;
 immune[n].imm=0; immune[n].imm |= (1<<radiation); immune[n].imm |= (1<<slashing); immune[n].imm |= (1<<cold);

 n = 6;
 immune[n].units=4736; immune[n].hp=8421; immune[n].init=3; immune[n].adam=17;
 immune[n].atype=0; immune[n].atype |= (1<<slashing);
 immune[n].weak=0; immune[n].weak |= (1<<cold);
 immune[n].imm=0;

 n = 7;
 immune[n].units=491; immune[n].hp=3518; immune[n].init=1; immune[n].adam=65;
 immune[n].atype=0; immune[n].atype |= (1<<radiation);
 immune[n].weak=0; immune[n].weak |= (1<<cold);
 immune[n].imm=0; immune[n].imm |= (1<<bludgeoning); immune[n].imm |= (1<<fire);

 n = 8;
 immune[n].units=2309; immune[n].hp=7353; immune[n].init=20; immune[n].adam=31;
 immune[n].atype=0; immune[n].atype |= (1<<bludgeoning);
 immune[n].weak=0;
 immune[n].imm=0; immune[n].imm |= (1<<radiation);

 n = 9;
 immune[n].units=411; immune[n].hp=6375; immune[n].init=14; immune[n].adam=151;
 immune[n].atype=0; immune[n].atype |= (1<<bludgeoning);
 immune[n].weak=0; immune[n].weak |= (1<<cold); immune[n].weak |= (1<<fire);
 immune[n].imm=0; immune[n].imm |= (1<<slashing);

 n = 2;
 printf("\n\n immune group %1d has a weakness of 0x%04x\n\n",n,immune[n].weak);
 n = 9;
 printf("\n\n immune group %1d has %d hp for %d units\n\n",n,immune[n].hp,immune[n].units);

 n = 0;
 infect[n].units=148; infect[n].hp=31914; infect[n].init=4; infect[n].adam=416;
 infect[n].atype=0; infect[n].atype |= (1<<cold);
 infect[n].weak=0; infect[n].weak |= (1<<bludgeoning);
 infect[n].imm=0; infect[n].imm |= (1<<radiation); infect[n].imm |= (1<<cold); infect[n].imm |= (1<<fire);

 n = 1;
 infect[n].units=864; infect[n].hp=38189; infect[n].init=6; infect[n].adam=72;
 infect[n].atype=0; infect[n].atype |= (1<<slashing);
 infect[n].weak=0;
 infect[n].imm=0;

 n = 2;
 infect[n].units=2981; infect[n].hp=7774; infect[n].init=15; infect[n].adam=4;
 infect[n].atype=0; infect[n].atype |= (1<<fire);
 infect[n].weak=0;
 infect[n].imm=0; infect[n].imm |= (1<<cold); infect[n].imm |= (1<<bludgeoning);

 n = 3;
 infect[n].units=5259; infect[n].hp=22892; infect[n].init=5; infect[n].adam=8;
 infect[n].atype=0; infect[n].atype |= (1<<fire);
 infect[n].weak=0;
 infect[n].imm=0;

 n = 4;
 infect[n].units=318; infect[n].hp=16979; infect[n].init=9; infect[n].adam=106;
 infect[n].atype=0; infect[n].atype |= (1<<bludgeoning);
 infect[n].weak=0; infect[n].weak |= (1<<fire);
 infect[n].imm=0;

 n = 5;
 infect[n].units=5017; infect[n].hp=32175; infect[n].init=17; infect[n].adam=11;
 infect[n].atype=0; infect[n].atype |= (1<<bludgeoning);
 infect[n].weak=0; infect[n].weak |= (1<<slashing);
 infect[n].imm=0; infect[n].imm |= (1<<radiation);

 n = 6;
 infect[n].units=4308; infect[n].hp=14994; infect[n].init=10; infect[n].adam=5;
 infect[n].atype=0; infect[n].atype |= (1<<fire);
 infect[n].weak=0; infect[n].weak |= (1<<slashing);
 infect[n].imm=0; infect[n].imm |= (1<<fire); infect[n].imm |= (1<<cold);

 n = 7;
 infect[n].units=208; infect[n].hp=14322; infect[n].init=19; infect[n].adam=133;
 infect[n].atype=0; infect[n].atype |= (1<<cold);
 infect[n].weak=0; infect[n].weak |= (1<<radiation);
 infect[n].imm=0;

 n = 8;
 infect[n].units=3999; infect[n].hp=48994; infect[n].init=11; infect[n].adam=20;
 infect[n].atype=0; infect[n].atype |= (1<<cold);
 infect[n].weak=0; infect[n].weak |= (1<<slashing); infect[n].weak |= (1<<cold);
 infect[n].imm=0;

 n = 9;
 infect[n].units=1922; infect[n].hp=34406; infect[n].init=12; infect[n].adam=35;
 infect[n].atype=0; infect[n].atype |= (1<<slashing);
 infect[n].weak=0; infect[n].weak |= (1<<slashing);
 infect[n].imm=0;

 n = 4;
 printf("\n\n infect group %1d has a weakness of 0x%04x\n\n",n,infect[n].weak);
 n = 5;
 printf("\n\n infect group %1d has %d hp for %d units\n\n",n,infect[n].hp,infect[n].units);

 return 0;
}
