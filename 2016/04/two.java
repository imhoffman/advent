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
   //temp = null;       // free temp ?
   //System.gc();

   puzzler p = new puzzler( listing ) ;
   p.puzzle( 3 );       // bit vector
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
 static int counter ( char ch, String str ) {
   int m = -1;
   if ( str.length() == 0 || str.isEmpty() ) { return 0; }
   if ( ( m = str.indexOf(ch) ) != -1 ) {
     return 1 + counter( ch, str.substring(m+1) );
   }
   return 0;
 }

 // checksum ruleset
 static boolean sumcheck ( String s ) {
   String checksum = new String( s.substring( s.indexOf('[')+1, s.indexOf(']') ) );
   String encrypted = new String( s.substring( 0, s.lastIndexOf('-') ) );
   int i, nthis, nnext, nlast;
   boolean stop=false, isReal=false;

   nlast = counter( checksum.charAt(checksum.length()-1), encrypted);
   for( i=0; i<checksum.length()-1 && !stop; i++ ) {
     nthis = counter( checksum.charAt(i)  , encrypted);
     nnext = counter(checksum.charAt(i+1) , encrypted);
     if (  nlast > 0
        && (  nthis > nnext
           || ( nthis == nnext
               &&  alpha.indexOf(checksum.charAt(i)) < alpha.indexOf(checksum.charAt(i+1)) )
           )
        ) { isReal = true; } else { isReal=false; stop=true; }
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

 // apply cypher to string
 static String decrypter ( String s ) {
   String encrypted = new String( s.substring( 0, s.lastIndexOf('-') ) );
   char[] decrypted = new char[encrypted.length()];
   int i;
   for( i=0; i<encrypted.length(); i++ ) {
     if ( encrypted.charAt(i) == '-' ) {
       decrypted[i] = ' ';
     } else {
      decrypted[i] = caesar( alpha.indexOf(encrypted.charAt(i)), (int)sector(s)%alpha.length() );
     }
   }
   return String.valueOf(decrypted);
 }
     
 // report desired results
 static void puzzle ( int part ) {
   int i;
   long id, total=0L;
   for ( i=0; i<N; i++ ) {
     if ( sumcheck( r[i] ) ) {
       id = sector( r[i] );
       total = total + id;
       if ( (part & 2)==2 ) {
         System.out.printf( "%5d %s\n", id, decrypter( r[i] ) );
       }
     }
   }
   if ( (part & 1)==1 ) { System.out.printf(" Total of real sector ids = %d\n", total); }
   return;
 }

}
