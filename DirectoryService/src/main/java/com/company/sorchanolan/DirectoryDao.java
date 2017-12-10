package com.company.sorchanolan;

import com.hubspot.rosetta.jdbi.BindWithRosetta;
import com.hubspot.rosetta.jdbi.RosettaMapperFactory;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import java.util.List;
import java.util.Optional;

@RegisterMapperFactory(RosettaMapperFactory.class)
public interface DirectoryDao {
  @SqlUpdate("INSERT INTO File VALUES(:id, :file_name)")
  void addNewFile(@Bind("id") int id, @Bind("file_name") String fileName);

  @SqlUpdate("INSERT INTO FileServer VALUES(:id, :port, :ip_address)")
  void addNewServer(@BindWithRosetta FileServer fileServer);

  @SqlUpdate("INSERT INTO ServerFileMapping VALUES(:file_server_id, :file_id)")
  void addNewMapping(@Bind("file_server_id") int fileServerId, @Bind("file_id") int fileId);

  @SqlQuery("SELECT * FROM FileServer WHERE id = (SELECT file_server_id FROM ServerFileMapping WHERE file_id = (SELECT id FROM File WHERE file_name = :file_name))")
  List<FileServer> getServersHoldingFile(@Bind("file_name") String fileName);

  @SqlQuery("SELECT file_name FROM File")
  List<String> getAllFileNames();

  @SqlQuery("SELECT * FROM FileServer ORDER BY RAND() LIMIT 1")
  FileServer getRandomFileServer();

  @SqlQuery("SELECT * FROM FileServer WHERE ip_address = :ip_address AND port = :port LIMIT 1")
  List<FileServer> getFileServer(@Bind("ip_address") String ipAddress, @Bind("port") int port);

  @SqlQuery("SELECT MAX(id) FROM ((SELECT id FROM FileServer UNION ALL SELECT id FROM File) AS id)")
  int getCurrentIdCounter();
}
