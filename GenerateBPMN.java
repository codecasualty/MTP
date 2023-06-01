import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;
import java.util.*;
class GenerateBPMN{

    /**
    * Generates BPMN file from graph. It is assumed that graph is acyclic. This method is called by GraphGenerator
    * 
    * @param map - Map of nodes to
    */
    public void generate(HashMap<String,Graph> map){

        try {
            FileWriter myWriter = new FileWriter("bpmn.dot");
            // myWriter.write("Files in Java might be tricky, but it is fun enough!");
            // myWriter.write("digraph D {");
            myWriter.write("digraph D { \n rankdir=LR;\n size=\"19,12\" ; \n ratio=\"fill\";\n fontsize=\"15\" \n;");
            metaData(map , myWriter);
            traverseGraph(map , myWriter);
            myWriter.write("}");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }



    }

    /**
    * Traverses the graph and writes to the file. This method is used for debugging purposes as a part of the GraphTrainer.
    * 
    * @param map - The map to traverse. The key is the name of the graph the value is the Graph.
    * @param writer - The FileWriter to write the graph to. The file is written to
    */
    public void  traverseGraph(HashMap<String,Graph> map , FileWriter writer){


        // for(Map.Entry<String , Graph> ele : map.entrySet()){
        //     try{
        //         String key = ele.getKey() ;
        //         Graph value = ele.getValue();
        //         // If the value is empty continue.
        //         if(value.getOutgoingNodes().size() == 0)continue;
        //         // System.out.println(key);
        //         writer.write(value.getName());
        //         // writer.write(key);

        //         // System.out.println("incoming Nodes = "+value.getIncomingNodes());
        //         writer.write("-> { " );
        //         // Write out all outgoing nodes in the map.
        //         for(int i = 0;i < value.outgoingNodes.size();i++){
        //             String id = value.getOutgoingNodes().get(i);
        //             writer.write(map.get(id).getName());
        //             // Write the next outgoing node to the writer.
        //             if(i < value.outgoingNodes.size() - 1)
        //             writer.write(",");
        //         }
                
        //         writer.write("}" );
        //         writer.write("\n");
        //     }
        //     catch(IOException e){
        //         System.out.println("An error occurred.");
        //         e.printStackTrace();
        //     }
            
        // }

        for(Map.Entry<String , Graph> ele : map.entrySet()){
            try{
                String key = ele.getKey() ;
                Graph value = ele.getValue();
                // If the value is empty continue.
                if(value.getOutgoingNodes().size() == 0)continue;
                for(int i = 0;i < value.getOutgoingNodes().size();i++){
                    String o_node = value.getOutgoingNodes().get(i);
                    if(!map.containsKey(o_node)  || convert(value.getName()).startsWith("_") || convert(map.get(o_node).getName()).startsWith("_"))continue;
                    // Utility.color("green");
                    // System.out.println("o_node = "+o_node+"  "+value.getName()+" "+convert(value.getName()));
                    // Utility.color("reset");
                    writer.write(convert(value.getName())+"-> { " +convert(map.get(o_node).getName())+"}"+ (  value.getOutgoingEdges().get(i) == -1 ? "[style = dashed]" : ""  ));
                    if(value.getOutgoingEdgesName().size() > i && value.getOutgoingEdgesName().get(i).length() >= 1){
                        writer.write("[label = \""+convert( value.getOutgoingEdgesName().get(i))+"\"];\n");
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
    * Writes meta data to file. This method is used to write meta data for each graph in the graph.
    * 
    * @param map - Map of graph names to graphs that should be written
    * @param writer - File to which we are writing
    */
    public void metaData(HashMap<String ,Graph> map , FileWriter writer){

        for(Map.Entry<String , Graph> ele : map.entrySet()){
            try{
                String key = ele.getKey() ;
                Graph value = ele.getValue();
                String name = value.getName();
                if(convert(name).startsWith("_") || printName(name).startsWith("_"))continue;
                // Write out the shape of the box.
                if(key.contains("Task")   || key.contains("participant") || key.contains("Activity")){
                    writer.write(convert(name)+" [ shape = box ,style=filled,height= 0.4,label= \"\", xlabel=\""+ printName(name)+" \" ] ;");
                }
                // Write the shape of the event.
                else if(key.contains("Gateway")){
                    writer.write(convert(name)+" [shape = diamond ,label= \"\", xlabel=\""+ printName(name)+" \" ] ;");
                }
                // Draw a circle circle style filled and filled label.
                else if(key.contains("StartEvent")){
                    writer.write(convert(name)+" [shape = circle ,label= \"\" , xlabel=\""+ printName(name)+" \" ] ;");
                }
                else if(key.contains("Intermediate")){
                    writer.write(convert(name)+" [shape = doublecircle,style=filled ,label= \"\", xlabel=\""+ printName(name)+" \" ] ;");
                }
                else{
                    writer.write(convert(name)+" [shape = doublecircle,style=filled ,label= \"\", xlabel=\""+ printName(name)+" \"  , fillcolor=black] ;");
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