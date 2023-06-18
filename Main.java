import java.io.File;
import java.util.Scanner;
import java.util.regex.*;
import java.util.*;


class Main{
   
    static HashMap<String , Graph> map ;
    static HashMap<String , String> refMapParticipant;
    static HashMap<String,SubEvents> subprocessEvents;
    static ArrayList<String> blackBoxNode;
    static ArrayList<String> participantOccurence;    
    static HashMap<String , HashSet<String>> participantWiseOrdering;
    static int participants;
    static int depth;// this variable will help in maintaing forward or backward edges
    public static void main(String[] args) {
    
        try{  
            File file=new File(args[0]);   
            Scanner sc = new Scanner(file);  //file to be scanned  
            StringBuilder sb = new StringBuilder("");   
            
            participantOccurence = new ArrayList<>();
            blackBoxNode = new ArrayList<>();
            participantWiseOrdering = new HashMap<>();

            while (sc.hasNextLine())        //returns true if and only if scanner has another token  
                sb.append(sc.nextLine());

            int startProcess = sb.toString().indexOf("<bpmn:process");
            if(startProcess == -1)startProcess = 0;
            int startCollab = sb.toString().indexOf("<bpmn:collaboration");
            if(startCollab == -1)startCollab = 0 ;
            int indexOfStartProcess = Math.min ( startProcess , startCollab);
            int indexOfEndProcess = sb.toString().lastIndexOf("</bpmn:process>") + 16;
            String processPart = sb.toString().substring(indexOfStartProcess , indexOfEndProcess + 1);
            /*creating map to store all ids and their incoming and outgoing nodes */
            map = new HashMap<>();
            refMapParticipant = new HashMap<>();


            // System.out.println("subprocess printing starts");
            ArrayList<String> subprocess = subprocessMeta(processPart);
            
            String[] searchPatters = {"startEvent" , "exclusiveGateway" , "inclusiveGateway" , "endEvent" , "parallelGateway","task" ,
            "receiveTask", "manualTask","scriptTask","userTask","sendTask","serviceTask",  "intermediateCatchEvent","intermediateThrowEvent", "sequenceFlow" , "messageFlow"};

            ArrayList<Integer> indexProcess = getOccurence(processPart, "<bpmn:process id=\"");
            indexProcess.add(processPart.lastIndexOf("</bpmn:process>"));

            participantWiseOrdering = participantWiseOrdering(indexProcess , processPart , searchPatters);
            int[] types = new int[]{0,0,0,0,1,1,1,1,1,1,1,1,1,1,-1 ,-2}; //here 1 denotes transition , 0 places and -1 denotes seq flows only -2 denotes message flows

            for(int i = 0;i < searchPatters.length;i++){
                patternsBlock(searchPatters[i], processPart, types[i]);
            }
            int collabStart = getIndexPattern(sb.toString() , "<bpmn:collaboration");
            int collabEnd = getIndexPattern(sb.toString(), "</bpmn:collaboration>");
            String collabBlock = sb.toString().substring(collabStart < 0 ? 0 : collabStart, collabEnd < 0 ? 0 : collabEnd );
            Participant ob = new Participant();
            ob.addLinks(map,collabBlock , participants , participantOccurence);
            
            
            //map all petri net node one by one from map generated
            
            HashMap<String , PetriNetNode> PetriNetmap = generatePetriNet(map);
            NodeModification nodeModification = new NodeModification();
            nodeModification.edgePreservation(PetriNetmap, blackBoxNode);
            GeneratePetriNet petri = new GeneratePetriNet();
            petri.generate(PetriNetmap);
            GenerateBPMN bpmn = new GenerateBPMN();
            bpmn.generate(map);
            sc.close();
            
        }  
        catch(Exception e){  
            e.printStackTrace( );  
        }    

        
        
    
    } //end Main

    public static HashMap<String,  HashSet<String>> participantWiseOrdering(ArrayList<Integer> indexProcess , String processPart , String[] searchPatters){
        HashMap<String,  HashSet<String>> participantWiseOrdering = new HashMap<>();
        for(int i = 0; i + 1 < indexProcess.size();i++){
            String str = processPart.substring(indexProcess.get(i));
            String processIds = printId(str, "<bpmn:process id=" , "\"");
            HashSet<String> set = new HashSet<>();
            String searchString = processPart.substring(indexProcess.get(i), indexProcess.get(i+1));
            for(int j = 0 ;j < searchPatters.length-2;j++){
                String searchPattern = "<bpmn:"+searchPatters[j]+" id=";
                int start = 0;
                while( (start = searchString.indexOf(searchPattern , start )) != -1){
                    String ids = getParameter(searchString, searchPattern , "\"" ,  start);
                    if(ids.length() >= 1){
                        // System.out.println(searchPatters[j]+" "+ids);
                        set.add(ids);
                    }
                    start += searchPattern.length();
                }
                
            }
            participantWiseOrdering.put(processIds,set);
            // System.out.println("key is "+processIds);
            // System.out.println(set);
            endl();
        }
        return participantWiseOrdering;
    }

    public static ArrayList<Integer> getOccurence(String str, String pattern){
        int start = 0;
        ArrayList<Integer> list = new ArrayList<>();
        while( (start = str.indexOf(pattern, start)) != -1){
            // System.out.println(start+"  "+(str.substring(start, start + pattern.length()+1)));
            list.add(start);
            start+=pattern.length();
        }
        return list;
    }

