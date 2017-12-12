package com.company.sorchanolan;

import com.hubspot.rosetta.jdbi.BindWithRosetta;
import com.hubspot.rosetta.jdbi.RosettaMapperFactory;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;

import java.util.List;
import java.util.Optional;

@RegisterMapperFactory(RosettaMapperFactory.class)
public interface DirectoryDao {
  @SqlUpdate("INSERT INTO File VALUES(:id, :file_name)")
  void addNewFile(@Bind("id") int id, @Bind("file_name") String fileName);

  @SqlUpdate("INSERT INTO FileServer VALUES(:id, :port, :ip_address, :running)")
  void addNewServer(@BindWithRosetta FileServer fileServer);

  @SqlUpdate("INSERT INTO ServerFileMapping VALUES(:file_server_id, :file_id)")
  void addNewMapping(@Bind("file_server_id") int fileServerId, @Bind("file_id") int fileId);

  @SqlQuery("SELECT file_name FROM File f JOIN ServerFileMapping m ON m.file_id = f.id JOIN FileServer fs ON m.file_server_id = fs.id WHERE f.file_name = :file_name AND fs.running = true")
  List<FileServer> getServersHoldingFile(@Bind("file_name") String fileName);

  @SqlQuery("SELECT file_name FROM File f JOIN ServerFileMapping m ON m.file_id = f.id JOIN FileServer fs ON m.file_server_id = fs.id WHERE fs.running = true")
  List<String> getAllFileNamesFromRunningServers();

  @SqlQuery("SELECT * FROM FileServer ORDER BY RAND() LIMIT 1")
  FileServer getRandomFileServer();

  @SqlQuery("SELECT * FROM FileServer WHERE ip_address = :ip_address AND port = :port LIMIT 1")
  List<FileServer> getFileServer(@Bind("ip_address") String ipAddress, @Bind("port") int port);

  @SqlQuery("SELECT id FROM File WHERE file_name = :file_name")
  List<Integer> getFileId(@Bind("file_name") String fileName);

  @SqlQuery("SELECT MAX(id) FROM ((SELECT id FROM FileServer UNION ALL SELECT id FROM File) AS id)")
  int getCurrentIdCounter();

  @SqlUpdate("INSERT INTO FileLock VALUES(:id, :file_id, :status, :valid_until, :user_id)")
  void addNewFileLock(@BindWithRosetta FileLock fileLock);

  @SqlUpdate("UPDATE FileLock SET status = :status WHERE file_id = :file_id AND user_id = :user_id")
  void updateLockStatus(@Bind("status") boolean status, @Bind("file_id") int fileId, @Bind("user_id") int userId);

  @SqlQuery("SELECT EXISTS (SELECT * FROM FileLock WHERE file_id = :file_id AND status = true AND valid_until > :current_time)")
  boolean lockExists(@Bind("file_id") int fileId, @Bind("current_time") long currentTime);

  @SqlUpdate("UPDATE FileServer SET running = false")
  void setAllFileServersToNotRunning();

  @SqlUpdate("UPDATE FileServer SET running = true WHERE id = :id")
  void setFileServerToRunning(@Bind("id") int id);
}
