import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;
import java.util.*;
class GeneratePetriNet{

    /**
    * Generates petrinet. dot file based on the map. It is assumed that the graph is acyclic i. e. all nodes are in the same rank direction
    * 
    * @param map - Map of nodes
    */
    public void generate(HashMap<String,PetriNetNode> map){

        try {
            FileWriter myWriter = new FileWriter("petrinet.dot");
            // myWriter.write("Files in Java might be tricky, but it is fun enough!");
            myWriter.write("digraph D { \n rankdir=LR;\n size=\"19,12\" ; \n ratio=\"fill\";\n fontsize=\"20\" \n;");
            //    earlier font value 15
            
            metaData(map , myWriter);
            traverseGraph(map , myWriter);
            myWriter.write("}");
            myWriter.close();
            System.out.println("Successfully wrote to the petrinet.dot file.");
          } catch (IOException e) {
            System.out.println("An error occurred. \n file not present");
            e.printStackTrace();
          }



    }

    /**
    * Traverses the graph and writes to the file. This is a debugging method to debug the graph.
    * 
    * @param map - the map of nodes to be traversed. The key is the node name the value is the PetriNetNode
    * @param writer - the file to write
    */
    public void  traverseGraph(HashMap<String,PetriNetNode> map , FileWriter writer){


        for(Map.Entry<String , PetriNetNode> ele : map.entrySet()){
            try{
                String key = ele.getKey() ;
                PetriNetNode value = ele.getValue();
                // If the value is empty continue.
                if(value.getOutgoingNodes().size() == 0)continue;
                for(int i = 0;i < value.getOutgoingNodes().size();i++){
                    String o_node = value.getOutgoingNodes().get(i);
                    if(!map.containsKey(o_node) || convert(value.getName()).startsWith("_") || i >= value.getOutgoingEdges().size() || convert(value.getName()).length()  < 1)continue;
                    writer.write(convert(value.getName())+"-> { " +convert(map.get(o_node).getName())+"}"+ (  value.getOutgoingEdges().get(i) == -1 ? "[style = dashed]" : ""  ));
                    if( i < value.getOutgoingEdgesName().size() &&  value.getOutgoingEdgesName().get(i).length() >= 1){
                        writer.write("[label = \""+value.getOutgoingEdgesName().get(i)+"\"];\n");
                    }
                    else writer.write("\n");
                }
                
                
            }
            catch(IOException e){
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            
        }



    }

    /**
    * Writes meta data to file. It is used to create table of data. The map has keys and values which are PetriNetNode objects
    * 
    * @param map - Map of name and PetriNetNode objects
    * @param writer - File to write meta data to. It is used to create table
    */
    public void metaData(HashMap<String ,PetriNetNode> map , FileWriter writer){

        for(Map.Entry<String , PetriNetNode> ele : map.entrySet()){
            try{
                String key = ele.getKey() ;
                PetriNetNode value = ele.getValue();
                String name = value.getName();
                // If the value is transition or place
                if(value.getisETransition() || value.getisTransition()){
                    writer.write(convert(name)+" [ shape = box ,style=filled,height= 0.4,label= \"\", xlabel=\""+printName(name)+" \" ] ;");
                }
                
                // if value. isEPlace or value. isPlace is true then the circle is written to the writer.
                else if((value.getisEPlace() || value.getisPlace()) ){
                    String res = convert(name).isBlank() ?  "res" : convert(name);
                    writer.write( res+" [shape = circle ,label= \"\" , xlabel=\""+printName(res)+" \" ] ;");
                }
                writer.write("\n");
            }
            catch(IOException e){
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            
        }


    }

    /**
    * Converts a string to a space separated list. This is used to make it easier to read the results of the user's program
    * 
    * @param str - The string to be converted
    * 
    * @return A space separated list of the user's program output as a single string with spaces in between each
    */
    public String convert(String str){
        String[] arr = str.split(" ");
        StringBuilder sb = new StringBuilder("");
        for(String val : arr){
            // Append the value to the buffer.
            if(!val.contains("?"))sb.append(val+"_");
        }
        return sb.toString();
    }

    /**
    * Prints a name to a new line. This is useful for debugging the program. 
    If you want to print the name of a class or class in a way that does not contain spaces use printClass ( name )
    * It is different from above method as convert uses "?" for adding "_" character to avoid clumsyness in output we use :"/n"
    * @param str - The name to print.
    * 
    * @return The name of the class or class in a new line separated by spaces ( " Class " ). For example : " Class_A_B
    */
    public static String printName(String str){
        String[] arr = str.split(" ");
        StringBuilder sb = new StringBuilder("");
        for(String val : arr){
            // Append a _n to the buffer
            if(!val.contains("?"))sb.append(val+"_\n");
        }
        return sb.toString();
    }

}