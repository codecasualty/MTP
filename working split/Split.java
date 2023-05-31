import java.io.File;
import java.util.Scanner;

import javax.swing.text.StyledEditorKit;

import java.util.*;
class Split{
    /**
     * @param args
     */
    static HashMap<String , Graph> map ;
    
    public static void main(String[] args) {
    
        try{  
            FileFinder filefinderOb = new FileFinder();
            String fileName = filefinderOb.finder();
            System.out.println(fileName);
            File file=new File(fileName);   
            Scanner sc = new Scanner(file);  //file to be scanned  
            StringBuilder sb = new StringBuilder("");   
            while (sc.hasNextLine())        //returns true if and only if scanner has another token  
                sb.append(sc.nextLine());

            
            int indexOfStartProcess = sb.toString().indexOf("<bpmn:process");
            int indexOfEndProcess = sb.toString().indexOf("</bpmn:process>");
            String processPart = sb.toString().substring(indexOfStartProcess , indexOfEndProcess + 1);
            
            String[] searchPatters = {"parallelGateway","task" , "startEvent" , "exclusiveGateway" , "endEvent" , "sequenceFlow"};
            // we are extracting sequence flow at last because it reperesent all elements in bpmn file thus to represent we first extract all elements
            // and then establish relationship among them.
            int[] types = new int[]{1,1,0,0,0,-1}; //here 1 denotes transition , 0 places and -1 denotes seq flows only
            map = new HashMap<>();
            
            for(int i = 0;i < searchPatters.length;i++)
                patternsBlock(searchPatters[i], processPart, types[i]);


            // printGraph(map);
            printGraph();
            GenerateBPMN ob = new GenerateBPMN();
            ob.generate(map);

            //map all petri net node one by one from map generated
            HashMap<String , PetriNetNode> PetriNetmap = generatePetriNet(map);
            String startId = printPetriNet(PetriNetmap);
            // System.out.println("StartId is = "+startId);
            // bfsLoopCheck(startId , PetriNetmap);
            GeneratePetriNet petri = new GeneratePetriNet();
            petri.generate(PetriNetmap);
            
        }  
        catch(Exception e){  
            e.printStackTrace( );  
        }    

        
    
    } //end Main


    public static String printPetriNet(HashMap<String , PetriNetNode> petrinet){
        endl();
        endl();
        System.out.println("Printing Petri Net Elements ");
        String startId= "";
        for(Map.Entry<String , PetriNetNode> ele : petrinet.entrySet()){
            String key = ele.getKey() ;
            if(key.contains("StartEvent"))startId = key;
            PetriNetNode value = ele.getValue();
            System.out.println(key);
            System.out.println("name = "+value.getName());
            System.out.println("incoming Nodes = "+value.getIncomingNodes());
            System.out.println("outgoing Node = "+value.getOutgoingNodes());
            System.out.println("Is this node Eplace = "+ (value.getisEPlace() == true));
            System.out.println("Is this node Etransition = "+(value.getisETransition() == true));
            System.out.println("Is this node Place = "+(value.getisPlace() == true));
            System.out.println("Is this node Transition = "+(value.getisTransition()==true));
            endl();

        }
        return startId;
    }
    
