public class Query implements Comparable<Query>{
    String id;
    String surName;
    String givenName;
    String taskID;
    double score;
    Query(String id,String surName,String givenName,String taskID,double score){
        this.id = id;
        this.surName = surName;
        this.givenName = givenName;
        this.taskID = taskID;
        this.score = score;
    }

    @Override
    public int compareTo(Query o) {
        if (taskID.compareTo(o.taskID)!=0){//作业编号升序
            return taskID.compareTo(o.taskID);
        }
        else{//作业成绩降序
            if (score<o.score){
                return 1;
            }
            else if (score>o.score){
                return -1;
            }
            else{//学号升序
                return id.compareTo(o.id);
            }
        }
    }
}
