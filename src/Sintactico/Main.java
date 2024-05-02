package Sintactico;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File file = new File("tokens.txt");
        if(comprobarTokens(file)) {
            AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico(file);
        }
    }

    public static boolean comprobarTokens(File file) {
        try{
            Scanner scanner = new Scanner(file);
            if(!scanner.hasNext()) {
                System.out.println("El archivo de tokens está vacío");
                return false;
            }
        } catch (FileNotFoundException e) {
            System.out.println("No se ha encontrado el archivo");
            return false;
        }
        return true;
    }
}
