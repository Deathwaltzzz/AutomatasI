package Sintactico;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AnalizadorSintactico {

    public List<Integer> tipoDatos = List.of(-11,-12,-13,-14);
    public List<Integer> identificadores = List.of(-51,-52,-53,-54,-55);

    public AnalizadorSintactico(File file) {
        leerTokens(file);
    }


    public void leerTokens(File file) {
        ArrayList<String[]> tokens = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()) {
                tokens.add(scanner.nextLine().split(","));
            }
            analizar(tokens);

        } catch (FileNotFoundException e) {
            System.out.println("No se ha encontrado el archivo");
        }
    }

    public void analizar(ArrayList<String[]> tokens) {
        for(int i = 0 ; i < tokens.size() ; i++){
            String lexema = tokens.get(i)[0];
            int token = Integer.parseInt(tokens.get(i)[1]);
            String tipo = tokens.get(i)[2];
            String linea = tokens.get(i)[3];
            System.out.println(lexema + " " + token + " " + tipo + " " + linea);
            if(tipoDeDato(token)) {
                i++;
                if (identificador(Integer.parseInt(tokens.get(i)[1]))){
                    i++;
                    if(asignacion(Integer.parseInt(tokens.get(i)[1]))){
                        i++;
                        if(isBool(Integer.parseInt(tokens.get(i)[1]))){
                            i++;
                            if(Integer.parseInt(tokens.get(i)[1]) == -75)
                                System.out.println("Expresion booleana correcta");
                        }
                    }
                }
            }

        }
    }

    public boolean tipoDeDato(int lexema){
        return tipoDatos.contains(lexema);
    }

    public boolean identificador(int lexema){
        return identificadores.contains(lexema);
    }

    public boolean asignacion(int lexema ){
        return lexema == -26;
    }

    public boolean isBool(int lexema){
        return lexema == -64 || lexema == -65;
    }


}
