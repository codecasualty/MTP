import java.util.*;

public class PetriNetNode{
    private ArrayList<String> incomingNodes ;
    private ArrayList<String> outgoingNodes ;
    private ArrayList<String> outgoingEdgesNames ;    
    private ArrayList<Integer> incomingEdges;
    private ArrayList<Integer> outgoingEdges;
    private String name , id;
    private boolean isPlace, isTransition , isEPlace , isETransition , isMessageFlow;
    private int taskType , cardinality;
    private boolean isTimer , isSpecialNode;
    PetriNetNode(){
        incomingNodes = new ArrayList<>();
        outgoingNodes = new ArrayList<>();
        incomingEdges = new ArrayList<>();
        outgoingEdges = new ArrayList<>();
        outgoingEdgesNames = new ArrayList<>();
        name = "";
        isPlace = isEPlace = isTransition = isETransition = isMessageFlow= isTimer = false;
        taskType = -1;
        cardinality = 0;
        isSpecialNode = false;
    }


    public ArrayList<String> getOutgoingEdgesName(){
        return outgoingEdgesNames;
    }

    public void setOutgoingEdgesName(String str){
        this.outgoingEdgesNames.add(str);
    }

    /**
    * Returns the list of incoming nodes. This is used to determine if we are talking to a node, all previous nodes
    */
    public ArrayList<String> getIncomingNodes(){
        return incomingNodes;
    }

    /**
    * Returns the outgoing nodes. This is a list of node IDs that are reachable from this node in the order they were added
    */
    public ArrayList<String> getOutgoingNodes(){
        return outgoingNodes;
    }

    /**
    * Sets the incoming node. This is used to determine if a node is the same as another node or not
    * 
    * @param incoming - The name of the node
    */
    public void setIncomingNode(String incoming){
        this.incomingNodes.add(incoming);
    }

    /**
    * Sets the outgoing nodes. This is used to determine if a node is in the graph or not.
    * 
    * @param outgoing - The outgoing node to add to the graph 
    */
    public void setOutgoingNodes(String outgoing){
        this.outgoingNodes.add(outgoing);
    }

    // adding list for edges direct 1 and messageflow -1
    /**
    * Returns the incoming edges of this edge - weighted digraph. This is a copy of getIncomingEdges but stores integer values so that we can avoid message and normal transition
    */
    public ArrayList<Integer> getIncomingEdges(){
        return incomingEdges;
    }

    /**
    * Returns the outgoing edges of this edge - weighted digraph. This is a copy of the outgoingEdges array but stores integer values so that we can avoid message and normal transition
    */
    public ArrayList<Integer> getOutgoingEdges(){
        return outgoingEdges;
    }

    /**
    * Sets the number of incoming edges. This is used to determine whether or not a node is connected to another node by an edge in the graph.
    * 
    * @param incoming - The number of incoming edges to be connected to the
    */
    public void setIncomingEdges(int incoming){
        this.incomingEdges.add(incoming);
    }

    /**
    * Sets the number of outgoing edges. This is used to determine whether or not a node is connected to another node by an edge in the graph.
    * 
    * @param outgoing - the number of outgoing edges to be connected to this
    */
    public void setOutgoingEdges(int outgoing){
        this.outgoingEdges.add(outgoing);
    }

    /**
    * Set the name of the entity. This is used to distinguish entities from other entities that are in the same entity group
    * 
    * @param n - The name of the
    */
    public void setName(String n){
        this.name = n;
    }

    /**
    * Returns the name of this entity. This is the entity's name in the form of a java. lang. String object.
    * 
    * 
    * @return the entity's name in the form of a java. lang. String object or null if there is no
    */
    public String getName(){
        return name;
    }

    /**
    * Sets isPlace to true. This is used so that a node is identified and ambiguity about transiton and transition is removed.
    */
    public void setisPlace(){
        this.isPlace =  true;
    }

    /**
    * Returns true if this is a place. This is used to determine if the user is placing a place
    */
    public boolean getisPlace(){
        return isPlace;
    }

    /**
    * Sets isEPlace to true. This is used to ensure that the place is Eplace so we dont  add duplicate place and avoid transition to transition edges.
    */
    public void setisEPlace(){
        this.isEPlace = true;
    }
    
    /**
    * Returns true if e - place is used false otherwise. This is used to determine if we are using eplace as a place
    */
    public boolean getisEPlace(){
        return isEPlace;
    }
    
    /**
    * Sets the isTransition flag to true. This is used to prevent an infinite loop in transition processing and the effect of a call to #getTransition ()
    */
    public void setisTransition(){
        this.isTransition = true;
    }

    /**
    * Returns true if this node is a transition node. This is used to determine if the state of an edge should be updated
    */
    public boolean getisTransition(){
        return isTransition;
    }

    /**
    * Sets the isETransition flag to true. This is used by transition methods to determine if the transition is an epsilon transition
    */
    public void setisETransition(){
        this.isETransition = true;
    }

    /**
    * Returns true if this is an e transition. This is used to determine if the transition is a divergent
    */
    public boolean getisETransition(){
        return isETransition;
    }

    /**
    * Sets message flow to true. This is used to set message values (basically for arcs)
    */
    public void setMessageFlow(){
        this.isMessageFlow = true;
    }

    /**
    * Returns true if this packet is message flowing. This is used to determine if an error or an empty packet should be sent
    */
    public boolean getMessageFLow(){
        return isMessageFlow;
    }

    /*
    * task type means weather its looping task, multiinstance sequenctial or multiinstance parallel or default task
    * value assigned are as follows
    * default = 0;
    * loop =1 
    * multinstance parallel = 2 (here issequential in not present)
    * multinstance sequential = 3 ( here issequential is set to true)
    */
    public void setTaskType(int type){
        this.taskType = type;
    }

    /**
     * return task type for identification
     */
    public int getTaskType(){
        return taskType;
    }

    /**
     * Set cardinatlity of given task type by default we assume it to be 1 but for cases where node is not
     * transition itself then we take tasktype parameter into consideration.
     */
    public void setCardinality(int cardinality){
        this.cardinality = cardinality;
    }

    /**
     * return the cardinality of task
     */
    public int getCardinality(){
        return cardinality;
    }

    public void setId(String str){
        this.id = str;
    }

    public String getId(){
        return id;
    }

    public void setIsTimerEvent(boolean val){
        this.isTimer = val;
    }

    public boolean getIsTimerEvent(){
        return isTimer;
    }
    
    public boolean getIsSpecialNode(){
        return isSpecialNode;
    }

    public void setIsSpecialNode(){
        this.isSpecialNode = true;
    }
}