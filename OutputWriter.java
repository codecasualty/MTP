

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;

public class OutputWriter {
    private final PrintWriter writer;

    public OutputWriter(OutputStream outputStream) {
        writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
    }

    public OutputWriter(Writer writer) {
        this.writer = new PrintWriter(writer);
    }

    /**
    * Writes the end of line. This is a no - op for text files as it does not have a newline
    */
    public void endl(){
        writer.write("--------------------------------");
    }
    

    /**
    * Prints a table to the console. This method is for debugging purposes only. If you want to be notified of the progress of the print use #printLine ( String )
    * 
    * @param table - char [] to be
    */
    public void printTable(char[][] table) {
        for (char[] row : table) {
            printLine(new String(row));
        }
    }

    /**
    * Prints a table of integers. The table is assumed to be an array of integers where each integer is represented by a pair of integers in the form [ i ] [ j ]
    * 
    * @param table - the table to be
    */
    public void printTable(int[][] table) {
        // Prints the table in the table.
        for(int i = 0;i < table.length;i++){
            // Prints the table to the console.
            for(int j = 0; j< table.length;j++){
                writer.print(table[i][j]);
                writer.print(' ');
            }
            writer.println();
        }
    }

    /**
    * Prints a table of longs. The table is assumed to be of the form { x1 x2... xn } where x1 and x2 are the indices of the rows and columns in the table.
    * 
    * @param table - the table to be printed. The values are assumed to be non - negative
    */
    public void printTable(long[][] table) {
        // Prints the table in the table.
        for(int i = 0;i < table.length;i++){
            // Prints the table to the console.
            for(int j = 0; j< table.length;j++){
                writer.print(table[i][j]);
                writer.print(' ');
            }
            writer.println();
        }
    }


    /**
    * Prints a 2D double array. The values are printed in column - major order. For example the table [ 0 ] [ 1 ] would be printed as follows
    * 
    * @param table - the 2D double array to
    */
    public void printTable(double[][] table) {
        // Prints the table in the table.
        for(int i = 0;i < table.length;i++){
            // Prints the table to the console.
            for(int j = 0; j< table.length;j++){
                writer.print(table[i][j]);
                writer.print(' ');
            }
            writer.println();
        }
    }

    /**
    * Print a character array. This method prints a line termination sequence after printing the character array to the underlying stream.
    * 
    * @param array - The character array to be printed. null values are not printed
    */
    public void print(char[] array) {
        writer.print(array);
    }

    /**
    * Prints an array of objects separated by spaces. Each object is printed using the #writer () method for its printing
    */
    public void print(Object... objects) {
        // Prints the objects in the list.
        for (int i = 0; i < objects.length; i++) {
            // Print the current value of the field.
            if (i != 0) {
                writer.print(' ');
            }
            writer.print(objects[i]);
        }
    }

    /**
    * Prints an array of integers to the file with a comma between each element. The array is treated as a single integer and not converted to a string.
    * 
    * @param array - the array to be printed to the file ; may not be
    */
    public void print(int[] array) {
        // Prints the array of values.
        for (int i = 0; i < array.length; i++) {
            // Print the current value of the field.
            if (i != 0) {
                writer.print(' ');
            }
            writer.print(array[i]);
        }
    }

    /**
    * Prints an array of doubles to the file with a comma between each value. The array is treated as a double array and each value is printed independently.
    * 
    * @param array - the array to be printed to the file ; the array itself cannot be
    */
    public void print(double[] array) {
        // Prints the array of values.
        for (int i = 0; i < array.length; i++) {
            // Print the current value of the field.
            if (i != 0) {
                writer.print(' ');
            }
            writer.print(array[i]);
        }
    }

    /**
    * Prints an long array. The array is treated as a space - separated list of longs and each long is printed with a space between the elements.
    * 
    * @param array - the long array to be printed. null values are ignored
    */
    public void print(long[] array) {
        // Prints the array of values.
        for (int i = 0; i < array.length; i++) {
            // Print the current value of the field.
            if (i != 0) {
                writer.print(' ');
            }
            writer.print(array[i]);
        }
    }

