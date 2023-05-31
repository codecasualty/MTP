import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.InputMismatchException;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;
/*
         While doing sorting in java dont use java 8 use java 17
            -time complexity for java 8 sorting is bad as compared to java 17
         Random stuff to try when stuck:
            -for combo/probability problems, expand the given form we're interested in
            -make everything the same then build an answer (constructive, make everything 0 then do something)
            -something appears in parts of 2 --> model as graph
            -assume a greedy then try to show why it works
            -find way to simplify into one variable if multiple exist (Number of pairs 1538C , same difference 1520D)
            -find lower and upper bounds on answer
            -figure out what ur trying to find and isolate it
            -see what observations you have and come up with more continuations
            -work backwards (in constructive, go from the goal to the start)
            -turn into prefix/suffix sum argument (often works if problem revolves around adjacent array elements)
            -instead of solving for answer, try solving for complement (ex, find n-(min) instead of max)
            -draw something
            -simulate a process
            -dont implement something unless if ur fairly confident its correct
            -after 3 bad submissions move on to next problem if applicable
            -do something instead of nothing and stay organized
            -write stuff down
         Random stuff to check when wa:
            -if code is way too long/cancer then reassess
            -int overflow
            -switched variables
            -wrong MOD
            -hardcoded edge case incorrectly
         Random stuff to check when tle:
            -continue instead of break
            -condition in for/while loop bad
         Random stuff to check when rte:
            -long to int/int overflow
            -division by 0
            -edge case for empty list/data structure/N=1
 */
 /*
  * 
3
9546
0000
3313

  */




public class garland{
    static InputStream inputStream = System.in;
    static OutputStream outputStream = System.out;
    static InputReader in = new InputReader(inputStream);
    static OutputWriter out = new OutputWriter(outputStream);

    public static void main(String[] agrs){
        
        int test = in.readInt();
        while(test-- > 0){
           
           char[] arr = in.readString().toCharArray();

           boolean allsame = true;
           for(int i = 0;i < 3;i++){
                if(arr[i] != arr[i+1])allsame = false;
           }

           if(allsame)out.printLine(-1);
           else{

                // boolean isOk = false;
                // int[] state = new int[4];
                // char curr = '*';
                // int steps = 0;
                // while(!isOk){
                //     boolean found = false;
                //     for(int i = 0;i < 4;i++){
                //         if(state[i] == 0 && arr[i] != curr){
                //             state[i] = 1;
                //             curr = arr[i];
                //             steps++;
                //             found = true;
                //             break;
                //         }
                //     }
                //     allsame = true;
                //     for(int i = 0;i < 4;i++){
                //         if(state[i] != 1)allsame = false;
                //     }                    

                //     if(allsame)break;

                //     if(!found){
                //         for(int i = 0;i < 4;i++){
                //             if(state[i] == 1 && arr[i] != curr){
                //                 state[i] = 0;
                //                 curr = arr[i] ;
                //                 steps++;
                //                 break;
                //             }
                //         }
                //     }

                // }

                int steps = 0;
                int [] freq = new int[10];
                int max = -1;
                for(int i = 0 ;i < 4;i++){
                    freq[arr[i] - '0']++;
                    max = Math.max(max , freq[arr[i] - '0']);
                }
                out.printLine(max == 1 || max == 2 ? 4 :6);


           }


        } 
        out.close();

    }
} 

class InputReader {
   
    private InputStream stream;
    private byte[] buf = new byte[1024];
    private int curChar;
    private int numChars;
    private SpaceCharFilter filter;

    public InputReader(InputStream stream) {
        this.stream = stream;
    }

    public double[] readDoubleArray(int size) {
        double[] array = new double[size];
        for (int i = 0; i < size; i++) {
            array[i] = readDouble();
        }
        return array;
    }

    public String[] readStringArray(int size) {
        String[] array = new String[size];
        for (int i = 0; i < size; i++) {
            array[i] = readString();
        }
        return array;
    }

    public char[] readCharArray(int size) {
        char[] array = new char[size];
        for (int i = 0; i < size; i++) {
            array[i] = readCharacter();
        }
        return array;
    }

    public void readIntArrays(int[]... arrays) {
        for (int i = 0; i < arrays[0].length; i++) {
            for (int j = 0; j < arrays.length; j++) {
                arrays[j][i] = readInt();
            }
        }
    }

    public void readLongArrays(long[]... arrays) {
        for (int i = 0; i < arrays[0].length; i++) {
            for (int j = 0; j < arrays.length; j++) {
                arrays[j][i] = readLong();
            }
        }
    }

