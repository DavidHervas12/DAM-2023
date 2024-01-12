package app.client;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

/**
 * Clase principal del client
 */
public class Client {

	private static final String HOST = "localhost";
	private static final int PORT = 5000;

	public static void main(String[] args) {
		boolean estaLoguejat = false;

		try (Socket conexio = new Socket(HOST, PORT);
				BufferedReader brTeclat = new BufferedReader(new InputStreamReader(System.in));
				PrintWriter pwServidor = new PrintWriter(conexio.getOutputStream());
				BufferedReader brServidor = new BufferedReader(new InputStreamReader(conexio.getInputStream()))) {

			while (!estaLoguejat) {
				System.out.println("AUTENTICACIO");
				String nomUsuari = obtenirInputUsuari("Introdueix el nom d'usuari: ", brTeclat);
				String contrasenya = obtenirInputUsuari("Introdueix la contrasenya: ", brTeclat);

				pwServidor.write(nomUsuari + ";" + contrasenya + "\n");
				pwServidor.flush();

				estaLoguejat = Boolean.valueOf(brServidor.readLine());
				if (!estaLoguejat) {
					System.out.println("Error en la autenticació");
				}
			}

			inicialitzarFilLector(conexio);

			System.out.println("Pulsa la tecla ENTER per a escriure un missatge...");
			while (estaLoguejat) {
				brTeclat.readLine();

				String input = JOptionPane.showInputDialog("Escriu el missatge: (exit per a tancar)");
				if (input.equals("exit")) {
					estaLoguejat = false;
				}
				System.out.println(generateTimeStamp() + ": " + input);
				pwServidor.println(input);
				pwServidor.flush();
			}

			System.out.println("Conexio finalitzada.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Comprova que l'usuari no tinga espais.
	 * 
	 * @param prompt
	 * @param reader
	 * @return el nom d'usuari si es correcte.
	 * @throws IOException
	 */
	private static String obtenirInputUsuari(String prompt, BufferedReader reader) throws IOException {
		String userInput = " ";
		while (userInput.indexOf(" ") != -1) {
			System.out.println(prompt);
			userInput = reader.readLine();
			if (userInput.indexOf(" ") != -1) {
				System.out.println("Aquest camp no pot contindre espais");
			}
		}
		return userInput;
	}

	private static void inicialitzarFilLector(Socket conexio) {
		Lector lector = new Lector(conexio);
		Thread thread = new Thread(lector);
		thread.start();
	}

	private static String generateTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return sdf.format(new Date());
	}
}
