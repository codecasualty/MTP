import java.util.*;

public class NodeModification{
    //these are variables for parallel multinstance tasks
    int countPlace , countTransition , countDiverging , countMerging;
    //these are variables for timerevents
    int countTimerPlace , countTimerTransition;
    //these variable are for sequential multinstance
    int countSequentialPlace;
    //these variable are for loop task
    int countLoopTask;


    public void edgePreservation(HashMap<String ,  PetriNetNode> petrinetmap ,  ArrayList<String> nodes){
        for(String str: nodes){
            String name = str;
            PetriNetNode node = petrinetmap.get(name);
            if(node.getTaskType() == 1 || node.getTaskType() == 3){
                sequentialMultinstanceOrLoop(petrinetmap , str);
            }
            else if(node.getTaskType() == 2){
                parallelMultinstance(petrinetmap ,str);
            }
            else 
                timerEvents(petrinetmap , str);
        }   
    }

    public void timerEvents(HashMap<String ,  PetriNetNode> petrinetmap ,  String str){
        
        /**
         * merging -> a -> epsilon place(etp) -> b -> diverging
         * epsilong place -> c -> merging to have determinism
         */

        // for(String str : nodes)
        {
            String name = petrinetmap.get(str).getName();
            PetriNetNode node = petrinetmap.get(str);
            ArrayList<String> incomingNodes = node.getIncomingNodes() , outgoingNodes  = node.getOutgoingNodes();
            ArrayList<Integer> typeOfIncomingEdges = node.getIncomingEdges() , typeOfOutgoingEdges = node.getOutgoingEdges();


            PetriNetNode merging = createEPlace(petrinetmap, "etm"+(countTimerPlace++));
            for(int i = 0;i < incomingNodes.size();i++){
                String incoming = incomingNodes.get(i);
                /**there is chance that the incoming node is place in that we need to add a epsilon transition */
                if(petrinetmap.get(incoming).getisEPlace() || petrinetmap.get(incoming).getisPlace()){
                    PetriNetNode extraTransition  =  createETransition(petrinetmap, "ett"+(countTimerTransition++));
                    petrinetmap.get(incoming).setOutgoingNodes(extraTransition.getName());
                    petrinetmap.get(incoming).setIncomingEdges(1);

                    petrinetmap.get(extraTransition.getName()).setOutgoingNodes(merging.getName());
                    petrinetmap.get(extraTransition.getName()).setOutgoingEdges(1);

                    petrinetmap.get(extraTransition.getName()).setIncomingNode(incoming);
                    petrinetmap.get(extraTransition.getName()).setIncomingEdges(1);

                    petrinetmap.get(merging.getName()).setIncomingNode(extraTransition.getName());
                    petrinetmap.get(merging.getName()).setIncomingEdges(1);



                    
                }
                else{
                    merging.setIncomingNode(incoming);
                    merging.setIncomingEdges(typeOfIncomingEdges.get(i));
                }
                
                /*we will write different code for deleting timerevent from incoming and outgoing nodes */
            }

            /**
             * timers have ending node as place which is called as diverging place now the next node in sequence after timer black box can be
             * 1)a transition in that case we set the outgoing node of diverging place to the transition node
             * 2)else we do not create a diverging place and use the last transition i.e. 'b' and set its outgoing nodes to the give place node
             */
            PetriNetNode a = createETransition(petrinetmap, "ett"+(countTimerTransition++));//epsilon timer place
            PetriNetNode b = createETransition(petrinetmap, "ett"+(countTimerTransition++));//epsilon timer place
            PetriNetNode c = createETransition(petrinetmap, "ett"+(countTimerTransition++));//epsilon timer place
            PetriNetNode etp = createEPlace(petrinetmap, "etp"+(countTimerPlace++));

            setEdges(merging, a);
            setEdges(a, etp);
            setEdges(etp, b);
            // setEdges(b, diverging);
            setEdges(etp,c);
            setEdges(c, merging);

            boolean isPlaceStart = false;

            
            for(int i = 0;i < outgoingNodes.size();i++){
                String outgoing = outgoingNodes.get(i);
                if(petrinetmap.get(outgoing).getisEPlace() || petrinetmap.get(outgoing).getisPlace()){
                    // for(String outgoingTransition : petrinetmap.get(outgoing).getOutgoingNodes()){
                    //     diverging.setOutgoingNodes(outgoingTransition);
                    // }
                    // for(int outgoingTransition : petrinetmap.get(outgoing).getOutgoingEdges()){
                    //     diverging.setOutgoingEdges(outgoingTransition);
                    // }
                    // for(String outgoingTransition : petrinetmap.get(outgoing).getOutgoingEdgesName()){
                    //     diverging.setOutgoingEdgesName(outgoingTransition);
                    // }
                    isPlaceStart = true;
                }
                // else{
                    // diverging.setOutgoingNodes(outgoing);
                    // diverging.setOutgoingEdges(typeOfOutgoingEdges.get(i));
                // }
                
            }
            String lastNode = "";
            if(isPlaceStart){
                for(int i = 0;i < outgoingNodes.size();i++){
                    String outgoing = outgoingNodes.get(i);
                    b.setOutgoingNodes(outgoing);
                    b.setOutgoingEdges(1);
                    
                }
                lastNode = b.getName();
            }
            else{
                PetriNetNode diverging =  createEPlace(petrinetmap, "etd"+(countTimerPlace++));
                for(int i = 0;i < outgoingNodes.size();i++){
                    String outgoing = outgoingNodes.get(i);
                    diverging.setOutgoingNodes(outgoing);
                    diverging.setOutgoingEdges(typeOfOutgoingEdges.get(i));
                    
                }
                lastNode = diverging.getName();
                setEdges(b, diverging);
            }
            

            removeIncomingNodesReference(petrinetmap, str, merging.getName(), incomingNodes);
            removeOutgoingNodesReference(petrinetmap, str, lastNode, outgoingNodes);

            petrinetmap.remove(str);

            

        }

    }

