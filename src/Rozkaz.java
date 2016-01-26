import java.io.*;
import java.io.Serializable;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Amadeusz on 14.01.2016.
 */
public class Rozkaz implements Serializable{

    String rozkaz;
    String pierwszyCzlon, drugiCzlon, trzeciCzlon;
    int numerPolecenia;


    Rozkaz(String rozkaz, int numerPolecenia)
    {
        this.numerPolecenia = numerPolecenia;
        this.rozkaz = rozkaz;
        podzial(rozkaz);
    }

    public void wykonajRozkaz()
    {
        switch(pierwszyCzlon)
        {
            case "ADD" :
                dodaj();
                break;
            case "MOV" :
                przepisz();
                break;
            case "SUB" :
                odejmij();
                break;
            case "WRITE" :
                zapiszDoPliku();
                break;
            case "READ" :
                try {
                    odczytZPliku();
                } catch (IOException e) {
                    showMessageDialog(null, "Brak pliku");
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    showMessageDialog(null, "Zły format pliku");
                    e.printStackTrace();
                }
                break;
            case "PUSH" :
                pchnijNaStos();
                break;
            case "POP" :
                zdejmijZeStosu();
                break;
            case "INT" :
            {
                wykonajPrzerwanie();
            }
            break;
            default:
                showMessageDialog(null, "Błąd  w poleceniu " + numerPolecenia );
        }
    }

    public void wykonajPrzerwanie()
    {
        String zawartoscRejestru;
        switch (drugiCzlon)
        {
            case "1":
                drugiCzlon = "AX";
                pchnijNaStos();
                Przerwanie.otworzZdjecie();
                zawartoscRejestru = Rejestr.intNaRejestr(1);
                zawartoscRejestru = dopiszZera(zawartoscRejestru);
                zapiszWartoscDoRejestru(zawartoscRejestru);
                zdejmijZeStosu();
                break;
            case "2":
                Przerwanie.otworzInternet();
                break;
            case "3":
                Przerwanie.ustawKursor();
                break;
            case "4":
                Przerwanie.otworzCMD();
                break;
            case "5":
                Przerwanie.podajIP();
                break;
            case "6":
                Przerwanie.grajDzwiek();
                break;
            case "7":
                Przerwanie.zmienTlo();
                break;
            case "8":
                Przerwanie.wycentrujOkno();
                break;
            case "9":
                Przerwanie.podajDateICzas();
                break;
            case "10":
                Przerwanie.zminimalizujOkno();
                break;
        }

    }

    public void zdejmijZeStosu()
    {
        System.out.println("A to nie " + Przerwanie.SP + " o wartosci " + Przerwanie.stos[Przerwanie.SP]);
        Przerwanie.SP--;
        System.out.println("Chcę zapisać SP " + Przerwanie.SP + " o wartosci " + Przerwanie.stos[Przerwanie.SP]);
        String zawartoscRejestru = Rejestr.intNaRejestr(Przerwanie.stos[Przerwanie.SP]);
        zawartoscRejestru = dopiszZera(zawartoscRejestru);

        zapiszWartoscDoRejestru(zawartoscRejestru);
    }

    public void pchnijNaStos()
    {
        String starsza = "";
        String mlodsza = "";

        switch(drugiCzlon)
        {
            case "AX" :
                starsza = Okno.rejestrA[0].getText();
                mlodsza = Okno.rejestrA[1].getText();
                break;
            case "BX" :
                starsza = Okno.rejestrB[0].getText();
                mlodsza = Okno.rejestrB[1].getText();
                break;
            case "CX" :
                starsza = Okno.rejestrC[0].getText();
                mlodsza = Okno.rejestrC[1].getText();
                break;
            case "DX" :
                starsza = Okno.rejestrD[0].getText();
                mlodsza = Okno.rejestrD[1].getText();
                break;
            default:
                showMessageDialog(null, "Podano niepoprawny rejestr w trzecim członie rozkazu w poleceniu " + numerPolecenia);
        }

        short zawartosc =(short)Rejestr.rejestrNaInt(starsza,mlodsza);
        Przerwanie.stos[Przerwanie.SP] = zawartosc;
        System.out.println("na pozycji " + Przerwanie.SP + " mamy " + Przerwanie.stos[Przerwanie.SP]);
        Przerwanie.SP++;
        System.out.println("na pozycji " + Przerwanie.SP + " mamy " + Przerwanie.stos[Przerwanie.SP]);
    }

