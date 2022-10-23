import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.*;
import java.security.GeneralSecurityException;
import java.io.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

/* class to demonstrate use of Gmail list labels API */
public class GmailQuickstart {
  /**
   * Application name.
   */
  private static final String APPLICATION_NAME = "Gmail API Java Get Message";
  /**
   * Global instance of the JSON factory.
   */
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  /**
   * Directory to store authorization tokens for this application.
   */
  private static final String TOKENS_DIRECTORY_PATH = "tokens";


  /**
   * Global instance of the scopes required by this quickstart.
   * If modifying these scopes, delete your previously saved tokens/ folder.
   */
  //   private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_LABELS);
  //private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
  private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_READONLY);
  private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

  /**
   * Creates an authorized Credential object.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
      throws IOException {
    // Load client secrets.
    InputStream in = GmailQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    if (in == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
        .setAccessType("offline")
        .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("use");
    //returns an authorized Credential object.
    return credential;
  }

  //public static void main(String... args) throws IOException, GeneralSecurityException {
    public  GmailQuickstart() throws IOException, GeneralSecurityException {
    
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
      .setApplicationName(APPLICATION_NAME)
      .build();
    String user = "me";
    
    ListMessagesResponse messageResponse = service.users().messages().list(user).execute();
    List<Message> messages = messageResponse.getMessages();

    Message m = messages.get(messages.size()-1);
    Message gmailMessage = service.users().messages().get(user, m.getId()).setFormat("full").execute();
    String body = ("Payload: \n" + StringUtils.newStringUtf8(Base64.decodeBase64(gmailMessage.getPayload().getParts().get(0).getBody().getData())));

    PrintStream output = new PrintStream("output.txt");
    output.print(body);

    /* 
    // This would find the body for all gmail bodies in the account
    int counter = 0;

    for (Message m : messages) {
      Message gmailMessage = service.users().messages().get(user, m.getId()).setFormat("full").execute();
      //String body = gmailMessage.getPayload().getBody().getData();
      // System.out.println(body);
      System.out.println("Payload, " + StringUtils.newStringUtf8(Base64.decodeBase64(gmailMessage.getPayload().getParts().get(0).getBody().getData()) ));
      // System.out.println(bodyTest);
      // System.out.println("Payload2, " + gmailMessage.payload.parts[counter]);
      
      // System.out.println(StringUtils.newStringUtf8(Base64.decodeBase64(gmailMessage.getPayload().getBody().getData())));
      counter++;
    }
*/

  }
}