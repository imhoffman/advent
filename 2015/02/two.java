import java.io.*;
import java.lang.String;
import java.lang.Character;

public class two {

  final static int max_lines = 16384;

  public static void main(String[] args) throws IOException {
    String[] temp = new String[max_lines];
    int i=0, numberOfPuzzleInputs, totalArea, totalLength;

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
    for ( i=0; i < numberOfPuzzleInputs ; i++ )
      puzzleInput[i] = temp[i];

    // compute values as per ruleset
    totalArea   = 0;
    totalLength = 0;
    for ( i=0; i < numberOfPuzzleInputs ; i++ ) {
      Present p = new Present( Parser.xRemover( puzzleInput[i] ) );
      totalArea   += p.surfaceArea() + p.areaOfSmallestSide();
      totalLength += p.volume()      + p.lengthOfSmallestPerimeter();
    }

    System.out.format( "\n answer to part one: %d\n\n", totalArea );
    System.out.format( "\n answer to part two: %d\n\n", totalLength );

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

  public int lengthOfSmallestPerimeter ( ) {
    int min = 2 * this.length  +  2 * this.width;
    if ( 2*this.length + 2*this.height < min ) min = 2*this.length + 2*this.height;
    if ( 2*this.width  + 2*this.height < min ) min = 2*this.width  + 2*this.height;
    return min;
  }

  public int volume ( ) {
    return this.length * this.width * this.height;
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


