package models.backend;

/**
 * Created with IntelliJ IDEA.
 * User: Alexander
 * Date: 19.12.12
 * Time: 08:22
 * To change this template use File | Settings | File Templates.
 */
public class UserMindmapInfo {
    public String mmIdOnServer;
    public String mmIdInternal;
    public String revision;
    public String filePath;
    public String fileName;

    public UserMindmapInfo(String mmIdOnServer, String mmIdInternal, String revision, String filePath, String fileName) {
        this.mmIdOnServer = mmIdOnServer;
        this.mmIdInternal = mmIdInternal;
        this.revision = revision;
        this.filePath = filePath;
        this.fileName = fileName;
    }
}

