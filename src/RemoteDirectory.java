import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.util.ArrayList;

public class RemoteDirectory extends Directory {
    public static SftpClient sftpClient;

    @Override
    protected void filesToList() {
        try {
            ArrayList<String> files = sftpClient.listFiles(directoryPath);

            if (files.size() == 0) {
                System.out.println("Katalog jest pusty");
                System.exit(0);
            }
            ArrayList<LogFile> filesList = new ArrayList<>();

            for (String file : files) {
                filesList.add(new RemoteLogFile(directoryPath, file));
            }
            this.filesList = filesList;
        } catch (SftpException | JSchException e) {
            System.out.println("Błąd listowania katalogu " + directoryPath + " (" + e.getMessage() + ")");
            System.exit(1);
        }
    }

    @Override
    protected boolean isDirectory(String directoryPath) {
        if (!sftpClient.isDirectory(directoryPath)) {
            System.out.println(directoryPath + " nie jest katalogiem!\n");
            return false;
        } else {
            return true;
        }
    }

    public static void setSftpClient(SftpClient sftpClient) {
        RemoteDirectory.sftpClient = sftpClient;
    }
}
