import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("LogsAnalizer v 1.1\n");

        Directory directory = new Directory();

        while (true) {
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            LogFile.setSearchValue(scanner.next());

            switch (LogFile.getSearchValue()) {
                case "exit":
                    System.exit(0);
                    break;
                case "cd":
                    directory = new Directory();
                    break;
                default:
                    search(directory.getFilesList());
                    break;
            }
        }
    }

    public static void search(ArrayList<LogFile> filesList) {
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

    public static void writeToFile(StringBuilder fileContent) {
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