    public void podzial(String rozkaz)
    {
        String tab[];

        try {

            tab = rozkaz.split(" ", 2);
            pierwszyCzlon = tab[0].trim();
            String reszta = tab[1];

            if(pierwszyCzlon.equals("READ")||pierwszyCzlon.equals("WRITE")||pierwszyCzlon.equals("INT")||pierwszyCzlon.equals("PUSH")||pierwszyCzlon.equals("POP"))
            {
                drugiCzlon = reszta.trim();
                trzeciCzlon = "";
            }
            else
            {
                String tab2[];
                tab2 = reszta.split(",", 2);
                drugiCzlon = tab2[0].trim();
                trzeciCzlon = tab2[1].trim();
            }

        }
        catch (Exception e)
        {
            showMessageDialog(null, "Rozkaz ma zły format w poleceniu " + numerPolecenia);
        }
    }

    public void odczytZPliku() throws IOException,ClassNotFoundException
    {
        String nazwaPliku = drugiCzlon;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(nazwaPliku));

        Okno.polecenie.setText("");
        String textLine = bufferedReader.readLine();
        do {
            Okno.polecenie.setText(Okno.polecenie.getText()  + textLine + '\n');

            textLine = bufferedReader.readLine();
        } while(textLine != null);

        bufferedReader.close();
    }

    public void zapiszDoPliku()
    {
        String nazwaPliku = drugiCzlon;

        PrintWriter zapis = null;
        try {
            zapis = new PrintWriter(nazwaPliku);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String doZapisu = Okno.polecenie.getText();
        doZapisu = doZapisu.replace("WRITE " + nazwaPliku + ";" ,"");
        doZapisu = doZapisu.replace("WRITE " + nazwaPliku,"");

        zapis.print(doZapisu);

        zapis.close();
    }

    public void przepisz()
    {
        String starsza = "";
        String mlodsza = "";
        switch(trzeciCzlon)
        {
            case "AX" :
                starsza = Okno.rejestrA[0].getText();
                mlodsza = Okno.rejestrA[1].getText();
                break;
            case "BX" :
                starsza = Okno.rejestrB[0].getText();
                mlodsza = Okno.rejestrB[1].getText();
                break;
            case "CX" :
                starsza = Okno.rejestrC[0].getText();
                mlodsza = Okno.rejestrC[1].getText();
                break;
            case "DX" :
                starsza = Okno.rejestrD[0].getText();
                mlodsza = Okno.rejestrD[1].getText();
                break;
            default:
                showMessageDialog(null, "Podano niepoprawny rejestr w trzecim członie rozkazu w poleceniu " + numerPolecenia);
        }

        switch(drugiCzlon)
        {
            case "AX" :
                Okno.rejestrA[0].setText(starsza);
                Okno.rejestrA[1].setText(mlodsza);
                break;
            case "BX" :
                Okno.rejestrB[0].setText(starsza);
                Okno.rejestrB[1].setText(mlodsza);
                break;
            case "CX" :
                Okno.rejestrC[0].setText(starsza);
                Okno.rejestrC[1].setText(mlodsza);
            break;
            case "DX" :
                Okno.rejestrD[0].setText(starsza);
                Okno.rejestrD[1].setText(mlodsza);
                break;
            default:
                showMessageDialog(null, "Podano niepoprawny rejestr w drugim członie rozkazu w poleceniu " + numerPolecenia);
        }
    }

    public void odejmij ()
    {
        int roznica = odczytajDrugiCzlon() - odczytajTrzeciCzlon();

        String zawartoscRejestru;

        if (Rejestr.walidacjaWyniku(roznica))
        {
            zawartoscRejestru = Rejestr.intNaRejestr(roznica);
        }
        else
        {
            zawartoscRejestru = "0";
        }

        //System.out.println("Różnica wynosi: " + roznica);


        zawartoscRejestru = dopiszZera(zawartoscRejestru);
        zapiszWartoscDoRejestru(zawartoscRejestru);
    }

    public void dodaj()
    {
        int suma = odczytajDrugiCzlon() + odczytajTrzeciCzlon();
        //System.out.println("Suma wynosi: " + suma);
        String zawartoscRejestru;

        if (Rejestr.walidacjaWyniku(suma))
        {
            zawartoscRejestru = Rejestr.intNaRejestr(suma);
        }
        else
        {
            zawartoscRejestru = "1111111111111111";
        }

        zawartoscRejestru = dopiszZera(zawartoscRejestru);

        zapiszWartoscDoRejestru(zawartoscRejestru);
    }

    public String dopiszZera(String zawartoscRejestru)
    {
        String zero ="";

        for (int i = zawartoscRejestru.length(); i <16; i ++)
        {
            zero = zero + "0";
        }

        zawartoscRejestru = zero + zawartoscRejestru;
        return zawartoscRejestru;
    }

    public void zapiszWartoscDoRejestru(String zawartosc)
    {

        switch(drugiCzlon)
        {
            case "AX" :
                Okno.rejestrA[0].setText(zawartosc.substring(0, 8));
                Okno.rejestrA[1].setText(zawartosc.substring(8, 16));
                break;
            case "BX" :
                Okno.rejestrB[0].setText(zawartosc.substring(0,8));
                Okno.rejestrB[1].setText(zawartosc.substring(8,16));
                break;
            case "CX" :
                Okno.rejestrC[0].setText(zawartosc.substring(0,8));
                Okno.rejestrC[1].setText(zawartosc.substring(8,16));
                break;
            case "DX" :
                Okno.rejestrD[0].setText(zawartosc.substring(0,8));
                Okno.rejestrD[1].setText(zawartosc.substring(8,16));
                break;
            default:
                showMessageDialog(null, "Podano niepoprawny rejestr w drugim członie rozkazu w poleceniu " + numerPolecenia);
        }
    }

    public int odczytajDrugiCzlon()
    {
        int pierwszyRejestr = 0;
        String starsza = "";
        String mlodsza = "";

        switch(drugiCzlon)
        {
            case "AX" :
                starsza = Okno.rejestrA[0].getText();
                mlodsza = Okno.rejestrA[1].getText();
                break;
            case "BX" :
                starsza = Okno.rejestrB[0].getText();
                mlodsza = Okno.rejestrB[1].getText();
                break;
            case "CX" :
                starsza = Okno.rejestrC[0].getText();
                mlodsza = Okno.rejestrC[1].getText();
                break;
            case "DX" :
                starsza = Okno.rejestrD[0].getText();
                mlodsza = Okno.rejestrD[1].getText();
                break;
            default:
                showMessageDialog(null, "Podano niepoprawny rejestr w drugim członie rozkazu w poleceniu " + numerPolecenia);
        }
        try {
            pierwszyRejestr = Rejestr.rejestrNaInt(starsza, mlodsza);
            System.out.println("wartość 1 rejestru" + pierwszyRejestr);
        }
        catch (Exception e)
        {
            System.out.println("Dupa wołowa");
        }
        return pierwszyRejestr;
    }

    public int odczytajTrzeciCzlon()
    {
        int drugiRejestr = 0;
        String starsza = "";
        String mlodsza = "";
        try
        {
            drugiRejestr = Integer.parseInt(trzeciCzlon);
        }
        catch (NumberFormatException e)
        {
            switch(trzeciCzlon)
            {
                case "AX" :
                    starsza = Okno.rejestrA[0].getText();
                    mlodsza = Okno.rejestrA[1].getText();
                    break;
                case "BX" :
                    starsza = Okno.rejestrB[0].getText();
                    mlodsza = Okno.rejestrB[1].getText();
                    break;
                case "CX" :
                    starsza = Okno.rejestrC[0].getText();
                    mlodsza = Okno.rejestrC[1].getText();
                    break;
                case "DX" :
                    starsza = Okno.rejestrD[0].getText();
                    mlodsza = Okno.rejestrD[1].getText();
                    break;
                default:
                    showMessageDialog(null, "Podano niepoprawny format danych w trzecim członie rozkazu w poleceniu " + numerPolecenia);
            }
            drugiRejestr = Rejestr.rejestrNaInt(starsza, mlodsza);
        }
        catch (Exception e)
        {
            showMessageDialog(null, "Podano niepoprawny format danych w trzecim członie rozkazu w poleceniu " + numerPolecenia);
        }
        return drugiRejestr;
    }
}
