
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.InputMismatchException;


public class InputReader {
    private boolean finished = false;

    private InputStream stream;
    private byte[] buf = new byte[1024];
    private int curChar;
    private int numChars;
    private SpaceCharFilter filter;

    // Sets the input stream to read from. This is used to determine if we are dealing with a file
    public InputReader(InputStream stream) {
        this.stream = stream;
    }

    /**
    * Reads an array of doubles. The array is written using writeDoubleArray ( double [] int ).
    * 
    * @param size - the number of doubles to read from the stream
    */
    public double[] readDoubleArray(int size) {
        double[] array = new double[size];
        // Reads a double value from the stream.
        for (int i = 0; i < size; i++) {
            array[i] = readDouble();
        }
        return array;
    }

    /**
    * Reads an array of strings from the parcel. The array is terminated by null. This method blocks until all data has been read the end of the parcel is detected or an exception is thrown
    * 
    * @param size
    */
    public String[] readStringArray(int size) {
        String[] array = new String[size];
        // Reads a string from the stream.
        for (int i = 0; i < size; i++) {
            array[i] = readString();
        }
        return array;
    }

    /**
    * Reads and returns a character array from the input. This method blocks until input data is available the end of the stream is detected or an exception is thrown.
    * 
    * @param size - the number of characters to read from the input
    */
    public char[] readCharArray(int size) {
        char[] array = new char[size];
        // Reads a character from the stream.
        for (int i = 0; i < size; i++) {
            array[i] = readCharacter();
        }
        return array;
    }

    /**
    * Reads the bytes for each of the arrays in the given order from the serial port and stores them in the given array
    */
    public void readIntArrays(int[]... arrays) {
        // Reads all the values from the array.
        for (int i = 0; i < arrays[0].length; i++) {
            // Reads the next array of integers from the array.
            for (int j = 0; j < arrays.length; j++) {
                arrays[j][i] = readInt();
            }
        }
    }

    /**
    * Reads long values from the stream and puts them into the given long arrays. The method blocks until all values have been read the end of the stream is detected or an exception is thrown
    */
    public void readLongArrays(long[]... arrays) {
        // Reads all the values from the array.
        for (int i = 0; i < arrays[0].length; i++) {
            // Reads a single value from the array.
            for (int j = 0; j < arrays.length; j++) {
                arrays[j][i] = readLong();
            }
        }
    }

    /**
    * Reads an array of doubles from the DataInput. The data is read starting at the current data pointer
    */
    public void readDoubleArrays(double[]... arrays) {
        // Reads a double value from the array.
        for (int i = 0; i < arrays[0].length; i++) {
            // Reads a double value from the stream.
            for (int j = 0; j < arrays.length; j++) {
                arrays[j][i] = readDouble();
            }
        }
    }

    /**
    * Reads a character table from the parcel. The table is read from the beginning of the parcel.
    * 
    * @param rowCount - The number of rows in the table.
    * @param columnCount - The number of columns in the table ( must be > 0 )
    */
    public char[][] readCharTable(int rowCount, int columnCount) {
        char[][] table = new char[rowCount][];
        // Read a table of characters from the stream.
        for (int i = 0; i < rowCount; i++) {
            table[i] = this.readCharArray(columnCount);
        }
        return table;
    }

    /**
    * Reads a 2D int array from the parcel. The first row is the header and the second row is the data
    * 
    * @param rowCount - The number of rows in the table
    * @param columnCount - The number of columns in the data ( must be > 0
    */
    public int[][] readIntTable(int rowCount, int columnCount) {
        int[][] table = new int[rowCount][];
        // Read the table from the table.
        for (int i = 0; i < rowCount; i++) {
            table[i] = readIntArray(columnCount);
        }
        return table;
    }

