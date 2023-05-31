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
            myWriter.write("digraph D {");
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


        for(Map.Entry<String , Graph> ele : map.entrySet()){
            try{
                String key = ele.getKey() ;
                Graph value = ele.getValue();
                // If the value is empty continue.
                if(value.getOutgoingNodes().size() == 0)continue;
                // System.out.println(key);
                writer.write(value.getName());
                // writer.write(key);

                // System.out.println("incoming Nodes = "+value.getIncomingNodes());
                writer.write("-> { " );
                // Write out all outgoing nodes in the map.
                for(int i = 0;i < value.outgoingNodes.size();i++){
                    String id = value.getOutgoingNodes().get(i);
                    writer.write(map.get(id).getName());
                    // Write the next outgoing node to the writer.
                    if(i < value.outgoingNodes.size() - 1)
                    writer.write(",");
                }
                
                writer.write("}" );
                writer.write("\n");
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
                // Write out the shape of the box.
                if(key.contains("Activity")){
                    writer.write(name+" [ shape = box ,style=filled,height= 0.4,label= \"\", xlabel=\""+name+" \" ] ;");
                }
                // Write the shape of the event.
                else if(key.contains("Gateway")){
                    writer.write(name+" [shape = diamond ,label= \"\", xlabel=\""+name+" \" ] ;");
                }
                // Draw a circle circle style filled and filled label.
                else if(key.contains("StartEvent")){
                    writer.write(name+" [shape = circle ,label= \"\" , xlabel=\""+name+" \" ] ;");
                }
                else{
                    writer.write(name+" [shape = doublecircle,style=filled ,label= \"\", xlabel=\""+name+" \" ] ;");
                }
                writer.write("\n");
            }
            catch(IOException e){
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            
        }


    }

}