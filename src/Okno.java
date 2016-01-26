import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

/**
 * Created by Amadeusz on 14.01.2016.
 */
public class Okno extends JFrame implements Serializable {

    public static JLabel rejestrA[] = new JLabel[2];
    public static JLabel rejestrB[] = new JLabel[2];
    public static JLabel rejestrC[] = new JLabel[2];
    public static JLabel rejestrD[] = new JLabel[2];
    public static JTextArea polecenie = new JTextArea(10,30);
    private JLabel numeracja[] = new JLabel[30];
    JButton nastepnyKrok;
    public static final int Szerokosc = 800;
    public static final int Wyskoosc = 600;
    public static JLabel wynikPrzerwania;
    public static Rozkaz r[];
    int numerPolecenia = 0;
    public static Okno o;


    public Okno ()
    {
        setLayout(null);
        setSize(Szerokosc, Wyskoosc);
        setLocation(0, 0);
        setResizable(false);
        init();
        setVisible(true);
        repaint();
    } // koniec konstruktora

    public static void main(String[] args)
    {
        o = new Okno();
        o.setDefaultCloseOperation(o.EXIT_ON_CLOSE);
    }

    public void init()
    {
        polecenie.setBounds(50,50,350,490);
        add(polecenie);

        JButton wykonaj = new JButton("Wykonaj");
        wykonaj.setBounds(600,400,150,25);
        wykonaj.addMouseListener(new Wykonaj());
        add(wykonaj);

        JButton krokowa = new JButton("Tryb krokowy");
        krokowa.setBounds(600,450,150,25);
        krokowa.addMouseListener(new PracaKrokowa());
        add(krokowa);

        nastepnyKrok = new JButton("Następne polecenie");
        nastepnyKrok.setBounds(600,500,150,25);
        nastepnyKrok.addMouseListener(new NastepnePolecenie());
        nastepnyKrok.setEnabled(false);
        add(nastepnyKrok);

        JLabel czStarsza = new JLabel("część starsza");
        czStarsza.setBounds(550,100,100,20);
        add(czStarsza);

        JLabel czMlodsza = new JLabel("część młodsza");
        czMlodsza.setBounds(650,100,100,20);
        add(czMlodsza);

        wynikPrzerwania = new JLabel();
        wynikPrzerwania.setBounds(200,25,150,20);
        add(wynikPrzerwania);

        for (int i = 0; i <30; i++)
        {
            numeracja[i] = new JLabel(String.valueOf(i+1));
            numeracja[i].setBounds(30,50 + i*16,30,15);
            add(numeracja[i]);

        }

        JLabel nazwa[] = new JLabel[4];

        for (int i = 3; i < 7; i++)
        {
            nazwa[i-3] = new JLabel("rejestr "+ Character.toString ((char) (62 + i))+ ":");
            nazwa[i-3].setBounds(450,i*50,100,20);
            add(nazwa[i-3]);
        }


        rejestrA = new JLabel[2];
        for (int i = 0; i < 2; i++)
        {
            rejestrA[i] = new JLabel("00000101");
            rejestrA[i].setBounds(500+i*100+50, 150, 100, 20);
            add(rejestrA[i]);
        }

        rejestrD = new JLabel[2];
        for (int i = 0; i < 2; i++)
        {
            rejestrD[i] = new JLabel("00000000");
            rejestrD[i].setBounds(500 + i * 100 + 50, 300, 100, 20);
            add(rejestrD[i]);
        }

        rejestrB = new JLabel[2];
        for (int i = 0; i < 2; i++)
        {
            rejestrB[i] = new JLabel("00000000");
            rejestrB[i].setBounds(500 + i*100 + 50, 200, 100, 20);
            add(rejestrB[i]);
        }

        rejestrC = new JLabel[2];
        for (int i = 0; i < 2; i++)
        {
            rejestrC[i] = new JLabel("01000000");
            rejestrC[i].setBounds(500 + i*100 + 50, 250, 100, 20);
            add(rejestrC[i]);
        }
    }

    class Wykonaj extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            dzielRozkazy();
            //System.out.println(r.length);
            for (int i = 0; i<r.length; i++)
            {
                r[i].wykonajRozkaz();
            }
        }
    }
    class PracaKrokowa extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            dzielRozkazy();
            nastepnyKrok.setEnabled(true);
            numerPolecenia = 0;
        }
    }

    class NastepnePolecenie extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            r[numerPolecenia].wykonajRozkaz();
            numerPolecenia++;
            System.out.println("numer polecenia wynosi " + numerPolecenia + "/" + r.length);
            if (numerPolecenia == r.length)
            {
                nastepnyKrok.setEnabled(false);
            }
        }
    }

    public void dzielRozkazy()
    {
        String rozkazy[];
        rozkazy = polecenie.getText().split(";");
        int puste = 0;
        for (int i = 0; i < rozkazy.length; i++)
        {
            if ((rozkazy[i].trim()).equals(""))
            {
                puste++;
            }
        }
        r = new Rozkaz[rozkazy.length-puste];
        int i = 0;

        for(String zadania : rozkazy) {
            if (!(rozkazy[i].trim()).equals(""))
            {
                r[i] = new Rozkaz(zadania, i+1);
                //System.out.println("Rozkaz " + i + " to " + r[i].rozkaz);
                System.out.println("_____________________________");
                if (i == r.length)
                {
                    System.out.println("bal bla " + zadania.charAt(zadania.length() - 1));
                }
            }
            else System.out.println("Tutaj ");
            i++;
        }
    }
}
