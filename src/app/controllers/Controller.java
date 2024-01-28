package app.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.FiltreExtensio;

/**
 * Clase controlador que maneja la lògica de l'aplicació.
 */
@RestController
public class Controller {

	/**
	 * Gestiona les peticions get per a vore la informació de totes les películes o
	 * de una en concret
	 * 
	 * @param id paràmetre de la petició http
	 * @return 200 OK si la pelicula está registrada | 404 Not Found si la película
	 *         no está registrada
	 * @throws IOException
	 */
	@GetMapping("/APIpelis/t")
	public ResponseEntity<String> verPeli(@RequestParam(value = "id") String id) throws IOException {
		String strJson = "";

		if (id.equals("all")) {
			strJson = construirJsonTotesLesPelis();
		} else {
			strJson = construirJsonPeliPerId(id);
		}

		if (strJson.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.status(HttpStatus.OK).body(strJson);
	}

	/**
	 * Construeix un json amb totes les películes y las seua informació
	 * 
	 * @return JSON
	 * @throws IOException
	 */
	private String construirJsonTotesLesPelis() throws IOException {
		StringBuilder strJsonBuilder = new StringBuilder("{\"titols\": [");
		File dir = new File("pelis");
		String[] listaPelis = dir.list(new FiltreExtensio(".txt"));
		if (listaPelis != null) {
			for (int i = 0; i < listaPelis.length; i++) {
				String id = listaPelis[i].substring(0, 1);
				String peliJson = construirJsonPeliPerId(id);
				strJsonBuilder.append(peliJson);
				if (i < listaPelis.length - 1) {
					strJsonBuilder.append(",");
				}
			}
		}
		strJsonBuilder.append("]}");
		return strJsonBuilder.toString();
	}

	/**
	 * Construix un json de una pelicula en concret a partir del seu id
	 * 
	 * @param id
	 * @return JSON
	 * @throws IOException
	 */
	private String construirJsonPeliPerId(String id) throws IOException {
		File peliFile = new File("pelis\\" + id + ".txt");
		if (!peliFile.exists()) {
			return "";
		}

		StringBuilder strJsonBuilder = new StringBuilder("{");
		strJsonBuilder.append("\"id\": \"").append(id).append("\", \"titol\": ");

		try (BufferedReader br = new BufferedReader(new FileReader(peliFile))) {
			String titulo = br.readLine().split(":")[1].trim();
			strJsonBuilder.append("\"").append(titulo).append("\",");

			strJsonBuilder.append("\"ressenyes\":[");
			String linea;
			while ((linea = br.readLine()) != null) {
				strJsonBuilder.append("\"").append(linea.trim()).append("\",");
			}
			if (strJsonBuilder.charAt(strJsonBuilder.length() - 1) == ',') {
				strJsonBuilder.deleteCharAt(strJsonBuilder.length() - 1); // Eliminar la última coma
			}
			strJsonBuilder.append("]}");
		}

		return strJsonBuilder.toString();
	}

	/**
	 * Agrega una ressenya a una pelicula en concret
	 * 
	 * @param body cos de la petició
	 * @return 200 OK si la peteció ha tingut exit | 401 UNAUTHORIZED si el usuari
	 *         no está autoritzat
	 * @throws IOException
	 */
	@PostMapping("/APIpelis/novaRessenya")
	public ResponseEntity<?> AgregarComentari(@RequestBody String body) throws IOException {
		JSONObject obj = new JSONObject(body);
		String usuari = obj.getString("usuari");
		String id = obj.getString("id");
		String ressenya = obj.getString("ressenya");

		if (UsuariAutoritzat(usuari)) {
			File dir = new File("pelis");
			String[] listaPelis = dir.list(new FiltreExtensio(".txt"));
			for (String fitxer : listaPelis) {
				if (fitxer.subSequence(0, 1).equals(id)) {
					System.out.println(fitxer.subSequence(0, 1));
					try (BufferedWriter bw = new BufferedWriter(new FileWriter("pelis/" + fitxer, true));) {
						String str = usuari + ":" + ressenya;
						bw.write("\n" + str);
						System.out.println("Escrito");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	/**
	 * Agrega una nova pelicula
	 * 
	 * @param body cos de la petició
	 * @return 200 OK si la peteció ha tingut exit | 401 UNAUTHORIZED si el usuari
	 *         no está autoritzat
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	@PostMapping("/APIpelis/novaPeli")
	public ResponseEntity<?> AgregarPeli(@RequestBody String body) throws NumberFormatException, IOException {
		JSONObject obj = new JSONObject(body);
		String usuari = obj.getString("usuari");
		String titol = obj.getString("titol");

		if (UsuariAutoritzat(usuari)) {
			File dir = new File("pelis");
			String[] listaPelis = dir.list(new FiltreExtensio(".txt"));
			String ultimFitxer = (String) listaPelis[listaPelis.length - 1].subSequence(0, 1);
			int ultimId = Integer.parseInt(ultimFitxer) + 1;
			try (BufferedWriter bw = new BufferedWriter(new FileWriter("pelis/" + ultimId + ".txt"));) {
				String str = "Titulo" + ": " + titol;
				bw.write(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	/**
	 * Agrega un nou usuari autoritzat
	 * 
	 * @param body cos de la petició
	 * @return
	 */
	@PostMapping("/APIpelis/nouUsuari")
	public ResponseEntity<?> AgregarUsuari(@RequestBody String body) {
		JSONObject obj = new JSONObject(body);
		String usuari = obj.getString("usuari");
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("autorizados.txt", true));) {
			bw.write("\n" + usuari);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * Comprova si l'usuari está autoritzat
	 * 
	 * @param nomUsuari
	 * @return true si está autoritzat | false si no está autoritzat
	 * @throws IOException
	 */
	private boolean UsuariAutoritzat(String nomUsuari) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader("autorizados.txt"));){
			String linea = "";
			while ((linea = br.readLine()) != null) {
				if (linea.equals(nomUsuari)) {
					return true;
				}
			}
			return false;			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
}
