import com.jcraft.jsch.SftpException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RemoteLogFile extends LogFile {
    public RemoteLogFile(String parent, String child) {
        super(parent, child);
    }

    @Override
    public String searchInFile() {
        try {
            List<String> matchingLines = new ArrayList<>();
            BufferedReader br = new BufferedReader(RemoteDirectory.sftpClient.downloadFile(this.getPath()));
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.contains(searchValue)) {
                    matchingLines.add("Linia " + lineNumber + ": " + line);
                }
            }
            br.close();
            if (!matchingLines.isEmpty()) {
                matchingLines.add(0, getName());
                matchingLines.add("");
                return String.join(System.lineSeparator(), matchingLines);
            } else return null;
        } catch (IOException | SftpException e) {
            System.out.println("Błąd odczytu \"" + getName() + "\"");
            return null;
        }
    }

    @Override
    public String getPath() {
        return super.getPath().replace('\\', '/');
    }
}
