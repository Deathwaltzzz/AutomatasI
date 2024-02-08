import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AppMain {
    static JTextField nombre = new JTextField("Nombre");
    static JTextField apPaterno = new JTextField("Apellido Paterno");
    static JTextField apMaterno = new JTextField("Apellido Materno");
    static JTextField edad = new JTextField("Edad");
    static ArrayList<String> list = new ArrayList<>();
    static File salida = new File("Salida.txt");


    public static void main(String[] args) throws IOException {
        CrearArchivo();
        FileWriter writer = new FileWriter(salida, true);
        int opc = 0;
        String menu = """
                    1. Capturar
                    2. Imprimir
                    3. Salir
                """;
        do{
            opc = Integer.parseInt(JOptionPane.showInputDialog(null, menu));
            if(opc != 3){

                Object[] fields = {
                        "Introduce los datos, no dejes ni uno vacio!",
                        "Nombre", nombre,
                        "Apellido Paterno", apPaterno,
                        "Apellido Materno", apMaterno,
                        "Edad", edad
                };
                switch(opc){
                    case 1 ->{
                        capturarDatos(fields);
                        writer.append("\nola");
                        writer.close();
                    }
                    case 2->{

                    }
                }
            }
        }while(opc != 3);
    }

    static void CrearArchivo() throws IOException {
        FileWriter writer;
        writer = new FileWriter(salida);
        if(salida.exists()){
            Scanner sc = new Scanner(salida);
            int lineas = 0;
            while(sc.hasNextLine()){
                lineas++;
                sc.nextLine();
            }
            if(lineas < 1)
                writer.write("|     NOMBRE      |       APELLIDO PATERNO         |     APELLIDO MATERNO         |          EDAD       |");
        }else
            salida.createNewFile();
        writer.close();
    }

    public static void capturarDatos(Object[] fields){
        int cantidad = Integer.parseInt(JOptionPane.showInputDialog(null,"Cuantos datos desea agregar?"));
        JOptionPane.showMessageDialog(null,fields,"Selecciona",JOptionPane.OK_CANCEL_OPTION);
        if(isNullOrBlank(nombre.getText()) || isNullOrBlank(apPaterno.getText()) || isNullOrBlank(apMaterno.getText()) ||
                !checkNumeric(edad.getText())) {
            JOptionPane.showMessageDialog(null, "Olvido llenar un dato o introduzco un valor no numerico en edad! ");
            list.add(nombre.getText());
            list.add(apPaterno.getText());
        }
        else
            JOptionPane.showMessageDialog(null,"Datos almacenados correctamente!");
    }

    public static boolean isNullOrBlank(String s){
        if(s.isBlank())
            return true;
        if(s == null)
            return true;
        return s.equalsIgnoreCase("Nombre") || s.contains("Apellido") || s.contains("edad");
    }

    public static boolean checkNumeric(String s){
        if(s == null) return false;
        try{
            double d = Double.parseDouble(s);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }
}
