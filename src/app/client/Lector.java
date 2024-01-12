package app.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Llig els missatges del servidor en un fil apart
 */
public class Lector implements Runnable {
	private Socket conexio;
	private volatile boolean conectat = true;

	/**
	 * Constructor de la clase lector
	 * 
	 * @param conexio
	 */
	public Lector(Socket conexio) {
		this.conexio = conexio;
	}

	public void setConectat(boolean conectat) {
		this.conectat = conectat;
	}

	public void run() {
		try (InputStream is = conexio.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr)) {
			System.out.println("Benvingut, ya pots escriure y rebre missatges, distruta!!");
			while (conectat) {
				String missatgeServidor = br.readLine();
				if (missatgeServidor.equals("exit")) {
					conectat = false;
				}
				System.out.println(missatgeServidor);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
