import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Directory {
    private final String directoryPath;
    private ArrayList<LogFile> filesList;

    public Directory() {
        String tempPath;

        do {
            tempPath = getDirectoryPath();
        } while (!isDirectory(tempPath));

        directoryPath = tempPath;
        filesToList();
    }

    private void filesToList() {

        File directory = new File(directoryPath);

        File[] files = directory.listFiles();

        if (files.length == 0) {
            System.out.println("Katalog jest pusty");
            System.exit(0);
        }
        ArrayList<LogFile> filesList = new ArrayList<>();

        for (File file : files) {
            filesList.add(new LogFile(file.getParentFile(), file.getName()));
        }
        this.filesList = filesList;
    }

    private String getDirectoryPath() {
        System.out.print("Podaj ścieżkę do katalogu\n> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    private boolean isDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.isDirectory()) {
            System.out.println(directoryPath + " nie jest katalogiem!\n");
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<LogFile> getFilesList() {
        return filesList;
    }
}

