package U3;

import java.io.File;

public class Practica4_Eq1 {
    public static void main(String[] args) {
        AnalizadorLexico al = new AnalizadorLexico();
        al.leerArchivo(new File("entrada.txt"));
    }
}
