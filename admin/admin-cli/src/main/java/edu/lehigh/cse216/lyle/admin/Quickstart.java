package edu.lehigh.cse216.lyle.admin;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.User;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class Quickstart {
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME =
            "Lyle Buzz";

    private static InputStream GOOGLE_APPLICATION_CREDENTIALS;
//    = "/Users/Kelli/Documents/Documents_Kellis MacBook Pro/cse216/cse216_lyle/admin/admin-cli/src/main/resources/app_credentials.json";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/drive-java-quickstart");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                Quickstart.class.getClassLoader().getResourceAsStream("client_secret.json");

        GOOGLE_APPLICATION_CREDENTIALS = Quickstart.class.getClassLoader().getResourceAsStream("app_credentials.json");

        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow =
//                new GoogleAuthorizationCodeFlow.Builder(
//                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                        .setDataStoreFactory(DATA_STORE_FACTORY)
//                        .setAccessType("offline")
//                        .build();

//        Credential credential = new AuthorizationCodeInstalledApp(
//                flow, new LocalServerReceiver()).authorize("user");

        GoogleCredential credential;
//        try {
//            credential = GoogleCredential.getApplicationDefault();
//        } catch (IOException e) {
        credential = GoogleCredential.fromStream(GOOGLE_APPLICATION_CREDENTIALS).createScoped(SCOPES);
//        }

//        System.out.println(
//                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     *
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void main(String[] args) throws IOException {
        printFiles();
    }

    /**
     * @throws IOException
     */
    public static void printFiles() throws IOException {
        // Build a new authorized API client service.
        Drive service = getDriveService();

        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name, modifiedTime, owners)")
                .execute();

        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            System.out.println("File Name\tFile Owner\tLast Modified");
            for(int i = 0; i < 95; i++){
                System.out.print("-");
            }
            for (File file : files) {
                List <User> owner = file.getOwners();
                String owners = "";
                for(int i = 0; i < owner.size(); i++){
                    owners += owner.get(i).getDisplayName();
                    if(i == owner.size() - 1)
                        break;
                    owners += ", ";
                }
//                System.out.printf("%s (%s)\n", file.getOwners(), file.getName());
                System.out.println("\n" + file.getName() + "\t" + owners + "\t" + file.getModifiedTime());
            }
        }
    }
}