    /**
     * @param map
     * @return
     */
    public static HashMap<String , PetriNetNode>  generatePetriNet(HashMap<String , Graph> map){
        HashMap<String , PetriNetNode> petrinet = new HashMap<>();
        
        //added all nodes to petrinetnode map
        String startId = "";
        for(Map.Entry<String , Graph> ele : map.entrySet()){

            String key = ele.getKey();
            if(key.contains("StartEvent"))startId = key;
            Graph val = ele.getValue();
            PetriNetNode node = new PetriNetNode();
            node.setName(val.getName());
            int transitionOrPlace = val.petriNetStyle;
            if(transitionOrPlace == 1)node.setisTransition();
            else if(transitionOrPlace == 0)node.setisPlace();
            petrinet.put(key , node ); 


        }

        // start traversing graph from startId and check if there are loops or backedges
        Queue<String> keys = new ArrayDeque<>();
        Queue<String> addedNodes = new ArrayDeque<>();
        HashSet<String> visited = new HashSet<>();
        int Enodes = 0; // variable to keep track of Enodes added to petri node
        keys.add(startId);
        while(!keys.isEmpty()){

            String currNode = keys.poll();
            Graph currGraph = map.get(currNode);
            int curr_type = currGraph.getPetriNetStyle();

            PetriNetNode currentPetriNetNode = petrinet.get(currNode);
            visited.add(currNode);
            //add outgoing nodes from current element in our queue
            // ---------------------------------------to write method-----------------------
            // addOutgoingNodes(keys , currNode);
            for(String out : map.get(currNode).outgoingNodes)
                if(!addedNodes.contains(out))keys.add(out);

            addedNodes.addAll(map.get(currNode).outgoingNodes);
// ------------------------------------------------------------------------------------------------

            ArrayList<String> outgoingNodesCurrNode = map.get(currNode).getOutgoingNodes();
            //here 1 denotes transition and 0 denotes place
            //added there transition and check need of Eplace and Etransitions 
            for(String outgoingNode : outgoingNodesCurrNode){

                String runningNode = outgoingNode;
                Graph runningGraph = map.get(runningNode);
                int runningNode_type = runningGraph.getPetriNetStyle();

                PetriNetNode runningPetriNetNode = petrinet.get(runningNode);

                //both are transitions then we need to add place but we also need to check if out running transition was already visited then we need to 
                // get incoming place of already visited transition and then point our current transition to incoming place of outgoing transition 
                // this case is possible in loop back in parallel gateway to transitions
                if(curr_type == 1 && runningNode_type == 1){
                    if(visited.contains(runningNode)){
                        ArrayList<String> incomingNodesRunningNode = runningPetriNetNode.getIncomingNodes();//as it was transition then incoming nodes will be places
                        //there will be edge from currNode to all these incomingNodes of running Node
                        for(String incoming:incomingNodesRunningNode){
                            currentPetriNetNode.setOutgoingNodes(incoming);
                            petrinet.get(incoming).setIncomingNode(currNode);
                        }
                        
                        
                    }
                    else{
                        PetriNetNode Eplace = new PetriNetNode();
                        Eplace.setisEPlace();
                        Eplace.setName("E"+Enodes);
                        petrinet.put(Eplace.getName() , Eplace);//add this Eplace into petri net nodes
                        currentPetriNetNode.setOutgoingNodes(Eplace.getName()); //add (Eplace) this node as outgoing node to current petri net transition 
                        Eplace.setOutgoingNodes(runningNode);//add this transition as outgoing transition to current Eplace
                        Eplace.setIncomingNode(currNode);//for this Eplace the incoing node is currentNode
                        runningPetriNetNode.setIncomingNode(Eplace.getName()); //for our running node the incoming place is Eplace
                        Enodes++;
                    }
                }
                //current type is transition and running node type is place
                else if(curr_type == 1 && runningNode_type == 0){

                    currentPetriNetNode.setOutgoingNodes(runningNode);
                    runningPetriNetNode.setIncomingNode(currNode);
                }
                //current node is place and running node is transition
                else if(curr_type == 0 && runningNode_type == 1){

                    if(visited.contains(runningNode)){
                        ArrayList<String> incomingNodesRunningNode = runningPetriNetNode.getIncomingNodes();//as it was transition then incoming nodes will be places
                        //there will be edge from currNode to all these incomingNodes of running Node
                        
                        for(int i = incomingNodesRunningNode.size() - 1;i >= 0 ;i--){
                            String incoming = incomingNodesRunningNode.get(i);
                            PetriNetNode Etransition = new PetriNetNode();
                            Etransition.setisETransition();
                            Etransition.setName("E"+Enodes);
                            petrinet.put(Etransition.getName() , Etransition);//add this Eplace into petri net nodes
                            currentPetriNetNode.setOutgoingNodes(Etransition.getName()); //add (Eplace) this node as outgoing node to current petri net transition 
                            Etransition.setOutgoingNodes(incoming);//add this transition as outgoing transition to current Eplace
                            Etransition.setIncomingNode(currNode);
                            petrinet.get(incoming).setIncomingNode(Etransition.getName());
                            Enodes++;
                            // currentPetriNetNode.setOutgoingNodes(incoming);
                            // petrinet.get(incoming).setOutgoingNodes(currNode);
                        }
                    }
                    else{
                        currentPetriNetNode.setOutgoingNodes(runningNode);
                        runningPetriNetNode.setIncomingNode(currNode);
                    }
                }
                //current node is place and running node is also place
                else if(curr_type == 0 && runningNode_type == 0){
                    PetriNetNode Etransition = new PetriNetNode();
                    Etransition.setisETransition();
                    Etransition.setName("E"+Enodes);
                    petrinet.put(Etransition.getName() , Etransition);//add this Eplace into petri net nodes
                    currentPetriNetNode.setOutgoingNodes(Etransition.getName()); //add (Eplace) this node as outgoing node to current petri net transition 
                    Etransition.setOutgoingNodes(runningNode);//add this transition as outgoing transition to current Eplace
                    Etransition.setIncomingNode(currNode);
                    runningPetriNetNode.setIncomingNode(Etransition.getName());
                    Enodes++;
                }

            }
        
                    
        
        }

        //here 1 denotes transition and 0 denotes place
        //added there transition and check need of Eplace and Etransitions 
       
       
        // for(Map.Entry<String , Graph> ele : map.entrySet()){

        //     String key = ele.getKey();
        //     Graph val = ele.getValue();
        //     int curr_type = val.getPetriNetStyle();
        //     ArrayList<String> outgoingNodesbpmn = val.getOutgoingNodes();
        //     // visited.add(key);//add this node as visited so that if any nodes route back to current node we can change the direction of edges like in loop.
        //     PetriNetNode currPetriNode = petrinet.get(key); // petrinet element where we need to add element . basically current petrinet element 
        //     for(String str : outgoingNodesbpmn){
        //         int o_type = map.get(str).getPetriNetStyle();
        //         //current node is place and outgoing node is transition then map transition into current node(place)
        //         if(o_type == 1 && curr_type == 0){
                  
        //             currPetriNode.setOutgoingNodes(str);
        //             petrinet.get(str).setIncomingNode(key);//for current outgoing node,the incoming node is the one a---> b , a is incoming for b and b is outgoing for a
                    
        //         }
        //         //current node is transition and outgoing node is place then map place to transition into current node(transition)
        //         else if(o_type == 0 && curr_type == 1){
        //             currPetriNode.setOutgoingNodes(str);
        //             petrinet.get(str).setIncomingNode(key);
        //         }
        //         //current type is transition and outgoing node is also transition then add Eplace in between them
        //         else if(o_type == 1 && curr_type == 1){
        //             PetriNetNode Eplace = new PetriNetNode();
        //             Eplace.setisEPlace();
        //             Eplace.setName("E"+Enodes);
        //             petrinet.put(Eplace.getName() , Eplace);//add this Eplace into petri net nodes
        //             currPetriNode.setOutgoingNodes(Eplace.getName()); //add (Eplace) this node as outgoing node to current petri net transition 
        //             Eplace.setOutgoingNodes(str);//add this transition as outgoing transition to current Eplace
        //             Eplace.setIncomingNode(key);
        //             petrinet.get(str).setIncomingNode(Eplace.getName());
        //             Enodes++;
        //         }
        //         //current type is place and outgoing ndoe is also place then add Etransition in between them
        //         else{
        //             PetriNetNode Etransition = new PetriNetNode();
        //             Etransition.setisETransition();
        //             Etransition.setName("E"+Enodes);
        //             petrinet.put(Etransition.getName() , Etransition);//add this Eplace into petri net nodes
        //             currPetriNode.setOutgoingNodes(Etransition.getName()); //add (Eplace) this node as outgoing node to current petri net transition 
        //             Etransition.setOutgoingNodes(str);//add this transition as outgoing transition to current Eplace
        //             Etransition.setIncomingNode(key);
        //             petrinet.get(str).setIncomingNode(Etransition.getName());
        //             Enodes++;
        //         }


        //     }


        // }

        return petrinet;
    }

