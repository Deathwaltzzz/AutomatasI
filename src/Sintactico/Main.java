package Sintactico;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        List<Token> tokens = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("tokens.txt"))) {
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                Pattern pattern = Pattern.compile("(.*?),(-?\\d+),(-?\\d+),(-?\\d+)");
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    String lexema = matcher.group(1);
                    int token = Integer.parseInt(matcher.group(2));
                    int posTabla = Integer.parseInt(matcher.group(3));
                    int noLinea = Integer.parseInt(matcher.group(4));
                    Token tokenObj = new Token(lexema, token, posTabla, noLinea);
                    tokens.add(tokenObj);
                } else {
                    System.out.println("Error en la línea " + lineNumber + ": formato incorrecto.");
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * for (Token token : tokens) {
         System.out.println(token);
         }
         */

        AnalizadorSintactico as = new AnalizadorSintactico(tokens);
    }
}