    public void sequentialMultinstanceOrLoop(HashMap<String , PetriNetNode> petrinetmap , String str){

        /**
         * for nodes which require transformation we look at the cardinality(this may be required to store the number of tokens in the place )
         *      for the same node we add the outgoing node to the previous transition and on eextra place which stores the cardinality of the task
         *      the same can be done for the loops as it the control flow not the data flow.
         */


        // for(String str : nodes)
        {
            String key = str;
            PetriNetNode transition = petrinetmap.get(key);

            /** 
             * all incoming nodes will the place so now we add the outgoing nodes from this transiton to incoming place and also add one extra place for cardinality
             * 
             */

            ArrayList<String> incomingPlaces = transition.getIncomingNodes();
            ArrayList<Integer> incomingEdges = transition.getIncomingEdges();

            for(int i = 0;i < incomingPlaces.size();i++){
                transition.setOutgoingNodes(incomingPlaces.get(i));
                transition.setOutgoingEdges(incomingEdges.get(i));
            }

            /**
             * now we add one extra epsilon place denoting cardinality or the conditions 
             * for this we can use the variable task type by using task type we will for sure that it is sequential or loop
             */

            String type = (transition.getTaskType() == 1 ? "loop condition" : "Cardinality"+(transition.getCardinality()));
            PetriNetNode place = createEPlace(petrinetmap,  type+(countSequentialPlace++) );
            // setEdges(place, transition);
            place.setOutgoingNodes(key);
            place.setOutgoingEdges(1);
            transition.setIncomingNode(place.getName());
            transition.setIncomingEdges(1);
            

        }


    }    

