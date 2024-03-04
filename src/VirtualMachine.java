import java.io.Serializable;
import java.util.ArrayList;

public abstract class VirtualMachine implements Serializable {
    private ArrayList<String> commands;
    private String forCourse;
    private boolean valid;
    VirtualMachine(){
        this.commands = new ArrayList<>();
        this.forCourse = null;
        this.valid = true;
    }
    VirtualMachine(String str){
        this.commands = new ArrayList<>();
        this.forCourse = str;
        this.valid = true;
    }
    public boolean appendCommands(String str){
        if (str.equals("EOF")){
            return false;
        }
        commands.add(str);
        return true;
    }
    public boolean isValid(){
        return this.valid;
    }
    public boolean isEmpty(){
        if (commands.isEmpty())
            return true;
        return false;
    }
    public void clearVM(){
        commands.clear();
        valid = false;
    }
    public String judgeType(){
        if (this instanceof VM_Windows){
            return "Windows";
        }
        else if (this instanceof VM_Linux){
            return "Linux";
        }
        else if (this instanceof VM_MacOS){
            return "macOS";
        }
        else{
            return "False";
        }
    }
    void printVMCommands(){
        for (String command:commands){
            System.out.println(command);
        }
    }
}

class VM_Windows extends VirtualMachine{
    VM_Windows(String str){
        super(str);
    }
}
class VM_Linux extends VirtualMachine{
    VM_Linux(String str){
        super(str);
    }
}
class VM_MacOS extends VirtualMachine{
    VM_MacOS(String str){
        super(str);
    }
}
