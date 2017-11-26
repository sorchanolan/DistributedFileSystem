package com.company.sorchanolan;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.Optional;

@Path("/file-server")
public class ApiResource {
  private FileDao fileDao = new FileDao();

  @GET
  @Path("/read-file/{fileName}")
  @Produces()
  public Optional<String> readFile(@PathParam("fileName") String fileName) throws IOException {
    return fileDao.getFile(fileName);
  }

  @POST
  @Path(("/write-file/{fileName}"))
  public void writeFile(@PathParam("fileName") String fileName, String updatedFileContents) throws Exception {
    fileDao.updateFile(fileName, updatedFileContents);
  }
}
