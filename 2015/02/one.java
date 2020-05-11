import java.io.*;
import java.lang.String;
import java.lang.Character;

public class one {
 final static int max_lines = 16384;
 public static void main(String[] args) throws IOException {
   String[] temp = new String[max_lines];
   int i=0, numberOfPuzzleInputs, totalArea;

   // https://stackoverflow.com/questions/5868369/how-to-read-a-large-text-file-line-by-line-using-java
   try(BufferedReader br = new BufferedReader(new FileReader("puzzle.txt"))) {
     for(String line; (line = br.readLine()) != null; ) {
       temp[i] = line;
       i++;
     }
   }
   numberOfPuzzleInputs = i;

   // copy reader array into appropriately-sized new array
   String[] puzzleInput = new String[ numberOfPuzzleInputs ];
   for ( i=0; i < numberOfPuzzleInputs ; i++ ) {
     puzzleInput[i] = temp[i];
   }

   // sum areas as per ruleset
   totalArea = 0;
   for ( i=0; i < numberOfPuzzleInputs ; i++ ) {
     Present p = new Present( Parser.xRemover( puzzleInput[i] ) );
     totalArea += p.surfaceArea() + p.areaOfSmallestSide();
   }

   System.out.format( "\n answer to part one: %d\n\n", totalArea );

   return;
 }
}


class Present {
  int length, width, height;
  static int number_of_presents;

  // take array as input since Parser returns an array
  Present ( int[] dims ) {
    this.length = dims[0];
    this.width  = dims[1];
    this.height = dims[2];
    number_of_presents += 1;
  }

  public int surfaceArea ( ) {
    return 2 * this.length * this.height +
           2 * this.length * this.width  +
           2 * this.height * this.width;
  }

  public int areaOfSmallestSide ( ) {
    int min = this.length * this.width;
    if ( this.length * this.height < min ) min = this.length * this.height;
    if (  this.width * this.height < min ) min =  this.width * this.height;
    return min;
  }
}



class Parser {

  public static int[] xRemover ( String inputLine ) {
    int[] out = new int[3];
    String[] splitInputLine = inputLine.split("x");
    int j = 0;

    while ( j < 3 ) {
      out[j] = Integer.parseInt( splitInputLine[j] );
      j++;
    }

    return out;
  }
}


