import java.io.File;
import java.io.IOException;

public class FileOperation{
    public static boolean fileExist(String inputPath, String outPath) throws IOException {
        File inputFile = new File(inputPath);
        if (!inputFile.exists())
            throw new IOException("input file not found");
        File outputFile = new File(outPath);
        String[] redirectPath = outPath.split("/");
        StringBuilder redirectDirStr = new StringBuilder();
        if (redirectPath.length > 1) {
            for (int i = 0; i < redirectPath.length - 1; i++) {
                redirectDirStr.append(redirectPath[i]);
                redirectDirStr.append("/");
            }
            File redirectDir = new File(redirectDirStr.toString());
            if (!redirectDir.exists()) {
                if (!redirectDir.mkdirs())
                    throw new IOException("file operation failed");
            }
        }
        if (outputFile.exists()){
            outputFile.delete();
        }
        outputFile.createNewFile();
        return true;
    }
    public static void deleteDir(String dirPath) throws Exception{
        File file = new File(dirPath);
        if(file.isFile()) {
            file.delete();
        }
        else {
            File[] files = file.listFiles();
            if(files == null) {
                file.delete();
            }
            else {
                for (int i = 0; i < files.length; i++) {
                    deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }
}