    public void readDoubleArrays(double[]... arrays) {
        for (int i = 0; i < arrays[0].length; i++) {
            for (int j = 0; j < arrays.length; j++) {
                arrays[j][i] = readDouble();
            }
        }
    }

    public char[][] readCharTable(int rowCount, int columnCount) {
        char[][] table = new char[rowCount][];
        for (int i = 0; i < rowCount; i++) {
            table[i] = this.readCharArray(columnCount);
        }
        return table;
    }

    public int[][] readIntTable(int rowCount, int columnCount) {
        int[][] table = new int[rowCount][];
        for (int i = 0; i < rowCount; i++) {
            table[i] = readIntArray(columnCount);
        }
        return table;
    }

    public double[][] readDoubleTable(int rowCount, int columnCount) {
        double[][] table = new double[rowCount][];
        for (int i = 0; i < rowCount; i++) {
            table[i] = this.readDoubleArray(columnCount);
        }
        return table;
    }

    public long[][] readLongTable(int rowCount, int columnCount) {
        long[][] table = new long[rowCount][];
        for (int i = 0; i < rowCount; i++) {
            table[i] = readLongArray(columnCount);
        }
        return table;
    }

    public String[][] readStringTable(int rowCount, int columnCount) {
        String[][] table = new String[rowCount][];
        for (int i = 0; i < rowCount; i++) {
            table[i] = this.readStringArray(columnCount);
        }
        return table;
    }

    public String readText() {
        StringBuilder result = new StringBuilder();
        while (true) {
            int character = read();
            if (character == '\r') {
                continue;
            }
            if (character == -1) {
                break;
            }
            result.append((char) character);
        }
        return result.toString();
    }

    public void readStringArrays(String[]... arrays) {
        for (int i = 0; i < arrays[0].length; i++) {
            for (int j = 0; j < arrays.length; j++) {
                arrays[j][i] = readString();
            }
        }
    }

    public long[] readLongArray(int size) {
        long[] array = new long[size];
        for (int i = 0; i < size; i++) {
            array[i] = readLong();
        }
        return array;
    }

