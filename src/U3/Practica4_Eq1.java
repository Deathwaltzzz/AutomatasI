package U3;

import java.io.File;

public class Practica4_Eq1 {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        if(args.length == 0)
            new AnalizadorLexico().leerArchivo(new File("entrada.txt"));
        else
            new AnalizadorLexico().leerArchivo(new File(args[0]));
        long endTime = System.nanoTime();
        System.out.println("Tiempo de ejecución: " + (endTime - startTime) / 1e6 + " ms");
    }
}
