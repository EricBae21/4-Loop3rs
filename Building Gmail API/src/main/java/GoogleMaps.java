import static com.teamdev.jxbrowser.engine.RenderingMode.*;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import java.awt.BorderLayout;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.util.*;
import java.io.*;
import java.nio.file.Paths;

public class GoogleMaps {

    private static final int MIN_ZOOM = 0;
    private static final int MAX_ZOOM = 21;

    public static String setMarker(double x, double y) {
        String setMarkerScript =
            "var myLatlng = new google.maps.LatLng(" + x + "," + y + ");\n" +
                    "var marker = new google.maps.Marker({\n" +
                    "    position: myLatlng,\n" +
                    "    map: map,\n" +
                    "    title: 'Hello World!'\n" +
                    "});";
        return setMarkerScript;
    }

    /**
     * In map.html file default zoom value is set to 4.
     */
    private static int zoomValue = 4;

    //public static void main(String[] args) throws FileNotFoundException{
    public GoogleMaps() throws FileNotFoundException {
        Scanner input = new Scanner(new File("coordinates.txt"));
        String line = input.nextLine();
        String lat = line.split(" ")[0];
        String lon = line.split(" ")[1];
        double x = Double.parseDouble(lat);
        double y = Double.parseDouble(lon);

        Engine engine = Engine.newInstance(
            EngineOptions.newBuilder(HARDWARE_ACCELERATED)
                    .licenseKey("1BNDHFSC1G4AHPK89AIN23FZKRXP17HXAEY18ZB73XZ2I0LXU1R2H1UIQKEAILW6W97HGW")
                    .build());

        Browser browser = engine.newBrowser();

        SwingUtilities.invokeLater(() -> {
            BrowserView view = BrowserView.newInstance(browser);

            JButton zoomInButton = new JButton("Zoom In");
            zoomInButton.addActionListener(e -> {
                if (zoomValue < MAX_ZOOM) {
                    browser.mainFrame().ifPresent(frame ->
                            frame.executeJavaScript("map.setZoom(" +
                                    ++zoomValue + ")"));
                }
            });

            JButton zoomOutButton = new JButton("Zoom Out");
            zoomOutButton.addActionListener(e -> {
                if (zoomValue > MIN_ZOOM) {
                    browser.mainFrame().ifPresent(frame ->
                            frame.executeJavaScript("map.setZoom(" +
                                    --zoomValue + ")"));
                }
            });

            JButton setMarkerButton = new JButton("Set Marker");
            setMarkerButton.addActionListener(e ->
                    browser.mainFrame().ifPresent(frame ->
                            frame.executeJavaScript(setMarker(x, y))));

            JPanel toolBar = new JPanel();
            toolBar.add(zoomInButton);
            toolBar.add(zoomOutButton);
            toolBar.add(setMarkerButton);

            JFrame frame = new JFrame("Google Maps");
            frame.add(toolBar, BorderLayout.SOUTH);
            frame.add(view, BorderLayout.CENTER);
            frame.setSize(800, 500);
            frame.setVisible(true);

            String in = GmailQuickstart.class.getResource("index.html").toString();
            browser.navigation().loadUrl(in);
        });
    }
}