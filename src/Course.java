import java.util.ArrayList;
import java.util.TreeMap;

class Course {
    private String id,name;
    private TreeMap<String,User> admins;
    private TreeMap<String,User> students;
    private TreeMap<String, Ware> wares;
    private TreeMap<String, Task> tasks;
    private ArrayList<VirtualMachine> VMs;
    Course(String id,String name,User admin){
        this.id = id;
        this.name = name;
        this.admins = new TreeMap<>();
        this.admins.put(admin.getId(),admin);
        this.students = new TreeMap<>();
        this.wares = new TreeMap<>();
        this.tasks = new TreeMap<>();
        this.VMs = new ArrayList<>();
        VMs.add(null);//占用索引为0的位置
    }

    String getId() {
        return id;
    }
    TreeMap<String, User> getAdmins() {
        return admins;
    }
    TreeMap<String, User> getStudents() {
        return students;
    }
    TreeMap<String, Ware> getWares() {
        return wares;
    }
    TreeMap<String, Task> getTasks() {
        return tasks;
    }
    VirtualMachine VM_Factory(String type){
        if (type.equals("Windows")){
            return new VM_Windows(this.id);
        }
        else if (type.equals("Linux")){
            return new VM_Linux(this.id);
        }
        else if (type.equals("MacOS")){
            return new VM_MacOS(this.id);
        }
        else{
            return null;
        }
    }
    int addVM(VirtualMachine vm){
        VMs.add(vm);
        return VMs.indexOf(vm);
    }
    VirtualMachine getVM(int index){
        return VMs.get(index);
    }

    public static boolean checkCourseId(String str){
        return str.matches("(C)(1[7-9]|2[0-2])(0[1-9]|[1-9][0-9])");
    }
    public static boolean checkCourseName(String str){
        return str.matches("[A-Za-z0-9_]{6,16}");
    }

    void listCourseInfo(){
        int teacherNum,assistantNum,studentNum;
        teacherNum=assistantNum=studentNum=0;
        for (User admin : this.admins.values()){
            if (admin.getType().equals("Professor")){
                teacherNum++;
            }
            else if (admin.getType().equals("Student")){
                assistantNum++;
            }
        }
        studentNum = this.students.size();
        System.out.printf("[ID:%s] [Name:%s] [TeacherNum:%d] [AssistantNum:%d] [StudentNum:%d]\n",this.id,this.name,teacherNum,assistantNum,studentNum);
    }
    void listAdminInfo(){
        for (User admin : this.admins.values()){
            String type = admin.getType();
            if (type.equals("Student")){
                type = "Assistant";
            }
            System.out.printf("[ID:%s] [Name:%s %s] [Type:%s] [Email:%s]\n",admin.getId(),admin.getSurName(),admin.getGivenName(),type,admin.getEmail());
        }
    }
    void listAdminInfo_s(){
        for (User admin : this.admins.values()){
            String type = admin.getType();
            if (type.equals("Student")){
                type = "Assistant";
            }
            System.out.printf("[Name:%s %s] [Type:%s] [Email:%s]\n",admin.getSurName(),admin.getGivenName(),type,admin.getEmail());
        }
    }
    void listWareInfo(){
        if (wares.isEmpty()){
            System.out.println("total 0 ware");
            return;
        }
        for (Ware ware : this.wares.values()){
            System.out.printf("[ID:%s] [Name:%s]\n",ware.getId(),ware.getName());
        }
    }
    void listTaskInfo(){
        if (tasks.isEmpty()){
            System.out.println("total 0 task");
            return;
        }
        for (Task task : this.tasks.values()){
            System.out.printf("[ID:%s] [Name:%s] [SubmissionStatus:%d/%d] [StartTime:%s] [EndTime:%s]\n",task.getId(),task.getName(),task.getReceiveNum(),students.size(),task.getStart(),task.getEnd());
        }
    }
    void listTaskInfo_s(String stu_id){
        if (tasks.isEmpty()){
            System.out.println("total 0 task");
            return;
        }
        String status;
        for (Task task : this.tasks.values()){
            status = task.checkSubmitted(stu_id)?"done":"undone";
            System.out.printf("[ID:%s] [Name:%s] [Status:%s] [StartTime:%s] [EndTime:%s]\n",task.getId(),task.getName(),status,task.getStart(),task.getEnd());
        }
    }
    void listStudentInfo(){
        for (User student : this.students.values()){
            System.out.printf("[ID:%s] [Name:%s %s] [Email:%s]\n",student.getId(),student.getSurName(),student.getGivenName(),student.getEmail());
        }
    }
}
