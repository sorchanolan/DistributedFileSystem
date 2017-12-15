package com.company.sorchanolan;

import com.company.sorchanolan.Models.CacheMapping;
import com.company.sorchanolan.Models.Client;
import com.company.sorchanolan.Models.FileMap;
import com.hubspot.rosetta.jdbi.BindWithRosetta;
import com.hubspot.rosetta.jdbi.RosettaMapperFactory;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;

import java.util.List;

@RegisterMapperFactory(RosettaMapperFactory.class)
public interface Dao {
  @SqlUpdate("INSERT IGNORE INTO Client VALUES(:id, :port, :ip_address, :running)")
  void addNewClient(@BindWithRosetta Client client);

  @SqlUpdate("UPDATE Client SET running = false WHERE id = :id")
  void clientOffline(@Bind("id") int id);

  @SqlUpdate("INSERT IGNORE INTO Cache VALUES(:file_id, :user_id)")
  void addCacheEntry(@BindWithRosetta CacheMapping cacheMapping);

  @SqlUpdate("INSERT IGNORE INTO File VALUES(:id, :file_name)")
  void addNewFile(@BindWithRosetta FileMap fileMap);

  @SqlQuery("SELECT id FROM File WHERE file_name = :file_name ORDER BY id ASC")
  List<Integer> getFile(@Bind("file_name") String fileName);
}
