import java.util.TreeMap;

class Task {
    private String id,name,start,end,forCourse;
    private int receiveNum;
    private TreeMap<String, Double> submitted;
    Task (String id,String name,String start,String end,String forCourse){
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.forCourse = forCourse;
        this.receiveNum = 0;
        this.submitted = new TreeMap<>();
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getReceiveNum() {
        return receiveNum;
    }
    public String getStart() {
        return start;
    }
    public String getEnd() {
        return end;
    }

    public static boolean checkTaskId(String str, Course course){
        String courseIdLast4 = course.getId().substring(course.getId().length()-4);
        if (str.matches("(T)([0-9]{4})(0[1-9]|[1-9][0-9])")){
            return str.indexOf(courseIdLast4) == 1;
        }
        return false;
    }
    public static boolean checkTaskName(String str){
        int count = 0;
        String []splitterName;
        if (str.matches("[A-Za-z0-9_.]{6,16}")){
            if (str.charAt(0) == '.' || str.charAt(str.length()-1) == '.'){//首尾有'.'
                return false;
            }
            for (int i=1;i<str.length()-1;i++){//遍历字符串除首尾，记'.'个数
                if (str.charAt(i) == '.'){
                    count++;
                }
            }
            if (count!=1){//中间有多个'.'或无
                return false;
            }
            splitterName = str.split("\\.");
            return splitterName.length == 2 && !splitterName[1].contains("_");
        }
        return false;
    }
    public static boolean checkTimeFormat(String str) {//year:1900-9999
        if (str.substring(0,4).matches("(19\\d{2})|([2-9]\\d{3})")) {
            return str.matches("(((\\d{3}[1-9]|\\d{2}[1-9]\\d|\\d[1-9]\\d{2}|[1-9]\\d{3})-(((0[13578]|1[02])-(0[1-9]|[12]\\d|3[01]))|((0[469]|11)-(0[1-9]|[12]\\d|30))|(02-(0[1-9]|1\\d|2[0-8]))))|(((\\d{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))-([0-1]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$");
        }
        return false;
    }
    public static boolean checkStartEnd(String str1,String str2){
        return str1.compareTo(str2) < 0;
    }
    public static boolean checkTime(String time1,String time2){
        if (checkTimeFormat(time1) && checkTimeFormat(time2)){
            return checkStartEnd(time1,time2);
        }
        return false;
    }
    public boolean checkSubmitted(String stu_id){
        if (submitted.containsKey(stu_id)){
            return true;
        }
        return false;
    }
    public void putSubmitted(String stu_id,double score){
        if (submitted.containsKey(stu_id)){
            submitted.remove(stu_id);
        }
        submitted.put(stu_id,score);
        receiveNum = submitted.size();
    }
}
