import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalLogFile extends LogFile {
    public LocalLogFile(File parent, String child) {
        super(parent, child);
    }

    @Override
    public String searchInFile() {
        try {
            List<String> matchingLines = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(this));
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
        } catch (IOException ignored) {
            System.out.println("Błąd odczytu \"" + getName() + "\"");
            return null;
        }
    }
}
