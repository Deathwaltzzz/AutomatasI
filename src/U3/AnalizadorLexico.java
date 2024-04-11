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
    public Pattern pattern;
    public Matcher matcher;

    public AnalizadorLexico() {
        // Declaracion de los posibles identificadores en una expresion
        inicializarIdentificadores();
        // Declaracion de las palabras reservadas
        inicializarPalabrasReservadas();
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
                System.out.println("Linea: " + linea);
                categorizar(linea, ++i);
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
        // Utilizamos expresión regular para encontrar caracteres especiales, cadenas
        // entre comillas y cualquier secuencia de caracteres que no sea un espacio en
        // blanco
        pattern = Pattern.compile("\"[^\"]*\"|\\(|\\)|;|,|:=|:|[+-/*]|!=|<=|>=|<|>|[^\\s();,:]+\\s*");
        matcher = pattern.matcher(linea);
        // ArrayList para almacenar los fragmentos obtenidos
        ArrayList<String> fragmentos = new ArrayList<>();

        // Agregar los fragmentos obtenidos al ArrayList
        while (matcher.find()) {
            String fragmento = matcher.group().trim(); // Eliminamos espacios en blanco al inicio y al final
            System.out.print("fragmento: " + fragmento+"\n");
            fragmentos.add(fragmento);
        }
        // Convertir el ArrayList a un arreglo de strings y regresarlo
        return fragmentos.toArray(new String[0]);
    }

    public String separarInts(String cadena, int linea) {
        Pattern pattern = Pattern.compile("\\b\\d+\\b");
        Matcher matcher = pattern.matcher(cadena);
        while (matcher.find()) {
            writeToFile(matcher.group(), -61, linea);
            cadena = cadena.replace(matcher.group(), "");
        }
        return cadena;
    }

    public String separarReales(String cadena, int linea) {
        Pattern pattern = Pattern.compile("-?\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(cadena);
        while (matcher.find()) {
            writeToFile(matcher.group(), -62, linea);
            cadena = cadena.replace(matcher.group(), "");
        }
        return cadena;
    }

    /*
     * Metodo para categorizar las cadenas que llegan y escribirlas en el archivo
     * de texto como salida.
     */
    public void categorizar(String cadena, int linea) {
        // Primero se separa por los que coincidan con el patron de los comentarios
        pattern = Pattern.compile("//.*?//");
        matcher = pattern.matcher(cadena);
        cadena = separarComentario(cadena, linea);
        cadena = separarStrings(cadena, linea);
        String[] cadenas = fragmentar(cadena);
        // De las cadenas que regresen del split se analizan para determiner a que
        // pertenece
        for (String cad : cadenas) {
            System.out.println("cadena fragmentizada: " + cad);
            operadoresArit(cad, linea);
            operadoresRel(cad, linea);
            operadoresLog(cad, linea);
            cad = separarIdentificadores(cad, linea);
            caracteresespeciales(cad, linea);
            if (reservadas.containsKey(cad)) { // Palabras reservadas
                writeToFile(cad, reservadas.get(cad), linea);
                continue;
            }
            if(numerosEnteros(cad))
                cad = separarInts(cad, linea);
            if(numeroReal(cad))
                cad = separarReales(cad, linea);
            valorBool(cad, linea);
        }
    }

    public String separarComentario(String cadena, int linea){
        Pattern pattern = Pattern.compile("//.*?//");
        Matcher matcher = pattern.matcher(cadena);
        while (matcher.find()) {
            writeToFile(matcher.group(), -56, linea);
            cadena = cadena.replace(matcher.group(), "");
        }
        return cadena.replaceAll("//.*", ""); //Esto es simplemente para quitar los comentarios que no sean validos
    }

    public String separarIdentificadores(String cadena, int linea){
        Pattern pattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9_]*[#%&$?]");
        Matcher matcher = pattern.matcher(cadena);
        while (matcher.find()) {
            writeToFile(matcher.group(), ids.get(matcher.group().charAt(matcher.group().length() - 1)), linea);
            cadena = cadena.replace(matcher.group(), "");
        }
        return cadena;
    }

    public String separarStrings(String cadena, int linea){
        Pattern pattern = Pattern.compile("\".*?\"");
        Matcher matcher = pattern.matcher(cadena);
        while (matcher.find()) {
            writeToFile(matcher.group(), -63, linea);
            cadena = cadena.replace(matcher.group(), "");
        }
        return cadena;
    }

    public void operadoresArit(String cadena, int linea) {
        System.out.println("cadena: " + cadena + " estoy en la oparit");
        int tamaño = cadena.length();
        for (int i = 0; i < tamaño; i++) {
            char operador = cadena.charAt(i);
            int aux = i + 1;
            if (operador == '*') {
                writeToFile("*", -21, linea);
            } else if (operador == '/') {
                writeToFile("/", -22, linea);
            } else if (operador == '+') {
                writeToFile("+", -24, linea);
            } else if (operador == '-') {
                writeToFile("-", -25, linea);
                i++;
            } else if (operador == ':' && aux < tamaño && cadena.charAt(aux) == '=') {
                writeToFile(":=", -26, linea);
                i++; // Avanzar un carácter extra
            }
        }
    }


    public static boolean isNumeric(char c) {
        return (c - '0') >= 0 && (c - '0') < 10;
    }

    public void operadoresRel(String cadena, int linea) {
        System.out.println("cadena: " + cadena+ "estoy en la oprel");
        int tamaño = cadena.length();
        for (int i = 0; i < tamaño; i++) {
            char operador = cadena.charAt(i);
            int aux = i+1;
            if (operador == '<') {
                if (aux < tamaño && cadena.charAt(aux) == '=') {
                    writeToFile("<=", -32, linea);
                    i++; // Avanzar un carácter extra
                } else {
                    writeToFile("<", -31, linea);
                }
            } else if (operador == '>') {
                if (aux < tamaño && cadena.charAt(aux) == '=') {
                    writeToFile(">=", -34, linea);
                    i++; // Avanzar un carácter extra
                } else {
                    writeToFile(">", -33, linea);
                }
            } else if (operador == '=' && aux < tamaño && cadena.charAt(aux) == '=') {
                writeToFile("==", -35, linea);
                i++; // Avanzar un carácter extra
            } else if (operador == '!' && aux < tamaño && cadena.charAt(aux) == '=') {
                writeToFile("!=", -36, linea);
                i++; // Avanzar un carácter extra
            }
        }
    }

    public void operadoresLog(String cadena, int linea) {
        System.out.print("cadena: " + cadena+"estoy en la oplog");
        int tamaño = cadena.length();
        for (int i = 0; i < tamaño; i++) {
            char operador = cadena.charAt(i);
            int aux = i+1;
            if (operador == '&' && aux < tamaño && cadena.charAt(aux) == '&') {
                writeToFile("&&", -41, linea);
                i++; // Avanzar un carácter extra
            } else if (operador == '|' && aux < tamaño && cadena.charAt(aux) == '|') {
                writeToFile("||", -42, linea);
                i++; // Avanzar un carácter extra
            } else if (operador == '!') {
                writeToFile("!", -43, linea);
            }
        }
    }

    // Expresión regular para números enteros (uno o más dígitos, no incluye
    // negativos
    public boolean numerosEnteros(String cadena) {
        String patronEntero = "^\\d+$";
        return pattern.matcher(cadena).matches();
    }

    // Expresión regular para números decimales (uno o más dígitos seguidos de punto
    // y uno o más dígitos
    public boolean numeroReal(String cadena) {
        String patronDecimal = "^-?\\d+\\.\\d+$";
        return Pattern.matches(patronDecimal, cadena);
    }


    public void caracteresespeciales(String cadena, int linea) {
        int tamaño = cadena.length();
        for (int i = 0; i < tamaño; i++) {
            char caracter = cadena.charAt(i);
            int aux = i+1;
            if (caracter == '(') {
                writeToFile("(", -73, linea);
            } else if (caracter == ')') {
                writeToFile(")", -74, linea);
            } else if (caracter == ';') {
                writeToFile(";", -75, linea);
            } else if (caracter == ',') {
                writeToFile(",", -76, linea);
            } else if (caracter == ':') {
                writeToFile(":", -77, linea);
                i++;
            }
        }
    }

    //Metodo que se usa para saber si hay un valor booleano
    private void valorBool(String cadena, int linea) {
        switch (cadena) {
            case "true":
                writeToFile(cadena, -64, linea);
                return;
            case "false":
                writeToFile(cadena, -65, linea);
                return;
            default:
        }
    }

    /*
     * Este metodo escribe a el archivo ARCHIVO_SALIDA(salida.txt) la cadena que le
     * llegue, su token,
     * -2 o -1 y la linea a la que pertenecen
     */
    public void writeToFile(String cadena, int token, int linea) {
        int id = -1;
        if (token == -51 || token == -52 || token == -53 || token == -54 || token == -55)
            id = -2;
        try (FileWriter fw = new FileWriter(ARCHIVO_SALIDA, true)) {
            fw.write(String.format("%s,%d,%d,%d\n", cadena, token, id, linea));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}