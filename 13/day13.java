public class day13 {
 public static void main(String[] args) {

  int tick = 0;

  System.out.format("\n ticking along... tick = %3d\n\n", tick);

  cart c = new cart(2, 3, '>');
//  cart c = new cart(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2].charAt(0));

  c.junk();
 }
}

class cart {
 private static int x0, y0, dir0;
 cart(int arg1, int arg2, char arg3) {
  x0 = arg1; y0 = arg2;
  switch(arg3) {
   case '^': dir0 = 1;
   case '>': dir0 = 2;
   case 'v': dir0 = 3;
   case '<': dir0 = 4;
  }
 }

 static void junk() {
  System.out.format("\n x0^2 + y0^2 = %3d; direction = %1d\n\n",
      x0*x0+y0*y0, dir0);
  return;
 }
}
