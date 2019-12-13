import java.io.*;
import java.lang.String;
import java.lang.Character;
//import java.util.ArrayList;
//import java.util.List;

public class one {
 final static int max_lines = 16384;
 public static void main ( String[] args ) throws IOException {
   String[] temp = new String[max_lines];
   int number_of_input_lines, A, B, C, answer;

   number_of_input_lines = 0;
   try(BufferedReader br = new BufferedReader(new FileReader("puzzle.txt"))) {
     for(String line; (line = br.readLine()) != null; ) {
       temp[ number_of_input_lines ] = line;
       number_of_input_lines++;
     }
   }

   //  commented lines are for ArrayList implementation

   //ArrayList<Triple> puzzle_input = new ArrayList<Triple>();
   Triple[] puzzle_input = new Triple[ number_of_input_lines ];
   String[] splits = new String[3];
   for ( int j=0; j < number_of_input_lines; j++ ) {
     splits = temp[j].trim().split("\\s+");
     A = Integer.parseInt( splits[0] );
     B = Integer.parseInt( splits[1] );
     C = Integer.parseInt( splits[2] );
     //puzzle_input.add( new Triple( A, B, C ) );
     puzzle_input[j] = new Triple( A, B, C );
   }

   answer = 0;
   for ( int k=0; k < number_of_input_lines; k++ ) {
     //if ( puzzle_input.get(k).is_triangle() ) { answer++; }
     if ( puzzle_input[k].is_triangle() ) { answer++; }
   }
   System.out.printf( "\n The number of possible triangles: %d\n\n", answer );

 }
}

class Triple {
  private int A, B, C;
  public Triple( int A_, int B_, int C_ ) {
    this.A = A_;  this.B = B_;  this.C = C_;
  }

  boolean is_triangle( ) {
    //System.out.printf( " considering: %d %d %d\n", this.A, this.B, this.C );
    if ( this.A + this.B > this.C  &&
       	this.A + this.C > this.B  &&
      	this.B + this.C > this.A ) {
      return true;
    } else {
      return false;
    }
  }

  void contents ( ) {
    System.out.printf( " this triple contains: %d %d %d\n", this.A, this.B, this.C );
    return;
  }
}
