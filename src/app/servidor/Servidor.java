package app.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal del Servidor
 */
public class Servidor {

	private static final int PORT = 5000;

	public static void main(String[] args) {

		try {
			iniciarServidor(PORT);
		} catch (IOException e) {
			System.err.println("Error al iniciar el servidor: " + e.getMessage());
		}
	}

	/**
	 * Arranca el servidor
	 * 
	 * @param port
	 * @throws IOException
	 */
	private static void iniciarServidor(int port) throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			ArrayList<Peticio> clients = new ArrayList<Peticio>();
			System.err.println("SERVIDOR >> Escoltant en el port " + port + "...");

			while (true) {
				Socket conexio = serverSocket.accept();
				manejadorConexio(conexio, clients);
			}
		}
	}

	/**
	 * Gestiona la petició e inicia un nou fil
	 * 
	 * @param connection
	 * @param clients
	 */
	private static void manejadorConexio(Socket connection, ArrayList<Peticio> clients) {
		Peticio request = new Peticio(connection, clients);
		System.out.println("SERVIDOR >> Client conectat, llançant fil");
		Thread thread = new Thread(request);
		thread.start();
	}

}
