package edu.upc.eetac.ea.group1.pandora.api.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/file")
public class UploadFileService {

	@Context
	private Application app;

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void uploadImage(@FormDataParam("title") String title,
			@FormDataParam("image") InputStream image,
			@FormDataParam("image") FormDataContentDisposition fileDisposition) {
		System.out.println("llego aqui");
		UUID uuid = writeAndConvertImage(image);
	}

	private UUID writeAndConvertImage(InputStream file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			throw new InternalServerErrorException(
					"Something has been wrong when reading the file.");
		}
		UUID uuid = UUID.randomUUID();
		String filename = uuid.toString() + ".jpg";
		try {
			ImageIO.write(
					image,
					"jpg",
					new File(app.getProperties().get("uploadFolder") + filename));
		} catch (IOException e) {
			throw new InternalServerErrorException(
					"Something has been wrong when converting the file.");
		}
		return uuid;
	}
}