    // public static void addAll

    public static void patternsBlock(String pattern , String value , int type){

        String startingPattern = "<bpmn:"+pattern , endingPattern  = !pattern.equals("sequenceFlow") ? "</bpmn:"+pattern : "/>";
        String[][] properties = { {"id=" ,"\"" } , {"name=" , "\""} , {"sourceRef=" , "\""} , {"targetRef=" , "\""}};

        // System.out.println(value.substring(1100)+" ");
        int startIndex = value.indexOf(startingPattern);
        while (startIndex != -1) {
            int endIndex = value.indexOf(endingPattern);
            String substring = value.substring(startIndex , endIndex);
            
            String[][] arr = new String[4][3];
            int index = 0;
            for(String[] ele : properties){
                // System.out.print(ele[0]+"  ");
                
                String ans = printId(substring , ele[0] , ele[1]);
                arr[index][0] = ele[0].trim();
                arr[index][1] = ans.trim();
                arr[index++][2] = type+"";
                // System.out.println(ans);
                // System.out.println();
            }
            // printValues(arr);
            createGraph(arr, map);
            // System.out.println();
            value = value.substring(endIndex+endingPattern.length());
            startIndex = value.indexOf(startingPattern);
            
        }

    }

    public static void printGraph(){
        endl();
        endl();
        System.out.println("Printing Elements extracted from bpmn file ");
        for(Map.Entry<String , Graph> ele : map.entrySet()){
            String key = ele.getKey() ;
            Graph value = ele.getValue();
            System.out.println(key);
            System.out.println("name = "+value.name);
            System.out.println("incoming Nodes = "+value.getIncomingNodes());
            System.out.println("outgoing Node = "+value.getOutgoingNodes());
            System.out.println("Petri Net style = "+value.getPetriNetStyle());
            endl();

        }
    }