    public int[] readIntArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = readInt();
        }
        return array;
    }

    public int read() {
        if (numChars == -1) {
            throw new InputMismatchException();
        }
        if (curChar >= numChars) {
            curChar = 0;
            try {
                numChars = stream.read(buf);
            } catch (IOException e) {
                throw new InputMismatchException();
            }
            if (numChars <= 0) {
                return -1;
            }
        }
        return buf[curChar++];
    }

    public int peek() {
        if (numChars == -1) {
            return -1;
        }
        if (curChar >= numChars) {
            curChar = 0;
            try {
                numChars = stream.read(buf);
            } catch (IOException e) {
                return -1;
            }
            if (numChars <= 0) {
                return -1;
            }
        }
        return buf[curChar];
    }

    public int peekNonWhitespace() {
        while (isWhitespace(peek())) {
            read();
        }
        return peek();
    }

    public int readInt() {
        int c = read();
        while (isSpaceChar(c)) {
            c = read();
        }
        int sgn = 1;
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        int res = 0;
        do {
            if (c < '0' || c > '9') {
                throw new InputMismatchException();
            }
            res *= 10;
            res += c - '0';
            c = read();
        } while (!isSpaceChar(c));
        return res * sgn;
    }

    public long readLong() {
        int c = read();
        while (isSpaceChar(c)) {
            c = read();
        }
        int sgn = 1;
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        long res = 0;
        do {
            if (c < '0' || c > '9') {
                throw new InputMismatchException();
            }
            res *= 10;
            res += c - '0';
            c = read();
        } while (!isSpaceChar(c));
        return res * sgn;
    }

    public String readString() {
        int c = read();
        while (isSpaceChar(c)) {
            c = read();
        }
        StringBuilder res = new StringBuilder();
        do {
            if (Character.isValidCodePoint(c)) {
                res.appendCodePoint(c);
            }
            c = read();
        } while (!isSpaceChar(c));
        return res.toString();
    }

    public boolean isSpaceChar(int c) {
        if (filter != null) {
            return filter.isSpaceChar(c);
        }
        return isWhitespace(c);
    }

    public static boolean isWhitespace(int c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
    }

    private String readLine0() {
        StringBuilder buf = new StringBuilder();
        int c = read();
        while (c != '\n' && c != -1) {
            if (c != '\r') {
                buf.appendCodePoint(c);
            }
            c = read();
        }
        return buf.toString();
    }

    public String readLine() {
        String s = readLine0();
        while (s.trim().length() == 0) {
            s = readLine0();
        }
        return s;
    }

    public String readLine(boolean ignoreEmptyLines) {
        if (ignoreEmptyLines) {
            return readLine();
        } else {
            return readLine0();
        }
    }

    public BigInteger readBigInteger() {
        try {
            return new BigInteger(readString());
        } catch (NumberFormatException e) {
            throw new InputMismatchException();
        }
    }

    public char readCharacter() {
        int c = read();
        while (isSpaceChar(c)) {
            c = read();
        }
        return (char) c;
    }

    public double readDouble() {
        int c = read();
        while (isSpaceChar(c)) {
            c = read();
        }
        int sgn = 1;
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        double res = 0;
        while (!isSpaceChar(c) && c != '.') {
            if (c == 'e' || c == 'E') {
                return res * Math.pow(10, readInt());
            }
            if (c < '0' || c > '9') {
                throw new InputMismatchException();
            }
            res *= 10;
            res += c - '0';
            c = read();
        }
        if (c == '.') {
            c = read();
            double m = 1;
            while (!isSpaceChar(c)) {
                if (c == 'e' || c == 'E') {
                    return res * Math.pow(10, readInt());
                }
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

    public boolean isExhausted() {
        int value;
        while (isSpaceChar(value = peek()) && value != -1) {
            read();
        }
        return value == -1;
    }

    public String next() {
        return readString();
    }

    public SpaceCharFilter getFilter() {
        return filter;
    }

    public void setFilter(SpaceCharFilter filter) {
        this.filter = filter;
    }

    public interface SpaceCharFilter {
        public boolean isSpaceChar(int ch);
    }

    public int[] clone(int[] arr){
        int n = arr.length;
        int[] dup = new int[n];
        for(int i = 0;i <  n;i++)dup[i] = arr[i];
        return dup;
    }

    public int[][] clone(int[][] arr){
        int row = arr.length , col = arr[0].length;
        int[][] dup = new int[row][col];
        for(int i = 0;i < row;i++){
            for(int j = 0 ;j < col;j++){
                dup[i][j] = arr[i][j];
            }
        }
        return dup;
    }

    public long[] clone(long[] arr){
        int n = arr.length;
        long[] dup = new long[n];
        for(int i = 0;i <  n;i++)dup[i] = arr[i];
        return dup;
    }

    public long[][] clone(long[][] arr){
        int row = arr.length , col = arr[0].length;
        long[][] dup = new long[row][col];
        for(int i = 0;i < row;i++){
            for(int j = 0 ;j < col;j++){
                dup[i][j] = arr[i][j];
            }
        }
        return dup;
    }

    public static int[][] getFactor(int len){
        /*the number 498960 has 200 factors*/
        int[][] arr = new int[len+1][202];
        // to store length of current factors
        for(int i = 0;i <= len;i++)arr[i][0] = 1;

        for(int i = 1;i <= len;i++){
            
            int cnt = arr[i][0];
            // out.printLine("factors of " , i , " are ");
            for(int j = i;j <= len;j+=i){
                
                int currIndex = arr[j][0];
                arr[j][currIndex] = i;
                arr[j][0] += 1;
                
            }
            
            
        }
        return arr;
    }


}

class OutputWriter {
    private final PrintWriter writer;

    public OutputWriter(OutputStream outputStream) {
        writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
    }

    public OutputWriter(Writer writer) {
        this.writer = new PrintWriter(writer);
    }

    public void endl(){
        writer.write("--------------------------------\n");
    }
    

    public void printTable(char[][] table) {
        for (char[] row : table) {
            printLine(new String(row));
        }
    }

    public void printTable(int[][] table) {
        for(int i = 0;i < table.length;i++){
            for(int j = 0; j< table[0].length;j++){
                writer.print(table[i][j]);
                writer.print(' ');
            }
            writer.println();
        }
    }

    public void printTable(long[][] table) {
        for(int i = 0;i < table.length;i++){
            for(int j = 0; j< table[0].length;j++){
                writer.print(table[i][j]);
                writer.print(' ');
            }
            writer.println();
        }
    }


    public void printTable(double[][] table) {
        for(int i = 0;i < table.length;i++){
            for(int j = 0; j< table[0].length;j++){
                writer.print(table[i][j]);
                writer.print(' ');
            }
            writer.println();
        }
    }

    public void print(char[] array) {
        writer.print(array);
    }

    public void print(Object... objects) {
        for (int i = 0; i < objects.length; i++) {
            if (i != 0) {
                writer.print(' ');
            }
            writer.print(objects[i]);
        }
    }

    public void print(int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                writer.print(' ');
            }
            writer.print(array[i]);
        }
    }

    public void print(double[] array) {
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                writer.print(' ');
            }
            writer.print(array[i]);
        }
    }

    public void print(long[] array) {
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                writer.print(' ');
            }
            writer.print(array[i]);
        }
    }

    public void printLine(int[] array) {
        print(array);
        writer.println();
    }

    public void printLine(double[] array) {
        print(array);
        writer.println();
    }

    public void printLine(long[] array) {
        print(array);
        writer.println();
    }

    public void printLine() {
        writer.println();
    }

    public void printLine(Object... objects) {
        print(objects);
        writer.println();
    }

    public void print(char i) {
        writer.print(i);
    }

    public void printLine(char i) {
        writer.println(i);
    }

    public void printLine(char[] array) {
        writer.println(array);
    }

    public void printFormat(String format, Object... objects) {
        writer.printf(format, objects);
    }

    public void close() {
        writer.close();
    }

    public void flush() {
        writer.flush();
    }

    public void print(long i) {
        writer.print(i);
    }

    public void printLine(long i) {
        writer.println(i);
    }

    public void print(int i) {
        // System.out.println("isvalled");
        writer.print(i);
    }

    public void printLine(int i) {
        writer.println(i);
    }

    public void separateLines(int[] array) {
        for (int i : array) {
            printLine(i);
        }
    }

    public void printList(List<?> answer) {
        for (Object o : answer) {
            printLine(o);
        }
    }

    public String converToString(int[] arr){
        StringBuilder sb = new StringBuilder();
        for(int ele : arr)sb.append(ele+",");
        return sb.toString();
    }

    public String converToString(long[] arr){
        StringBuilder sb = new StringBuilder();
        for(long ele : arr)sb.append(ele+",");
        return sb.toString();
    }
}


