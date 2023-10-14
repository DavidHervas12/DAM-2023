package mvc;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class Controlador {

	private Vista vista;
	private Model model;

	/**
	 * Constructor de la clase Controlador, classe encarregada de controlar tots els
	 * events de la classe Vista e implementar en aquestos, els métodes situats dins
	 * de la classe controlador.
	 * 
	 * @param vista
	 * @param model
	 */
	public Controlador(Vista vista, Model model) {
		this.vista = vista;
		this.model = model;
		LlistarFitxers();
		TrobarCoincidencies();
		AgregarRuta();
		FusionarFitxers();
		SeleccionarDirectori();
	}

	/**
	 * Funcionalitat per a seleccionar un directori amb l'objecte JFileChooser.
	 */
	private void SeleccionarDirectori() {
		vista.getBtnFileExplorer().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int resp = fc.showOpenDialog(null);
				if (resp == JFileChooser.APPROVE_OPTION) {
					File dir = fc.getSelectedFile();
					vista.getTxtRuta().setText(dir.getAbsolutePath());
					;
				}
			}
		});
	}

	private void LlistarFitxers() {
		vista.getBtnLlista().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (!vista.getTxtRuta().getText().isEmpty()) {
					String ordre = vista.getRdbtnAscendent().isSelected() ? "ascendent" : "descendent";

					vista.getTxtLlistaFitxers().setText(model.LlistarElements(vista.getTxtRuta().getText(), ordre,
							vista.getCmbOrdenarPer().getSelectedItem().toString()));
				} else {
					JOptionPane.showMessageDialog(null, "La ruta no pot estar buida", "Alert",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	private void TrobarCoincidencies() {
		vista.getBtnBusca().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (!vista.getTxtRuta().getText().isEmpty()) {
					vista.getTxtLlistaFitxers().setText(model.TrobarCoincidencies(vista.getTxtRuta().getText(),
							vista.getTxtTrobarCoincidencia().getText()));
				} else {
					JOptionPane.showMessageDialog(null, "La ruta no pot estar buida", "Alert",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	private void AgregarRuta() {
		vista.getBtnAgregar().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				model.getLlistaRutes().add(vista.getTextRutaFusionar().getText());
			}
		});
	}

	private void FusionarFitxers() {
		vista.getBtnFusionar().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String input = JOptionPane.showInputDialog("Introdueix el nom del nou fitxer resultant");

				if (input != null) {
					if (ExisteixDirectori(input)) {
						int result = JOptionPane.showConfirmDialog(null,
								"Ya existeix un fitxer amb aquest nom, ¿desitjes sobrescriure-lo?", "Alert",
								JOptionPane.WARNING_MESSAGE);
						if (result == JOptionPane.OK_OPTION) {
							model.FusionarFitxers(input);
						}
					} else {
						model.FusionarFitxers(input);
					}
				}
			}
		});
	}

	/**
	 * Métode amb la tarea de comprobar si al directori ya existeix un fitxer amb el nom
	 * proporcionat per l'usuari;
	 * @param ruta
	 * @return true si el nom existeix o false si no existeix.
	 */
	private boolean ExisteixDirectori(String ruta) {
		Path path = Paths.get(ruta);
		File directori = new File(path.getParent().toString());

		for (String str : directori.list()) {
			if (str.equals(path.getFileName().toString())) {
				return true;
			}
		}

		return false;
	}
}
