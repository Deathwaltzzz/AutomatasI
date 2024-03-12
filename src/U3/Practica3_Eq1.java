package U3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

@SuppressWarnings("unused")
public class Practica3_Eq1 {
    static File file = new File("Escrito.txt");
    public static String[] opArit = {"+", "-", "/", "*", "%", "**", "//"};
    public static String[] opRelacionales = {">", "<", "==", ">=", "<=", "!="};
    public static String[] opLogicos = {"and", "or", "not"};

	public static void main(String[] args) throws IOException {
		try {
			if (leerArchivo() == null) {
				JOptionPane.showMessageDialog(null,
						"El archivo \"Escrito.txt\" no existe o esta vacio! favor de comprobarlo");
				file.createNewFile();
				System.exit(1);
			}

			String[] texto = leerArchivo();

			if (texto != null) {

				ArrayList<Componente> cadenasValidas = new ArrayList<>();
				int opciones, opcion = 0;
				String menu = "\nMenú \n";
				menu += "Elige una opción para ver las cadenas válidas y no válidas para cada expresión \n";
				menu += "1. Leer archivo  y Clasificar cadenas\n";
				menu += "2. Escribir en archivo Clasificacion.txt\n";
				menu += "3. Salir  \n";
				opciones = 3;

				do {
					opcion = Integer.parseInt(JOptionPane.showInputDialog(menu));

					switch (opcion) {
					case 1:
						String clasificacion = null, clasificacion2 = null, clasificacion3 = null;
						// identififcadores en java
						Componente comp = null;
						for (int a = 0; a < texto.length; a++) {
							clasificacion = identificadorJava(texto[a]);
							comp = new Componente(texto[a], clasificacion);
							if (clasificacion != null) {
								cadenasValidas.add(comp);
							}

						}
						// NUMEROS EN LENGUAJE C
						comp = null;
						for (int a = 0; a < texto.length; a++) {

							clasificacion2 = numerosC(texto[a]);
							// clasificacion2 = "es un numero entero ";
							comp = new Componente(texto[a], clasificacion2);
							if (clasificacion2 != null) {
								cadenasValidas.add(comp);
							}

						}
						// operadores phyron
						comp = null;
						for (int a = 0; a < texto.length; a++) {
							String x = "";
							if (texto[a].contains("or"))
								x = "or";
							else if (texto[a].contains("and"))
								x = "and";
							else if (texto[a].contains("not"))
								x = "not";
							if (x.isEmpty()) {
								Pattern pattern = Pattern.compile("[0-9a-zA-Z]");
								x = pattern.matcher(texto[a]).replaceAll("");
							}
							clasificacion3 = operadoresPhyton(texto[a]);
							if (clasificacion3 != null) {
								comp = new Componente(x, clasificacion3);
								cadenasValidas.add(comp);
							}
						}
						break;
					case 2:
						escribirEnArchivo("Clasificacion.txt", cadenasValidas);
						break;
					case 3:
						JOptionPane.showMessageDialog(null, "Fin del programa");
						break;
					default:
						JOptionPane.showMessageDialog(null, "Opción no válida. Inténtelo de nuevo.");
					}

				} while (opcion != opciones);

			} else {
				JOptionPane.showMessageDialog(null,
						"El archivo \"Escrito.txt\" no existe o está vacío. Favor de comprobarlo");
			}
		} catch (IOException e) {
			System.out.println("Error general de E/S: " + e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println("Error de formato numérico: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error no especificado: " + e.getMessage());
		}
	}//Fin de metodo main

    public static String[] leerArchivo() {
        try (Scanner sc = new Scanner(file)) {
            ArrayList<String> x = new ArrayList<>();
            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().trim().split("\\s+");
                Collections.addAll(x, line);
            }
            if (x.isEmpty())
                return null;
            return x.toArray(new String[0]);
        } catch (FileNotFoundException e) {
            mostrarErrorYTerminar("El archivo no existe. Asegúrese de crear un archivo con el nombre \"texto.txt\"");
            return null;
        }
    }

