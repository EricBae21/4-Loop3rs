import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

public class mainClass {
    
    public static void main(String[] args) throws FileNotFoundException, IOException, GeneralSecurityException, URISyntaxException {
        // 1) scrape mail
        // 2) Process file
        // 3) Find Coordinates
        // 4) run map

        GmailQuickstart scrapeGmail = new GmailQuickstart();
        ProcessFile processFile = new ProcessFile();
        URL in = GmailQuickstart.class.getResource("FindCoordinates.py");
        String path = Paths.get(in.toURI()).toAbsolutePath().toString();

        Process p = new ProcessBuilder("Python", path).start();

        GoogleMaps maps = new GoogleMaps();
    }
}
