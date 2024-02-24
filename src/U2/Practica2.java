package U2;

import javax.swing.*;
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
        menu += "2.  ER = \n";
        menu += "3.  ER = (ab|xy)+&(0-9)*  \n";
        menu += "4.  Salir  \n";
        opciones = 4;

        do {
            opcion = Integer.parseInt(JOptionPane.showInputDialog(menu));
            switch (opcion) {
                case 1:
                    evaluador = new EvaluarExpresion(texto,1);
                    break;
                case 2:
                    // porción de código
                    break;
                case 3:
                    evaluador = new EvaluarExpresion(texto,3);
                    break;
                case 4:
                    JOptionPane.showMessageDialog(null, "Fin del programa");
                    break;
            }
            if(evaluador != null && opcion != 4) {
                JOptionPane.showMessageDialog(null, String.format("""
                                Numero de coincidencias:\s
                                %d\s
                                Caracteres que coincidieron:\s
                                %s""",
                        evaluador.returnCoincidencias().length, Arrays.toString(evaluador.returnCoincidencias()))
                );
                JOptionPane.showMessageDialog(null, String.format("""
                                Numero de NO coincidencias:\s
                                %d\s
                                Caracteres que NO coincidieron:\s
                                %s""",
                        evaluador.returnNoCoincidencias().length, Arrays.toString(evaluador.returnNoCoincidencias()))
                );
            }

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
}
