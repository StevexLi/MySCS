import java.util.TreeMap;

class User {
    private String id,givenName,surName,email,pwd,type;
    private boolean isAssistant;
    private TreeMap<String,Course> myAdminCourse;
    private TreeMap<String,Course> myLearnCourse;
    private String selected;
    private TreeMap<String,String> myTask;
    private TreeMap<String,Integer> myVM;
    User(String id, String givenName, String surName, String email, String pwd, String type){
        this.id = id;
        this.givenName = givenName;
        this.surName = surName;
        this.email = email;
        this.pwd = pwd;
        this.type = type;
        this.isAssistant = false;
        this.myAdminCourse = new TreeMap<String,Course>();
        this.myLearnCourse = new TreeMap<String,Course>();
        this.selected = "";
        this.myTask = new TreeMap<String,String>();
        this.myVM = new TreeMap<>();
    }
    void setSelected(String selected) {
        this.selected = selected;
    }
    void changeIsAssistant(){
        this.isAssistant = !this.isAssistant;
    }
    void addVM(String course,int index){
        if (this.myVM.containsKey(course)){
            this.myVM.remove(course);
        }
        this.myVM.put(course,index);
    }
    int hasVM(String course){
        if (this.myVM.containsKey(course)){
            return this.myVM.get(course);
        }
        return -1;
    }

    String getId(){
        return this.id;
    }
    String getGivenName() {
        return givenName;
    }
    String getSurName() {
        return surName;
    }
    String getEmail() {
        return email;
    }
    String getType() {
        return type;
    }
    boolean getIsAssistant(){
        return isAssistant;
    }
    TreeMap<String,Course> getMyAdminCourse(){
        return this.myAdminCourse;
    }
    TreeMap<String, Course> getMyLearnCourse() {
        return this.myLearnCourse;
    }
    String getSelected() {
        return selected;
    }
    TreeMap<String,String> getMyTask(){
        return this.myTask;
    }

    public static String checkUserId(String str){
        if (str.matches("(1[7-9]|2[0-2])(0[1-9]|[1-3][0-9]|4[0-3])([1-6])(00[1-9]|0[1-9][0-9]|[1-9][0-9][0-9])")){
            return "Student";//本科
        }
        else if (str.matches("(SY|ZY)(19|2[0-2])(0[1-9]|[1-3][0-9]|4[0-3])([1-6])+(0[1-9]|[1-9][0-9])")){
            return "Student";//硕士
        }
        else if (str.matches("(BY)(1[7-9]|2[0-2])(0[1-9]|[1-3][0-9]|4[0-3])([1-6])(0[1-9]|[1-9][0-9])")){
            return "Student";//博士
        }
        else if (str.matches("\\d{5}") && !str.matches("0{5}")){
            return "Professor";//老师
        }
        else{
            return "false";
        }
    }
    public boolean verifyUserPassword(String pwdToCheck){
        return pwdToCheck.equals(this.pwd);
    }
    public static boolean checkUserName(String str1, String str2){
        return str1.matches("[A-Z]|[A-Z][a-z]{1,19}") && str2.matches("[A-Z]|[A-Z][a-z]{1,19}");
    }
    public static boolean checkEmail(String str){
        return str.matches("\\w+@\\w+(\\.\\w+)+");
    }
    public static boolean checkUserPassword(String str){
        return str.matches("[A-Za-z]+\\w{7,15}");
    }

    void printUserInfo(){
        System.out.println("Name: "+this.givenName+" "+this.surName);
        System.out.println("ID: "+this.id);
        System.out.println("Type: "+this.type);
        System.out.println("Email: "+this.email);
    }
}