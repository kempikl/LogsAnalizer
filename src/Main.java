import com.jcraft.jsch.JSchException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws JSchException {
        System.out.println("LogsAnalizer v 1.1\n");

        Directory directory = setDirectoryType();

        while (true) {
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            LocalLogFile.setSearchValue(scanner.next());

            switch (LocalLogFile.getSearchValue()) {
                case "exit":
                    System.exit(0);
                    break;
                case "cd":
                    //directory = new LocalDirectory();
                    break;
                default:
                    directory.search();
                    break;
            }
        }
    }

    private static Directory setDirectoryType() throws JSchException {
        System.out.print("Wybierz typ katalogu [1 - lokalny, 2 - zdalny (SFTP)\n> ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        if (choice == 1) return new LocalDirectory();
        else if (choice == 2) {
            System.out.print("Podaj nazwę hosta\n> ");
            String hostName = scanner.next();
            System.out.print("Podaj nazwę użytkownika\n> ");
            String userName = scanner.next();
            SftpClient sftpClient = new SftpClient(hostName, userName);
            System.out.print("Podaj hasło\n> ");
            String password = scanner.next();
            sftpClient.authPassword(password);
            RemoteDirectory.setSftpClient(sftpClient);
            return new RemoteDirectory();
        }
        return setDirectoryType();
    }
}