    /**
    * This method prints an array of int to the file with a new line. This method behaves as though it invokes #print ( int ) and then #println ().
    * 
    * @param array - The array to be printed to the file. This array is not modified
    */
    public void printLine(int[] array) {
        print(array);
        writer.println();
    }

    /**
    * This method prints a double array on the file followed by a line terminator. This method behaves as though it invokes #print ( double ) and then #println ().
    * 
    * @param array - The double array to be printed on the file
    */
    public void printLine(double[] array) {
        print(array);
        writer.println();
    }

    /**
    * This method prints a long array on the stream. The value printed is determined by the #print ( long ) method.
    * 
    * @param array - The long array to be printed on the stream
    */
    public void printLine(long[] array) {
        print(array);
        writer.println();
    }

    /**
    * Print a line of text to the output stream. This method behaves as though it invokes #writeLine
    */
    public void printLine() {
        writer.println();
    }

    /**
    * Print a list of objects and terminate the line with a new line character. This method behaves as though it invokes #print ( Object [] ) and then #println ()
    */
    public void printLine(Object... objects) {
        print(objects);
        writer.println();
    }

    /**
    * Print a character. This method behaves as though it invokes the writer's print method. The printing is done using the current encoding.
    * 
    * @param i - The character to be printed. If i is a null character the method #print ( char ) is called
    */
    public void print(char i) {
        writer.print(i);
    }

    /**
    * Prints a character to the file followed by a line terminator. This method behaves as though it invokes the writer's println method.
    * 
    * @param i - The character to be printed to the file followed by a line terminator
    */
    public void printLine(char i) {
        writer.println(i);
    }

    /**
    * Prints a character array on the file followed by a line terminator. This method behaves as though it invokes #write ( char [] ) and then #println ().
    * 
    * @param array - the array to be printed on the file ; characters are converted to
    */
    public void printLine(char[] array) {
        writer.println(array);
    }

    /**
    * Prints a formatted string to the writer. This method behaves as though it invokes #write ( Writer ).
    * 
    * @param format - The format string to print. The format string is interpreted as follows : Each element of the array is converted to a String using String#format ( String
    */
    public void printFormat(String format, Object... objects) {
        writer.printf(format, objects);
    }

    /**
    * Closes the writer. After this method is called no more data can be written to the writer. Note that you cannot call this method more than once
    */
    public void close() {
        writer.close();
    }

    /**
    * Flushes the output. This is a no - op if there is no output to be written to
    */
    public void flush() {
        writer.flush();
    }

    /**
    * This method prints a long. This is a convenience method that simply calls writer. print ( i ).
    * 
    * @param i - The long to print. This value is converted to a string using the String. valueOf () method
    */
    public void print(long i) {
        writer.print(i);
    }

    /**
    * This method prints a long to the file with a line terminator. This method behaves as though it invokes the writer's println method.
    * 
    * @param i - The long to be printed to the file with a line terminator
    */
    public void printLine(long i) {
        writer.println(i);
    }

    /**
    * This method prints an integer to the stream. The value printed is determined by the write method of the DataOutput class.
    * 
    * @param i - The integer to be printed to the stream. The value can be converted to a string using the String. valueOf
    */
    public void print(int i) {
        // System.out.println("isvalled");
        writer.print(i);
    }

    /**
    * Print an integer and then terminate the line with a new line secquence. This method behaves as though it invokes #writer. println ().
    * 
    * @param i - The integer to be printed. It is assumed that 0 < = i < Integer. MAX_VALUE
    */
    public void printLine(int i) {
        writer.println(i);
    }

    /**
    * Prints an array of integers on the same line. This method is equivalent to printLine ( array )
    * 
    * @param array - The array of integers to
    */
    public void separateLines(int[] array) {
        for (int i : array) {
            printLine(i);
        }
    }

    /**
    * Prints a list of objects. Each object is printed with a new line after it. This method is useful for dumping a list of objects to the console.
    * 
    * @param answer - the list to be printed. It must be a List
    */
    public void printList(List<?> answer) {
        for (Object o : answer) {
            printLine(o);
        }
    }

    
}