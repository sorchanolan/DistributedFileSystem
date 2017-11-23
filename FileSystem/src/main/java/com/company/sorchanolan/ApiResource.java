package com.company.sorchanolan;

import javax.ws.rs.*;
import java.io.File;
import java.util.Optional;

@Path("/file-server")
public class ApiResource {
  FileDao fileDao = new FileDao();

  @GET
  @Path("/read-file/{fileName}")
  @Produces()
  public Optional<File> readFile(@PathParam("fileName") String fileName) {
    return fileDao.getFile(fileName);
  }

  @POST
  @Path(("/write-file/{fileName}"))
  public void writeFile(@PathParam("fileName") String fileName, File updatedFile) {
    fileDao.getFile(fileName);
  }
}
