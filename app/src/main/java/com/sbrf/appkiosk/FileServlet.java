package com.sbrf.appkiosk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

/**
 * The File servlet for serving from absolute path.
 * @author BalusC
 * @link http://balusc.blogspot.com/2007/07/fileservlet.html
 */
public class FileServlet extends HttpServlet {

    // Constants ----------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.




    // Properties ---------------------------------------------------------------------------------

    private String filePath;
//    private String friendPlistFileName;
//    private String friendIpaFileName;
//    private String apkFileName;
//    private String sbgPlistFileName;
//    private String sbgIpaFileName;

    // Actions ------------------------------------------------------------------------------------


    public void init(ServletConfig paramServletConfig) throws ServletException {

        // Define base path somehow. You can define it as init-param of the servlet.
        String localPath = Settings.deployPath;
        if(localPath != null)
        {
            this.filePath = localPath;
        }else
        {
            throw new ServletException("Не заполнен параметр localPath. Работа невозможна");
        }

        System.out.println("FileServlet.init() " + localPath);
//        friendPlistFileName	= ProjectProperties.getProperty(FRIEND_PLIST_FILE_NAME);
//        friendIpaFileName	= ProjectProperties.getProperty(FRIEND_IPA_FILE_NAME);
//        apkFileName		= ProjectProperties.getProperty(APK_FILE_NAME);
//        sbgPlistFileName	= ProjectProperties.getProperty(SBG_PLIST_FILE_NAME);
//        sbgIpaFileName	= ProjectProperties.getProperty(SBG_IPA_FILE_NAME);



        // In a Windows environment with the Applicationserver running on the
        // c: volume, the above path is exactly the same as "c:\files".
        // In UNIX, it is just straightforward "/files".
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        // Get requested file by path info.
        String requestedFile = request.getPathInfo();

        // Check if file is actually supplied to the request URI.
        if (requestedFile == null) {
            // Do your thing if the file is not supplied to the request URI.
            // Throw an exception, or send 404, or show default/warning page, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "запрошен файл null"); // 404.
            return;
        }else if (!requestedFile.equalsIgnoreCase(Settings.friendPlistFileName) && !requestedFile.equalsIgnoreCase(Settings.friendIpaFileName) && !requestedFile.equalsIgnoreCase(Settings.sbgPlistFileName) && !requestedFile.equalsIgnoreCase(Settings.sbgIpaFileName) && !requestedFile.equalsIgnoreCase(Settings.apkFileName ))
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "   ");
        }

        // Decode the file name (might contain spaces and on) and prepare file object.
        File file = new File(filePath, URLDecoder.decode(requestedFile, "UTF-8"));

        // Check if file actually exists in filesystem.
        if (!file.exists()) {
            // Do your thing if the file appears to be non-existing.
            // Throw an exception, or send 404, or show default/warning page, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND, " файл не найден " + file.getAbsolutePath()); // 404.
            return;
        }

        // Get content type by filename.
        String contentType= "application/octet-stream";

        if (requestedFile.equalsIgnoreCase(Settings.friendPlistFileName) || requestedFile.equalsIgnoreCase(Settings.sbgPlistFileName))
        {
            contentType= "application/x-plist";
        }



        // Init servlet response.
        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setContentType(contentType);
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

        // Prepare streams.
        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        try {
            // Open streams.

            final OutputStream out = response.getOutputStream();

            if (requestedFile.equalsIgnoreCase(Settings.friendPlistFileName))
            {
                String plist = FileUtils.readFileToString(file, "UTF-8");

                URL base = new URL(request.getScheme(), request.getServerName(), "");

                String modifedPlist=plist.replace("{ipa_file}", base + request.getServletContext().getContextPath() + "/file"+Settings.friendIpaFileName);

                response.setHeader("Content-Length", String.valueOf(modifedPlist.length()));
                Writer w= new OutputStreamWriter(out, "UTF-8");

                w.write(modifedPlist);
                w.flush();
                w.close();
            }else if (requestedFile.equalsIgnoreCase(Settings.sbgPlistFileName))
            {
                String plist = FileUtils.readFileToString(file, "UTF-8");

                URL base = new URL(request.getScheme(), request.getServerName(), "");

                String modifedPlist=plist.replace("{ipa_file}", base + request.getServletContext().getContextPath() + "/file"+Settings.sbgIpaFileName);

                response.setHeader("Content-Length", String.valueOf(modifedPlist.length()));
                Writer w= new OutputStreamWriter(out, "UTF-8");

                w.write(modifedPlist);
                w.flush();
                w.close();
            }else
            {
                input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
                output = new BufferedOutputStream(out, DEFAULT_BUFFER_SIZE);

                // Write file contents to response.
                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            }
            out.flush();
        }
        catch(Exception err)
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "запрошен файл null"); // 404.
        }
        finally {
            // Gently close streams.
            close(output);
            close(input);
        }
    }

    // Helpers (can be refactored to public utility class) ----------------------------------------

    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                // Do your thing with the exception. Print it, log it or mail it.
                e.printStackTrace();
            }
        }
    }

}