    /**
    * Reads a double table from the parcel at the current dataPosition () and advances the dataPosition ().
    * 
    * @param rowCount - The number of rows in the table.
    * @param columnCount - The number of columns in the table ( must be > 0
    */
    public double[][] readDoubleTable(int rowCount, int columnCount) {
        double[][] table = new double[rowCount][];
        // Read a double array of doubles from the stream.
        for (int i = 0; i < rowCount; i++) {
            table[i] = this.readDoubleArray(columnCount);
        }
        return table;
    }

    /**
    * Read a table of longs. The first row is the header and the second row is the data
    * 
    * @param rowCount - Number of rows in the table
    * @param columnCount - Number of columns in the data ( must be > 0
    */
    public long[][] readLongTable(int rowCount, int columnCount) {
        long[][] table = new long[rowCount][];
        // Read the next row of the table.
        for (int i = 0; i < rowCount; i++) {
            table[i] = readLongArray(columnCount);
        }
        return table;
    }

    /**
    * Reads a string table from the parcel at the current dataPosition () and returns it as an array of String.
    * 
    * @param rowCount - The number of rows in the table.
    * @param columnCount - The number of columns in the table ( zero based
    */
    public String[][] readStringTable(int rowCount, int columnCount) {
        String[][] table = new String[rowCount][];
        // Read a string from the stream and store it in the table.
        for (int i = 0; i < rowCount; i++) {
            table[i] = this.readStringArray(columnCount);
        }
        return table;
    }

    /**
    * Reads text from the stream. This method is useful for reading a file that contains plain text. The text is read until a newline is encountered or the end of the stream is reached whichever comes first.
    * 
    * 
    * @return the text read from the stream or null if the end of the stream has been reached before the text could be read
    */
    public String readText() {
        StringBuilder result = new StringBuilder();
        // Reads a single character from the stream.
        while (true) {
            int character = read();
            // Skips the next character.
            if (character == '\r') {
                continue;
            }
            // If the character is 1 break the loop.
            if (character == -1) {
                break;
            }
            result.append((char) character);
        }
        return result.toString();
    }

    /**
    * Reads strings from the stream and puts them into the given array of String arrays. This is equivalent to calling readString for each of the given arrays
    */
    public void readStringArrays(String[]... arrays) {
        // Reads all the data from the array.
        for (int i = 0; i < arrays[0].length; i++) {
            // Reads a string from the stream.
            for (int j = 0; j < arrays.length; j++) {
                arrays[j][i] = readString();
            }
        }
    }

    /**
    * Reads an array of longs. This does not change the position of the read data. Use #readLong ( int ) for that.
    * 
    * @param size - the number of longs to read ( must be >
    */
    public long[] readLongArray(int size) {
        long[] array = new long[size];
        // Reads a long value from the array.
        for (int i = 0; i < size; i++) {
            array[i] = readLong();
        }
        return array;
    }

    /**
    * Reads an array of int from the parcel at the current dataPosition () and advances the dataPosition () by the number of elements read.
    * 
    * @param size - the number of elements to read from the parcel
    */
    public int[] readIntArray(int size) {
        int[] array = new int[size];
        // Reads the next value from the array.
        for (int i = 0; i < size; i++) {
            array[i] = readInt();
        }
        return array;
    }

    /**
    * Reads and returns the next character from the input stream. Returns - 1 if the end of the stream has been reached
    */
    public int read() {
        // Check if the number of characters in the input is 1.
        if (numChars == -1) {
            throw new InputMismatchException();
        }
        // Reads the next character from the input.
        if (curChar >= numChars) {
            curChar = 0;
            try {
                numChars = stream.read(buf);
            } catch (IOException e) {
                throw new InputMismatchException();
            }
            // Returns the number of characters in the input string.
            if (numChars <= 0) {
                return -1;
            }
        }
        return buf[curChar++];
    }

