import java.io.*;

public class day13 {
 public static void main(String[] args) {

  int tick = 0;
  File filename = new File(args[0]);    // run as    $ java day13 input.txt
  char[][] map = null;

  try{ map = parser.map(filename); } catch(IOException e) { System.out.format("problem\n"); }

  System.out.format("\n ticking along... tick = %3d\n\n", tick);
  System.out.format("\n %c\n\n", map[1][3]);

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
   case '^': dir0 = 1; break;
   case '>': dir0 = 2; break;
   case 'v': dir0 = 3; break;
   case '<': dir0 = 4; break;
  }
 }

 static void junk() {
  System.out.format("\n x0^2 + y0^2 = %3d; direction = %1d\n\n",
      x0*x0+y0*y0, dir0);
  return;
 }
}

class parser {
 static char[][] map (File filename) throws IOException {
  FileReader input = new FileReader(filename);
  char[] a = new char[1];  // .read takes a char array
  char[][] temp = new char[2048][2048];
  int i=0, j=0, rows=1, cols=0;

  while ( input.read(a) != -1 ) {
   if ( a[0] == '\n' || a[0] == '\r' ) { j++; rows++; cols = i; i = 0; }
   else { temp[i][j] = a[0]; i++; }
//   System.out.format("not there yet; j = %d\n", j);
  }
  input.close();
  char[][] output = new char[cols][rows-1];
  for ( i=0; i<cols; i++ ) {
   for ( j=0; j<rows-1; j++ ) {
    output[i][j] = temp[i][j];
   }
  }
  System.out.format("returning array of dim %d,%d\n", cols, rows-1);
  
//  char[][] output = { "/-----\\".toCharArray(), "\\-----/".toCharArray() };
  return output;
 }
}
