package U3;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorLexico {
    public final String ARCHIVO_SALIDA = "salida.txt";
    public HashMap<Character, Integer> ids = new HashMap<>();
    public HashMap<String, Integer> reservadas = new HashMap<>();
    public HashMap<String, Integer> opAritmeticos = new HashMap<>();
    public HashMap<String, Integer> opRelacionales = new HashMap<>();
    public HashMap<String, Integer> opLogicos = new HashMap<>();
    public HashMap<String, Integer> carEspecial = new HashMap<>();
    public Pattern pattern;
    public Matcher matcher;

    public AnalizadorLexico() {
        // Declaracion de los posibles identificadores en una expresion
        inicializarIdentificadores();
        // Declaracion de las palabras reservadas
        inicializarPalabrasReservadas();
        // Declaracion de los operadores aritmeticos
        inicializarOperadoresAritmeticos();
        // Declaracion de los operadores relacionales
        inicializarOperadoresRelacionales();
        // Declaracion de los operadores logicos
        inicializarOperadoresLogicos();
        // Declaracion de los caracteres especiales
        inicializarCaracteresEspeciales();
        // Clear file
        try (FileWriter fw = new FileWriter(ARCHIVO_SALIDA)) {
            fw.write("");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void inicializarIdentificadores() {
        ids.clear();
        ids.put('#', -53);
        ids.put('%', -52);
        ids.put('&', -51);
        ids.put('$', -54);
        ids.put('?', -55);
    }

    public void inicializarPalabrasReservadas() {
        reservadas.clear();
        reservadas.put("program", -1);
        reservadas.put("begin", -2);
        reservadas.put("end", -3);
        reservadas.put("read", -4);
        reservadas.put("write", -5);
        reservadas.put("if", -6);
        reservadas.put("else", -7);
        reservadas.put("while", -8);
        reservadas.put("repeat", -9);
        reservadas.put("until", -10);
        reservadas.put("int", -11);
        reservadas.put("real", -12);
        reservadas.put("string", -13);
        reservadas.put("bool", -14);
        reservadas.put("var", -15);
        reservadas.put("then", -16);
        reservadas.put("do", -17);
    }

    public void inicializarOperadoresAritmeticos() {
        opAritmeticos.clear();
        opAritmeticos.put("+", -24);
        opAritmeticos.put("-", -25);
        opAritmeticos.put("*", -21);
        opAritmeticos.put("/", -22);
        opAritmeticos.put(":=", -26);
    }
    public void inicializarOperadoresRelacionales() {
        opRelacionales.clear();
        opRelacionales.put("<", -31);
        opRelacionales.put(">", -33);
        opRelacionales.put("<=", -32);
        opRelacionales.put(">=", -34);
        opRelacionales.put("==", -35);
        opRelacionales.put("!=", -36);
    }
    public void inicializarOperadoresLogicos() {
        opLogicos.clear();
        opLogicos.put("&&", -41);
        opLogicos.put("||", -42);
        opLogicos.put("!", -43);
    }

    public void inicializarCaracteresEspeciales() {
        carEspecial.clear();
        carEspecial.put("(", -73);
        carEspecial.put(")", -74);
        carEspecial.put(";", -75);
        carEspecial.put(",", -76);
        carEspecial.put(":", -77);
    }

    public void leerArchivo(File archivo) {
        if (!archivo.exists()) {
            System.out.println("El archivo no existe, se creara a continuacion");
            crearArchivo(archivo.getName());
            return;
        }
        try {
            int i = 0;
            Scanner sc = new Scanner(archivo);
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                categorizar(linea.trim(), ++i);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Metodo para crear nuevo archivo, recibe como parametro el nombre del archivo
    public void crearArchivo(String name) {
        File archivo = new File(name);
        try {
            archivo.createNewFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String[] fragmentar(String linea) {
        /*Ni Dios sabe como hice esta REGEX, pero de alguna forma funciona para TODOS Los casos (en teoria)*/
        pattern = Pattern.compile("\".*?\"|//.*?//|:=|<=|>=|==|!=|\\||&&|-?\\d+\\.\\d*|[-+*;,<>():!]|\\d+!|\\b[a-zA-Z\\d_]+\\b[#%&$?]*|\\.[^ \\t\\n\\r\\f\\v]+");
        matcher = pattern.matcher(linea);
        // ArrayList para almacenar los fragmentos obtenidos
        ArrayList<String> fragmentos = new ArrayList<>();
        // Agregar los fragmentos obtenidos al ArrayList
        while (matcher.find()) {
            String fragmento = matcher.group().trim(); // Eliminamos espacios en blanco al inicio y al final
            fragmentos.add(fragmento);
        }
        // Convertir el ArrayList a un arreglo de strings y regresarlo
        return fragmentos.toArray(new String[0]);
    }

    public void separarInts(String cadena, int linea) {
        writeToFile(cadena, -62, linea);
    }

    public void separarReales(String cadena, int linea) {
        writeToFile(cadena, -62, linea);
    }

    /*
     * Metodo para categorizar las cadenas que llegan y escribirlas en el archivo
     * de texto como salida.
     */
    public void categorizar(String cadena, int linea) {
        String[] cadenas = fragmentar(cadena);
        for (String cad : cadenas) {
            if(isComentario(cad)){
                separarComentario(cad, linea);
                continue;
            }
            if(isCaraEspecial(cad)){
                caracteresespeciales(cad, linea);
                continue;
            }
            if(isOperadorAritmetico(cad)){
                operadoresArit(cad, linea);
                continue;
            }
            if(isOperadorRelacional(cad)){
                operadoresRel(cad, linea);
                continue;
            }
            if(isOperadorLogico(cad)){
                operadoresLog(cad, linea);
                continue;
            }
            if (ids.containsKey(cad.charAt(cad.length() - 1))) {
                writeToFile(cad, ids.get(cad.charAt(cad.length() - 1)), linea);
                continue;
            }
            if (reservadas.containsKey(cad)) { // Palabras reservadas
                writeToFile(cad, reservadas.get(cad), linea);
                continue;
            }
            if(numerosEnteros(cad)) {
                separarInts(cad, linea);
                continue;
            }
            if(numeroReal(cad)) {
                separarReales(cad, linea);
                continue;
            }
            if(isBoolean(cad)){
                valorBool(cad, linea);
                continue;
            }
            if(isString(cad)) {
                separarStrings(cad, linea);
                continue;
            }
            System.out.printf("Error en la linea %d, no se reconoce el token %s\n", linea, cad);
        }
    }

    public void separarComentario(String cadena, int linea){
        writeToFile(cadena, -56, linea);
    }

    public boolean isComentario(String cadena){
        return cadena.matches("//.*?//");
    }

    public boolean isBoolean(String cadena){
        return cadena.matches("true|false");
    }

    public boolean isString(String cadena){
        return cadena.matches("\".*?\"");
    }

    public boolean isOperadorAritmetico(String cadena) {
        return cadena.matches("[+\\-*/]|:=");
    }

    // Expresión regular para números enteros (uno o más dígitos, no incluye
    // negativos
    public boolean numerosEnteros(String cadena) {
        String patronEntero = "^\\d+$";
        return Pattern.matches(patronEntero, cadena);
    }

    // Expresión regular para números decimales (uno o más dígitos seguidos de punto
    // y uno o más dígitos
    public boolean numeroReal(String cadena) {
        String patronDecimal = "^-?\\d+\\.\\d*$";
        return Pattern.matches(patronDecimal, cadena);
    }

    public boolean isCaraEspecial(String cadena) {
        return  cadena.matches("[;:,()]+");
    }

    public boolean isOperadorRelacional(String cadena) {
        return cadena.matches("[<>=!]+");
    }

    public boolean isOperadorLogico(String cadena) {
        return cadena.matches("[&|!]+");
    }

    public void operadoresRel(String cadena, int linea) {
        if(!opRelacionales.containsKey(cadena))
            return;
        writeToFile(cadena, opRelacionales.get(cadena), linea);
    }

    public void operadoresLog(String cadena, int linea) {
        if(!opLogicos.containsKey(cadena))
            return;
        writeToFile(cadena, opLogicos.get(cadena), linea);
    }

    public void caracteresespeciales(String cadena, int linea) {
        if(!carEspecial.containsKey(cadena))
            return;
        writeToFile(cadena, carEspecial.get(cadena), linea);
    }

    //Metodo que se usa para saber si hay un valor booleano
    private void valorBool(String cadena, int linea) {
        switch (cadena) {
            case "true":
                writeToFile(cadena, -64, linea);
                break;
            case "false":
                writeToFile(cadena, -65, linea);
                break;
        }
    }

    public void separarStrings(String cadena, int linea){
        writeToFile(cadena, -63, linea);
    }
    public void operadoresArit(String cadena, int linea) {
        // System.out.println("cadena: " + cadena+"estoy en la oparit");
        writeToFile(cadena, opAritmeticos.get(cadena), linea);
    }

    /*
     * Este metodo escribe a el archivo ARCHIVO_SALIDA(salida.txt) la cadena que le
     * llegue, su token,
     * -2 o -1 y la linea a la que pertenecen
     */
    public void writeToFile(String cadena, int token, int linea) {
        int id = -2;
        if (token == -51 || token == -52 || token == -53 || token == -54 || token == -55)
            id = -1;
        try (FileWriter fw = new FileWriter(ARCHIVO_SALIDA, true)) {
            fw.write(String.format("%s,%d,%d,%d\n", cadena, token, id, linea));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}