    /**
    * Returns the next character without advancing the reader. Returns - 1 if there are no more characters
    */
    public int peek() {
        // Returns the number of characters in the input string.
        if (numChars == -1) {
            return -1;
        }
        // Read the next character from the stream.
        if (curChar >= numChars) {
            curChar = 0;
            try {
                numChars = stream.read(buf);
            } catch (IOException e) {
                return -1;
            }
            // Returns the number of characters in the input string.
            if (numChars <= 0) {
                return -1;
            }
        }
        return buf[curChar];
    }

    /**
    * Returns the next non - whitespace character without advancing the reader. This method does not consume the character
    */
    public int peekNonWhitespace() {
        // Reads the next token from the input stream.
        while (isWhitespace(peek())) {
            read();
        }
        return peek();
    }

    /**
    * Reads an integer from the input stream. This method skips white space as defined by Character#isSpaceChar ( char )
    */
    public int readInt() {
        int c = read();
        // Reads a space character and removes the next character.
        while (isSpaceChar(c)) {
            c = read();
        }
        int sgn = 1;
        // Read the next character from the input stream.
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        int res = 0;
        do {
            // Checks if the character is a valid character.
            if (c < '0' || c > '9') {
                throw new InputMismatchException();
            }
            res *= 10;
            res += c - '0';
            c = read();
        } while (!isSpaceChar(c));
        return res * sgn;
    }

    /**
    * Reads a long from this file. This method skips white space as defined by Character#isSpaceChar ( char )
    */
    public long readLong() {
        int c = read();
        // Reads a space character and removes the next character.
        while (isSpaceChar(c)) {
            c = read();
        }
        int sgn = 1;
        // Read the next character from the input stream.
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        long res = 0;
        do {
            // Checks if the character is a valid character.
            if (c < '0' || c > '9') {
                throw new InputMismatchException();
            }
            res *= 10;
            res += c - '0';
            c = read();
        } while (!isSpaceChar(c));
        return res * sgn;
    }

    /**
    * Reads a string from the input. This method skips white space and returns the string as a String.
    * 
    * 
    * @return the string that was read from the input or null if the end of the input has been reached or there was an error
    */
    public String readString() {
        int c = read();
        // Reads a space character and removes the next character.
        while (isSpaceChar(c)) {
            c = read();
        }
        StringBuilder res = new StringBuilder();
        do {
            // Append the character to the res.
            if (Character.isValidCodePoint(c)) {
                res.appendCodePoint(c);
            }
            c = read();
        } while (!isSpaceChar(c));
        return res.toString();
    }

    /**
    * Returns true if the character is a space character. This method delegates to the filter if set or returns the result of isWhitespace ( c ) if none is set.
    * 
    * @param c - the character to check for space in the range
    */
    public boolean isSpaceChar(int c) {
        // Returns true if the character is a space character.
        if (filter != null) {
            return filter.isSpaceChar(c);
        }
        return isWhitespace(c);
    }

    /**
    * Returns true if the character is whitespace. This method differs from Character#isWhitespace ( int ) in that it doesn't throw an exception if the character is not a whitespace character.
    * 
    * @param c - the character to check for whitespace as an int
    */
    public static boolean isWhitespace(int c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
    }

    /**
    * Reads a line of text. This is a version of readLine that doesn't require a String to be passed as an argument.
    * 
    * 
    * @return the string that was read or null if end of file was reached before a line of text could be
    */
    private String readLine0() {
        StringBuilder buf = new StringBuilder();
        int c = read();
        // Read a single line from the stream.
        while (c != '\n' && c != -1) {
            // Append a character to the buffer.
            if (c != '\r') {
                buf.appendCodePoint(c);
            }
            c = read();
        }
        return buf.toString();
    }

    /**
    * Read a line from the input stream. This method blocks until a line is read or an exception is thrown.
    * 
    * 
    * @return the line read from the input stream or null if the end of the stream has been reached before the first line
    */
    public String readLine() {
        String s = readLine0();
        // Read a line of data from the stream.
        while (s.trim().length() == 0) {
            s = readLine0();
        }
        return s;
    }

