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
    public HashMap<Character,Integer> ids = new HashMap<Character,Integer>();
    public HashMap<String,Integer> reservadas = new HashMap<String,Integer>();
    public AnalizadorLexico() {
        //Declaracion de los posibles identificadores en una expresion
        inicializarIdentificadores();
        // Declaracion de las palabras reservadas
        inicializarPalabrasReservadas();
        //Clear file
    try(FileWriter fw = new FileWriter(ARCHIVO_SALIDA)) {
        fw.write("");
    }catch (Exception e) {
        System.out.println(e.getMessage());
    }
    }

    public void inicializarIdentificadores(){
        ids.clear();
        ids.put('#',-53);
        ids.put('%',-52);
        ids.put('&',-51);
        ids.put('$',-54);
        ids.put('?',-55);
    }

    public void inicializarPalabrasReservadas(){
        reservadas.clear();
        reservadas.put("program",-1);
        reservadas.put("begin",-2);
        reservadas.put("end",-3);
        reservadas.put("read",-4);
        reservadas.put("write",-5);
        reservadas.put("if",-6);
        reservadas.put("else",-7);
        reservadas.put("while",-8);
        reservadas.put("repeat",-9);
        reservadas.put("until",-10);
        reservadas.put("int",-11);
        reservadas.put("real",-12);
        reservadas.put("string",-13);
        reservadas.put("bool",-14);
        reservadas.put("var",-15);
        reservadas.put("then",-16);
        reservadas.put("do",-17);
    }

    public void identificadores(String cadena){

    }

    public boolean leerArchivo(File archivo){
        if(!archivo.exists()){
            System.out.println("El archivo no existe, se creara a continuacion");
            crearArchivo(archivo.getName());
            return false;
        }
        try{
            int i = 0;
            Scanner sc = new Scanner(archivo);
            while(sc.hasNextLine()){
                String linea = sc.nextLine();
                categorizar(linea,++i);
            }
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    //Metodo para crear nuevo archivo, recibe como parametro el nombre del archivo
    public void crearArchivo(String name){
        File archivo = new File(name);
        try{
            archivo.createNewFile();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    /*Metodo para categorizar las cadenas que llegan y escribirlas en el archivo
    * de texto como salida.*/
    public void categorizar(String cadena, int linea){
        //Primero se separa por los que coincidan con el patron de los comentarios
        Pattern pattern = Pattern.compile("//.*?//");
        Matcher matcher = pattern.matcher(cadena);
        while(matcher.find()){
            writeToFile(matcher.group(),-56,linea);
            cadena = cadena.replace(matcher.group(), "");
        }
        cadena = cadena.replaceAll("//.*","");
        //Se separa por "," esto es temporal ya que no me corresponde a mi hacer esto xd
        String[] cadenas = splitComma(cadena);
        pattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9_]*[#%&$?]"); //Identificadores
        //De las cadenas que regresen del split se analizan para determinar a que pertenece
        for(String cad : cadenas){
            if(pattern.matcher(cad).matches()){ //Identificadores
                System.out.println("Identificador: "+cad);
                if(ids.containsKey(cad.charAt(cad.length()-1)))
                    writeToFile(cad,ids.get(cad.charAt(cad.length()-1)),linea);
                continue;
            }
            if(reservadas.containsKey(cad)){ //Palabras reservadas
                writeToFile(cad,reservadas.get(cad),linea);
                continue;
            }
            if(cad.charAt(0) == '"' && cad.charAt(cad.length()-1) == '"' && cad.length() > 2) //Cadenas
                writeToFile(cad,-63,linea);
        }
    }

    /*Este metodo simplemente actua como un .split() pero especificamente para separar por , que es el dilimitador
     * del lenguaje de prueba, este metodo no es final ya que no me corresponde, pero lo estoy utilizando para pruebas.
     * Pude usar un regex de una sola linea pero que aburrido no?
     * Tambien separa los que pertenezcan a un string, nuevamente lo pude hacer con un regex pero fuck it, we ball*/
    public String[] splitComma(String cadena){
        ArrayList<String> list = new ArrayList<String>();
        String current = "";
        boolean isString = false;
        for (int i = 0; i < cadena.length(); i++) {
            int aux = i;
            char c = cadena.charAt(i);
            if(c == '.') continue;
            if(c == '"' && !isString) isString = true;
            if(c == ' ' && !isString && i < cadena.length()-1 && !current.isEmpty()){
                list.add(current.replace(" ",""));
                current = "";
                continue;
            }
            if(c != ','){
                current += c;
                if(i != cadena.length()-1) continue;
            }
            if(!current.isEmpty() || (isString && cadena.charAt(aux-1) == '"')){
                list.add(current.replace(" ",""));
                isString = false;
                current = "";
                continue;
            }
            list.add(",");
            current = "";
        }
        return list.toArray(new String[0]);
    }

    /*Este metodo escribe a el archivo ARCHIVO_SALIDA(salida.txt) la cadena que le llegue, su token,
    * -2 o -1 y la linea a la que pertenecen*/
    public void writeToFile(String cadena, int token, int linea){
        int id = -2;
        if(token == -51 || token == -52 || token == -53 || token == -54 || token == -55) id = -1;
        try(FileWriter fw = new FileWriter(ARCHIVO_SALIDA, true)){
            fw.write(String.format("%s,%d,%d,%d\n",cadena,token,id,linea));
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

