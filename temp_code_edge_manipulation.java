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
