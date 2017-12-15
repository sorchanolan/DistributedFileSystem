package com.company.sorchanolan;

import com.hubspot.rosetta.jdbi.BindWithRosetta;
import com.hubspot.rosetta.jdbi.RosettaMapperFactory;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;

@RegisterMapperFactory(RosettaMapperFactory.class)
public interface Dao {
  @SqlUpdate("INSERT INTO Client VALUES(:id, :port, :ip_address, :running)")
  void addNewClient(@BindWithRosetta Client client);

  @SqlUpdate("UPDATE Client SET running = false WHERE id = :id")
  void clientOffline(@Bind("id") int id);

  @SqlUpdate("INSERT INTO Cache VALUES(:user_id, :file_id)")
  void addCacheEntry(@BindWithRosetta CacheMapping cacheMapping);
}
