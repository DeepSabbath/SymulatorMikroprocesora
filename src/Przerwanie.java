import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Amadeusz on 22.01.2016.
 */
public class Przerwanie {

    private static Dimension ekranRozmiar;
    public static int stos [] = new int [16];
    public static short SP = 0;

    public static void otworzZdjecie() {
        JFrame obrazek = new JFrame();
        obrazek.setSize(301, 301);
        JButton jb = new JButton(new ImageIcon("logo_eti.png"));
        jb.setBorderPainted(false);
        jb.setFocusPainted(false);
        obrazek.add(jb);
        obrazek.setResizable(false);
        obrazek.setVisible(true);
    }

    public static void otworzInternet() {
        String url = "http://eti.pg.edu.pl/";
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ustawKursor() {
        Robot r = null;
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        pobierzWielkoscEkranu();
        r.mouseMove((ekranRozmiar.width ) / 2, (ekranRozmiar.height) / 2);

    }

    public static void otworzCMD() {
        try {
            Runtime.getRuntime().exec("cmd.exe /c start");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void podajIP(){
        try {
            Okno.wynikPrzerwania.setText("IP: " + Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public static void grajDzwiek() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("bb8.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
    public static void zminimalizujOkno() {
        Okno.o.setState(Frame.ICONIFIED);
    }
    public static void zmienTlo(){
        Okno.polecenie.setBackground(Color.black);
        Okno.polecenie.setForeground(Color.white);
    }
    public static void wycentrujOkno(){
        pobierzWielkoscEkranu();
        Dimension oknoRozmiar = Okno.o.getSize();
        if (oknoRozmiar.height > ekranRozmiar.height)
            oknoRozmiar.height = ekranRozmiar.height;
        if (oknoRozmiar.width > ekranRozmiar.width)
            oknoRozmiar.width = ekranRozmiar.width;
        Okno.o.setLocation((ekranRozmiar.width - oknoRozmiar.width) / 2, (ekranRozmiar.height - oknoRozmiar.height) / 2);
    }
    public static void podajDateICzas(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss   dd/MM/yyyy ");
        Date date = new Date();
        Okno.wynikPrzerwania.setText(dateFormat.format(date));
    }
    public static void pobierzWielkoscEkranu() {
        ekranRozmiar = Toolkit.getDefaultToolkit().getScreenSize();
    }
}