    public static void escribirEnArchivo(String nombreArchivo, List<Componente> contenido) {
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            for (Componente componente : contenido) {
                writer.write(componente.toString());
            }
            JOptionPane.showMessageDialog(null,"Contenido escrito en el archivo correctamente.");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    private static void mostrarErrorYTerminar(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
        System.exit(1);
    }

/**
 * This method is used to validate if a given string is a valid identifier in Java.
 * An identifier in Java can be a class name, method name, variable name, etc.
 * The method follows the following rules to validate a Java identifier:
 * 1. A valid identifier in Java can start with a letter (uppercase or lowercase), an underscore (_) or a dollar sign ($).
 * 2. The following characters can be letters, digits, dollar signs or underscore characters.
 * 3. It cannot start with a digit.
 * 4. It cannot be equal to a Java keyword.
 * <p>
 * The method uses a HashMap to store the state of each character in the string. The states are represented as "q1", "q2", "q3", "q4" and "q5".
 * Each state represents a specific rule of the identifiers in Java.
 * - "q1": The character is a dollar sign ($) or an underscore character (_).
 * - "q2": The character is a letter (uppercase or lowercase).
 * - "q3": The character is a digit.
 * - "q4": The character is an underscore and it is the only character in the string.
 * - "q5": The character is not valid for a Java identifier.
 * <p>
 * The method goes through each character of the string and checks its state. If the state of the last character is "q1", "q2" or "q3",
 * the string is a valid Java identifier and the method returns "Identificador valido de Java". Otherwise, it returns null.
 *
 * @param cadena The string to be validated.
 * @return A string indicating if the input is a valid Java identifier or null if it is not.
 */
public static String identificadorJava(String cadena) {
    char currChar = cadena.charAt(0);
    HashMap<Character, String> estados = new HashMap<>();
    for (int i = 0; i < cadena.length(); i++) {
        currChar = cadena.charAt(i);
        if (i == 0 && currChar == '_' && cadena.length() == 1) {
            estados.put(currChar, "q4");
            return null;
        }
        if (i == 0 && isNumeric(currChar)) {
            estados.put(currChar, "q5");
            return null;
        }
        if (!(isLower(currChar) || isUpper(currChar) || isSpecial(currChar) || isNumeric(currChar))) {
            estados.put(currChar, "q5");
            return null;
        }
        if (isLower(currChar) || isUpper(currChar)) estados.put(currChar, "q2");
        if (isNumeric(currChar)) estados.put(currChar, "q3");
        if (isSpecial(currChar)) estados.put(currChar, "q1");
    }
    String lastSt = estados.get(cadena.charAt(cadena.length() - 1));
    String[] estadosValidos = {"q1", "q2", "q3"};
    List<String> valido = Arrays.asList(estadosValidos);
    if (valido.contains(lastSt)) return "Identificador valido de Java";
    return null;
}

    // Metodo que dice si es un caracter minuscula
    public static boolean isLower(char c) {
        return (c >= 65 && c <= 90);
    }

    //Metodo que dice si es mayuscula
    public static boolean isUpper(char c) {
        return (c >= 97 && c <= 122);
    }

    //Metodo para ver si es numerico
    public static boolean isNumeric(char c) {
        return (c - '0') >= 0 && (c - '0') < 10;
    }

    //Metodo para ver si es un caracter especial de Java $ o _
    public static boolean isSpecial(char c) {
        return (c) == '$' || (c) == '_';
    }

    // NUMEROS REALES Y ENTEROS EN LENGUAJE C - LA PARTE DE ADRIAN
    public static String numerosC(String cadena) {
        String clasificacion = "";

        if (cadena.isEmpty()) {
            return clasificacion = "Cadena vacía";
        }

        if (numerosEnterosC(cadena)) {
            return clasificacion = " es un número entero";
        } else if (numeroRealC(cadena)) {
            return clasificacion = " es un número real";
        } else {
            return clasificacion = null;
        }
    }

    public static boolean numerosEnterosC(String cadena) {
        boolean regreso = false;
        boolean guionEncontrado = false;

        for (char c : cadena.toCharArray()) {
            if (c == '-' && !guionEncontrado) {
                guionEncontrado = true;
            } else if (c >= '0' && c <= '9') {
                // Continuar si es un dígito
                regreso = true;
            } else {
                // Caracter no permitido, no es un número entero
                regreso = false;
                break; // Salir del bucle si se encuentra un carácter no permitido
            }
        }

        return regreso;
    }

    private static boolean numeroRealC(String cadena) {
        boolean puntoEncontrado = false;
        boolean guionEncontrado = false;

        for (char c : cadena.toCharArray()) {
            if (c == '-' && !guionEncontrado) {
                // Continuar si hay un -
                guionEncontrado = true;
            } else if (Character.isDigit(c)) {
                // Continuar si es un dígito
            } else if (c == '.' && !puntoEncontrado) {
                // Permitir un solo punto y marcarlo como encontrado
                puntoEncontrado = true;
            } else if (c == 'L' || c == 'f') {
                //Continua si tiene una letra l o f
            } else {
                // Caracter no permitido, no coincide con la expresión regular
                return false;
            }
        }

        // Verificar si al menos hay un dígito y que no termina con un punto
        return guionEncontrado || puntoEncontrado;
    }

    //operadores phyton
    public static String operadoresPhyton(String cadena) {
        String clasificacion = null;
        String x = "";
        //Lo que el codigo ahora hace es checar si la cadena que recibe contiene or, and o not
        if (cadena.contains("or")) x = "or";
        else if (cadena.contains("and")) x = "and";
        else if (cadena.contains("not")) x = "not";
        if (x.isEmpty()) { //Si no contiene alguna de los 3 se eliminan todas las letras o numeros que tenga, dejando solamente los caracteres especiales
            Pattern pattern = Pattern.compile("[0-9a-zA-Z]");
            x = pattern.matcher(cadena).replaceAll("");
        }
        List<String> aritmeticos = List.of(opArit);
        List<String> relacionales = List.of(opRelacionales);
        List<String> logicos = List.of(opLogicos); //Y se hara una comparacion con switches para ver cual coincide y regresar ese valor
        if (aritmeticos.contains(x)) {
            clasificacion = switch (x) {
                case "+" -> "Operador aritmético suma";
                case "-" -> "Operador aritmético resta";
                case "/" -> "Operador aritmético división";
                case "*" -> "Operador aritmético multiplicación";
                case "%" -> "Operador aritmético módulo";
                case "**" -> "Operador aritmético potencia";
                case "//" -> "Operador aritmetico cociente division";
                default -> null;
            };
        } else if (relacionales.contains(x)) {
            clasificacion = switch (x) {
                case ">" -> "Operador relacional mayor que";
                case "<" -> "Operador relacional menor que";
                case "==" -> "Operador relacional igualdad";
                case ">=" -> "Operador relacional mayor o igual que";
                case "<=" -> "Operador relacional menor o igual que";
                case "!=" -> "Operador relacional desigualdad";
                default -> null;
            };
        } else if (logicos.contains(x)) {
            clasificacion = switch (x) {
                case "not" -> "Operador lógico negación";
                case "or" -> "Operador lógico disyunción";
                case "and" -> "Operador lógico conjunción";
                default -> null;
            };
        }
        return clasificacion;
    }
}//fin de la clase