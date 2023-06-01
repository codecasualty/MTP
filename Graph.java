import java.util.*;
public class Graph{
    // Returns a list of nodes that are incoming to this node. The list is sorted
    ArrayList<String> incomingNodes ;
    ArrayList<String> outgoingNodes ;
    ArrayList<String> outgoingEdgesNames;
    ArrayList<Integer> incomingEdges;
    ArrayList<Integer> outgoingEdges;
    String name , id , type;
    int petriNetStyle;
    int taskType , cardinality;
    boolean isTimer , isSpecialNode;
    // This is called by the constructor to initialize the fields that are used to determine which nodes and edges are valid
    Graph(){
        incomingNodes = new ArrayList<>();
        outgoingNodes = new ArrayList<>();
        incomingEdges = new ArrayList<>();
        outgoingEdges = new ArrayList<>();
        /**
         * this list stores the names of edges , this will be used in graphviz for naming the edges (outgoing edges)
         */
        outgoingEdgesNames = new ArrayList<>();
        name = "";
        id = "";
        type = "";
        petriNetStyle = -1;
        /*
         * task type means weather its looping task, multiinstance sequenctial or multiinstance parallel or default task
         * value assigned are as follows
         * default = 0;
         * loop =1 
         * multinstance parallel = 2 (here issequential in not present)
         * multinstance sequential = 3 ( here issequential is set to true)
         */
        taskType = 0;
        cardinality = 0;

        /**
         * when dealing with timer events we need to make sure non determinism behaviour
         */
        isTimer = false;
        /**
         * this is set to true if the node is constructed for timers , multinstance parallel or sequential or loops structures
         */
        isSpecialNode = false;
    }

    public ArrayList<String> getOutgoingEdgesName(){
        return outgoingEdgesNames;
    }

    public void setOutgoingEdgesName(String str){
        this.outgoingEdgesNames.add(str);
    }

    
    /**
    * Returns the list of incoming nodes. This is used to determine if we are talking to a node, incoming nodes
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
// setting incoming edges type which can be direct 1 or message flow -1
    /**
    * Returns the incoming edges of this edge - weighted digraph. This is a copy of getIncomingEdges
    */
    public ArrayList<Integer> getIncomingEdges(){
        return incomingEdges;
    }

    /**
    * Returns the outgoing edges of this edge - weighted digraph. This is a copy of the outgoingEdges array
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
    * Sets the type of this node. This is used to determine the type of the node when it is added to the XML document
    * 
    * @param n - The name of the
    */
    public void setType(String n){
        this.name = n;
    }

    /**
    * Returns the type of this property. This is the name of the property without the namespace. For example " Object " will be converted to " Object ".
    * 
    * 
    * @return the type of this property as a java. lang. String ( not null ). Note that null is returned if this property has no type
    */
    public String getType(){
        return name;
    }

    /**
    * Sets the ID of the entity. This is used to distinguish entities that have been created by the client from those that have been deleted by the server.
    * 
    * @param Id - the ID of the entity to be created by the
    */
    public void setId(String Id){
        this.id = Id;
    }

    /**
    * Returns the id of this message. This is used to distinguish messages from other messages that have the same id.
    * 
    * 
    * @return the id of this node of graph, this way we can identify it
    */
    public String getId(){
        return id;
    }

    /**
    * Returns the petri net style of this node. 
     @return interger values 
    */
    public int getPetriNetStyle(){
        return petriNetStyle;
    }

    /**
    * Set or get the petri net style. Default is 0. See class description for more details.
    * 
    * @param val - The value to set it to ( 0 - 3)
    */
    public void setPetriNetStyle(int val){
        this.petriNetStyle = val;
    }

    /**
    * Sets the message flow to - 2. This is used to indicate that there is no petri net or basically a message or annotations
    */
    public void setMessageFlow(){
        this.petriNetStyle = -2;
    }

    /**
     * Sets the task type so that we can make changes accordingly
     */
    public void setTaskType(int type){
        this.taskType = type;
    }

    /**
     * Return tasktype so that the correct petri net can be mapped
     */
    public int getTaskType(){
        return taskType;
    }

    /**
     * set the cardinality of multiinstance activity
     */
    public void setCardinality(int val){
        this.cardinality = val;
    }

    /**
     * Returns the cardinatlity of particular instance
     */
    public int getCardinality(){
        return cardinality;
    }

    public void setIsTimerEvent(boolean val){
        this.isTimer = val;
    }

    public boolean getIsTimerEvent(){
        return isTimer;
    }


    public void setIsSpecialNode(){
        isSpecialNode = true;
    }

    public boolean getIsSpecialNode(){
        return isSpecialNode;
    }
} 
