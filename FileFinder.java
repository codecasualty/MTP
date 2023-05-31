import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.*;
public class FileFinder {

    /**
    * Find BPMN files in system and return the path to them. This is a command line tool that allows the user to select which file to run
    * 
    * 
    * @return the file name (string format)
    */
    public String finder(){

        try {
            Scanner s = new Scanner(System.in);
            List<String> files = findFiles(Paths.get("./"), "bpmn");
            // files.forEach(x -> System.out.println(x));

            // Returns the list of files that are not empty
            if(files.size() == 0){
                System.out.println("There are no files present ");
                return "";
            }

            System.out.println("files present in your system are ");
            // Prints the list of files in the list.
            for(int i =0 ;i < files.size();i++){
                System.out.println( (i+1)+" "+files.get(i) );
            }

            System.out.println("Enter the number of file which you want to run");
            int n = s.nextInt();

            return files.get(n-1);

        } catch (IOException e) {
            System.out.println("path provided is not correct");
            e.printStackTrace();
            return "";
        }
    } 

    /**
    * Find files with specific extenstion in a directory. This is a recursive method. If you want to search for all files use Files#listFiles ( Path )
    * 
    * @param path - the directory to search for files
    * @param fileExtension - the file extension to search for e. g
    */
    public static List<String> findFiles(Path path, String fileExtension)
        throws IOException {

        // Checks if path is a directory.
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory!");
        }

        List<String> result;

        try (Stream<Path> walk = Files.walk(path)) {
            result = walk
                    .filter(p -> !Files.isDirectory(p))
                    // this is a path, not string,
                    // this only test if path end with a certain path
                    //.filter(p -> p.endsWith(fileExtension))
                    // convert path to string first
                    .map(p -> p.toString().toLowerCase())
                    .filter(f -> f.endsWith(fileExtension))
                    .collect(Collectors.toList());
                    
        }

        return result;
    }

}