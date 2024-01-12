package app.servidor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * La clase Peticio es el manjador d'usuaris y l'encarregada de gestionar la
 * comunicació amb el client.
 */
public class Peticio implements Runnable {
	private Socket conexio;
	private BufferedReader reader;
	private PrintWriter writer;
	private boolean conectat = false;
	private List<Peticio> conexions;
	private String nom;

	/**
	 * Contructor de la clase petició
	 * 
	 * @param conexió   del client
	 * @param conexions llista de conexions que gestiona els usuaris conectats
	 */
	public Peticio(Socket conexio, List<Peticio> conexions) {
		this.conexio = conexio;
		this.conexions = conexions;
	}

	public void run() {
		try (InputStream is = conexio.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				OutputStream os = conexio.getOutputStream();
				PrintWriter pw = new PrintWriter(os)) {
			this.reader = br;
			this.writer = pw;

			autenticarUsuari();

			while (conectat) {
				String missatge = br.readLine();
				procesarMissatge(missatge);
			}

			System.out.println("Usuari " + nom + " desconectat.");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conectat) {
				System.out.println("SERVER >> Conexio finalitzada.");
			}
		}
	}

	/**
	 * Controla l'autenticació de l'usuari
	 * 
	 * @throws IOException
	 */
	private void autenticarUsuari() throws IOException {
		System.out.println("AUTENTICACIO");
		while (!conectat) {
			String logIn = reader.readLine();
			if (existeixUsuari(logIn) && !usuariJaEstaConectat(logIn.split(";")[0])) {
				this.nom = logIn.split(";")[0];
				Thread.currentThread().setName(this.nom);
				conexions.add(this);
				writer.println("true");
				writer.flush();
				System.out.println("SERVIDOR >> Usuari loguejat");
				conectat = true;
			} else {
				System.out.println("SERVIDOR >> Error en l'autenticacio");
				writer.println("false");
				writer.flush();
			}
		}
	}

	/**
	 * Comprova si l'usuari ja està conectat
	 * 
	 * @param nomUsuari
	 * @return true si l'usuari està conectat | false si no ho està
	 */
	private boolean usuariJaEstaConectat(String nomUsuari) {
		for (Peticio p : conexions) {
			if (p.getNom().equals(nomUsuari)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Comprova si l'usuari existeix.
	 * 
	 * @param logIn nom d'usuari y contrasenya separats amb ";"
	 * @return true si l'usuari existeix | false si no existeix
	 */
	private boolean existeixUsuari(String logIn) {
		try (BufferedReader fileReader = new BufferedReader(new FileReader("autorizados.txt"))) {
			String linea;
			while ((linea = fileReader.readLine()) != null) {
				if (logIn.equals(linea)) {
					System.out.println(linea);
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Procesa el missatge y realitza una opció depenent del seu tipus
	 * 
	 * @param missatge a enviar
	 * @throws IOException
	 */
	private void procesarMissatge(String missatge) throws IOException {
		if (missatge.equals("?")) {
			System.out.println("Mostrar usuaris");
			writer.println(mostrarUsuarisConectats());
			writer.flush();
		} else if (missatge.startsWith("@")) {
			System.out.println("Susurro");
			String user = missatge.split(" ")[0].substring(1);
			susurro(user, missatge);
		} else if (missatge.equals("exit")) {
			conectat = false;
			conexions.remove(this);
			writer.println("exit");
			writer.flush();
		} else {
			enviarATots(missatge);
			System.out.println("Enviant Missatges");
		}
	}

	/**
	 *
	 * @return String amb els usuaris conectats
	 */
	private String mostrarUsuarisConectats() {
		StringBuilder usuarisConectats = new StringBuilder("Usuaris conectats:\n");
		for (Peticio p : conexions) {
			usuarisConectats.append(p.getNom()).append("\n");
		}
		return usuarisConectats.toString();
	}

	/**
	 * Envia el missatge a tots els usuaris conectats
	 * 
	 * @param missatge a enviar
	 * @throws IOException
	 */
	private void enviarATots(String missatge) throws IOException {
		for (Peticio con : conexions) {
			if (!con.getNom().equals(nom)) {
				String missatgeFinal = generarTimeStamp() + ": " + missatge;
				PrintWriter pwUser = new PrintWriter(con.getConexio().getOutputStream());
				pwUser.println(generarTimeStamp() + ": " + nom + " >>> " + missatgeFinal);
				pwUser.flush();
			}
		}
	}

	/**
	 * Envia un missatge a un usuari en concret
	 * 
	 * @param usuari
	 * @param missatge
	 */
	private void susurro(String usuari, String missatge) {
		for (Peticio con : conexions) {
			if (con.getNom().equals(usuari)) {
				try {
					String missatgeFinal = generarTimeStamp() + ":";
					String[] missatgeSeparat = missatge.split(" ");
					for (int i = 1; i < missatgeSeparat.length; i++) {
						missatgeFinal += " " + missatgeSeparat[i];
					}
					PrintWriter pwUsuari = new PrintWriter(con.getConexio().getOutputStream());
					pwUsuari.println(generarTimeStamp() + ": " + nom + "(privado) >>> " + missatgeFinal);
					pwUsuari.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * genera un timeStamp
	 * 
	 * @return timeStamp
	 */
	private String generarTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy_HH:mm:ss");
		return sdf.format(new Date());
	}

	public Socket getConexio() {
		return conexio;
	}

	public String getNom() {
		return nom;
	}
}