    public static String getString(int startIndex, char endingPattern , String str){
        int start = startIndex;
        while(str.charAt(start) != '\'')start++;
        return str.substring(startIndex, start);
    }   

    public static int getIndexPattern(String str , String patterns){
        return str.toString().indexOf(patterns);
    }

    public static void printIntList(ArrayList<Integer> list){
        for(int i = 0;i < list.size();i++){
            int val = list.get(i);
            System.out.print(val+" \t ");
        }
        System.out.println();
        return;
    }

    public static ArrayList<String> printList(ArrayList<String> list){
        ArrayList<String> l = new ArrayList<>(list);
        if(list.size() == 0)System.out.println("list is empty");
        Utility.color("blue");
        for(String str: list){
            
            System.out.println(str+" ");
        }
        Utility.color("reset");
        System.out.println();
        return l;
    }


    /**
     * Prints the elements of a Petri Net.
     *
     * @param petrinet A HashMap containing the nodes of the Petri Net
     * @return The ID of the start event node
     */
    public static String printPetriNet(HashMap<String , PetriNetNode> petrinet){
        endl();
        endl();
        color("blue");
        System.out.println("Printing Petri Net Elements printPetNet()");
        color("white");
        
        String startId= "";
        for(Map.Entry<String , PetriNetNode> ele : petrinet.entrySet()){
            String key = ele.getKey() ;
            Utility.color("red");
            System.out.println( key);
            Utility.color("reset");
            PetriNetNode value = ele.getValue();
            // getColorCode(participants);
            
            System.out.println("name = "+value.getName());
            System.out.println("incoming Nodes = "+value.getIncomingNodes());
            System.out.println("incoming Edges = "+value.getIncomingEdges());
            System.out.println("outgoing Nodes = "+value.getOutgoingNodes());
            System.out.println("outgoing Edges = "+value.getOutgoingEdges());
            System.out.println("outgoing Edges = "+value.getOutgoingEdgesName());
            System.out.println("this node is "+typeOfNode(value));
            // System.out.println("Is this node Eplace = "+ (value.getisEPlace() == true));
            // System.out.println("Is this node Etransition = "+(value.getisETransition() == true));
            // System.out.println("Is this node Place = "+(value.getisPlace() == true));
            // System.out.println("Is this node Transition = "+(value.getisTransition()==true));
            System.out.println("Is this node MessageFlow = "+(value.getMessageFLow()==true));
            System.out.println("type of node is "+getNameOfType(value.getTaskType()));
            System.out.println("cardinality of the above node is  "+value.getCardinality());
            System.out.println("is this timer event = "+(value.getIsTimerEvent() ? "YES" : "NO"));
            endl();

        }
        endl();
        return startId;
    }

    public static String typeOfNode(PetriNetNode node){
        if(node.getisEPlace()){
            return "Eplace";
        }
        else if(node.getisETransition()){
            return "ETransition";
        }
        else if(node.getisPlace()){
            return "Place";
        }
        else return "Transition";
    }

    public static String getNameOfType(int value){
        if(value == -1)return "Not task";
        else if (value == 0)return "Normal task";
        else if(value  == 1)return "Loop task";
        else if(value == 2)return "Multinstance parallel";
        else return "Multiinstance sequential";
    }

    public static void color(String str){
        str = str.toLowerCase();
        if(str.equals("red"))getColorCode(1);
        else if(str.equals("black"))getColorCode(0);
        else if(str.equals(("green ")))getColorCode(2);
        else if(str.equals("yellow"))getColorCode(3);
        else if(str.equals("blue"))getColorCode(4);
        else if(str.equals("magneta"))getColorCode(5);
        else if(str.equals("cyan"))getColorCode(6);
        else if(str.equals("white"))getColorCode(7);
        else getColorCode(8);

    }

    public static void getColorCode(int index){
        /**
         * Color codes are as follows :- 
         * 
            Black: \u001b[30m
            Red: \u001b[31m
            Green: \u001b[32m
            Yellow: \u001b[33m
            Blue: \u001b[34m
            Magenta: \u001b[35m
            Cyan: \u001b[36m
            White: \u001b[37m

            Reset: \u001b[0m

         */
        if(index == 0)System.out.println("\u001b[30m");
        else if(index == 1)System.out.println("\u001b[31m");
        else if(index == 2)System.out.println("\u001b[32m");
        else if(index == 3)System.out.println("\u001b[33m");
        else if(index == 4)System.out.println("\u001b[34m");
        else if(index == 5)System.out.println("\u001b[35m");
        else if(index == 6)System.out.println("\u001b[36m");
        else if(index == 7)System.out.println("\u001b[37m");
        else if(index == 8)System.out.println("\u001b[0m");





    }
    