    public void parallelMultinstance(HashMap<String , PetriNetNode> petrinetmap , String str){
        /**
         * we will be applying transformation for each node which is of type parallel multiinstance
         * for each node  
         *  we look at its cardinatlity 
         *  for each cardinality we attach a sequence of place->transition->place , as outgoing node to transition tpd (transition for parallel diversion)
         *      ans as incoming node to transition to tpm(transition for parallel merging)
         *  finally for the current node we know its incoming and outgoing edges 
         *  using above data we change the entry of current node to this one and remove this as key from petrinetmap
         * but while deleting this entry we need to make sure that all the node which were incoming to this have new entry as incoming nodes
         * and same for outgoing nodes
         */
        // for(String str: nodes)
        {
            String name = petrinetmap.get(str).getName();
            PetriNetNode node = petrinetmap.get(str);
            int cardinality = node.getCardinality();
            ArrayList<String> incomingNodes = node.getIncomingNodes() , outgoingNodes  = node.getOutgoingNodes();
            ArrayList<Integer> typeOfIncomingEdges = node.getIncomingEdges() , typeOfOutgoingEdges = node.getOutgoingEdges();
            /*
             * we create a diverging transition and a merging transition and add sequence generated to it
             */
            PetriNetNode diverging = new PetriNetNode();
            diverging.setisETransition();
            diverging.setTaskType(0);//normal
            diverging.setCardinality(1);
            diverging.setIsSpecialNode();;
            diverging.setName("e_pmi_d"+countDiverging);
            countDiverging++;

            PetriNetNode merging = new PetriNetNode();
            merging.setisETransition();
            merging.setCardinality(1);
            merging.setTaskType(0);
            merging.setIsSpecialNode();
            merging.setName("e_pmi_m"+countMerging);
            countMerging++;

            petrinetmap.put(diverging.getName() , diverging);
            petrinetmap.put(merging.getName() , merging);

            // Adds sequences to petrinetmap and sets the edge weights of the diverging and merging
            for(int i = 0 ;i < cardinality;i++){
                ArrayList<String> endNodes = addSequence(petrinetmap, "epmi_p", name);
                String left = endNodes.get(0) , right = endNodes.get(1);
                diverging.setOutgoingNodes(left);
                diverging.setOutgoingEdges(1);
                merging.setIncomingNode(right);
                merging.setIncomingEdges(1);

                PetriNetNode leftNode = petrinetmap.get(left) , rightNode = petrinetmap.get(right);
                // setEdges(leftNode, rightNode);
                leftNode.setIncomingNode(diverging.getName());
                leftNode.setIncomingEdges(1);
                rightNode.setOutgoingNodes(merging.getName());
                rightNode.setOutgoingEdges(1);


            }

            for(int i = 0;i < incomingNodes.size();i++){
                diverging.setIncomingNode(incomingNodes.get(i));
                diverging.setIncomingEdges(typeOfIncomingEdges.get(i));
            }

            for(int i = 0;i < outgoingNodes.size();i++){
                merging.setOutgoingNodes(outgoingNodes.get(i));
                merging.setOutgoingEdges(typeOfOutgoingEdges.get(i));
            }

            /**
             * Now for all nodes for which this was incoming Node we need to replace it with merging transition
             * and for all nodes for which this was outgoing node we need to replace it with diverging transition
             */

            // removeReferenceNodes(petrinetmap, incomingNodes, str, diverging.getName());
            // removeReferenceNodes(petrinetmap, outgoingNodes, str, merging.getName()); 

            removeIncomingNodesReference(petrinetmap, str, diverging.getName(), incomingNodes);
            removeOutgoingNodesReference(petrinetmap, str, merging.getName(), outgoingNodes);

            // for(String incomingNode: incomingNodes){
            //     ArrayList<String> outgoingNode  = petrinetmap.get(incomingNode).getOutgoingNodes();
            //     ArrayList<Integer> outgoingEdge  = petrinetmap.get(incomingNode).getOutgoingEdges();
            //     int index = removeEleList(outgoingNode , str , diverging.getName());
            //     int val = outgoingEdge.get(index);
            //     outgoingEdge.remove(index);
            //     outgoingEdge.add(val);
            // }
            
            // for(String outgoingNode : outgoingNodes){
            //     ArrayList<String> incomingNode  = petrinetmap.get(outgoingNode).getIncomingNodes();
            //     ArrayList<Integer> incomingEdge  = petrinetmap.get(outgoingNode).getIncomingEdges();
            //     int index = removeEleList(incomingNode , str , merging.getName());
            //     int val = incomingEdge.get(index);
            //     incomingEdge.remove(index);
            //     incomingEdge.add(val);
            // }

            petrinetmap.remove(str);
        }   

        
        return;
    }
    
