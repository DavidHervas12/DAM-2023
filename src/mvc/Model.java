package mvc;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.util.Date;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Comparator;
import java.util.Arrays;

public class Model {

	private ArrayList<String> llistaRutes = new ArrayList<String>();

	/**
	 * Constructor de la classe model, part de l'aplicació que conté tots els
	 * métodes per a realitzar operacions y la llogica.
	 */
	public Model() {

	}

	public ArrayList<String> getLlistaRutes() {
		return llistaRutes;
	}

	/**
	 * Métode per a llistar els fitxers de text, retornant un string en format nom,
	 * tamany y data, segons el ordre que indiques, per defecte estará ordenat per
	 * nom y ascendent.
	 * 
	 * @param ruta  del directori
	 * @param ordre ascendent o descendent.
	 * @param tipus ordenat per nom, tamany o data.
	 * @return Fitxers .txt amb les seues característiques.
	 */
	public String LlistarElements(String ruta, String ordre, String tipus) {
		File dir = new File(ruta);
		Filtre filtre = new Filtre(".txt");
		File[] fitxers;
		String llistaNoms = "No s'ha trobat ningun arxiu .txt";

		if (dir.isDirectory()) {
			fitxers = dir.listFiles(filtre);
			ArrayList<String> llistaOrdenada = OrdenaPer(fitxers, ordre, tipus);
			llistaNoms = "";
			for (String str : llistaOrdenada) {
				llistaNoms += str + "\n";
			}
		}

		return llistaNoms;
	}

	/**
	 * Métode per a ordenar la llista de fitxers del directori segons el ordre y el
	 * tipus seleccionat.
	 * 
	 * @param fitxers array de fitxers a ordenar
	 * @param ordre   ascendent o descendent.
	 * @param per     ordenat per nom, tamany o data.
	 * @return torna una llista dels fitxers en ordre correcte.
	 */
	private ArrayList<String> OrdenaPer(File[] fitxers, String ordre, String per) {
		ArrayList<String> novaLlista = new ArrayList<String>();
		Comparator<File> fileComparator = null;

		if ("nom".equalsIgnoreCase(per)) {
			fileComparator = Comparator.comparing(File::getName);
		} else if ("tamany".equalsIgnoreCase(per)) {
			fileComparator = Comparator.comparingLong(File::length);
		} else {
			fileComparator = Comparator.comparingLong(File::lastModified);
		}

		// Ordena los archivos en función del Comparator
		Arrays.sort(fitxers, ordre.equalsIgnoreCase("ascendent") ? fileComparator : fileComparator.reversed());
		novaLlista = FormaLlista(fitxers);

		return novaLlista;
	}

	/**
	 * Métode per a convertir de array a llista
	 * 
	 * @param fitxers array de fitxers
	 * @return array convertida a llista
	 */
	private ArrayList<String> FormaLlista(File[] fitxers) {
		ArrayList<String> llista = new ArrayList<String>();
		for (File arxiu : fitxers) {
			Date data = new Date(arxiu.lastModified());
			String[] arrayData = data.toString().split(" ");
			String dataFormatada = data.getDay() + "/" + data.getMonth() + "/" + arrayData[arrayData.length - 1] + " "
					+ data.toGMTString().split(" ")[3];
			llista.add(arxiu.getName() + "\t" + arxiu.length() + " B\t" + dataFormatada);
		}

		return llista;
	}

	/**
	 * Métode encarregat de trobar el nombre de coincidencies de la paraula
	 * proporcionada y tornar una llista amb el nom de cada fitxer y el nombre de
	 * coincidencies.
	 * 
	 * @param ruta del directori
	 * @param text paraula proporcionada per el usuari
	 * @return llista amb el nom de cada fitxer y el nombre de coincidencies.
	 */

	public String TrobarCoincidencies(String ruta, String text) {
		File dir = new File(ruta);
		Filtre filtre = new Filtre(".txt");
		File[] fitxers;
		String llistaNoms = "No s'ha trobat ningun arxiu .txt";

		int coincidencies = 0;

		if (dir.isDirectory()) {
			fitxers = dir.listFiles(filtre);
			if (fitxers.length > 0) {
				llistaNoms = "";
				for (File arxiu : fitxers) {
					try {
						FileReader fr = new FileReader(arxiu);
						BufferedReader br = new BufferedReader(fr);
						String linea = br.readLine();

						while (linea != null) {
							coincidencies += ContarCoincidencies(text, linea);
							linea = br.readLine();
						}
						fr.close();
						br.close();
						llistaNoms += arxiu.getName() + " -> " + coincidencies + "\n";
						coincidencies = 0;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return llistaNoms;
	}

	/**
	 * Métode encarregat de tornar el nombre de coincidencies en cada linea.
	 * 
	 * @param text  paraula a trobar.
	 * @param linea on se vol trobar.
	 * @return Nombre de coincidencies.
	 */
	private int ContarCoincidencies(String text, String linea) {

		String regex = Pattern.quote(text);
		int coincidencies = 0;

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(linea);

		while (matcher.find()) {
			coincidencies++;
		}

		return coincidencies;

	}

	/**
	 * Aquest métode utilitza el atribut "llistaRutes" per a llegir el text dels
	 * fitxers que s'han agregat a la llista y fusionar'ho tot dins un nou fitxer,
	 * el qual es crearà metjançant la ruta proporcionada per l'usuari.
	 * 
	 * @param rutaFusion ruta proporcionada per l'usuari.
	 */

	public void FusionarFitxers(String rutaFusion) {
		File nouFitxer = new File(rutaFusion);
		try {
			FileWriter fw = new FileWriter(nouFitxer);
			BufferedWriter bw = new BufferedWriter(fw);

			String linea;
			for (String ruta : llistaRutes) {
				try {
					FileReader fr = new FileReader(ruta);
					BufferedReader br = new BufferedReader(fr);
					linea = br.readLine();
					while (linea != null) {
						bw.write(linea);
						bw.newLine();
						linea = br.readLine();
					}
					br.close();
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			bw.close();
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		llistaRutes.clear();
	}

}