    /**
     * @param map
     * @return
     */
    public static HashMap<String , PetriNetNode>  generatePetriNet(HashMap<String , Graph> map){
        HashMap<String , PetriNetNode> petrinet = new HashMap<>();
        Queue<String> keys = new ArrayDeque<>();
        Queue<String> addedNodes = new ArrayDeque<>();
        HashSet<String> visited = new HashSet<>();
        //added all nodes to petrinetnode map
        
        /**
         * Converts a map of Graph objects to a PetriNetNode object and adds it to a PetriNet.
         *
         * @param map The map of Graph objects to convert.
         * @param petrinet The PetriNet to add the converted PetriNetNode object to.
         * @return void
         */
        for(Map.Entry<String , Graph> ele : map.entrySet()){

            String key = ele.getKey();
            if(key.contains("StartEvent"))keys.add(key);
            Graph val = ele.getValue();
            PetriNetNode node = new PetriNetNode();
            node.setName(val.getName());
            val.setTime(depth);
            int transitionOrPlace = val.petriNetStyle;
            if(transitionOrPlace == 1)node.setisTransition();
            else if(transitionOrPlace == 0)node.setisPlace();
            else if(transitionOrPlace == -2)node.setMessageFlow();
            // System.out.println(val.getName()+"  "+val.petriNetStyle);
            petrinet.put(key , node ); 


        }

        // start traversing graph from startId and check if there are loops or backedges
        
        int Enodes = 0; // variable to keep track of Enodes added to petri node
        
        while(!keys.isEmpty()){

            String currNode = keys.poll();
            Graph currGraph = map.get(currNode);
            if(currGraph == null )continue;
            int curr_type = currGraph.getPetriNetStyle();

            if(visited.contains(currNode))continue;
            depth++;
            // currGraph.setTime(depth++);//to identify forward or backward edges
            PetriNetNode currentPetriNetNode = petrinet.get(currNode);
            currentPetriNetNode.setTaskType(currGraph.getTaskType());
            currentPetriNetNode.setCardinality(currGraph.getCardinality());
            currentPetriNetNode.setId(currNode);
            currentPetriNetNode.setIsTimerEvent(currGraph.getIsTimerEvent());
            currentPetriNetNode.setKind(currGraph.getKind());
            
            // if(currGraph.getTaskType() == 2 && currGraph.getCardinality() > 1)parallelMultinstance.add(currNode);
            // if(currGraph.getTaskType() == 3 && currGraph.getCardinality() > 1)sequentialMultinstanceLoop.add(currNode);
            // if(currGraph.getTaskType() == 1)sequentialMultinstanceLoop.add(currNode);
            // if(currGraph.getIsTimerEvent())timerEvents.add(currNode);//this is key through which we identify it

            if(currGraph.getTaskType() >= 1 || currGraph.getIsTimerEvent())blackBoxNode.add(currNode);

            visited.add(currNode);
            //add outgoing nodes from current element in our queue
            // ---------------------------------------to write method-----------------------
            // addOutgoingNodes(keys , currNode);
            for(String out : map.get(currNode).outgoingNodes)
                if(!addedNodes.contains(out)){
                    map.get(out).setTime(depth);
                    keys.add(out);
                }

            addedNodes.addAll(map.get(currNode).outgoingNodes);
// ------------------------------------------------------------------------------------------------

            ArrayList<String> outgoingNodesCurrNode = map.get(currNode).getOutgoingNodes();
/*
 *          march 30 adding outgoingedges for current incoming nodes
 */
            ArrayList<Integer> outgoingEdgesCurrNode = map.get(currNode).getOutgoingEdges();

            ArrayList<String> outgoingEdgesCurrNodeNames = map.get(currNode).getOutgoingEdgesName();
            // System.out.println("Node val = "+currNode);
            // System.out.println("outgoingNodes = "+outgoingNodesCurrNode);
            // System.out.println("outgoingEdges = "+outgoingEdgesCurrNode);
            // endl();
            //here 1 denotes transition and 0 denotes place
            //added there transition and check need of Eplace and Etransitions 
            int start = 0;
            int edgeName = 0;
            for(String outgoingNode : outgoingNodesCurrNode){

                String runningNode = outgoingNode;
                Graph runningGraph = map.get(runningNode);
                int edgeType = outgoingEdgesCurrNode.get(start);
                int runninNode_time = runningGraph.getTime();                

                if(runningGraph == null)continue;
                int runningNode_type = runningGraph.getPetriNetStyle();
                
                PetriNetNode runningPetriNetNode = petrinet.get(runningNode);

                //both are transitions then we need to add place but we also need to check if out running transition was already visited then we need to 
                // get incoming place of already visited transition and then point our current transition to incoming place of outgoing transition 
                // this case is possible in loop back in parallel gateway to transitions
                if(curr_type == 1 && runningNode_type == 1){
                    if(visited.contains(runningNode) && currGraph.getTime() > runninNode_time && runningGraph.getKind().contains("exclusiveGateway") ){
                        // Utility.color("blue");
                        // System.out.println("current node "+currGraph.getTime()+"  "+currGraph.getName()+"  running node "+runninNode_time+"   "+runningGraph.getName());
                        // Utility.color("reset");
                        ArrayList<String> incomingNodesRunningNode = runningPetriNetNode.getIncomingNodes();//as it was transition then incoming nodes will be places
                        ArrayList<String> incomingNodesRunningNodeEdges = runningPetriNetNode.getOutgoingEdgesName();
                        // ArrayList<Integer> incomingEdgesRunningNode = runningPetriNetNode.getIncomingEdges();//as it was transition then incoming nodes will be places
                        // System.out.println("runnning node "+runningNode);
                        // System.out.println(" incoming node from running Node "+incomingNodesRunningNode);
                        // System.out.println(" incoming edge from running Node "+incomingEdgesRunningNode);
                        //there will be edge from currNode to all these incomingNodes of running Node
                        // endl();
                        int init = 0;
                        for(String incoming:incomingNodesRunningNode){
                            currentPetriNetNode.setOutgoingNodes(incoming);
                            //march 30
                            currentPetriNetNode.setOutgoingEdges(edgeType);
                            currentPetriNetNode.setOutgoingEdgesName(incomingNodesRunningNodeEdges.get(init++));
                            petrinet.get(incoming).setIncomingNode(currNode);
                            petrinet.get(incoming).setIncomingEdges(1);
                        }
                        
                        
                    }
                    else{
                       
                        PetriNetNode Eplace = new PetriNetNode();
                        Eplace.setisEPlace();
                        Eplace.setName("E"+Enodes);
                        /** only changes performed here are using getId instead of getName and using "<>" for separation */
                        Eplace.setId("E"+Enodes+"<>"+currentPetriNetNode.getId());
                        petrinet.put(Eplace.getId() , Eplace);//add this Eplace into petri net nodes
                        currentPetriNetNode.setOutgoingNodes(Eplace.getId()); //add (Eplace) this node as outgoing node to current petri net transition 
                        // march 30
                        // adding type on basis of current and running node edges
                        currentPetriNetNode.setOutgoingEdges(edgeType);
                        currentPetriNetNode.setOutgoingEdgesName("");//setting empty to avoid extra or less names i.e. the data should match with other edges

                        Eplace.setOutgoingNodes(runningNode);//add this transition as outgoing transition to current Eplace
                        Eplace.setIncomingNode(currNode);//for this Eplace the incoing node is currentNode
                        //march 30
                        Eplace.setIncomingEdges(edgeType);
                        Eplace.setOutgoingEdges(1);//direct edge
                        Eplace.setOutgoingEdgesName("");//same reason to match the data with other nodes and edges
                        runningPetriNetNode.setIncomingNode(Eplace.getId()); //for our running node the incoming place is Eplace
                        runningPetriNetNode.setIncomingEdges(1);
                        Enodes++;
                    }
                }
                //current type is transition and running node type is place
                else if(curr_type == 1 && runningNode_type == 0){
                    // Utility.color("red");
                    // System.out.println("current node "+currNode+"  "+currGraph.getName()+"  running node "+runningNode+"   "+runningGraph.getName());
                    // Utility.color("reset");
                    currentPetriNetNode.setOutgoingNodes(runningNode);
                    currentPetriNetNode.setOutgoingEdges(edgeType);//march 30
                    //added on jun1
                    if(edgeName < outgoingEdgesCurrNodeNames.size())
                    currentPetriNetNode.setOutgoingEdgesName(outgoingEdgesCurrNodeNames.get(edgeName++));
                    else
                    currentPetriNetNode.setOutgoingEdgesName("");
                    runningPetriNetNode.setIncomingNode(currNode);
                    runningPetriNetNode.setIncomingEdges(edgeType);//march 30
                }
                //current node is place and running node is transition
                else if(curr_type == 0 && runningNode_type == 1){
                    Utility.color("red");
                    System.out.println("current node "+currNode+"  "+currGraph.getName()+"  running node "+runningNode+"   "+runningGraph.getName());
                    Utility.color("reset");
                    if((visited.contains(runningNode) && runningGraph.getKind().contains("exclusiveGateway")) ){
                        ArrayList<String> incomingNodesRunningNode = runningPetriNetNode.getIncomingNodes();//as it was transition then incoming nodes will be places
                        //there will be edge from currNode to all these incomingNodes of running Node
                        //march 30
                        ArrayList<Integer> incomingEdgesRunningNode = runningPetriNetNode.getIncomingEdges();//as it was transition then incoming nodes will be places
                        
                        for(int i = incomingNodesRunningNode.size() - 1;i >= 0 ;i--){
                            String incoming = incomingNodesRunningNode.get(i);

                            // march 30
                            int newtype = incomingEdgesRunningNode.get(i);

                            PetriNetNode Etransition = new PetriNetNode();
                            Etransition.setisETransition();
                            Etransition.setName("E"+Enodes);
                            petrinet.put(Etransition.getName() , Etransition);//add this Eplace into petri net nodes
                            currentPetriNetNode.setOutgoingNodes(Etransition.getName()); //add (Eplace) this node as outgoing node to current petri net transition 
                            //march 30
                            currentPetriNetNode.setOutgoingEdges(1);
                            currentPetriNetNode.setOutgoingEdgesName("");
                            Etransition.setOutgoingNodes(incoming);//add this transition as outgoing transition to current Eplace
                            //march 30
                            Etransition.setOutgoingEdges(newtype);
                            Etransition.setOutgoingEdgesName("");
                            Etransition.setIncomingNode(currNode);
                            //march 30
                            Etransition.setIncomingEdges(1);
                            petrinet.get(incoming).setIncomingNode(Etransition.getName());

                            //following lines are added on march 30
                            


                            Enodes++;
                            // currentPetriNetNode.setOutgoingNodes(incoming);
                            // petrinet.get(incoming).setOutgoingNodes(currNode);
                        }
                    }
                    else{
                        currentPetriNetNode.setOutgoingNodes(runningNode);
                        currentPetriNetNode.setOutgoingEdges(edgeType);//march 30
                        if(edgeName < outgoingEdgesCurrNodeNames.size())
                        currentPetriNetNode.setOutgoingEdgesName(outgoingEdgesCurrNodeNames.get(edgeName++));
                        else 
                        currentPetriNetNode.setOutgoingEdgesName("");
                        runningPetriNetNode.setIncomingNode(currNode);
                        runningPetriNetNode.setIncomingEdges(edgeType);//march 30
                    }
                }
                //current node is place and running node is also place
                else if(curr_type == 0 && runningNode_type == 0){
                    PetriNetNode Etransition = new PetriNetNode();
                    Etransition.setisETransition();
                    Etransition.setName("E"+Enodes);
                    petrinet.put(Etransition.getName() , Etransition);//add this Eplace into petri net nodes
                    currentPetriNetNode.setOutgoingNodes(Etransition.getName()); //add (Eplace) this node as outgoing node to current petri net transition 
                    currentPetriNetNode.setOutgoingEdgesName(outgoingEdgesCurrNodeNames.get(start));
                    Etransition.setOutgoingNodes(runningNode);//add this transition as outgoing transition to current Eplace
                    Etransition.setIncomingNode(currNode);
                    runningPetriNetNode.setIncomingNode(Etransition.getName());

                    //following 4 lines are added on march 30
                    currentPetriNetNode.setOutgoingEdges(edgeType);
                    Etransition.setOutgoingEdges(1);
                    Etransition.setOutgoingEdgesName("");
                    Etransition.setIncomingEdges(edgeType);
                    runningPetriNetNode.setIncomingEdges(1);

                    Enodes++;
                }
                start++;
            }
        
                    
        
        }

        
        return petrinet;
    }

