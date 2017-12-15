package com.company.sorchanolan;

import com.company.sorchanolan.Models.*;
import com.hubspot.rosetta.jdbi.BindWithRosetta;
import com.hubspot.rosetta.jdbi.RosettaMapperFactory;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import java.util.List;

@UseStringTemplate3StatementLocator
@RegisterMapperFactory(RosettaMapperFactory.class)
public interface DirectoryDao {
  @SqlUpdate("INSERT INTO File VALUES(:id, :file_name)")
  void addNewFile(@Bind("id") int id, @Bind("file_name") String fileName);

  @SqlUpdate("INSERT INTO FileServer VALUES(:id, :port, :ip_address, :running)")
  void addNewServer(@BindWithRosetta FileServer fileServer);

  @SqlUpdate("INSERT INTO ServerFileMapping VALUES(:file_server_id, :file_id)")
  void addNewMapping(@Bind("file_server_id") int fileServerId, @Bind("file_id") int fileId);

  @SqlQuery("SELECT * FROM FileServer fs JOIN ServerFileMapping m ON m.file_server_id = fs.id JOIN File f ON m.file_id = f.id WHERE f.file_name = :file_name AND fs.running = true")
  List<FileServer> getServersHoldingFile(@Bind("file_name") String fileName);

  @SqlQuery("SELECT DISTINCT file_name FROM File f JOIN ServerFileMapping m ON m.file_id = f.id JOIN FileServer fs ON m.file_server_id = fs.id WHERE fs.running = true")
  List<String> getAllFileNamesFromRunningServers();

  @SqlQuery("SELECT DISTINCT file_name, port, ip_address FROM File f JOIN ServerFileMapping m ON m.file_id = f.id JOIN FileServer fs ON m.file_server_id = fs.id")
  List<ServerFileMapping> getServerFileMappings();

  @SqlQuery("SELECT * FROM FileServer WHERE id NOT IN (<file_server_ids>) AND running = true ORDER BY RAND() LIMIT 1")
  FileServer getRandomFileServer(@BindIn("file_server_ids") List<Integer> fileServerIds);

  @SqlQuery("SELECT * FROM FileServer WHERE running = true ORDER BY RAND() LIMIT 1")
  FileServer getRandomFileServer();

  @SqlQuery("SELECT * FROM FileServer WHERE ip_address = :ip_address AND port = :port")
  List<FileServer> getFileServer(@Bind("ip_address") String ipAddress, @Bind("port") int port);

  @SqlQuery("SELECT id FROM File WHERE file_name = :file_name")
  List<Integer> getFileId(@Bind("file_name") String fileName);

  @SqlQuery("SELECT file_name, id FROM File WHERE file_name IN (<file_name>)")
  List<File> getFiles(@BindIn("file_name") List<String> fileNames);

  @SqlQuery("SELECT MAX(id) FROM ((SELECT id FROM FileServer UNION ALL SELECT id FROM File UNION ALL SELECT id FROM FileLock UNION ALL SELECT id FROM Client) AS id)")
  int getCurrentIdCounter();

  @SqlUpdate("INSERT INTO FileLock VALUES(:id, :file_id, :status, :valid_until, :user_id)")
  void addNewFileLock(@BindWithRosetta FileLock fileLock);

  @SqlUpdate("UPDATE FileLock SET status = :status WHERE file_id = :file_id AND user_id = :user_id")
  void updateLockStatus(@Bind("status") boolean status, @Bind("file_id") int fileId, @Bind("user_id") int userId);

  @SqlQuery("SELECT EXISTS (SELECT * FROM FileLock WHERE file_id = :file_id AND status = true AND valid_until > :current_time)")
  boolean lockExists(@Bind("file_id") int fileId, @Bind("current_time") long currentTime);

  @SqlUpdate("UPDATE FileLock SET status = false WHERE user_id = :user_id")
  void removeLocksForUser(@Bind("user_id") int userId);

  @SqlUpdate("UPDATE FileServer SET running = false")
  void setAllFileServersToNotRunning();

  @SqlUpdate("UPDATE FileServer SET running = true WHERE id = :id")
  void setFileServerToRunning(@Bind("id") int id);

  @SqlUpdate("INSERT INTO LockQueue VALUES(:file_id, :user_id, :timestamp)")
  void addToLockQueue(@BindWithRosetta LockQueueEntry lockQueueEntry);

  @SqlUpdate("DELETE FROM LockQueue WHERE file_id = :file_id AND :user_id = :user_id AND timestamp = :timestamp")
  void removeFromLockQueue(@BindWithRosetta LockQueueEntry lockQueueEntry);

  @SqlQuery("SELECT * FROM LockQueue WHERE timestamp IN (SELECT MIN(timestamp) FROM LockQueue GROUP BY file_id)")
  List<LockQueueEntry> getTopsOfQueues();

  @SqlUpdate("INSERT IGNORE INTO Client VALUES(:id, :port, :ip_address, :running)")
  void addNewClient(@BindWithRosetta Client client);

  @SqlUpdate("UPDATE Client SET running = false WHERE id = :id")
  void clientOffline(@Bind("id") int id);

  @SqlQuery("SELECT * FROM Client WHERE id = :id ORDER BY id ASC")
  List<Client> getClient(@Bind("id") int id);
}