    public static void print(Graph node){
        System.out.println("name = "+node.name);
        System.out.println("incoming node = "+node.getIncomingNodes());
        System.out.println("outgoing node = "+node.getOutgoingNodes());
    }

    public static void createGraph(String[][] arr , HashMap<String,Graph> map){
        for(int i = 0;i < arr.length;i++){
            String prop = arr[i][0] , values = arr[i][1] , type = arr[i][2];
            if(prop.contains("id") && arr[2][1].length() == 0){
                map.put(values , new Graph());
                map.get(values).setPetriNetStyle(Integer.parseInt(type));
            }
            else if(prop.contains("name") && map.containsKey(arr[0][1])){
                if(values.length() == 0)
                    (map.get(arr[0][1])).setName(arr[0][1]);
                else
                    (map.get(arr[0][1])).setName(values);
            }
            //setting outgoing nodes for each element
            else if(prop.contains("sourceRef") && values.length() > 0){
                map.get(values).setOutgoingNodes(arr[i+1][1]);
                // endl();
                // System.out.println("new node "+values);
                // print(map.get(values));
                // endl();
            }
            //setting incoming nodes for each element
            else if(prop.contains("targetRef") && values.length() > 0){
                map.get(values).setIncomingNode(arr[i-1][1]);
                // endl();
                // System.out.println("new node "+values);
                // print(map.get(values));
                // endl();
            }

        }
    }



    public static void printValues(String[][] arr){
        for(int i = 0;i < arr.length;i++){
            System.out.println(arr[i][0]+"  "+arr[i][1]);
        }
        endl();
    }
    public static void endl(){
        System.out.println("------------------------------");   
    }

    public static String printId(String value , String startPattern , String endPattern){
        int idIndex = value.indexOf(startPattern) ;
        if(idIndex == -1)return "";
        idIndex += + (startPattern).length() + 1;
        StringBuilder sb = new StringBuilder("");
        while( !(value.charAt(idIndex)+"").equals(endPattern) ){
            sb.append(value.charAt(idIndex));
            idIndex++;
        }
        return sb.toString();
    }

 }