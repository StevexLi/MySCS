class Ware {
    private String id,name,forCourse;
    Ware (String id,String name,String forCourse){
        this.id = id;
        this.name = name;
        this.forCourse = forCourse;
    }

    String getId() {
        return id;
    }
    String getName() {
        return name;
    }

    public static boolean checkWareId(String str, Course course){
        String courseIdLast4 = course.getId().substring(course.getId().length()-4);
        if (str.matches("(W)([0-9]{4})(0[1-9]|[1-9][0-9])")){
            return str.indexOf(courseIdLast4) == 1;
        }
        return false;
    }
    public static boolean checkWareName(String str){
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
}
