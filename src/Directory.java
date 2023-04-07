import java.io.File;
import java.util.ArrayList;

public class Directory {
    private final String directoryPath;

    public Directory(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public ArrayList<LogFile> filesToList() {

        File directory = new File(directoryPath);

        if (!directory.isDirectory()) {
            System.out.println(directoryPath + " nie jest katalogiem!");
            System.exit(2);
        }

        File[] files = directory.listFiles();

        if (files.length == 0) {
            System.out.println("Katalog jest pusty");
            System.exit(0);
        }
        ArrayList<LogFile> filesList = new ArrayList<>();

        for (File file : files) {
            filesList.add(new LogFile(file.getParentFile(), file.getName()));
        }
        return filesList;
    }
}

