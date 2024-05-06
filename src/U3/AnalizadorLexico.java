package U3;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorLexico {
    public final String ARCHIVO_SALIDA = "tokens.txt";
    public HashMap<Character, Integer> ids = new HashMap<>();
    public HashMap<String, Integer> reservadas = new HashMap<>();
    public Pattern pattern;
    public Matcher matcher;
    public int errores = 0;

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
                categorizar(linea, ++i);
            }
            if(errores >= 1) {
                clearFile();
                System.out.printf("Se encontraron %d errores\n", errores);
            }else{
                System.out.println("Archivo analizado correctamente \n No se encontraron errores\n Tokens generados en tokens.txt\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void clearFile() {
        try (FileWriter fw = new FileWriter(ARCHIVO_SALIDA)) {
            fw.write("");
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
        Pattern pattern = Pattern.compile("\".*?\"|//.*?//|:=|<=|>=|==|!=|!|&&|\\|\\||[-+*;,<>()#:&!=/]\\s*|-?\\d+\\.\\d*\\s*|\\d+!\\s*|\\b[a-zA-Z\\d_]+\\b[#%&$?]?|[^\\s*]");
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

    /*
     * Metodo para categorizar las cadenas que llegan y escribirlas en el archivo
     * de texto como salida.
     */
    public void categorizar(String cadena, int linea) {
        String[] cadenas = fragmentar(cadena);
        // De las cadenas que regresen del split se analizan para determinar a que
        // pertenece
        for (String cad : cadenas) {
            //System.out.print(cad + " estoy en el métood categorizar\n");
            if(caracteresespeciales(cad, linea)) continue;
            if(operadoresLog(cad, linea)) continue;
            if(operadoresArit(cad, linea)) continue;
            if(operadoresRel(cad, linea)) continue;
            if(separarIdentificadores(cad, linea)) continue;
            if(separarReservadas(cad, linea)) continue;
            if(separarInts(cad, linea)) continue;
            if(separarReales(cad, linea)) continue;
            if(valorBool(cad, linea)) continue;
            if(separarComentario(cad, linea)) continue;
            if(separarStrings(cad, linea)) continue;
            System.out.printf("Error en la linea %d, no se reconoce el token %s\n", linea, cad);
            errores++;
        }
    }

    public boolean separarIdentificadores(String cadena, int linea) {
        if(!cadena.matches("[a-zA-Z][a-zA-Z\\d_]*[%$#&?]$"))
            return  false;
        writeToFile(cadena, ids.get(cadena.charAt(cadena.length()-1)), linea);
        return true;
    }

    public boolean separarReservadas(String cadena, int linea) {
        if(!reservadas.containsKey(cadena)) return false;
        writeToFile(cadena, reservadas.get(cadena), linea);
        return true;
    }

    public boolean separarInts(String cadena, int linea) {
        if(!cadena.matches("\\b\\d+\\b")) return false;
        Pattern pattern = Pattern.compile("\\b\\d+\\b");
        Matcher matcher = pattern.matcher(cadena);
        int i = 0;
        while (matcher.find()) {
            writeToFile(matcher.group(), -61, linea);
            i++;
        }
        return true;
    }

    public boolean separarReales(String cadena, int linea) {
        if (!cadena.matches("-?\\d+\\.\\d*")) return false;
        Pattern pattern = Pattern.compile("-?\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(cadena);
        while (matcher.find())
            writeToFile(matcher.group(), -62, linea);
        return true;
    }

    public boolean separarComentario(String cadena, int linea){
        if(!cadena.matches("//.*?//")) return false;
        System.out.println(cadena + " comentario en linea "+ linea );
        return true;
    }

    public boolean isComentario(String cadena){
        return cadena.matches("//.*?//");
    }

    public boolean separarStrings(String cadena, int linea){
        if(!cadena.matches("\".*?\"")) return false;
        writeToFile(cadena, -63, linea);
        return true;
    }

    public boolean operadoresArit(String cadena, int linea) {
        // System.out.println("cadena: " + cadena+"estoy en la oparit");
        if(isComentario(cadena) || !cadena.matches("[+\\-*/]|:="))
            return false;
        int size = cadena.length();
        for (int i = 0; i < size; i++) {
            char operador = cadena.charAt(i);
            if (operador == '*') {
                writeToFile("*", -21, linea);
            } else if (operador == '/') {
                writeToFile("/", -22, linea);
            } else if (operador == '+') {
                writeToFile("+", -24, linea);
            } else if (operador == '-') {
                writeToFile("-", -25, linea);
            } else if (operador == ':' && !(size == 1) && cadena.charAt(i + 1) == '=') {
                writeToFile(":=", -26, linea);
                i++; // Avanzar un carácter extra
            }
        }
        return true;
    }

    public boolean operadoresRel(String cadena, int linea) {
        if(!cadena.matches("[<>=!]+")) return false;
        if(cadena.equals("=")) return false;
        int size = cadena.length();
        for (int i = 0; i < size; i++) {
            char operador = cadena.charAt(i);
            if (operador == '<') {
                if (i + 1 < size && cadena.charAt(i + 1) == '=') {
                    writeToFile("<=", -32, linea);
                    i++; // Avanzar un carácter extra
                } else {
                    writeToFile("<", -31, linea);
                }
            } else if (operador == '>') {
                if (i + 1 < size && cadena.charAt(i + 1) == '=') {
                    writeToFile(">=", -34, linea);
                    i++; // Avanzar un carácter extra
                } else {
                    writeToFile(">", -33, linea);
                }
            } else if (operador == '=' && i + 1 < size && cadena.charAt(i + 1) == '=') {
                writeToFile("==", -35, linea);
                i++; // Avanzar un carácter extra
            } else if (operador == '!' && i + 1 < size && cadena.charAt(i + 1) == '=') {
                writeToFile("!=", -36, linea);
                i++; // Avanzar un carácter extra
            }
        }
        return true;
    }

    public boolean operadoresLog(String cadena, int linea) {
        //System.out.print(cadena + " estoy en el métoodo operadores lógicos\n");
        if (cadena.matches("!|&&|\\|\\|")) {
            switch (cadena) {
                case "&&":
                    writeToFile(cadena, -41, linea);
                    break;
                case "||":
                    writeToFile(cadena, -42, linea);
                    break;
                case "!":
                    writeToFile(cadena, -43, linea);
                    break;
            }
            return true;
        }
        return false;
    }



    public boolean caracteresespeciales(String cadena, int linea) {
        if(!cadena.matches("[;:,()]+"))
            return false;
        int size = cadena.length();
        for (int i = 0; i < size; i++) {
            char caracter = cadena.charAt(i);
            if (caracter == '(') {
                writeToFile("(", -73, linea);
            } else if (caracter == ')') {
                writeToFile(")", -74, linea);
            } else if (caracter == ';') {
                writeToFile(";", -75, linea);
            } else if (caracter == ',') {
                writeToFile(",", -76, linea);
            } else if (caracter == ':' && cadena.length() == 1 ) {
                writeToFile(":", -77, linea);
                i++;
            }
        }
        return true;
    }

    //Metodo que se usa para saber si hay un valor booleano
    private boolean valorBool(String cadena, int linea) {
        switch (cadena) {
            case "true":
                writeToFile(cadena, -64, linea);
                return true;
            case "false":
                writeToFile(cadena, -65, linea);
                return true;
            default:
        }
        return false;
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
}//fin de la clase