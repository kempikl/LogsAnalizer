import java.io.File;

public abstract class LogFile extends File {
    protected static String searchValue;

    public LogFile(String parent, String child) {
        super(parent, child);
    }

    public LogFile(File parent, String child) {
        super(parent, child);
    }

    public abstract String searchInFile();

    public static void setSearchValue(String searchValue) {
        LogFile.searchValue = searchValue;
    }

    public static String getSearchValue() {
        return searchValue;
    }
}