/**
 * https://github.com/phishman3579/java-algorithms-implementation/tree/master/src/com/jwetherell/algorithms/mathematics
 * In mathematics, modular arithmetic is a system of arithmetic for integers, where numbers "wrap around" 
 * upon reaching a certain value—the modulus (plural moduli). The modern approach to modular arithmetic was 
 * developed by Carl Friedrich Gauss in his book Disquisitiones Arithmeticae, published in 1801.
 * <p>
 * @see <a href="https://en.wikipedia.org/wiki/Modular_arithmetic">Modular Arithmetic (Wikipedia)</a>
 * <br>
 * @author Szymon Stankiewicz <mail@stankiewicz.me>
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
class Modular {

    private static long modularAbs(long n, long mod) {
        n %= mod;
        if (n < 0)
            n += mod;
        return n;
    }


    /**
     * Adds two numbers in modulo arithmetic.
     * This function is safe for large numbers and won't overflow long.
     *
     * @param a
     * @param b
     * @param mod grater than 0
     * @return (a+b)%mod
     */
    public static long add(long a, long b, long mod) {
        if(mod <= 0)
            throw new IllegalArgumentException("Mod argument is not grater then 0");
        a = modularAbs(a, mod);
        b = modularAbs(b, mod);
        if(b > mod-a) {
            return b - (mod - a);
        }
        return (a + b)%mod;
    }

    /**
     * Subtract two numbers in modulo arithmetic.
     * This function is safe for large numbers and won't overflow or underflow long.
     *
     * @param a
     * @param b
     * @param mod grater than 0
     * @return (a-b)%mod
     */
    public static long subtract(long a, long b, long mod) {
        if(mod <= 0)
            throw new IllegalArgumentException("Mod argument is not grater then 0");
        return add(a, -b, mod);
    }

    /**
     * Multiply two numbers in modulo arithmetic.
     * This function is safe for large numbers and won't overflow or underflow long.
     *
     * Complexity O(log b)
     *
     * @param a
     * @param b
     * @param mod grater than 0
     * @return (a*b)%mod
     */

    public static long multiply(long a, long b, long mod) {
        if(mod <= 0)
            throw new IllegalArgumentException("Mod argument is not grater then 0");
        a = modularAbs(a, mod);
        b = modularAbs(b, mod);
        if(b == 0) return 0;
        return add(multiply(add(a, a, mod), b/2, mod), (b%2 == 1 ? a : 0), mod);
    }

    /**
     * Calculate power in modulo arithmetic.
     * This function is safe for large numbers and won't overflow or underflow long.
     *
     * Complexity O(log a * log b)
     *
     * @param a
     * @param b integer grater or equal to zero
     * @param mod grater than 0
     * @return (a^b)%mod
     */
    public static long pow(long a, long b, long mod) {
        if(mod <= 0)
            throw new IllegalArgumentException("Mod argument is not grater then 0");
        if (b < 0)
            throw new IllegalArgumentException("Exponent have to be grater or equal to zero");
        a = modularAbs(a, mod);
        if (a == 0 && b == 0)
            throw new IllegalArgumentException("0^0 expression");
        if (a == 0)
            return 0;
        long res = 1;
        while(b > 0) {
            if(b%2 == 1) res = multiply(res, a, mod);
            a = multiply(a, a, mod);
            b /= 2;
        }
        return res;
    }

    /**
     * Divide two numbers in modulo arithmetic.
     * This function is safe for large numbers and won't overflow or underflow long.
     * b and mod have to be coprime.
     *
     * Complexity O(sqrt(mod))
     *
     * @param a
     * @param b non zero
     * @param mod grater than 0
     * @return (a/b)%mod
     */

    public static long divide(long a, long b, long mod) {
        a = modularAbs(a, mod);
        b = modularAbs(b, mod);
        if(mod <= 0)
            throw new IllegalArgumentException("Mod argument is not grater then 0");
        if (b == 0)
            throw new IllegalArgumentException("Dividing by zero");
        if (GreatestCommonDivisor.gcdUsingRecursion(b, mod) != 1) {
            throw new IllegalArgumentException("b and mod are not coprime");
        }
        if (a == 0) {
            return 0;
        }
        if (b == 1) {
            return a;
        }

        long reverted = pow(b, Coprimes.getNumberOfCoprimes(mod)-1, mod);
        return multiply(reverted, a, mod);

    }
}


