import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Directory {
    protected final String directoryPath;
    protected ArrayList<LogFile> filesList;

    public Directory() {
        directoryPath = getDirectoryPath();
        filesToList();
    }

    protected abstract void filesToList();

    private String getDirectoryPath() {
        System.out.print("Podaj ścieżkę do katalogu\n> ");
        Scanner scanner = new Scanner(System.in);
        String tempPath = scanner.next();
        if (isDirectory(tempPath)) return tempPath;
        return getDirectoryPath();
    }

    protected abstract boolean isDirectory(String directoryPath);

    public void search() {
        StringBuilder fileContent = new StringBuilder();

        for (LogFile file : filesList) {
            String tempStr = file.searchInFile();
            if (tempStr != null) {
                System.out.println(tempStr);
                fileContent.append(tempStr).append("\n");
            }
        }

        if (fileContent.length() != 0) {
            writeToFile(fileContent);
        } else System.out.println("Nic nie znaleziono\n");
    }

    private void writeToFile(StringBuilder fileContent) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Zapisać do pliku? [t/n] > ");
        if (scanner.next().contains("t")) {
            System.out.print("Podaj nazwę pliku > ");
            String fileName = scanner.next() + ".txt";
            try {
                FileOutputStream outputStream = new FileOutputStream(fileName);
                byte[] strToBytes = fileContent.toString().getBytes();
                outputStream.write(strToBytes);
                outputStream.close();

                System.out.println();
                System.out.println("Plik zostal poprawnie zapisany");
            } catch (IOException e) {
                System.out.println("Blad zapisu pliku (" + e.getMessage() + ")");
            }
        }
    }
}
