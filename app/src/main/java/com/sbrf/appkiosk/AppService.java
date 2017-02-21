package com.sbrf.appkiosk;

import java.io.*;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.http.HTTPException;

import com.google.gson.Gson;

@Path("/appService")
@Consumes(MediaType.APPLICATION_JSON )
public class AppService
{


	@GET
	@Path("/{ver}")
	@Produces(MediaType.APPLICATION_JSON+";charset=UTF-8")
	public String getVersion(@PathParam ("ver") String version) throws HTTPException
	{
		System.out.println("AppService.getVersion()" + version);

		File releaseNotesFile = new File(Settings.releaseFileName);
		if (releaseNotesFile.exists()) {
			try(BufferedReader br = new BufferedReader(new FileReader(releaseNotesFile))) {

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		String currentVersion = "";
		System.out.println("AppService.getVersion()" + version);
		try
		{
			BufferedReader br;

			br = new BufferedReader(new InputStreamReader( new FileInputStream(releaseNotesFile), "UTF8"));
			currentVersion =  br.readLine();
			VersionQueryResponse responce= new VersionQueryResponse();

			boolean isMandatory=false;

			if (currentVersion.contains("!"))
			{
				isMandatory=true;
				currentVersion=currentVersion.replace("!", "");
			}


			if (version.equalsIgnoreCase(currentVersion))
			{
				responce.setStatus("ok");
			}else
			{
				responce.setVersion(currentVersion);
				responce.setStatus("update");
				if (isMandatory) responce.setMandatory(true);
				List<String> releaseNotes = responce.getReleaseNotes();
				while (true)
				{
					String f = br.readLine();
					if (f!=null)
					{
						releaseNotes.add(f);
					}else
					{
						break;
					}
				}

			}

			br.close();

			Gson gson = new Gson();

			return gson.toJson(responce);

		} catch (Exception e)
		{
			e.printStackTrace();
			throw new HTTPException(500);
		}




	}

}