/**
 * In mathematics, the greatest common divisor (gcd) of two or more integers, when at least one of them is not 
 * zero, is the largest positive integer that is a divisor of both numbers. 
 * <p>
 * @see <a href="https://en.wikipedia.org/wiki/Greatest_common_divisor">Greatest Common Divisor (Wikipedia)</a>
 * <br>
 * @author Szymon Stankiewicz <mail@stankiewicz.me>
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
class GreatestCommonDivisor {

    /**
     * Calculate greatest common divisor of two numbers using recursion.
     * <p>
     * Time complexity O(log(a+b))
     * <br>
     * @param a Long integer
     * @param b Long integer
     * @return greatest common divisor of a and b
     */
    public static long gcdUsingRecursion(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        return a == 0 ? b : gcdUsingRecursion(b%a, a);
    }

    /**
     * A much more efficient method is the Euclidean algorithm, which uses a division algorithm such as long division 
     * in combination with the observation that the gcd of two numbers also divides their difference.
     * <p>
     * @see <a href="https://en.wikipedia.org/wiki/Greatest_common_divisor#Using_Euclid.27s_algorithm">Euclidean Algorithm (Wikipedia)</a>
     */
    public static final long gcdUsingEuclides(long x, long y) {
        long greater = x;
        long smaller = y;
        if (y > x) {
            greater = y;
            smaller = x;
        }

        long result = 0;
        while (true) {
            if (smaller == greater) {
                result = smaller; // smaller == greater
                break;
            }

            greater -= smaller;
            if (smaller > greater) {
                long temp = smaller;
                smaller = greater;
                greater = temp;
            }
        }
        return result;
    }
}


/**
 * In number theory, two integers a and b are said to be relatively prime, mutually prime, or coprime (also spelled 
 * co-prime) if the only positive integer that divides both of them is 1. That is, the only common positive factor 
 * of the two numbers is 1. This is equivalent to their greatest common divisor being 1.
 * <p>
 * @see <a href="https://en.wikipedia.org/wiki/Coprime_integers">Mutually Prime / Co-prime (Wikipedia)</a>
 * <br>
 * @author Szymon Stankiewicz <mail@stankiewicz.me>
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
class Coprimes {

    private Coprimes() { }

    /**
     *
     * Euler's totient function. Because this function is multiplicative such implementation is possible.
     * <p>
     * Time complexity: O(sqrt(n))
     * <p>
     * @param n Long integer
     * @return number of coprimes smaller or equal to n
     */
    public static long getNumberOfCoprimes(long n) {
        if(n < 1)
            return 0;
        long res = 1;
        for(int i = 2; i*i <= n; i++) {
            int times = 0;
            while(n%i == 0) {
                res *= (times > 0 ? i : i-1);
                n /= i;
                times++;
            }
        }
        if(n > 1)
            res *= n-1;
        return res;
    }
}