    // public static void addAll

    public static void patternsBlock(String pattern , String value , int type){
        /*
         * this block over here iterates for each pattern like task 
         * for taks it will find all task ids their name, source ref and target ref one by one
         */
        String startingPattern = "<bpmn:"+pattern , endingPattern  = !pattern.equals("messageFlow") && !pattern.equals("sequenceFlow") ? "</bpmn:"+pattern : "/>";
        String[][] properties = { {"id=" ,"\"" } , {"name=" , "\""} , {"sourceRef=" , "\""} , {"targetRef=" , "\""} , {"loop=" ,  "\""} , {"timerEventDefinition" , "\'"}};

        // System.out.println("current pattern is \u001B[31m"+pattern+" ");
        // System.out.println("starting pattern "+startingPattern+" ending pattern "+endingPattern+"\u001B[0m ");
        // System.out.println(value.substring(1100)+" ");
        int startIndex = value.indexOf(startingPattern);
        while (startIndex != -1) {
            int endIndex = !endingPattern.equals("/>") ? value.indexOf(endingPattern) : getIndex(endingPattern , startIndex , value);
            String substring = value.substring(startIndex , endIndex);
            
            String[][] arr = new String[7][3];//here  5 denotes properties which we will be extracting 
            int index = 0;
            for(int i = 0;i < 4;i++){
                // System.out.print(ele[0]+"  ");
                String[] ele = properties[i];
                String ans = printId(substring , ele[0] , ele[1]);
                arr[index][0] = ele[0].trim();
                arr[index][1] = ans.trim();
                arr[index++][2] = type+"";
                
                // System.out.println(ans);
                // System.out.println();
            }
            /**
             * Some properties are easy to disect whereas the properties such as loop cardinality and its type, timerEvents and other properties are
             * difficult to remove and create a node out of it
             * thus we extract it in new way
             */
            index += 2;
            arr[index-2][0] = properties[index-2][0];
            int[] typeAndCardinality = getLoopType(substring, pattern);
            arr[index-2][1] = typeAndCardinality[0]+" "+typeAndCardinality[1];
            // arr[index-1]
            // System.out.println(pattern+"  -> "+typeAndCardinality[0]+"  "+typeAndCardinality[1]);
            // System.out.println(substring+"  pattern is "+pattern+"  "+arr[index-1][1]);
            // printValues(arr);
            arr[index-1][0] = properties[index-1][0];
            arr[index-1][1] = substring.contains(properties[index-1][0]) == true ? "YES" : "NO";
            arr[index][0] = pattern;
            createGraph(arr, map);
            // printGraph();
            // System.out.println();
            value = value.substring(endIndex+endingPattern.length());
            startIndex = value.indexOf(startingPattern);
            
        }

    }

