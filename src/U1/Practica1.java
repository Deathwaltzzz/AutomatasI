package U1;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Practica1 {
    static JTextField nombre = new JTextField("Nombre");
    static JTextField apPaterno = new JTextField("Apellido Paterno");
    static JTextField apMaterno = new JTextField("Apellido Materno");
    static JTextField edad = new JTextField("Edad");
    static File salida = new File("Salida.txt");
    static ArrayList<String[]> existingData = new ArrayList<>();
    /*Metodo main, contiene solamente lo necesario para el menu e interactuar con el programa*/
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
                    case 2-> mostrarDatosLeidos();
                }
            }
        }while(opc != 3);
    }
/*Metodo crear archivo, este metodo se encarga de crear el archivo de texto "Salida.txt", si este no existe lo creara y escribira los parametros
* iniciales de este.*/
    static void CrearArchivo() throws IOException {
        FileWriter writer;
        if(salida.exists()){
            Scanner sc = new Scanner(salida);
            int lineas = 0;
            String linea;
            while(sc.hasNextLine()){
                lineas++;
                linea = sc.nextLine().substring(2);
                if(lineas > 1){
                    String[] arr = linea.split("\\|");
                    existingData.add(arr);
                }
            }
            sc.close();
            if(lineas == 0) {
                writer = new FileWriter(salida);
                writer.write("|     NOMBRE      |       APELLIDO PATERNO         |     APELLIDO MATERNO         |          EDAD       |");
                writer.close();
            }
        }else
            salida.createNewFile();
    }
/*Metodo de captura de datos, este metodo recibe como parametro los campos que se capturaron*/
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
/*Metodo para escrbir en el archivo, recibe como parametro el objeto U1.Persona y lo escribe en el archivo*/
    public static void escribirArchivo(Persona persona) throws IOException {
        FileWriter writer = new FileWriter(salida,true);
        writer.append(String.format("\n| %s | %s | %s | %d |",persona.getNombre(),persona.getApPaterno(),persona.getApMaterno(),persona.getEdad()));
        writer.close();
        existingData.add(persona.returnArray());
    }
/*Metodo que muestra con una JTable los datos almacenados en el archivo*/
    public static void mostrarDatosLeidos() {
        Object[] cols = {"Nombre", "Apellido Paterno", "Apellido Materno", "Edad"};
        if(!salida.exists()){
            JOptionPane.showMessageDialog(null,"El archivo no existe!, reinicie el programa");
            return;
        }
        int amount = existingData.size();
        Object[][] rows = new Object[amount][4];
        for (int i = 0; i < amount; i++)
            for (int j = 0; j < 4; j++)
                rows[i][j] = existingData.get(i)[j];
        JTable table = new JTable(rows,cols);
        JOptionPane.showMessageDialog(null,new JScrollPane(table));
    }
/*Funcion auxiliar que determina si un string esta vacio o es null, devuelve true si es cualquiera de estos o falso si es un archivo valido*/
    public static boolean isNullOrBlank(String s){
        if(s.isBlank())
            return true;
        if(s == null)
            return true;
        return s.equalsIgnoreCase("Nombre") || s.contains("Apellido") || s.contains("edad");
    }
/*Funcion que checa si un string es numerico, devuelve true si lo es, si no devuelve false*/
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
