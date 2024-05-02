package Sintactico;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AnalizadorSintactico {

    public List<Integer> tipoDatos = List.of(-11,-12,-13,-14);
    public List<Integer> identificador = List.of(-51,-52,-53,-54,-55);

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
            if(tipoDatos(Integer.parseInt(tokens.get(i)[0]))){

            }
        }
    }

    public boolean tipoDatos(int lexema){
        if(tipoDatos.contains(lexema)){
            return identificador(lexema);
        }
    }

    public boolean identificador(int lexema){
        return identificador.contains(lexema);
    }

    public boolean asignacion(String token, int i){
        return true;
    }


}
