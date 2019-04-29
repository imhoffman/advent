import java.io.*;
import java.lang.String;
import java.lang.Character;

public class two {
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
   p.puzzle( 3 );

 }
}


class puzzler {
 final static String alpha = new String("abcdefghijklmnopqrstuvwxyz");
 private static String[] r;
 private static int N;
 puzzler( String[] rr ) {
   r = rr;
   N = r.length;
 }

 // recursive char counter
 static int counter ( int n, char ch, String str ) {
   int m = -1;

   if ( str.length() == 0 || str.isEmpty() ) { return 0; }

   if ( ( m = str.indexOf(ch) ) != -1 ) {
     return 1 + counter( n+1, ch, str.substring(m+1) );
   }

   return 0;
 }

 // checksum ruleset
 static boolean sumcheck ( String s ) {
   String checksum = new String( s.substring( s.indexOf('[')+1, s.indexOf(']') ) );
   String encrypted = new String( s.substring( 0, s.lastIndexOf('-') ) );
   int i, nthis, nnext, nlast;
   boolean stop=false, isReal=false;

   nlast = counter(0, checksum.charAt(checksum.length()-1), encrypted);
   for( i=0; i<checksum.length()-1 && !stop; i++ ) {
     nthis = counter(0,  checksum.charAt(i)  , encrypted);
     nnext = counter(0, checksum.charAt(i+1) , encrypted);
     if (  nlast > 0
        && (  nthis > nnext
           || ( nthis == nnext
               &&  alpha.indexOf(checksum.charAt(i)) < alpha.indexOf(checksum.charAt(i+1)) ) ) ) { isReal = true; } else { isReal=false; stop=true; }
   }

   return isReal;
 }

 // parse sector id from registry listing
 static long sector ( String s ) {
   String nums = new String("");
   int i;
   for( i=0; i<s.length(); i++ ) {
     if ( Character.isDigit( s.charAt(i) ) ) { nums = nums.concat( s.substring(i,i+1) ); }
   }
   return Long.parseLong( nums );
 }

 // cypher rules
 static char caesar ( int orig, int rotate ) {
   int newchi;
   if ( orig + rotate > alpha.length() - 1 ) {
     newchi = orig + rotate - alpha.length();
   } else {
     newchi = orig + rotate;
   }
   return alpha.charAt(newchi);
 }


 static void decrypter ( String s ) {
   return;
 }
     

 static void puzzle ( int part ) {
   int i;
   long total=0L;
   for ( i=0; i<N; i++ ) {
     if ( sumcheck( r[i] ) ) { total = total + sector( r[i] ); }
   }
   if ( (part & 2)==2 ) { System.out.println(" part two answer pending..."); }
   if ( (part & 1)==1 ) { System.out.printf(" Total of real sector ids = %d\n", total); }
   return;
 }

}