    public void setEdges(PetriNetNode left , PetriNetNode right){
        left.setOutgoingNodes(right.getName());
        left.setOutgoingEdges(1);
        right.setIncomingNode(left.getName());
        right.setIncomingEdges(1);
    }

    public PetriNetNode createETransition (HashMap<String ,  PetriNetNode> petrinetmap , String name){
        PetriNetNode node = new PetriNetNode();
        node.setisETransition();
        node.setTaskType(0);
        node.setName(name);
        node.setIsSpecialNode();
        petrinetmap.put(name , node);
        return node;
    }

    public PetriNetNode createEPlace(HashMap<String, PetriNetNode> petrinetmap , String name){
        PetriNetNode node = new PetriNetNode();
        node.setisEPlace();
        node.setName(name);
        node.setIsSpecialNode();
        petrinetmap.put(name , node);
        return node;
    }

    public void removeIncomingNodesReference(HashMap<String,PetriNetNode> petrinetmap , String oldKey , String newKey , ArrayList<String> list){
        for(String incomingNode: list){
            ArrayList<String> outgoingNode  = petrinetmap.get(incomingNode).getOutgoingNodes();
            ArrayList<Integer> outgoingEdge  = petrinetmap.get(incomingNode).getOutgoingEdges();
            int index = removeEleList(outgoingNode , oldKey , newKey);
            if(index < 0)continue;
            int val = outgoingEdge.get(index);
            outgoingEdge.remove(index);
            outgoingEdge.add(val);
        }
    }

    public void removeOutgoingNodesReference(HashMap<String,PetriNetNode> petrinetmap , String oldKey , String newKey , ArrayList<String> list){
        for(String outgoingNode : list){
            ArrayList<String> incomingNode  = petrinetmap.get(outgoingNode).getIncomingNodes();
            ArrayList<Integer> incomingEdge  = petrinetmap.get(outgoingNode).getIncomingEdges();
            int index = removeEleList(incomingNode , oldKey , newKey);
            if(index < 0)continue;
            int val = incomingEdge.get(index);
            incomingEdge.remove(index);
            incomingEdge.add(val);
        }
        
    }


    public  int removeEleList(ArrayList<String> list , String pattern , String key){
        int reqIndex = -1;
        for(int i = 0;i < list.size();i++){
            String str = list.get(i);
            if(str.equals(pattern)){
                list.remove(i);
                reqIndex = i;
                list.add(key);
                break;
            }
        }
        return reqIndex;
    }

    public  ArrayList<String> addSequence(HashMap<String , PetriNetNode> petrinetmap , String startStringPlace , String startStringTransition){
        PetriNetNode leftNode = new  PetriNetNode();
        leftNode.setisEPlace();
        leftNode.setIsSpecialNode();
        leftNode.setName(startStringPlace+""+countPlace);
        countPlace++;
        PetriNetNode transition = new PetriNetNode();
        transition.setisETransition();
        transition.setIsSpecialNode();
        transition.setTaskType(0);//normal
        transition.setCardinality(1);
        transition.setName(startStringTransition+""+countTransition);
        countTransition++;
        PetriNetNode rightNode = new  PetriNetNode();
        rightNode.setisEPlace();
        rightNode.setIsSpecialNode();
        rightNode.setName(startStringPlace+""+countPlace);
        countPlace++;

        setEdges(leftNode, transition);
        setEdges(transition, rightNode);

        // leftNode.setOutgoingNodes(transition.getName());
        // leftNode.setOutgoingEdges(1);
        // transition.setIncomingNode(leftNode.getName());
        // transition.setIncomingEdges(1);

        // transition.setOutgoingNodes(rightNode.getName());
        // transition.setOutgoingEdges(1);
        // rightNode.setIncomingNode(transition.getName());
        // rightNode.setIncomingEdges(1);

        petrinetmap.put(leftNode.getName() , leftNode);
        petrinetmap.put(rightNode.getName() , rightNode);
        petrinetmap.put(transition.getName(), transition);
        ArrayList<String> list = new ArrayList<>();
        list.add(leftNode.getName());
        list.add(rightNode.getName());
        return list;
    }