    //date 14 May

    public static int[] getLoopType(String str , String pattern){
        // System.out.println("printing string input " + str + "pattern is " + pattern);
        // str = str.toLowerCase();
        if(!pattern.toLowerCase().contains("task"))return  new int[]{-1 , 0};
        else if(str.contains("standardLoopCharacteristics"))return new int[]{ 1,1};
        else if(str.contains("multiInstanceLoopCharacteristics") && str.contains("isSequential"))return new int[]{3 , getCardinality(str)};
        else if(str.contains("multiInstanceLoopCharacteristics") && !str.contains("isSequential"))return new int[]{2 , getCardinality(str)};
        return new int[]{0,1};
    }

    public static int getCardinality(String str){
        int index = str.indexOf("</bpmn:loopCardinality>");
        int start = index - 1;
        while((int)(str.charAt(start) - '0') >= 0 && (int)(str.charAt(start) - '0') <= 9)start--;
        int val = Integer.parseInt(str.substring(start + 1, index));
        // System.out.println(str.substring(start, index));
        return val; 
    }

    // date april 17 2023

    /*
     * for each subprocess there will be list of start and end list which will depicts
     * the start and end of  subprocess thus it will be better to use hashmap in such case
     * Key = subprocess id 
     * value = call storing start and end events as list
     *    
     * the below function will find all subproces in process and return its index
     * or string possible 
     * for this we will be using regex and use list to store all index of subprocess
     * ever occured
     * 
     */
    static class SubEvents{
        ArrayList<String> start , end;
        String name;
        SubEvents(ArrayList<String> start , ArrayList<String> end , String name){
            this.start = start;
            this.end = end;
            this.name = name;
        }
    }
    /*
     * this methods removes complete string pointed by given startIndex and endIndex
     */
    public static ArrayList<String> metaData(ArrayList<Integer> startIndex , ArrayList<Integer> endIndex , String str){

        ArrayList<String> subprocess = new ArrayList<>();
        for(int i = 0;i < startIndex.size();i++){
            int start = startIndex.get(i) , end = endIndex.get(i);
            String substring = str.substring(start , end);
            subprocess.add(substring);
        }
        return subprocess;
    }

    

