import java.util.*;

public class Participant{

    public HashMap<String  , ParticipantData > extractIds(String value , int startIndex , int endIndex){
        System.out.println("reached participant");
        HashMap<String  , ParticipantData > collabData = new HashMap<>();
        String startingPattern = "<bpmn:participant id=";
        String[][] properties = { {"id=" ,"\"" } , {"name=" , "\""} , {"processRef=" , "\""}};

        // System.out.println("current pattern is \u001B[31m"+pattern+" ");
        // System.out.println("starting pattern "+startingPattern+" ending pattern "+endingPattern+"\u001B[0m ");
        // System.out.println(value.substring(1100)+" ");
        startIndex = value.indexOf(startingPattern);
        while (startIndex != -1) {
            endIndex = getIndex("/>" , startIndex , value);
            String substring = value.substring(startIndex , endIndex);
            
            String[][] arr = new String[4][3];
            int index = 0;
            ParticipantData ob = new ParticipantData();
            for(String[] ele : properties){
                // System.out.print(ele[0]+"  ");
                
                String ans = getId(substring , ele[0] , ele[1]);
                arr[index][0] = ele[0].trim();
                arr[index][1] = ans.trim();
                arr[index++][2] = 1+"";
                // System.out.println(ans);
                // System.out.println();
            }
            // printValues(arr);
            // createGraph(arr, map);
            // printGraph();
            // System.out.println();
            ob.setId(arr[0][1]);
            ob.setName(arr[1][1]);
            ob.setProcessRef(arr[2][1]);
            collabData.put(arr[0][1] , ob);
            value = value.substring(endIndex+1);
            startIndex = value.indexOf(startingPattern);
        }

        return collabData;
    }

    public static  int getIndex(String substr , int startIndex, String value){
        int len = substr.length();
        for(int i = startIndex;i + len <= value.length(); i++){
            if(value.substring(i, i+len).equals(substr))return i;
        }
        return -1;
    }


    public static String getId(String value , String startPattern , String endPattern){
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

    public  void refrencingParticipant(HashMap<String , ParticipantData> map , HashMap<String , String> refMap , String refStart){
        int start = 1;
        for(Map.Entry<String , ParticipantData> ele : map.entrySet()){
            String key = ele.getKey() ;
            refMap.put(key , refStart+""+(start++));
        }
        return;
    }

    public void addNodes(HashMap<String  , Graph> map , int nodes){
        for(int i = 0;i < nodes;i++){
            map.put("participanttask"+(i+1) , new Graph());
        }
    }

    public void addLinks(HashMap<String , Graph> map , String str , int nodes , ArrayList<String> list){

        // for(int i = 0;i < nodes;i++){
        //     map.put("participant"+(i) , new Graph());
        // }

        if(nodes <= 0)return;

        for(int i = 0;i + 1 < list.size();i++){
            String prev = list.get(i) , next = list.get(i+1);
            if(i + 1 < nodes){
                Graph prevNode = map.get(prev);
                prevNode.setOutgoingNodes(next);
                prevNode.setOutgoingEdges(1);
                prevNode.setOutgoingEdgesName("");
                prevNode.setPetriNetStyle(1);//these are transitions
                Graph nextNode = map.get(next);
                nextNode.setIncomingNode(prev);
                nextNode.setIncomingEdges(1);
                nextNode.setPetriNetStyle(1);
            }
           
        }

        // map.put("startParticipant" , new Graph());
        // map.put("endParticipant" , new Graph());

        Graph startNode = new Graph();
        startNode.setOutgoingNodes("participant0");
        startNode.setOutgoingEdges(1);
        startNode.setOutgoingEdgesName("");
        startNode.setPetriNetStyle(0);
        startNode.setName("StartEvent");
        map.put("StartEvent" , startNode);

        map.get("participant0").setIncomingNode("StartEvent");
        map.get("participant0").setIncomingEdges(1);

        Graph endNode = new Graph();
        endNode.setIncomingNode("participant"+(nodes-1));
        endNode.setIncomingEdges(1);
        endNode.setPetriNetStyle(0);
        endNode.setName("EndEvent");
        map.put("EndEvent" , endNode);

        map.get("participant"+(nodes-1)).setOutgoingNodes("EndEvent");
        map.get("participant"+(nodes-1)).setOutgoingEdges(1);
        map.get("participant"+(nodes-1)).setOutgoingEdgesName("");
        
        



        return;

    }

}
