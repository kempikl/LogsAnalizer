import com.jcraft.jsch.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

/**
 * A simple SFTP client using JSCH <a href="http://www.jcraft.com/jsch/">...</a>
 */
public final class SftpClient {
    private final String host;
    private final int port;
    private final String username;
    private final JSch jsch;
    private ChannelSftp channel;
    private Session session;

    /**
     * @param host     remote host
     * @param port     remote port
     * @param username remote username
     */
    public SftpClient(String host, int port, String username) {
        this.host = host;
        this.port = port;
        this.username = username;
        jsch = new JSch();
    }

    /**
     * Use default port 22
     *
     * @param host     remote host
     * @param username username on host
     */
    public SftpClient(String host, String username) {
        this(host, 22, username);
    }

    /**
     * File size in bytes
     *
     * @param bytes size of file
     * @return human-readable file size using binary unit
     */
    public static String humanReadableByteCount(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        StringCharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }

    /**
     * Authenticate with remote using password
     *
     * @param password password of remote
     * @throws JSchException If there is problem with credentials or connection
     */
    public void authPassword(String password) throws JSchException {
        session = jsch.getSession(username, host, port);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setPassword(password);
        session.connect();
        channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
    }

    /**
     * Print all files including directories
     *
     * @param remoteDir Directory on remote from which files will be listed
     * @throws SftpException If there is any problem with listing files related to permissions etc
     * @throws JSchException If there is any problem with connection
     */
    @SuppressWarnings("unchecked")
    public void printListFiles(String remoteDir) throws SftpException, JSchException {
        if (channel == null) {
            throw new IllegalArgumentException("Connection is not available");
        }
        System.out.printf("Listing [%s]...%n", remoteDir);
        channel.cd(remoteDir);
        Vector<ChannelSftp.LsEntry> files = channel.ls(".");
        for (ChannelSftp.LsEntry file : files) {
            String name = file.getFilename();
            SftpATTRS attrs = file.getAttrs();
            String permissions = attrs.getPermissionsString();
            String size = humanReadableByteCount(attrs.getSize());
            if (attrs.isDir()) {
                size = "PRE";
            }
            System.out.printf("[%s] %s(%s)%n", permissions, name, size);
        }
    }

    /**
     * List all files including directories
     *
     * @param remoteDir Directory on remote from which files will be listed
     * @return List all files in directory
     * @throws SftpException If there is any problem with listing files related to permissions etc
     * @throws JSchException If there is any problem with connection
     */
    public ArrayList<String> listFiles(String remoteDir) throws SftpException, JSchException {
        ArrayList<String> files = new ArrayList<>();

        if (channel == null) {
            throw new IllegalArgumentException("Connection is not available");
        }
        System.out.printf("Listowanie [%s]...%n", remoteDir);
        channel.cd(remoteDir);
        Vector<ChannelSftp.LsEntry> remoteFiles = channel.ls(".");
        for (ChannelSftp.LsEntry file : remoteFiles) {
            String name = file.getFilename();
            SftpATTRS attrs = file.getAttrs();

            if (!attrs.isDir()) files.add(name);
        }
        return files;
    }

    public boolean isDirectory(String remoteDir) {
        try {
            channel.cd(remoteDir);
            return true;
        } catch (SftpException e) {
            return false;
        }
    }

    /**
     * Download a file from remote
     *
     * @param remotePath full path of remote file
     * @throws SftpException If there is any problem with downloading file related permissions etc
     */
    public InputStreamReader downloadFile(String remotePath) throws SftpException {
        System.out.printf("Pobieranie [%s]...%n", remotePath);
        if (channel == null) {
            throw new IllegalArgumentException("Brak połączenia z serwerem");
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        channel.get(remotePath, outputStream);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return new InputStreamReader(inputStream);
    }

    /**
     * Disconnect from remote
     */
    public void close() {
        if (channel != null) {
            channel.exit();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }
}