     public static ArrayList<String> subprocessMeta(String str){
        /*first finding out all subprocess indexes and then one by one finding all start and end events */

        // System.out.println("string received = "+str);
        
        ArrayList<Integer> startIndex = getListIndex("<bpmn:subProcess", str) ;

        ArrayList<Integer> endIndex = getListIndex("</bpmn:subProcess>", str) ;
    
        /*parsing the subprocess from complete model all at once */
        ArrayList<String> subprocess = metaData(startIndex, endIndex , str);

        subprocessEvents = new HashMap<>();

        for(int i = 0 ;i < subprocess.size();i++){
            String ele = subprocess.get(i);
            String id = printId(ele,"id=", "\"");
            String name = printId(ele , "name=" , "\"");
            // System.out.println("ele is  = "+ele);
            /*removing startEvents indexes and the complete block pointed by the given indexes but we need their
             * id's for that we will remove using printId method and for that we need to call it
             * recursively or iteratively
            */
            ArrayList<Integer> startEle = getListIndex("<bpmn:startEvent", ele);
            ArrayList<Integer> endEle = getListIndex("</bpmn:startEvent>" , ele);
            ArrayList<String> startmetaIdStrings = metaData(startEle , endEle , ele);

            ArrayList<String> startIds = new ArrayList<>();

            for(String val : startmetaIdStrings){
                startIds.add( printId(val, "id=", "\"")) ;
            }


            /*removing endEvents indexes */
            startEle = getListIndex("<bpmn:endEvent", ele);
            endEle = getListIndex("</bpmn:endEvent>" , ele);
            ArrayList<String> endmetaIdStrings = metaData(startEle , endEle , ele);

            ArrayList<String> endIds = new ArrayList<>();

            for(String val: endmetaIdStrings){
                endIds.add( printId(val, "id=", "\""));
            }
            
            /*
             * adding this nodes in map so that the else if block in createGraph will have this as values
             */
            Graph ob = new Graph();
            for(int j = 0;j < startIds.size();j++){
                ob.setIncomingNode(startIds.get(j));
                ob.setIncomingEdges(1);
            }
            for(int j = 0;j < endIds.size();j++){
                ob.setOutgoingNodes(endIds.get(j));
                ob.setOutgoingEdges(1);
            }
            // SubEvents obj = new SubEvents(startIds, endIds, name);
            // subprocessEvents.put(id , obj);
            map.put(id , ob); 
            
        }

        return subprocess;

     }

     public static void printlist(ArrayList<String> list){
        for(int i = 0;i < list.size();i++){
            String ele = list.get(i);
            System.out.println(ele);
        }
        endl();
        return;
     }

     public static ArrayList<Integer> getListIndex(String pattern , String str){
        ArrayList<Integer> startIndex = new ArrayList<>();
        Matcher matcher = Pattern.compile(pattern).matcher(str);
        while (matcher.find()) {
            startIndex.add(matcher.start());
        }
        return startIndex;
     }

    /*

    NOTE:- this function will be written for single subprocess, but with 
    a process with multiple subprocess then we need to make individual call to this 
    function as this is sinle working string  function

     * writing two function two find start and end of subprocess
     * as the link are not directly to elements to subprocess as whole
     * thus two methods are needed so that we can find start as well as the
     * end of subprocess
     * so when we encounter subprocess in  targetref then we need to replace it
     * with the start events of subprovess or if we find subprocess in sourceref
     * then we will replace it with the end event of subprovess
     * NOTE:- we need to make sure that the links are established properly aas the start and end 
     * events can be various thus using it as list will be viable option
     */



    // public static SubEvents findStartEventSubprocess(String str){

    // }

