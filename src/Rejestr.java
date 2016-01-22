import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Amadeusz on 14.01.2016.
 */
public class Rejestr {

    public static int rejestrNaInt(String starsza, String mlodsza)
    {
        int czL = Integer.parseInt(mlodsza, 2);
        int czH = Integer.parseInt(starsza, 2) << 8;
        // System.out.println("czesci " + czH + " " + czL);
        int suma = czL + czH;
        return suma;
    }

    public static String intNaRejestr(int n)
    {
        return Integer.toString(n,2);
    }

    public static boolean walidacjaWyniku(int liczba)
    {
        if(liczba>65535 || liczba < 0)
        {
            showMessageDialog(null, "Uzyskano liczbę spoza zakresu 0 - 65535");
            return false;
        }
        else return true;
    }

}
