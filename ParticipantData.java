public class ParticipantData{
    String id, name , processRef;
    ParticipantData(){
        
    }
    ParticipantData(String id ,String name , String processRef){
        this.id = id;
        this.name = name;
        this.processRef = processRef;
    }

    public void setId(String str){
        this.id = str;
    }

    public String getId(String str){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setProcessRef(String ref){
        this.processRef = ref;
    }

    public String getProcessRef(){
        return processRef;
    }

}