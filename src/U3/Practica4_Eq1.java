package U3;

import java.io.File;

public class Practica4_Eq1 {
    public static void main(String[] args) {
        if(args.length == 0)
            new AnalizadorLexico().leerArchivo(new File("entrada.txt"));
        else
            new AnalizadorLexico().leerArchivo(new File(args[0]));
    }
}
