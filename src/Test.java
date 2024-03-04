import java.io.IOException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.TreeMap;
import java.io.File;


public class Test {
    final static String quit = "QUIT";
    static boolean logged = false;
    static String loggedUsr;
    static File dataDir = new File("./data");
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try{
            FileOperation.deleteDir(dataDir.getPath());
            if (!dataDir.mkdirs()){
                throw new SCSException("file operation failed");
            }
            Command cmd = new Command();
            HashMap<String, User> users = new HashMap<>();
            TreeMap<String, Course> courses = new TreeMap<>();
            TreeMap<String, Ware> wares = new TreeMap<>();
            TreeMap<String, Task> tasks = new TreeMap<>();
            do{
                cmd.setCommand(sc.nextLine(),users,courses,wares,tasks);
                cmd.excCommand();
            }while (!(cmd.isQUIT()));
            FileOperation.deleteDir(dataDir.getPath());
            System.out.println("----- Good Bye! -----");
        }catch (SCSException e){
            System.out.println(e.getMessage());
        }catch (IOException e){
            System.out.println("file operation failed");
        }catch (Exception e){
            System.out.println("unexpected error");
        }
    }
}