    /**
    * Reads a line of text from the input stream. This method is useful for reading text from the input stream that has been written with write ( String ).
    * 
    * @param ignoreEmptyLines - Whether empty lines should be ignored.
    * 
    * @return The line read or null if the end of the stream has been reached before the data can be read
    */
    public String readLine(boolean ignoreEmptyLines) {
        // Returns the next line from the input stream.
        if (ignoreEmptyLines) {
            return readLine();
        } else {
            return readLine0();
        }
    }

    /**
    * Reads a BigInteger from the buffer. This method blocks until the value can be read the end of the buffer is detected or an exception is thrown.
    * 
    * 
    * @return the next value in the buffer as a BigInteger or null if the buffer is empty or the value cannot be
    */
    public BigInteger readBigInteger() {
        try {
            return new BigInteger(readString());
        } catch (NumberFormatException e) {
            throw new InputMismatchException();
        }
    }

    /**
    * Reads a character from the input. This method skips white space as defined by Character#isSpaceChar ( char )
    */
    public char readCharacter() {
        int c = read();
        // Reads a space character and removes the next character.
        while (isSpaceChar(c)) {
            c = read();
        }
        return (char) c;
    }

    /**
    * Reads a double from the stream. This method assumes that the current position is a number and advances the position by one
    */
    public double readDouble() {
        int c = read();
        // Reads a space character and removes the next character.
        while (isSpaceChar(c)) {
            c = read();
        }
        int sgn = 1;
        // Read the next character from the input stream.
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        double res = 0;
        // Read a number of digits from the input.
        while (!isSpaceChar(c) && c != '.') {
            // e E or E.
            if (c == 'e' || c == 'E') {
                return res * Math.pow(10, readInt());
            }
            // Checks if the character is a valid character.
            if (c < '0' || c > '9') {
                throw new InputMismatchException();
            }
            res *= 10;
            res += c - '0';
            c = read();
        }
        // Read a number of digits.
        if (c == '.') {
            c = read();
            double m = 1;
            // Read a character from the input.
            while (!isSpaceChar(c)) {
                // e E or E.
                if (c == 'e' || c == 'E') {
                    return res * Math.pow(10, readInt());
                }
                // Checks if the character is a valid character.
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                m /= 10;
                res += (c - '0') * m;
                c = read();
            }
        }
        return res * sgn;
    }

    /**
    * Returns true if there are no more characters to read. This is useful for determining whether a token is exhausted
    */
    public boolean isExhausted() {
        int value;
        // Reads a space character.
        while (isSpaceChar(value = peek()) && value != -1) {
            read();
        }
        return value == -1;
    }

    /**
    * Reads the next token as a string. This method does not advance the cursor. If the end of the stream is reached before a token can be read a NullPointerException is thrown.
    * 
    * 
    * @return the next token as a string or null if there are no more tokens to read or the end of the stream
    */
    public String next() {
        return readString();
    }

    /**
    * Returns the SpaceCharFilter used to filter characters. By default this is { @link com. codename1. ui. space. SpaceCharFilter#SPACE_CHAR_FILTER }.
    * 
    * 
    * @return the SpaceCharFilter used to filter characters ; may be null if this is not a SpaceCharFilter
    */
    public SpaceCharFilter getFilter() {
        return filter;
    }

    /**
    * Sets the filter to be used. By default this is null. You can override this with a custom filter.
    * 
    * @param filter - the filter to be used. By default this is
    */
    public void setFilter(SpaceCharFilter filter) {
        this.filter = filter;
    }

    public interface SpaceCharFilter {
        /**
        * Returns true if the character is a space character. This is used to determine if a string should be treated as a string of space characters when parsing the XML document.
        * 
        * @param ch - The character to check for space characters in the
        */
        public boolean isSpaceChar(int ch);
    }
}
