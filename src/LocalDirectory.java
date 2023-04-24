import java.io.File;
import java.util.ArrayList;

public class LocalDirectory extends Directory {
    @Override
    protected void filesToList() {
        File directory = new File(directoryPath);

        File[] files = directory.listFiles();

        if (files.length == 0) {
            System.out.println("Katalog jest pusty");
            System.exit(0);
        }
        ArrayList<LogFile> filesList = new ArrayList<>();

        for (File file : files) {
            if (file.isDirectory()) continue;
            filesList.add(new LocalLogFile(file.getParentFile(), file.getName()));
        }
        this.filesList = filesList;
    }

    @Override
    protected boolean isDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.isDirectory()) {
            System.out.println(directoryPath + " nie jest katalogiem!\n");
            return false;
        } else {
            return true;
        }
    }
}
