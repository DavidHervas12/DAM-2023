package app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Clase encarregada del proces de fabricació de tetrónims.
 */
public class Manufacture implements Runnable {
	private static final int MAX_MAQUINES = 8;
	private static final Queue<String> cuaDePeces = new LinkedList<>();
	private static int maquinesDisponibles = MAX_MAQUINES;
	private String tipus;

	public static void main(String[] args) {
		int quantitat = Integer.parseInt(args[0]);
		String tipus = args[1];

		for (int i = 0; i < quantitat; i++) {
			Thread fil = new Thread(new Manufacture(tipus));
			fil.start();
		}
	}

	public Manufacture(String tipus) {
		this.tipus = tipus;
	}

	/**
	 * Métode implementat per l'interfície Runnable, en aquest cas s'encarrega de
	 * gestionar el procés de fabricació de les peces simulant que soles hi han 8
	 * màquines, aleshores, quan está ple,espera fins que hi ha un nou lloc a la
	 * cua, i s'inicialitza el nou fil, així fins que s'han fabricat totes les
	 * peces.
	 */
	public void run() {
		synchronized (cuaDePeces) {
			while (maquinesDisponibles == 0) {
				try {
					cuaDePeces.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			maquinesDisponibles--;
		}

		FabricaPesa();
		GeneraPesa();

		synchronized (cuaDePeces) {
			maquinesDisponibles++;
			cuaDePeces.notify();
		}
	}

	/**
	 * El temps de fabricació es correspon a aquest mètode, on es simula l’ocupació
	 * de la màquina com si fora un consum de processador:
	 * 
	 * @param tempsFabricacio
	 */
	public void ProcesFabricacio(int tempsFabricacio) {
		long tempsInici = System.currentTimeMillis();
		long tempsFinal = tempsInici + tempsFabricacio;
		int iteracions = 0;
		while (System.currentTimeMillis() < tempsFinal) {
			iteracions++;
		}
	}

	/**
	 * Métode que simula la fabricació de cada peça, aplicant un temps de fabricació
	 * determinat, depenent del tipus de la peça.
	 */
	public void FabricaPesa() {
		switch (tipus) {
		case "I":
			ProcesFabricacio(1000);
			break;
		case "O":
			ProcesFabricacio(2000);
			break;
		case "T":
			ProcesFabricacio(3000);
			break;
		case "J":
		case "L":
			ProcesFabricacio(4000);
			break;
		case "S":
		case "Z":
			ProcesFabricacio(5000);
			break;
		default:
			break;
		}
	}

	/**
	 * Métode encarregat de agregar la peça a la cua i imprimir un output amb el
	 * tipus de la peça, la data i l'hora.
	 */
	public void GeneraPesa() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fechaHora = sdf.format(new Date());
		String pesa = tipus + "_" + fechaHora;
		System.out.println(pesa);
		cuaDePeces.add(pesa);
	}
}
