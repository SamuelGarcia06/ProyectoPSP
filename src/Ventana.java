import javafx.scene.control.Spinner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class Ventana extends JFrame implements ActionListener {
    private ArrayList<String> lineas = new ArrayList<>();
    private JButton convertir;
    private JPasswordField pswd;
    private JSpinner spnrTiempo;
    private String pswdStr = "";
    private int tiempo = 10;
    private JPanel pPrincipal;
    private JPanel pCodificar;
    private JPanel pDecodificar;
    private CardLayout cl;


    private JLabel contador;
    private JButton btnDecodificar;
    private TextField contrasenia;

    private Thread t1;

    

    private void instancias() {
        t1 = new Thread(contador());

        spnrTiempo = new JSpinner();
        spnrTiempo.setValue(10);
        pCodificar = new JPanel();
        pDecodificar = new JPanel();
        convertir = new JButton("CODIFICAR");
        convertir.addActionListener(this);
        pswd = new JPasswordField();
        pswd.setColumns(20);
        pPrincipal = new JPanel();
        cl = new CardLayout();

        pDecodificar = new JPanel();
        contador = new JLabel();
        btnDecodificar = new JButton("OK");
        btnDecodificar.addActionListener(this);
        contrasenia = new TextField();
        contrasenia.setColumns(20);

    }

    public void initGUI() {
        crearTXT();
        setVisible(true);
        setDefaultCloseOperation(3);
        setSize(500, 500);
        this.add(pPrincipal());
    }

    public Ventana() {
        instancias();
        initGUI();

    }

    public JPanel pPrincipal() {
        pPrincipal.setLayout(cl);
        pPrincipal.add(pCodificar(), "code");
        pPrincipal.add(pDecodificar(), "decode");
        cl.show(pPrincipal, "code");

        return pPrincipal;
    }

    private JPanel pDecodificar() {
        pDecodificar.add(contador);
        pDecodificar.add(contrasenia);
        pDecodificar.add(btnDecodificar);
        return pDecodificar;
    }

    private Runnable contador() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                long tiempoAct = System.currentTimeMillis();
                while (true) {
                    int cuenta = (int) (System.currentTimeMillis() - tiempoAct) / 1000;
                    contador.setText(Integer.toString(cuenta) + "/" + tiempo);

                    if (cuenta == tiempo) {
                        borrarTXT();
                        System.exit(0);
                    }
                }
            }
        };
        return r;
    }
    
    public void crearTXT() {
        ProcessBuilder pb = new ProcessBuilder("powerShell.exe", "/c", "notepad texto.txt");
        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void abrirTXT() {
        ProcessBuilder pb = new ProcessBuilder("powerShell.exe", "/c", "start texto.txt");
        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void borrarTXT() {
        ProcessBuilder pb = new ProcessBuilder("powerShell.exe", "/c", "rm texto.txt");
        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JPanel pCodificar() {
        pCodificar.add(spnrTiempo);
        pCodificar.add(pswd);
        pCodificar.add(convertir);
        return pCodificar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == convertir) {
            char[] pswdChar = pswd.getPassword();
            pswdStr = "";
            for (int i = 0; i < pswd.getPassword().length; i++) {
                pswdStr += pswdChar[i];
            }
            tiempo = (int) spnrTiempo.getValue();

            cl.show(pPrincipal, "decode");
            codificar();
            t1.start();

        } else if (e.getSource() == btnDecodificar) {
            if (pswdStr.equals(contrasenia.getText())) {
                decodificar();
                abrirTXT();
                System.exit(0);

            }
        }
    }


    private void codificar() {
        int codificador;


        try {
            File f = new File("./texto.txt");
            BufferedReader br = new BufferedReader(new FileReader(f));
            String linea;
            while ((linea = br.readLine()) != null) {                           //GUARDA EN EL ARRAY LIST LAS LINEAS DEL TXT
                lineas.add(linea);
            }
            br.close();


            codificador = (int) ((Math.random() * 9) + 1);                       //Crea un aleatorio entre 1 y 10


            for (int i = 0; i < lineas.size(); i++) {
                int[] letras = new int[lineas.get(i).length()];                 //crea un array de letras que van a ser codificadas segun el aleatorio generado antes
                for (int j = 0; j < letras.length; j++) {

                    letras[j] = (int) lineas.get(i).charAt(j);
                    int neoLetra = conversor(codificador, letras[j]);           //connvierte la letra usando el aleatorio
                    letras[j] = neoLetra;

                }
                String neoPalabra = "";

                for (int j = 0; j < letras.length; j++) {
                    neoPalabra += letras[j] + " ";
                }

                lineas.set(i, neoPalabra);                                     //guarda en el array de linas todas las lineas pero ya convertidas usando la variable auxiliar neoPalabra

            }
            lineas.set(0, codificador + "6 " + lineas.get(0));                  //añade a la primera linea el codigo de conversion


            FileWriter fichero = new FileWriter(f);
            PrintWriter pw = new PrintWriter(fichero);
            for (int i = 0; i < lineas.size(); i++) {
                pw.println(lineas.get(i));
            }
            pw.close();


        } catch (Exception e) {
        }


    }

    private void decodificar() {
        int codificador;

        try {
            File f = new File("./texto.txt");
            BufferedReader br = new BufferedReader(new FileReader(f));
            String linea;
            while ((linea = br.readLine()) != null) {                       //GUARDA EN EL ARRAY LIST LAS LINEAS DEL TXT
                lineas.add(linea);
            }
            br.close();


            codificador = Integer.parseInt(Character.toString(lineas.get(0).charAt(0)));                            //Crea un aleatorio entre 1 y 10

            ArrayList<String> listaNuneros = new ArrayList<>();
            String neoLetra = "";
            for (int i = 3; i < lineas.get(0).length(); i++) {
                if (lineas.get(0).charAt(i) != ' ') {
                    neoLetra += lineas.get(0).charAt(i);
                } else {
                    listaNuneros.add(neoLetra);
                    neoLetra = "";
                }
            }

            String lineaPrimera = "";
            for (int i = 0; i < listaNuneros.size(); i++) {
                lineaPrimera += (char) deconversor(codificador, Integer.parseInt(listaNuneros.get(i)));
            }
            lineas.set(0, lineaPrimera);


            FileWriter fichero = new FileWriter(f);
            PrintWriter pw = new PrintWriter(fichero);
            for (int i = 0; i < lineas.size(); i++) {
                pw.println(lineas.get(0));                                              //escribe en el txt la solucion final
            }
            pw.close();


        } catch (Exception e) {
        }


    }

    /////////////////////////////////////////////////////// SUBMETODOS ///////////////////////////////////////////////////////////////
    private static int conversor(int codigo, int letra) {
        int salida = letra + codigo;

        if (salida > 255) {
            salida = 255 - salida;
        }

        return salida;
    }

    private static int deconversor(int codigo, int letra) {
        int salida = letra - codigo;

        if (salida < 0) {
            salida = 255 + salida;
        }
        return salida;
    }
}