    public static  int getIndex(String substr , int startIndex, String value){
        int len = substr.length();
        for(int i = startIndex;i + len <= value.length(); i++){
            if(value.substring(i, i+len).equals(substr))return i;
        }
        return -1;
    } 

    public static void printGraph(){
        endl();
        endl();
        System.out.println("Printing Elements extracted from bpmn file printgraph() ");
        for(Map.Entry<String , Graph> ele : map.entrySet()){
            String key = ele.getKey() ;
            Graph value = ele.getValue();
            Utility.color("red");
            System.out.println(key);
            Utility.color("reset");
            System.out.println("name = "+value.name);
            System.out.println("kind of element = "+value.getKind());
            System.out.println("Time of this element = "+value.getTime());
            System.out.println("incoming Nodes = "+value.getIncomingNodes());
            System.out.println("incoming Edges = "+value.getIncomingEdges());
            System.out.println("outgoing Node = "+value.getOutgoingNodes());
            System.out.println("outgoing Edges = "+value.getOutgoingEdges());
            System.out.println("outgoing Edges Names = "+value.getOutgoingEdgesName());
            System.out.println("Sizes of list are equal  = "+(value.getOutgoingEdgesName().size() == value.getOutgoingNodes().size() && value.getOutgoingNodes().size() == value.getOutgoingEdges().size()));
            System.out.println("values of list are  = "+(value.getOutgoingEdgesName().size() +"  "+ value.getOutgoingNodes().size()+" "+ value.getOutgoingEdges().size()));
            System.out.println("Petri Net style = "+value.getPetriNetStyle());
            System.out.println("type of task = "+value.getTaskType());
            System.out.println("cardinality of task = "+value.getCardinality());
            System.out.println("is this timer event = "+(value.getIsTimerEvent() ? "YES" : "NO"));
            endl();

        }
    }

    public static void print(Graph node){
        System.out.println("name = "+node.name);
        System.out.println("incoming node = "+node.getIncomingNodes());
        System.out.println("outgoing node = "+node.getOutgoingNodes());
    }

