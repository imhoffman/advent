import java.io.*;

public class two {
 final static String alpha = new String("abcdefghijklmnopqrstuvwxyz");
 final static int max_lines = 16384;
 public static void main(String[] args) throws IOException {
   String[] temp = new String[max_lines];
   int i=0;

   // https://stackoverflow.com/questions/5868369/how-to-read-a-large-text-file-line-by-line-using-java
   try(BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
     for(String line; (line = br.readLine()) != null; ) {
       temp[i] = line;
       i++;
     }
   }

   String[] listing = new String[i];
   for ( i=0; i<listing.length; i++ ) {
     listing[i] = temp[i];
   }
   //temp = null;
   //System.gc();

   puzzler p = new puzzler( listing ) ;
   System.out.println( p.Nrooms() );

 }
}


class puzzler {
 private static String[] r;
 private static int N;
 puzzler( String[] rr ) {
   r = rr;
   N = r.length;
 }

 static int Nrooms () {
   return N;
 }
}
