import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
                    case 1 -> capturarDatos(fields);
                    case 2->
                        mostrarDatosLeidos();
                }
            }
        }while(opc != 3);
    }

    static void CrearArchivo() throws IOException {
        FileWriter writer;
        if(salida.exists()){
            Scanner sc = new Scanner(salida);
            int lineas = 0;
            while(sc.hasNextLine()){
                lineas++;
                sc.nextLine();
            }
            if(lineas == 0) {
                writer = new FileWriter(salida);
                writer.write("|     NOMBRE      |       APELLIDO PATERNO         |     APELLIDO MATERNO         |          EDAD       |");
                writer.close();
            }
        }else
            salida.createNewFile();
    }

    public static void capturarDatos(Object[] fields) throws IOException {
        JOptionPane.showMessageDialog(null,fields,"Selecciona",JOptionPane.OK_CANCEL_OPTION);
        if(isNullOrBlank(nombre.getText()) || isNullOrBlank(apPaterno.getText()) || isNullOrBlank(apMaterno.getText()) ||
                !checkNumeric(edad.getText())) {
            JOptionPane.showMessageDialog(null, "Olvido llenar un dato o introduzco un valor no numerico en edad! ");
            return;
        }
        JOptionPane.showMessageDialog(null,"Datos almacenados correctamente!");
        Persona persona = new Persona(nombre.getText(), apPaterno.getText(), apMaterno.getText(), Integer.parseInt(edad.getText()));
        escribirArchivo(persona);
    }

    public static void escribirArchivo(Persona persona) throws IOException {
        FileWriter writer = new FileWriter(salida,true);
        writer.append(String.format("\n| %s | %s | %s | %d |",persona.getNombre(),persona.getApPaterno(),persona.getApMaterno(),persona.getEdad()));
        writer.close();
    }

    public static void mostrarDatosLeidos() {
        Scanner sc = null;
        Object[] cols = {"Nombre", "Apellido Paterno", "Apellido Materno", "Edad"};
        boolean exists = false;
        try{
            sc = new Scanner(salida);
            exists = true;
        }catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(null,"El archivo aun no existe! seleccione la opcion 1 primero");
        }
        if(!exists) return;
        ArrayList<String[]> list = new ArrayList<>();
        int amount = 0;
        while(sc.hasNextLine()){
            String line = sc.nextLine().substring(2);
            String[] arr = line.split("\\|");
            if(arr.length == 0) continue;
            list.add(arr);
            amount++;
        }
        Object[][] rows = new Object[amount][4];
        for (int i = 1; i < amount; i++)
            for (int j = 0; j < 4; j++)
                rows[i][j] = list.get(i)[j];
        JTable table = new JTable(rows,cols);
        JOptionPane.showMessageDialog(null,new JScrollPane(table));
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
