import java.io.*;
import java.nio.file.Files;
import java.util.*;

class Command {
    String command;
    String []args;
    HashMap<String, User> users;
    TreeMap<String, Course> courses;
    TreeMap<String, Ware> wares;
    TreeMap<String, Task> tasks;
    int argsNum;
    //preprocess------------------------------
    boolean isQUIT(){
        return this.command.equals(Test.quit);
    }
    void split(){
        this.command = this.command.trim();
        this.args = this.command.split("\s+");
    }
    void illegal(){
        System.out.println("arguments illegal");
    }
    //initialization------------------------------
    void setCommand(String str,HashMap<String, User> users,TreeMap<String, Course> courses,TreeMap<String, Ware> wares,TreeMap<String, Task> tasks){
        this.command = str;
        this.argsNum = 0;
        this.users = users;
        this.courses = courses;
        this.wares = wares;
        this.tasks = tasks;
    }
    void excCommand(){
        try{
            split();
            switch (this.args[0]){
                case "":
                case Test.quit:
                    break;
                case "register":
                    register();
                    break;
                case "login":
                    login();
                    break;
                case "logout":
                    logout();
                    break;
                case "printInfo":
                    printInfo();
                    break;
                case "addCourse":
                    addCourse();
                    break;
                case "removeCourse":
                    removeCourse();
                    break;
                case "listCourse":
                    listCourse();
                    break;
                case "selectCourse":
                    selectCourse();
                    break;
                case "addAdmin":
                    addAdmin();
                    break;
                case "removeAdmin":
                    removeAdmin();
                    break;
                case "listAdmin":
                    listAdmin();
                    break;
                case "changeRole":
                    changeRole();
                    break;
                case "addWare":
                    addWare();
                    break;
                case "removeWare":
                    removeWare();
                    break;
                case "listWare":
                    listWare();
                    break;
                case "addTask":
                    addTask();
                    break;
                case "removeTask":
                    removeTask();
                    break;
                case "listTask":
                    listTask();
                    break;
                case "addStudent":
                    addStudent();
                    break;
                case "removeStudent":
                    removeStudent();
                    break;
                case "listStudent":
                    listStudent();
                    break;
                case "downloadFile":
                    downloadFile();
                    break;
                case "openFile":
                    openFile();
                    break;
                case "submitTask":
                    submitTask();
                    break;
                case "addAnswer":
                    addAnswer();
                    break;
                case "queryScore":
                    queryScore();
                    break;
                case "requestVM":
                    requestVM();
                    break;
                case "startVM":
                    startVM();
                    break;
                case "clearVM":
                    clearVM();
                    break;
                case "logVM":
                    logVM();
                    break;
                case "uploadVM":
                    uploadVM();
                    break;
                case "downloadVM":
                    downloadVM();
                    break;
                default:
                    System.out.println("command '" +this.args[0]+ "' not found");
                    break;
            }
        }catch (SCSException e){
            System.out.println(e.getMessage());
        }catch (IOException e){
            System.out.println("file operation failed");
        }catch (Exception e){
            System.out.println("unexpected error");
        }
    }
    //functions------------------------------
    void register(){
        String type;
        if (this.args.length!=7){//参数个数
            illegal();
            return;
        }
        if (Test.logged){//是否登录状态
            System.out.println("already logged in");
            return;
        }
        type = User.checkUserId(this.args[1]);
        if (type.equals("false")){
            System.out.println("user id illegal");//用户id是否合法
            return;
        }
        if (users.containsKey(this.args[1])){
            System.out.println("user id duplication");
            return;
        }
        if (!User.checkUserName(this.args[2],this.args[3])){//用户名是否合法
            System.out.println("user name illegal");
            return;
        }
        if (!User.checkEmail(this.args[4])){//邮箱
            System.out.println("email address illegal");
            return;
        }
        if (!User.checkUserPassword(this.args[5])){//密码
            System.out.println("password illegal");
            return;
        }
        if (!this.args[5].equals(this.args[6])){//确认密码
            System.out.println("passwords inconsistent");
            return;
        }
        users.put(this.args[1],new User(this.args[1],this.args[2],this.args[3],this.args[4],this.args[5],type)) ;
        System.out.println("register success");
    }
    void login(){
        String type;
        if (this.args.length!=3){//参数个数
            illegal();
            return;
        }
        if (Test.logged){
            System.out.println("already logged in");//已登录
            return;
        }
        if (User.checkUserId(this.args[1]).equals("false")){
            System.out.println("user id illegal");//用户id是否合法
            return;
        }
        if (users.containsKey(this.args[1])){//学工号是否被注册
            User tmp_user = users.get(this.args[1]);
            if (tmp_user.verifyUserPassword(this.args[2])){//密码是否正确
                type = tmp_user.getType();
                if (type.equals("Student")){
                    System.out.println("Hello "+tmp_user.getGivenName()+"~");
                }
                else if (type.equals("Professor")){
                    System.out.println("Hello Professor "+tmp_user.getSurName()+"~");
                }
                Test.loggedUsr = this.args[1];
                Test.logged = true;
            }
            else{
                System.out.println("wrong password");
            }
        }
        else {
            System.out.println("user id not exist");
        }
    }
    void logout(){
        if (this.args.length != 1){
            illegal();
            return;
        }
        if (Test.logged){
            if (users.get(Test.loggedUsr).getIsAssistant()){
                users.get(Test.loggedUsr).changeIsAssistant();
            }
            users.get(Test.loggedUsr).setSelected("");
            Test.loggedUsr = "";
            Test.logged = false;
            System.out.println("Bye~");
        }
        else{
            System.out.println("not logged in");
        }
    }
    void printInfo(){
        String type;
        if (this.args.length>2){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("login first");
            return;
        }
        User tmp_user = users.get(Test.loggedUsr);
        type = tmp_user.getType();
        if (type.equals("Student")){
            if (args.length!=1){
                System.out.println("permission denied");
            }
            else{
                tmp_user.printUserInfo();
            }
        }
        else if (type.equals("Professor")){
            if (args.length == 1){
                tmp_user.printUserInfo();
            }
            else if (User.checkUserId(this.args[1]).equals("false")){
                System.out.println("user id illegal");//用户id是否合法
            }
            else if (users.containsKey(this.args[1])){
                users.get(this.args[1]).printUserInfo();
            }
            else {
                System.out.println("user id not exist");
            }
        }
    }
    void addCourse(){
        if (this.args.length!=3){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor")){
            System.out.println("permission denied");
            return;
        }
        if (!Course.checkCourseId(this.args[1])){
            System.out.println("course id illegal");
            return;
        }
        else if (courses.containsKey(this.args[1])){
            System.out.println("course id duplication");
            return;
        }
        if (!Course.checkCourseName(this.args[2])){
            System.out.println("course name illegal");
            return;
        }
        Course newCourse = new Course(this.args[1],this.args[2],users.get(Test.loggedUsr));
        courses.put(this.args[1],newCourse);//新建课程
        users.get(Test.loggedUsr).getMyAdminCourse().put(this.args[1],newCourse);//课程添加到老师的管理列表
        System.out.println("add course success");
    }
    void removeCourse(){
        if (this.args.length!=2){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor")){
            System.out.println("permission denied");
            return;
        }
        if (!Course.checkCourseId(this.args[1])){
            System.out.println("course id illegal");
            return;
        }
        if (!courses.containsKey(this.args[1]) || !courses.get(this.args[1]).getAdmins().containsKey(Test.loggedUsr)){
            System.out.println("course id not exist");//课程不存在或不在自己名下
            return;
        }
        for (String userId : courses.get(this.args[1]).getAdmins().keySet()){//删除课程管理员中的该课程
            users.get(userId).getMyAdminCourse().remove(this.args[1]);
        }
        for (String userId : courses.get(this.args[1]).getStudents().keySet()){//删除学生中的该课程
            users.get(userId).getMyLearnCourse().remove(this.args[1]);
        }
        for (String wareId : courses.get(this.args[1]).getWares().keySet()){//删除资料
            wares.remove(wareId);
        }
        for (String taskId : courses.get(this.args[1]).getTasks().keySet()){//删除作业
            tasks.remove(taskId);
        }
        if (users.get(Test.loggedUsr).getSelected().equals(this.args[1])){//清除选择课程
            users.get(Test.loggedUsr).setSelected("");
        }
        courses.remove(this.args[1]);//删除课程
        System.out.println("remove course success");
    }
    void listCourse(){
        if (this.args.length!=1){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor") && !users.get(Test.loggedUsr).getIsAssistant()){//普通学生
            if (users.get(Test.loggedUsr).getMyLearnCourse().size() == 0){
                System.out.println("course not exist");//学生无课程
                return;
            }
            for (Course courseToList : users.get(Test.loggedUsr).getMyLearnCourse().values()){
                courseToList.listCourseInfo();
            }
        }
        else{//老师或助教
            if (users.get(Test.loggedUsr).getMyAdminCourse().size() == 0){
                System.out.println("course not exist");//老师或助教名下无课程
                return;
            }
            for (Course courseToList : users.get(Test.loggedUsr).getMyAdminCourse().values()){
                courseToList.listCourseInfo();
            }
        }
    }
    void selectCourse(){
        if (this.args.length!=2){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (!Course.checkCourseId(this.args[1])){
            System.out.println("course id illegal");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor") && !users.get(Test.loggedUsr).getIsAssistant()){//普通学生
            if (!courses.containsKey(this.args[1]) || !courses.get(this.args[1]).getStudents().containsKey(Test.loggedUsr)){
                System.out.println("course id not exist");//课程不存在或自己不参与
                return;
            }
        }
        else{//老师或助教
            if (!courses.containsKey(this.args[1]) || !courses.get(this.args[1]).getAdmins().containsKey(Test.loggedUsr)){
                System.out.println("course id not exist");//课程不存在或不在自己名下
                return;
            }
        }
        users.get(Test.loggedUsr).setSelected(this.args[1]);
        System.out.println("select course success");
    }
    void addAdmin(){
        if (this.args.length<2){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor")){
            System.out.println("permission denied");
            return;
        }
        if (users.get(Test.loggedUsr).getSelected().isBlank()){
            System.out.println("no course selected");
            return;
        }
        int i,length;
        for (i=1,length = this.args.length;i<length;i++){
            if (User.checkUserId(this.args[i]).equals("false")){//学工号不合法
                System.out.println("user id illegal");
                return;
            }
            if (!users.containsKey(this.args[i])){//学工号未被注册
                System.out.println("user id not exist");
                return;
            }
        }
        String selected = users.get(Test.loggedUsr).getSelected();
        for (i=1;i<length;i++){
            courses.get(selected).getAdmins().put(this.args[i],users.get(this.args[i]));//管理员添加到课程
            users.get(this.args[i]).getMyAdminCourse().put(selected,courses.get(selected));//课程添加到管理员
        }
        System.out.println("add admin success");
    }
    void removeAdmin(){
        if (this.args.length!=2){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor")){
            System.out.println("permission denied");
            return;
        }
        if (users.get(Test.loggedUsr).getSelected().isBlank()){
            System.out.println("no course selected");
            return;
        }
        if (User.checkUserId(this.args[1]).equals("false")){//学工号不合法
            System.out.println("user id illegal");
            return;
        }
        String selected = users.get(Test.loggedUsr).getSelected();
        if (!users.containsKey(this.args[1]) || !courses.get(selected).getAdmins().containsKey(this.args[1])){//学工号未被注册或不是管理员
            System.out.println("user id not exist");
            return;
        }
        courses.get(selected).getAdmins().remove(this.args[1]);//移出课程中的管理员
        users.get(this.args[1]).getMyAdminCourse().remove(selected);//移出原管理员的课程
        System.out.println("remove admin success");
    }
    void listAdmin(){
        if (this.args.length!=1){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (users.get(Test.loggedUsr).getSelected().isBlank()){
            System.out.println("no course selected");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor") && !users.get(Test.loggedUsr).getIsAssistant()){
            courses.get(users.get(Test.loggedUsr).getSelected()).listAdminInfo_s();//非老师/助教
            return;
        }
        courses.get(users.get(Test.loggedUsr).getSelected()).listAdminInfo();//是老师/助教
    }
    void changeRole(){
        if (this.args.length!=1){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (users.get(Test.loggedUsr).getType().equals("Professor") || users.get(Test.loggedUsr).getMyAdminCourse().isEmpty()){
            System.out.println("permission denied");//不是有助教身份的学生
            return;
        }
        users.get(Test.loggedUsr).setSelected("");//清空选择课程
        users.get(Test.loggedUsr).changeIsAssistant();
        System.out.printf("change into %s success\n",users.get(Test.loggedUsr).getIsAssistant()?"Assistant":"Student");
    }
    void addWare() throws Exception{
        if (this.args.length!=3){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor") && !users.get(Test.loggedUsr).getIsAssistant()){
            System.out.println("permission denied");//不是老师/助教
            return;
        }
        if (users.get(Test.loggedUsr).getSelected().isBlank()){
            System.out.println("no course selected");
            return;
        }
        if (!Ware.checkWareId(this.args[1],courses.get(users.get(Test.loggedUsr).getSelected()))){
            System.out.println("ware id illegal");
            return;
        }
        String[] filepath;
        String wareName,fileName;
        filepath = this.args[2].split("/");
        wareName = filepath[filepath.length-1];
        if (!Ware.checkWareName(wareName)){
            System.out.println("ware name illegal");
            return;
        }
        File file_to_add = new File(this.args[2]);
        if(!file_to_add.exists())
            throw new SCSException("ware file does not exist");
        File wareDir = new File(Test.dataDir,users.get(Test.loggedUsr).getSelected()+"/wares");
        if (!wareDir.exists()){//检测课程资料文件夹存在
            if (!wareDir.mkdirs()){//创建课程资料文件夹
                throw new SCSException("ware file operation failed");
            }
        }
        if (wares.containsKey(this.args[1])){//有重资料编号文件
            File ware_to_delete = new File(wareDir,this.args[1]+"_"+wares.get(this.args[1]).getName());
            if (!ware_to_delete.delete())
                throw new SCSException("ware file operation failed");
            courses.get(users.get(Test.loggedUsr).getSelected()).getWares().remove(this.args[1]);//删除课程中的该资料
            wares.remove(this.args[1]);//删除资料
        }
        fileName = this.args[1]+"_"+wareName;
        File new_ware_file = new File(wareDir,fileName);
        Files.copy(file_to_add.toPath(),new_ware_file.toPath());//exception
        Ware newWare = new Ware(this.args[1],wareName,users.get(Test.loggedUsr).getSelected());
        wares.put(this.args[1],newWare);//新建资料，存到资料库
        courses.get(users.get(Test.loggedUsr).getSelected()).getWares().put(this.args[1],newWare);//资料存到对应课程
        System.out.println("add ware success");
    }
    void removeWare() throws Exception{
        if (this.args.length!=2){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor") && !users.get(Test.loggedUsr).getIsAssistant()){
            System.out.println("permission denied");//不是老师/助教
            return;
        }
        if (users.get(Test.loggedUsr).getSelected().isBlank()){
            System.out.println("no course selected");
            return;
        }
        if (!wares.containsKey(this.args[1]) || !courses.get(users.get(Test.loggedUsr).getSelected()).getWares().containsKey(this.args[1])){
            System.out.println("ware not found");//资料不存在或不在选择的课程中
            return;
        }
        File wareDir = new File(Test.dataDir,users.get(Test.loggedUsr).getSelected()+"/wares");
        File ware_to_delete = new File(wareDir,this.args[1]+"_"+wares.get(this.args[1]).getName());
        if (!ware_to_delete.delete())
            throw new SCSException("delete file failed");
        courses.get(users.get(Test.loggedUsr).getSelected()).getWares().remove(this.args[1]);//删除课程中的该资料
        wares.remove(this.args[1]);//删除资料
        System.out.println("remove ware success");
    }
    void listWare(){
        if (this.args.length!=1){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (users.get(Test.loggedUsr).getSelected().isBlank()){
            System.out.println("no course selected");
            return;
        }
        courses.get(users.get(Test.loggedUsr).getSelected()).listWareInfo();
    }
    void addTask() throws Exception{
        if (this.args.length!=5){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor") && !users.get(Test.loggedUsr).getIsAssistant()){
            System.out.println("permission denied");//非老师/助教
            return;
        }
        if (users.get(Test.loggedUsr).getSelected().isBlank()){
            System.out.println("no course selected");
            return;
        }
        if (!Task.checkTaskId(this.args[1],courses.get(users.get(Test.loggedUsr).getSelected()))){
            System.out.println("task id illegal");
            return;
        }
        String[] filepath;
        String taskName,fileName;
        filepath = this.args[2].split("/");
        taskName = filepath[filepath.length-1];
        if (!Task.checkTaskName(taskName)){
            System.out.println("task name illegal");
            return;
        }
        if (!Task.checkTime(this.args[3],this.args[4])){
            System.out.println("task time illegal");
            return;
        }
        File file_to_add = new File(this.args[2]);
        if(!file_to_add.exists())
            throw new SCSException("task file not found");
        File taskDir = new File(Test.dataDir,users.get(Test.loggedUsr).getSelected()+"/tasks/"+this.args[1]);
        if (!taskDir.exists()){//检测课程资料文件夹存在
            if (!taskDir.mkdirs()){//创建课程资料文件夹
                throw new SCSException("file operation failed");
            }
        }
        if (tasks.containsKey(this.args[1])){//有重资料编号文件
            File task_to_delete = new File(taskDir,tasks.get(this.args[1]).getName());
            if (!task_to_delete.delete())
                throw new SCSException("file operation failed");
            courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().remove(this.args[1]);//删除课程中的该资料
            tasks.remove(this.args[1]);//删除资料
        }
        fileName = taskName;
        File new_task_file = new File(taskDir,fileName);
        Files.copy(file_to_add.toPath(),new_task_file.toPath());
        Task newTask = new Task(this.args[1],taskName,this.args[3],this.args[4],users.get(Test.loggedUsr).getSelected());
        tasks.put(this.args[1],newTask);//新建作业，存到作业库
        courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().put(this.args[1],newTask);//存作业到对应课程
        System.out.println("add task success");
    }
    void removeTask() throws Exception{
        if (this.args.length!=2){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor") && !users.get(Test.loggedUsr).getIsAssistant()){
            System.out.println("permission denied");//非老师/助教
            return;
        }
        if (users.get(Test.loggedUsr).getSelected().isBlank()){
            System.out.println("no course selected");
            return;
        }
        if (!tasks.containsKey(this.args[1]) || !courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().containsKey(this.args[1])){
            System.out.println("task not found");//资料不存在或不在选择的课程中
            return;
        }
        File taskDir = new File(Test.dataDir,users.get(Test.loggedUsr).getSelected()+"/tasks/"+this.args[1]);
        File task_to_delete = new File(taskDir,tasks.get(this.args[1]).getName());
        if (!task_to_delete.delete())
            throw new SCSException("delete file failed");
        courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().remove(this.args[1]);//删除课程中的该作业
        tasks.remove(this.args[1]);//删除作业
        System.out.println("remove task success");
    }
    void listTask(){
        if (this.args.length!=1){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (users.get(Test.loggedUsr).getSelected().isBlank()){
            System.out.println("no course selected");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor") && !users.get(Test.loggedUsr).getIsAssistant()){
            courses.get(users.get(Test.loggedUsr).getSelected()).listTaskInfo_s(Test.loggedUsr);
            return;
        }
        courses.get(users.get(Test.loggedUsr).getSelected()).listTaskInfo();
    }
    void addStudent(){
        if (this.args.length<2){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor") && !users.get(Test.loggedUsr).getIsAssistant()){
            System.out.println("permission denied");//非老师/助教
            return;
        }
        if (users.get(Test.loggedUsr).getSelected().isBlank()){
            System.out.println("no course selected");
            return;
        }
        int i,length;
        for (i=1,length = this.args.length;i<length;i++){
            if (User.checkUserId(this.args[i]).equals("false")){//学工号不合法
                System.out.println("user id illegal");
                return;
            }
            if (!users.containsKey(this.args[i])){//学工号未被注册
                System.out.println("user id not exist");
                return;
            }
            if (User.checkUserId(this.args[i]).equals("Professor")){//学生不能是老师
                System.out.println("I'm professor rather than student!");
                return;
            }
        }
        String selected = users.get(Test.loggedUsr).getSelected();
        for (i=1;i<length;i++){
            courses.get(selected).getStudents().put(this.args[i],users.get(this.args[i]));//学生添加到课程
            users.get(this.args[i]).getMyLearnCourse().put(selected,courses.get(selected));//课程添加到学生
        }
        System.out.println("add student success");
    }
    void removeStudent(){
        if (this.args.length!=2){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor") && !users.get(Test.loggedUsr).getIsAssistant()){
            System.out.println("permission denied");//非老师/助教
            return;
        }
        if (users.get(Test.loggedUsr).getSelected().isBlank()){
            System.out.println("no course selected");
            return;
        }
        if (User.checkUserId(this.args[1]).equals("false")){//学工号不合法
            System.out.println("user id illegal");
            return;
        }
        String selected = users.get(Test.loggedUsr).getSelected();
        if (!users.containsKey(this.args[1]) || !courses.get(selected).getStudents().containsKey(this.args[1])){//学工号未被注册或学生不在选中的课程
            System.out.println("user id not exist");
            return;
        }
        courses.get(selected).getStudents().remove(this.args[1]);//移除课程中学生
        users.get(this.args[1]).getMyLearnCourse().remove(selected);//移除学生中课程
        System.out.println("remove student success");
    }
    void listStudent(){
        if (this.args.length!=1){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor") && !users.get(Test.loggedUsr).getIsAssistant()){
            System.out.println("permission denied");//非老师/助教
            return;
        }
        if (users.get(Test.loggedUsr).getSelected().isBlank()){
            System.out.println("no course selected");
            return;
        }
        courses.get(users.get(Test.loggedUsr).getSelected()).listStudentInfo();
    }
    void downloadFile() throws Exception{
        if (this.args.length==1){//没有参数
            illegal();
            return;
        }
        int i,length,redirect_flag = 0,redirect_pos = 0;
        for (i=1,length = this.args.length;i<length;i++){
            if (args[i].equals(">")){
                redirect_flag = 1;//复制并写入
                redirect_pos = i;
            }
            else if(args[i].equals(">>")){
                redirect_flag = 2;//追加写入
                redirect_pos = i;
            }
        }
        if (redirect_flag==0){//不存在重定向符
            if (this.args.length != 3) {//
                illegal();
                return;
            }
            if (!Test.logged){
                System.out.println("not logged in");
                return;
            }
            if (users.get(Test.loggedUsr).getSelected().isBlank()){
                System.out.println("no course selected");
                return;
            }
            File download_file = new File(this.args[1]);
            if (this.args[2].toCharArray()[0]=='W'){//资料
                File fileDir = new File(Test.dataDir,users.get(Test.loggedUsr).getSelected()+"/wares");
                if (!courses.get(users.get(Test.loggedUsr).getSelected()).getWares().containsKey(this.args[2])){
                    throw new SCSException("file not found");
                }
                File filePath = new File(fileDir,this.args[2]+"_"+courses.get(users.get(Test.loggedUsr).getSelected()).getWares().get(this.args[2]).getName());
                if (!filePath.exists()){
                    throw new SCSException("file not found");
                }
                if (download_file.exists()){
                    if (!download_file.delete()){
                        throw new SCSException("file operation failed");
                    }
                }
                String []downloadPath = this.args[1].split("/");
                StringBuilder downloadDirStr = new StringBuilder();
                if (downloadPath.length > 1) {
                    for (i=0;i<downloadPath.length-1;i++){
                        downloadDirStr.append(downloadPath[i]);
                        downloadDirStr.append("/");
                    }
                    File downloadDir = new File(downloadDirStr.toString());
                    if (!downloadDir.exists()){
                        if (!downloadDir.mkdirs())
                            throw new SCSException("file operation failed");
                    }
                }
                Files.copy(filePath.toPath(),download_file.toPath());
                DataInputStream in = new DataInputStream(new FileInputStream(download_file));
                BufferedReader d  = new BufferedReader(new InputStreamReader(in));
                String count;
                while((count = d.readLine()) != null){
                    System.out.println(count);
                }
                in.close();
                d.close();
                return;
            }
            else if (this.args[2].toCharArray()[0]=='T'){//作业
                File fileDir = new File(Test.dataDir,users.get(Test.loggedUsr).getSelected()+"/tasks/"+this.args[2]);
                if (!courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().containsKey(this.args[2])){
                    throw new SCSException("file not found");
                }
                File filePath = new File(fileDir,courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().get(this.args[2]).getName());
                if (!filePath.exists()){
                    throw new SCSException("file not found");
                }
                if (download_file.exists()){
                    if (!download_file.delete()){
                        throw new SCSException("file operation failed");
                    }
                }
                String []downloadPath = this.args[1].split("/");
                StringBuilder downloadDirStr = new StringBuilder();
                if (downloadPath.length > 1) {
                    for (i=0;i<downloadPath.length-1;i++){
                        downloadDirStr.append(downloadPath[i]);
                        downloadDirStr.append("/");
                    }
                    File downloadDir = new File(downloadDirStr.toString());
                    if (!downloadDir.exists()){
                        if (!downloadDir.mkdirs())
                            throw new SCSException("file operation failed");
                    }
                }
                Files.copy(filePath.toPath(),download_file.toPath());
                DataInputStream in = new DataInputStream(new FileInputStream(filePath));
                BufferedReader d  = new BufferedReader(new InputStreamReader(in));
                String count;
                while((count = d.readLine()) != null){
                    System.out.println(count);
                }
                in.close();
                d.close();
                return;
            }
            else{
                throw new SCSException("file not found");
            }
        }
        else {//存在重定向符
            if ((this.args.length-1)==redirect_pos){//重定向符在最后一个参数
                System.out.println("please input the path to redirect the file");
                return;
            }
            if ((this.args.length-3)>=redirect_pos){//重定向符在倒数第三个参数(后跟多个文件)
                illegal();
                return;
            }
        }
        //以下是存在重定向符且重定向符后参数个数正确的情况
        File redirect_file = new File(this.args[redirect_pos+1]);
        String[] redirectPath = this.args[redirect_pos+1].split("/");
        StringBuilder redirectDirStr = new StringBuilder();
        if (redirectPath.length > 1) {
            for (i = 0; i < redirectPath.length - 1; i++) {
                redirectDirStr.append(redirectPath[i]);
                redirectDirStr.append("/");
            }
            File redirectDir = new File(redirectDirStr.toString());
            if (!redirectDir.exists()) {
                if (!redirectDir.mkdirs())
                    throw new SCSException("file operation failed");
            }
        }
        if (redirect_flag==1){//复制
            if (redirect_file.exists()){
                if (!redirect_file.delete()){
                    throw new SCSException("file operation failed");
                }
            }
        }
        if (!redirect_file.exists()){
            if (!redirect_file.createNewFile()){
                throw new SCSException("file operation failed");
            }
        }
        File filePath = null;
        FileWriter writer = new FileWriter(redirect_file,true);
        PrintWriter printer = new PrintWriter(writer);
        if ((this.args.length>5) || (this.args.length<4)){//可选参数1和参数2数量不正确
            printer.println("arguments illegal");
            writer.close();
            printer.close();
            return;
        }
        else if (this.args.length==5){//有可选参数1和参数2
            if (this.args[1].equals(this.args[4])){
                System.out.println("input file is output file");
                writer.close();
                printer.close();
                return;
            }
            if (!Test.logged){
                printer.println("not logged in");
                writer.close();
                printer.close();
                return;
            }
            if (users.get(Test.loggedUsr).getSelected().isBlank()){
                printer.println("no course selected");
                writer.close();
                printer.close();
                return;
            }
            File download_file = new File(this.args[1]);
            if (this.args[2].toCharArray()[0]=='W') {//资料
                File fileDir = new File(Test.dataDir, users.get(Test.loggedUsr).getSelected() + "/wares");
                if (!courses.get(users.get(Test.loggedUsr).getSelected()).getWares().containsKey(this.args[2])) {
                    printer.println("file not found");
                    writer.close();
                    printer.close();
                    return;
                }
                filePath = new File(fileDir, this.args[2] + "_" + courses.get(users.get(Test.loggedUsr).getSelected()).getWares().get(this.args[2]).getName());
                if (!filePath.exists()) {
                    printer.println("file not found");
                    writer.close();
                    printer.close();
                    return;
                }
                if (download_file.exists()){
                    if (!download_file.delete()){
                        printer.println("file operation failed");
                        writer.close();
                        printer.close();
                        return;
                    }
                }
                String []downloadPath = this.args[1].split("/");
                StringBuilder downloadDirStr = new StringBuilder();
                if (downloadPath.length > 1) {
                    for (i=0;i<downloadPath.length-1;i++){
                        downloadDirStr.append(downloadPath[i]);
                        downloadDirStr.append("/");
                    }
                    File downloadDir = new File(downloadDirStr.toString());
                    if (!downloadDir.exists()){
                        if (!downloadDir.mkdirs()){
                            printer.println("file operation failed");
                            writer.close();
                            printer.close();
                            return;
                        }
                    }
                }
                Files.copy(filePath.toPath(),download_file.toPath());//下载,if-else语句执行完后重定向
            }
            else if (this.args[2].toCharArray()[0]=='T') {//作业
                File fileDir = new File(Test.dataDir, users.get(Test.loggedUsr).getSelected() + "/tasks/"+this.args[2]);
                if (!courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().containsKey(this.args[2])) {
                    printer.println("file not found");
                    writer.close();
                    printer.close();
                    return;
                }
                filePath = new File(fileDir, courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().get(this.args[2]).getName());
                if (!filePath.exists()) {
                    printer.println("file not found");
                    writer.close();
                    printer.close();
                    return;
                }
                if (download_file.exists()){
                    if (!download_file.delete()){
                        printer.println("file operation failed");
                        writer.close();
                        printer.close();
                        return;
                    }
                }
                String []downloadPath = this.args[1].split("/");
                StringBuilder downloadDirStr = new StringBuilder();
                if (downloadPath.length > 1) {
                    for (i=0;i<downloadPath.length-1;i++){
                        downloadDirStr.append(downloadPath[i]);
                        downloadDirStr.append("/");
                    }
                    File downloadDir = new File(downloadDirStr.toString());
                    if (!downloadDir.exists()){
                        if (!downloadDir.mkdirs()){
                            printer.println("file operation failed");
                            writer.close();
                            printer.close();
                            return;
                        }
                    }
                }
                Files.copy(filePath.toPath(),download_file.toPath());//下载,if-else语句执行完后重定向
            }
            else{
                printer.println("file not found");
                writer.close();
                printer.close();
                return;
            }
        }
        else if (this.args.length==4){//没有可选参数1只有参数2
            if (!Test.logged){
                printer.println("not logged in");
                writer.close();
                printer.close();
                return;
            }
            if (users.get(Test.loggedUsr).getSelected().isBlank()){
                printer.println("no course selected");
                writer.close();
                printer.close();
                return;
            }
            if (this.args[1].toCharArray()[0]=='W') {//资料
                File fileDir = new File(Test.dataDir, users.get(Test.loggedUsr).getSelected() + "/wares");
                if (!courses.get(users.get(Test.loggedUsr).getSelected()).getWares().containsKey(this.args[1])) {
                    printer.println("file not found");
                    writer.close();
                    printer.close();
                    return;
                }
                filePath = new File(fileDir, this.args[1] + "_" + courses.get(users.get(Test.loggedUsr).getSelected()).getWares().get(this.args[1]).getName());
                if (!filePath.exists()) {
                    printer.println("file not found");
                    writer.close();
                    printer.close();
                    return;
                }
                String[] downloadPath = this.args[3].split("/");
                StringBuilder downloadDirStr = new StringBuilder();
                if (downloadPath.length > 1) {
                    for (i = 0; i < downloadPath.length - 1; i++) {
                        downloadDirStr.append(downloadPath[i]);
                        downloadDirStr.append("/");
                    }
                    File downloadDir = new File(downloadDirStr.toString());
                    if (!downloadDir.exists()) {
                        if (!downloadDir.mkdirs()){
                            printer.println("file operation failed");
                            writer.close();
                            printer.close();
                            return;
                        }
                    }
                }
            }
            else if (this.args[1].toCharArray()[0]=='T') {//作业
                File fileDir = new File(Test.dataDir, users.get(Test.loggedUsr).getSelected() + "/tasks/"+this.args[1]);
                if (!courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().containsKey(this.args[1])) {
                    printer.println("file not found");
                    writer.close();
                    printer.close();
                    return;
                }
                filePath = new File(fileDir, courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().get(this.args[1]).getName());
                if (!filePath.exists()) {
                    printer.println("file not found");
                    writer.close();
                    printer.close();
                    return;
                }
                String[] downloadPath = this.args[3].split("/");
                StringBuilder downloadDirStr = new StringBuilder();
                if (downloadPath.length > 1) {
                    for (i = 0; i < downloadPath.length - 1; i++) {
                        downloadDirStr.append(downloadPath[i]);
                        downloadDirStr.append("/");
                    }
                    File downloadDir = new File(downloadDirStr.toString());
                    if (!downloadDir.exists()) {
                        if (!downloadDir.mkdirs()){
                            printer.println("file operation failed");
                            writer.close();
                            printer.close();
                            return;
                        }
                    }
                }
            }
            else{
                printer.println("file not found");
                writer.close();
                printer.close();
                return;
            }
        }
        //以下是重定向
        if (this.args[redirect_pos].equals(">")){//复制
            writer.close();
            redirect_file.delete();
            Files.copy(filePath.toPath(), redirect_file.toPath());
            return;
        }
        else if (this.args[redirect_pos].equals(">>")){//追加
            DataInputStream in = new DataInputStream(new FileInputStream(filePath));
            BufferedReader d = new BufferedReader(new InputStreamReader(in));
            String count;
            while ((count = d.readLine()) != null) {
                printer.println(count);//包含换行符
            }
            in.close();
            d.close();
            writer.close();
            return;
        }
    }
    void openFile() throws Exception{
        if (this.args.length==1){//未使用重定向时缺失文件路径
            System.out.println("please input the path to open the file");
            return;
        }
        int i,length,redirect_pos = 0,file_path_arg;
        for (i=1,length = this.args.length;i<length;i++){
            if (args[i].equals("<")){
                redirect_pos = i;
            }
        }
        if (redirect_pos==this.args.length-1){//
            System.out.println("please input the path to redirect the file");
            return;
        }
        if (redirect_pos==1){//没有可选参数1
            if (this.args.length==2){
                System.out.println("please input the path to redirect the file");
                return;
            }
            if (this.args.length>3){//重定向路径参数不为1
                System.out.println("arguments illegal");
                return;
            }
            file_path_arg = 2;
        }
        else{//有可选参数1，忽略重定向
            if ((redirect_pos>2) || (redirect_pos==0 && this.args.length>2)){//有重定向符号，通过位置知道可选参数1个数
                System.out.println("arguments illegal");
                return;
            }
            file_path_arg = 1;
        }
        File filePath = new File(this.args[file_path_arg]);
        if (!filePath.exists()){
            throw new SCSException("file open failed");
        }
        DataInputStream in = new DataInputStream(new FileInputStream(filePath));
        BufferedReader d  = new BufferedReader(new InputStreamReader(in));
        String count;
        while((count = d.readLine()) != null){
            System.out.println(count);
        }
        in.close();
        d.close();
        return;
    }
    void submitTask() throws Exception{
        if (this.args.length==1){//没有参数
            illegal();
            return;
        }
        int i,length,redirect_pos = 0,file_path_arg;
        for (i=1,length = this.args.length;i<length;i++){
            if (args[i].equals("<")){
                redirect_pos = i;
            }
        }
        if (redirect_pos==this.args.length-1){//
            System.out.println("please input the path to redirect the file");
            return;
        }
        String taskID;
        if (redirect_pos==1){//没有参数2
            if (this.args.length==2){//没有指定重定向文件
                System.out.println("please input the path to redirect the file");
            }
            else{//没有参数2，可能重定向文件过多
                System.out.println("arguments illegal");
            }
            return;
        }
        if (redirect_pos==2){//没有可选参数1，有参数2
            if (this.args.length==3){
                System.out.println("please input the path to redirect the file");
                return;
            }
            if (this.args.length>4){//重定向路径参数多于1个
                System.out.println("arguments illegal");
                return;
            }
            file_path_arg = 3;
            taskID = this.args[1];
        }
        else{//有可选参数1，忽略重定向
            if ((redirect_pos>3) || (redirect_pos==0 && (this.args.length!=3))){//有重定向符号，通过位置知道可选参数1个数
                System.out.println("arguments illegal");
                return;
            }
            file_path_arg = 1;
            taskID = this.args[2];
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (users.get(Test.loggedUsr).getType().equals("Professor") || users.get(Test.loggedUsr).getIsAssistant()){
            System.out.println("operation not allowed");//非老师/助教
            return;
        }
        if (users.get(Test.loggedUsr).getSelected().isBlank()){
            System.out.println("no course selected");
            return;
        }
        if (!tasks.containsKey(taskID) || !courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().containsKey(taskID)){
            System.out.println("task not found");//资料不存在或不在选择的课程中
            return;
        }
        //新增文件
        File fileDir = new File(Test.dataDir, users.get(Test.loggedUsr).getSelected() + "/tasks/"+taskID);
        if (!fileDir.exists()){
            if (!fileDir.mkdirs()){
                throw new SCSException("file operation failed");
            }
        }
        File filePath = new File(this.args[file_path_arg]);
        if (!filePath.exists()){
            throw new SCSException("file operation failed");
        }
        int replace_flag = 0;
        File submitFile = new File(fileDir,Test.loggedUsr+".task");
        if (submitFile.exists()){//原先提交过同作业文件
            String read;
            System.out.println("task already exists, do you want to overwrite it? (y/n)");
            read = Test.sc.nextLine();
            if (read.equals("y") || read .equals("Y") || read.equals("\n")){//覆盖原先文件
                if (!submitFile.delete()){
                    throw new SCSException("file operation failed");
                }
                replace_flag = 1;
            }
            else{//保存原先提交的文件
                System.out.println("submit canceled");
                return;
            }
        }
        Files.copy(filePath.toPath(),submitFile.toPath());
        double score = judgeTask(Test.loggedUsr,taskID,submitFile);
        if (score<0){//答案不存在
            System.out.println("submit success\n"+"your score is: None");
        }
        else{//答案存在
            System.out.printf("submit success\n"+"your score is: %.1f\n",score);
        }
        tasks.get(taskID).putSubmitted(Test.loggedUsr,score);//提交到作业库中作业
        courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().get(taskID).putSubmitted(Test.loggedUsr,score);//提交到课程中作业
        //TODO:myTask++
    }
    void addAnswer() throws Exception{
        if (this.args.length==1){//没有参数
            illegal();
            return;
        }
        int i,length,redirect_pos = 0,file_path_arg;
        for (i=1,length = this.args.length;i<length;i++){
            if (args[i].equals("<")){
                redirect_pos = i;
            }
        }
        if (redirect_pos==this.args.length-1){
            System.out.println("please input the path to redirect the file");
            return;
        }
        String taskID;
        if (redirect_pos==1){//没有参数2
            if (this.args.length==2){//没有指定重定向文件
                System.out.println("please input the path to redirect the file");
            }
            else{//没有参数2，可能重定向文件过多
                System.out.println("arguments illegal");
            }
            return;
        }
        if (redirect_pos==2){//没有可选参数1，有参数2
            if (this.args.length==3){
                System.out.println("please input the path to redirect the file");
                return;
            }
            if (this.args.length>4){//重定向路径参数多于1个
                System.out.println("arguments illegal");
                return;
            }
            file_path_arg = 3;
            taskID = this.args[1];
        }
        else{//有可选参数1，忽略重定向
            if ((redirect_pos>3) || (redirect_pos==0 && this.args.length!=3)){//有重定向符号，通过位置知道可选参数1个数
                System.out.println("arguments illegal");
                return;
            }
            file_path_arg = 1;
            taskID = this.args[2];
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (!users.get(Test.loggedUsr).getType().equals("Professor") && !users.get(Test.loggedUsr).getIsAssistant()){
            System.out.println("permission denied");//非老师/助教
            return;
        }
        if (users.get(Test.loggedUsr).getSelected().isBlank()){
            System.out.println("no course selected");
            return;
        }
        if (!tasks.containsKey(taskID) || !courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().containsKey(taskID)){
            System.out.println("task not found");//资料不存在或不在选择的课程中
            return;
        }
        File fileDir = new File(Test.dataDir, users.get(Test.loggedUsr).getSelected() + "/answers");
        if (!fileDir.exists()){
            if (!fileDir.mkdirs()){
                throw new SCSException("file operation failed");
            }
        }
        File filePath = new File(this.args[file_path_arg]);
        if (!filePath.exists()){
            throw new SCSException("file operation failed");
        }
        File answerFile = new File(fileDir,taskID+".ans");
        if (answerFile.exists()){
            if (!answerFile.delete()){
                throw new SCSException("file operation failed");
            }
        }
        Files.copy(filePath.toPath(),answerFile.toPath());
        System.out.println("add answer success");
    }
    void queryScore() throws Exception{
        if (this.args.length>3){
            illegal();
            return;
        }
        if (!Test.logged){
            System.out.println("not logged in");
            return;
        }
        if (users.get(Test.loggedUsr).getSelected().isBlank()){
            System.out.println("no course selected");
            return;
        }
        ArrayList<Query> queries= new ArrayList<Query>();
        File courseTaskDir = new File(Test.dataDir, users.get(Test.loggedUsr).getSelected() + "/tasks");
        File submitDir = null;
        File submitFile = null;
        String logged = Test.loggedUsr;
        if (!users.get(Test.loggedUsr).getType().equals("Professor") && !users.get(Test.loggedUsr).getIsAssistant()){
            if (this.args.length>2){//指定学号查询
                System.out.println("permission denied");
                return;
            }
            if (this.args.length==1){//1.不指定，查询自己该课程所有作业成绩
                for(String t:courses.get(users.get(logged).getSelected()).getTasks().keySet()){
                    submitDir = new File(courseTaskDir,t);
                    submitFile = new File(submitDir,logged+".task");
                    if (submitFile.exists()){
                        queries.add(new Query(logged,users.get(logged).getSurName(),users.get(logged).getGivenName(),t,judgeTask(logged,t,submitFile)));
                    }
                }
            }
            else{//2.指定作业编号，查询自己对应作业成绩
                if (!tasks.containsKey(this.args[1]) || !courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().containsKey(this.args[1])){
                    System.out.println("task not found");
                    return;
                }
                submitDir = new File(courseTaskDir,this.args[1]);
                submitFile = new File(submitDir,logged+".task");
                if (submitFile.exists()){
                    queries.add(new Query(logged,users.get(logged).getSurName(),users.get(logged).getGivenName(),this.args[1],judgeTask(logged,this.args[1],submitFile)));
                }
            }
        }
        else{//管理端
            if (this.args.length==1){//1.不指定，查询所有作业的所有学生的成绩
                for(String t:courses.get(users.get(logged).getSelected()).getTasks().keySet()){
                    submitDir = new File(courseTaskDir,t);
                    for (String stu:courses.get(users.get(logged).getSelected()).getStudents().keySet()){
                        submitFile = new File(submitDir,stu+".task");
                        if (submitFile.exists()){
                            queries.add(new Query(stu,users.get(stu).getSurName(),users.get(stu).getGivenName(),t,judgeTask(stu,t,submitFile)));
                        }
                    }
                }
            }
            else if (this.args.length==2){//2.3.指定学号或作业编号
                if (this.args[1].matches("^T[0-9]{6}$")){//查询3.指定作业编号
                    if (!tasks.containsKey(this.args[1]) || !courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().containsKey(this.args[1])){
                        System.out.println("task not found");
                        return;
                    }
                    submitDir = new File(courseTaskDir,this.args[1]);
                    for (String stu:courses.get(users.get(logged).getSelected()).getStudents().keySet()){
                        submitFile = new File(submitDir,stu+".task");
                        if (submitFile.exists()){
                            queries.add(new Query(stu,users.get(stu).getSurName(),users.get(stu).getGivenName(),this.args[1],judgeTask(stu,this.args[1],submitFile)));
                        }
                    }
                }
                else{//查询2.指定学号
                    if (!users.containsKey(this.args[1]) || !courses.get(users.get(Test.loggedUsr).getSelected()).getStudents().containsKey(this.args[1])){
                        System.out.println("student not found");
                        return;
                    }
                    for(String t:courses.get(users.get(logged).getSelected()).getTasks().keySet()){
                        submitDir = new File(courseTaskDir,t);
                        submitFile = new File(submitDir,this.args[1]+".task");
                        if (submitFile.exists()){
                            queries.add(new Query(this.args[1],users.get(this.args[1]).getSurName(),users.get(this.args[1]).getGivenName(),t,judgeTask(this.args[1],t,submitFile)));
                        }
                    }
                }
            }
            else{//4.指定作业编号和学号
                if (!tasks.containsKey(this.args[1]) || !courses.get(users.get(Test.loggedUsr).getSelected()).getTasks().containsKey(this.args[1])){
                    System.out.println("task not found");
                    return;
                }
                if (!users.containsKey(this.args[2]) || !courses.get(users.get(Test.loggedUsr).getSelected()).getStudents().containsKey(this.args[2])){
                    System.out.println("student not found");
                    return;
                }
                submitDir = new File(courseTaskDir,this.args[1]);
                submitFile = new File(submitDir,this.args[2]+".task");
                if (submitFile.exists()){
                    queries.add(new Query(this.args[2],users.get(this.args[2]).getSurName(),users.get(this.args[2]).getGivenName(),this.args[1],judgeTask(this.args[2],this.args[1],submitFile)));
                }
            }
        }
        Collections.sort(queries);
        int results = queries.size();
        if (results<=1){
            System.out.println("total "+results+" result");
        }
        else{
            System.out.println("total "+results+" results");
        }
        int i=0;
        if (results>0){
            for (Query q:queries){
                i++;
                if (q.score<0){
                    System.out.printf("[%d] [ID:%s] [Name:%s %s] [Task_ID:%s] [Score:%s]\n",i,q.id,q.surName,q.givenName,q.taskID,"None");
                }
                else{
                    System.out.printf("[%d] [ID:%s] [Name:%s %s] [Task_ID:%s] [Score:%.1f]\n",i,q.id,q.surName,q.givenName,q.taskID,q.score);
                }
            }
        }
    }
    void requestVM() throws Exception{
        if (!Test.logged || users.get(Test.loggedUsr).getSelected().isBlank() || !users.get(Test.loggedUsr).getType().equals("Student") || users.get(Test.loggedUsr).getIsAssistant())
            return;
        User logged = users.get(Test.loggedUsr);
        Course course_selected = courses.get(users.get(Test.loggedUsr).getSelected());
        String type = this.args[1];
        VirtualMachine vm = course_selected.VM_Factory(type);
        if (vm!=null){
            logged.addVM(course_selected.getId(),course_selected.addVM(vm));
            System.out.println("requestVM success");
        }
    }
    void startVM() throws Exception{
        if (!Test.logged || users.get(Test.loggedUsr).getSelected().isBlank() || !users.get(Test.loggedUsr).getType().equals("Student") || users.get(Test.loggedUsr).getIsAssistant())
            return;
        User logged = users.get(Test.loggedUsr);
        Course course_selected = courses.get(users.get(Test.loggedUsr).getSelected());
        int index = logged.hasVM(course_selected.getId());
        if (index==-1){
            System.out.println("no VM");
            return;
        }
        VirtualMachine vm= course_selected.getVM(index);
        String type;
        if (!vm.isValid() || (type=vm.judgeType()).equals("False")){
            System.out.println("no VM");
            return;
        }
        System.out.println("welcome to "+type);
        boolean flag = true;
        String read;
        while (flag){
            read = Test.sc.nextLine();
            flag = vm.appendCommands(read);
        }
        System.out.println("quit "+type);
    }
    void clearVM() throws Exception{
        if (!Test.logged || users.get(Test.loggedUsr).getSelected().isBlank() || !users.get(Test.loggedUsr).getType().equals("Professor"))
            return;
        User logged = users.get(Test.loggedUsr);
        Course course_selected = courses.get(users.get(Test.loggedUsr).getSelected());
        String index_str = this.args[1];
        int index = Integer.parseInt(index_str);
        VirtualMachine vm= course_selected.getVM(index);
        vm.clearVM();
        String type=vm.judgeType();
        System.out.println("clear "+type+" success");
    }
    void logVM() throws Exception{
        if (!Test.logged || users.get(Test.loggedUsr).getSelected().isBlank() || !users.get(Test.loggedUsr).getType().equals("Student") || users.get(Test.loggedUsr).getIsAssistant())
            return;
        User logged = users.get(Test.loggedUsr);
        Course course_selected = courses.get(users.get(Test.loggedUsr).getSelected());
        int index = logged.hasVM(course_selected.getId());
        if (index==-1){
            System.out.println("no log");
            return;
        }
        VirtualMachine vm= course_selected.getVM(index);
        if (!vm.isValid() || vm.isEmpty() || vm.judgeType().equals("False")){
            System.out.println("no log");
            return;
        }
        vm.printVMCommands();
    }
    void uploadVM() throws Exception{
        if (!Test.logged || users.get(Test.loggedUsr).getSelected().isBlank() || !users.get(Test.loggedUsr).getType().equals("Student") || users.get(Test.loggedUsr).getIsAssistant())
            return;
        User logged = users.get(Test.loggedUsr);
        Course course_selected = courses.get(users.get(Test.loggedUsr).getSelected());
        int index = logged.hasVM(course_selected.getId());
        if (index==-1){
            return;
        }
        VirtualMachine vm= course_selected.getVM(index);
        if (!vm.isValid() || vm.judgeType().equals("False")){
            return;
        }
//        FileOperation.fileExist(Test.dataDir.getPath(),this.args[1]);
        FileOutputStream fos = new FileOutputStream(this.args[1]);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(vm);
        oos.flush();
        oos.close();
        fos.close();
        System.out.println("uploadVM success");
    }
    void downloadVM() throws Exception{
        if (!Test.logged || users.get(Test.loggedUsr).getSelected().isBlank() || !users.get(Test.loggedUsr).getType().equals("Student") || users.get(Test.loggedUsr).getIsAssistant())
            return;
        User logged = users.get(Test.loggedUsr);
        Course course_selected = courses.get(users.get(Test.loggedUsr).getSelected());
//        FileOperation.fileExist(this.args[1],./data/temp);
        FileInputStream fis = new FileInputStream(this.args[1]);
        ObjectInputStream ois = new ObjectInputStream(fis);
        VirtualMachine vm = (VirtualMachine) ois.readObject();
        if (vm!=null){
            logged.addVM(course_selected.getId(),course_selected.addVM(vm));
            System.out.println("downloadVM success");
        }
        ois.close();
        fis.close();
    }
    double judgeTask(String userID,String taskID,File submitFile) throws Exception{
        int correct,all;
        File fileDir = new File(Test.dataDir, courses.get(users.get(Test.loggedUsr).getSelected()).getId() + "/answers");
        File answerFile = new File(fileDir,taskID+".ans");
        if (!answerFile.exists()){
            return -1;
        }
        DataInputStream in1 = new DataInputStream(new FileInputStream(submitFile));
        BufferedReader d1  = new BufferedReader(new InputStreamReader(in1));
        DataInputStream in2 = new DataInputStream(new FileInputStream(answerFile));
        BufferedReader d2  = new BufferedReader(new InputStreamReader(in2));
        String str,ans = null;
        correct = all = 0;
        while(((str = d1.readLine())!=null) && ((ans = d2.readLine()) != null)){
            all++;
            if (str.equalsIgnoreCase(ans)){
                correct++;
            }
        }
        if (ans!=null){
            while ((ans = d2.readLine()) != null){
                all++;
            }
        }
        in1.close();
        d1.close();
        in2.close();
        d2.close();
        return (1.0*correct/all)*100;
    }
}