package U3;

import java.io.File;

public class Practica4_Eq1 {
    public static void main(String[] args) {
        try{
            new AnalizadorLexico().leerArchivo(new File("entrada.txt"));
        }catch (Exception e){
            System.out.println("Ocurrio un error" + e.getMessage());
        }
    }
}