    public static void createGraph(String[][] arr , HashMap<String,Graph> map){

        // System.out.println("Printing Map \u001B[33m"+map+"\u001B[0m ");
        // out.printLine("create graph");
        for(int i = 0;i < arr.length;i++){
            String prop = arr[i][0] , values = arr[i][1] , type = arr[i][2];
            // out.printLine("\u001b[32m");
            // out.printLine(prop+" "+values+" "+type+" "+map.containsKey(values));
            // out.printLine("\u001b[0m");
            if(prop.contains("id") && arr[2][1].length() == 0){
                map.put(values , new Graph());
                map.get(values).setPetriNetStyle(Integer.parseInt(type));
                map.get(values).setKind(arr[arr.length-1][0]);
            }
            else if(prop.contains("name") && map.containsKey(arr[0][1])){
                if(values.length() == 0)
                    (map.get(arr[0][1])).setName(arr[0][1]);
                else
                    (map.get(arr[0][1])).setName(values);
            }
            //setting outgoing nodes for each element
            else if(prop.contains("sourceRef") && values.length() > 0 && map.containsKey(values)){
                if(arr[i+1][1].contains("Participant")){
                    Graph ob = new Graph();
                    ob.setName("participant"+participants);
                    ob.setIncomingNode(values);
                    ob.setIncomingEdges(-1);//it is message flow type
                    ob.setPetriNetStyle(1);//it is transition
                    // map.remove(arr[0][1]);
                    // break;
                    map.get(values).setOutgoingNodes("participant"+participants);
                    map.get(values).setOutgoingEdges(-1);
                    map.get(values).setOutgoingEdgesName(arr[i-1][1]);
                    if(!map.containsKey("participant"+participants))participantOccurence.add("participant"+participants);
                    map.put("participant"+participants , ob);
                    
                    participants++;
                }
                else if(values.contains("SubProcess")){
                    // out.printLine("\u001b[32m"); //green
                    // out.printLine("Subprocess is sourceRef"+map.get(values).name);
                    ArrayList<String> outcomingNodes = map.get(values).getOutgoingNodes();
                    ArrayList<Integer> outcomingEdges = map.get(values).getOutgoingEdges();
                    ArrayList<String> outcomingEdgesNames = map.get(values).getOutgoingEdgesName();
                    int ptr = 0;
                    for(String str : outcomingNodes){
                        map.get(str).setOutgoingNodes(arr[i+1][1]);
                        map.get(str).setOutgoingEdges(outcomingEdges.get(ptr));
                        /*setting incoming nodes and edges in both nodes to avoid reprocessing in main function which 
                         * removes unnecessary Place to place or Transition to transition errors
                         */
                        if(ptr < outcomingEdgesNames.size())
                        map.get(str).setOutgoingEdgesName(outcomingEdgesNames.get(ptr));
                        else 
                        map.get(str).setOutgoingEdgesName("");
                        ptr++;
                        map.get(arr[i+1][1]).setIncomingNode(str);
                        map.get(arr[i+1][1]).setIncomingEdges(1);
                    }
                    // print(map.get(values));
                    // out.printLine("\u001b[0m ");
                }
                else {
                 map.get(values).setOutgoingNodes(arr[i+1][1]);
                 /** adding names of edges */
                 map.get(values).setOutgoingEdgesName(arr[i-1][1]);
                }

                if(arr[0][1].contains("MessageFlow")){
                    map.get(values).setOutgoingEdges(-1);
                    // map.get(values).setOutgoingEdgesName("");
                    // map.get(values).setMessageFlow();
                }
                else{
                    map.get(values).setOutgoingEdges(1);
                    // map.get(values).setOutgoingEdgesName("");
                } 
                // endl();
                // System.out.println("new node "+values);
                // System.out.println("\u001B[31m"); //RED
                // System.out.println("print function executed");
                // print(map.get(values));
                // System.out.println("\u001B[0m ");

                // endl();
            }
            //setting incoming nodes for each element
            else if(prop.contains("targetRef") && values.length() > 0 && map.containsKey(values)){
                if(arr[i-1][1].contains("Participant")){
                    Graph ob = new Graph();
                    ob.setName("participant"+participants);
                    ob.setOutgoingNodes(values);
                    ob.setOutgoingEdges(-1);//it is message flow type
                    map.get(values).setOutgoingEdgesName(arr[i-2][1]);
                    ob.setPetriNetStyle(1);//it is transition
                    // map.remove(arr[0][1]);
                    // break;
                    map.get(values).setIncomingNode("participant"+participants);
                    map.get(values).setIncomingEdges(-1);
                    if(!map.containsKey("participant"+participants))participantOccurence.add("participant"+participants);
                    map.put("participant"+participants , ob);
                    participants++;
                    
                }
                else if(values.trim().contains("SubProcess")){
                    // System.out.println("\u001b[32m"); //green
                    System.out.println("Subprocess is targetRef"+map.get(values).name);
                    ArrayList<String> incomingNodes= map.get(values).getIncomingNodes();
                    ArrayList<Integer> incomingEdges = map.get(values).getIncomingEdges();
                    ArrayList<String> outgoingEdgesName = map.get(values).getOutgoingEdgesName();
                    int ptr = 0;
                    for(String str : incomingNodes){
                        map.get(str).setIncomingNode(arr[i-1][1]);

                        map.get(str).setIncomingEdges(incomingEdges.get(ptr++));
                        
                        map.get(arr[i-1][1]).setOutgoingNodes(str);
                        map.get(arr[i-1][1]).setOutgoingEdges(1);
                        map.get(arr[i-1][1]).setOutgoingEdgesName("");
                    }
                    print(map.get(values));
                    System.out.println("\u001b[0m ");
                }
                else
                    map.get(values).setIncomingNode(arr[i-1][1]);

                if(arr[0][1].contains("MessageFlow")){
                    map.get(values).setIncomingEdges(-1);
                    map.get(values).setOutgoingEdgesName("");
                    // map.get(values).setMessageFlow();
                }
                else map.get(values).setIncomingEdges(1);
                // endl();
                // System.out.println("new node "+values);
                // print(map.get(values));
                // endl();
            }
            else if(map.containsKey(arr[0][1]) && prop.contains("loop")){
                String[] loop = values.split(" ");
                int typeOfLoop  = Integer.parseInt(loop[0]) , cardinality = Integer.parseInt(loop[1]);
                map.get(arr[0][1]).setTaskType(typeOfLoop);
                map.get(arr[0][1]).setCardinality(cardinality);

            }
            else if(map.containsKey(arr[0][1]) && prop.contains("timerEventDefinition")){
                map.get(arr[0][1]).setIsTimerEvent(values.toLowerCase().equalsIgnoreCase("yes") ? true : false);

            }
            // endl();

        }
        // endl();
        // endl();
        // out.printLine("event completed");

        // endl();
        // endl();
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
        idIndex +=  (startPattern).length() + 1;
        StringBuilder sb = new StringBuilder("");
        while( !(value.charAt(idIndex)+"").equals(endPattern) ){
            char ch = value.charAt(idIndex);
            sb.append(ch);
            idIndex++;
        }
        return sb.toString();
    }

    public static String getParameter(String value , String startPattern , String endPattern , int start){
        int idIndex = start ;
        if(idIndex == -1)return "";
        idIndex +=  (startPattern).length() + 1;
        StringBuilder sb = new StringBuilder("");
        while( !(value.charAt(idIndex)+"").equals(endPattern) ){
            char ch = value.charAt(idIndex);
            sb.append(ch);
            idIndex++;
        }
        return sb.toString();
    }

    public static void printMap(HashMap<String , SubEvents> map){
        for(Map.Entry<String , SubEvents> ele : map.entrySet()){
            String key = ele.getKey() ;
            SubEvents val = ele.getValue();

            System.out.println("ele is = "+key);
            printlist(val.start);
            printlist(val.end);
            System.out.println("name of this ele is = "+val.name);

            endl();

        }
    }

    public static void printParticipantMap(HashMap<String , ParticipantData> map){
        for(Map.Entry<String , ParticipantData> ele : map.entrySet()){
            String key = ele.getKey() ;
            ParticipantData val = ele.getValue();

            System.out.println("ele is = "+key);
            System.out.println("name of this ele is = "+val.getName());
            System.out.println("process ref is " + val.getProcessRef());

            endl();

        }
    }

    

    /*
     * Note: for multiinstance activity we can create loop (to same transition)
     * or can add epsilon transition or place 
     */

 }

