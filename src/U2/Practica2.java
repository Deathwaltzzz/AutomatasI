package U2;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Practica2 {
    static File file = new File("Escrito.txt");
    public static void main(String[] args) throws IOException {
        int opciones, opcion =0;
        String[] texto = leerArchivo();
        EvaluarExpresion evaluador = null;
        //Se checa primero que el archivo exista, si no existe se terminara el programa y se indicara al usuario que el archivo esta vacio o no existe
        if(leerArchivo() == null){
            JOptionPane.showMessageDialog(null,"El archivo \"Escrito.txt\" no existe o esta vacio! favor de comprobarlo");
            file.createNewFile();
            System.exit(1);
        }
        String menu = "\nMenú \n";
        menu += "Eliga una opción para ver las cadenas validas y no validas para cada expresión \n";
        menu += "1.  ER =(a|e|i|o|u)+b_(09)*  \n";
        menu += "2.  ER = a[a-z0-9]*(1|3|5|7|9)+\n";
        menu += "3.  ER = (ab|xy)+&(0-9)*  \n";
        menu += "4.  Salir  \n";
        opciones = 4;

        String regex = null;

        do {
            opcion = Integer.parseInt(JOptionPane.showInputDialog(menu));
            switch (opcion) {
                case 1:
                    evaluador = new EvaluarExpresion(texto,1);
                    regex = "(a | e | i | o | u)+ b (0-9)*";
                    break;
                case 2:
                    evaluador = new EvaluarExpresion(texto,2);
                    regex = "a[a-z0-9]*(1|3|5|7|9)";
                    break;
                case 3:
                    evaluador = new EvaluarExpresion(texto,3);
                    regex = "(ab|xy)+&(0-9)*";
                    break;
                case 4:
                    JOptionPane.showMessageDialog(null, "Fin del programa");
                    break;
            }
            if(evaluador != null && opcion != 4)
                mostrarResultados(evaluador.returnCoincidencias(), evaluador.returnNoCoincidencias(),regex);
        } while (opcion != opciones);
    }

    /*Metodo para leer el archivo de texto.
    * En caso de no encontrar el archivo de texto se le indicara al usuario, retornara null y crasheara el programa para que exista texto dentro del archivo.
    * Tambien retornara null en caso de que el archivo este vacio, indicandole al usuario que esta vacio*/
    public static String[] leerArchivo()  {
        Scanner sc = null;
        try{
             sc = new Scanner(file);
        }catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(null,"El archivo no existe! asegurese de crear un archivo con el nombre \"texto.txt!\"");
            return null;
        }
        ArrayList<String> x = new ArrayList<>();
        while(sc.hasNextLine()){
            String[] line = sc.nextLine().trim().split("\\s+");
            Collections.addAll(x, line);
        }
        if(x.isEmpty()) return null;
        return x.toArray(new String[0]);
    }

    public static void mostrarResultados(String[] coincidencia, String[] noCoincidencia, String regex){
        Object[] cols = {"Coinciden", "Cantidad", "No coinciden", "Cantidad"};
        int cantidad = Math.max(coincidencia.length, noCoincidencia.length);
        Object[][] rows = new Object[cantidad][4];
        for (int i = 0; i < coincidencia.length; i++)
            rows[i][0] = coincidencia[i];
        for (int i = 0; i < noCoincidencia.length; i++)
            rows[i][2] = noCoincidencia[i];
        rows[0][1] = String.valueOf(coincidencia.length);
        rows[0][3] = String.valueOf(noCoincidencia.length);

        JLabel label = new JLabel("EVALUACION DEL REGEX " + regex);
        JTable table = new JTable(rows, cols);
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(label, BorderLayout.PAGE_START); // Place label at the top
        container.add(table, BorderLayout.CENTER);
        JScrollPane pane = new JScrollPane(container);
        pane.setColumnHeaderView(table.getTableHeader());
        JOptionPane.showMessageDialog(null, pane);
    }
}