    public void edgeBalancing(HashMap<String , HashSet<String>> participantWiseOrdering , HashMap<String , PetriNetNode> PetriNetmap){
       /**
        * for each transition we keep track of incoming places
        * if the incoming places are from same participant they were just behind the current transition
        * whereas if they are not in same participant then it means they are in different participant and now
        * we remove all incoming transition which were referring to this place and point it to a place which is in
        * same process as the current transition
        */
        ArrayList<String> nodesRemove = new ArrayList<>();
        for(Map.Entry<String, PetriNetNode> ele : PetriNetmap.entrySet()){
            String key = ele.getKey();
            PetriNetNode node = ele.getValue();
            if(node.getisEPlace() || node.getisPlace())continue;
            // System.out.println("current transition is "+key+" "+node.getName());
            ArrayList<String> incomingPlaceCurrentTransition = node.getIncomingNodes();
            ArrayList<Integer> incomingEdgesCurrentTransition = node.getIncomingEdges();
            
            String participantId = getParticipantId(key, participantWiseOrdering);
            if(participantId.length() <= 0)continue;
            HashSet<String> elementsInParticipant = participantWiseOrdering.get(participantId);

            String mappingPlace = getMappingPlace(incomingPlaceCurrentTransition , elementsInParticipant);
            // print(incomingEdgesCurrentTransition);
            if(mappingPlace.length() <= 0)continue;

            for(int i = 0;i < incomingEdgesCurrentTransition.size();i++){
                String temp = incomingPlaceCurrentTransition.get(i);
                if(temp.contains("<>"))temp = (temp.split("<>"))[1];
                // Utility.color("blue");
                // System.out.println("running place is "+temp);
                // Utility.color("reset");
                if(elementsInParticipant.contains(temp) || PetriNetmap.get(incomingPlaceCurrentTransition.get(i)).getIsSpecialNode() )continue;
                String runningPlaceId = incomingPlaceCurrentTransition.get(i);
                removeIncomingNodesReference(PetriNetmap, runningPlaceId, mappingPlace, PetriNetmap.get(runningPlaceId).getIncomingNodes());
                // PetriNetmap.remove(runningPlaceId);
                nodesRemove.add(runningPlaceId);
            }


        }

        for(String str: nodesRemove)
                PetriNetmap.remove(str);


    }

    public String getMappingPlace(ArrayList<String> list , HashSet<String> set){
        // Utility.color("red");
        // System.out.println(list);
        // System.out.println(set);
        // Utility.color("reset");
        if(set.isEmpty() || set == null)return "";
        for(String str: list){
            if(set.contains(str))return str;
        }
        return "";
    }

    public String getParticipantId(String str , HashMap<String , HashSet<String>> participantWiseOrdering){
        for(Map.Entry<String , HashSet<String>> ele : participantWiseOrdering.entrySet()){
            String processId = ele.getKey();
            HashSet<String> keys = ele.getValue();
            if(keys.contains(str))return processId;
        }
        return "";
    }

    public void print(ArrayList<Integer> list){
        for(int ele : list){
            System.out.print(ele+" ");
        }
        System.out.println();
    }

}