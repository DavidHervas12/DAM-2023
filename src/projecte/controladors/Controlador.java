package projecte.controladors;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

import projecte.models.Model;
import projecte.vistes.VistaApp;
import projecte.vistes.VistaLogIn;

public class Controlador {
	Model model;
	VistaLogIn vistaLogIn;
	VistaApp vistaApp;
	private static String tipus;

	/**
	 * Constructor de la clase Controlador, classe encarregada de controlar tots els
	 * events de la classe Vista e implementar en aquestos, els métodes situats dins
	 * de la classe controlador.
	 * 
	 * @param vista
	 * @param model
	 */
	public Controlador(Model model, VistaLogIn vistaLogIn) {
		this.model = model;
		this.vistaLogIn = vistaLogIn;
		model.AbrirConexio("client.xml");
		IniciaSessio();
	}

	public void IniciaSessio() {
		vistaLogIn.getBtnIniciarSesio().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tipus = model.ComprobarCredencials(vistaLogIn.getTxtNomUsuari().getText(),
						vistaLogIn.getTxtContrasenya().getText());
				System.out.println(tipus);
				if (tipus.equals("admin") || tipus.equals("client")) {
					vistaLogIn.setVisible(false);
					vistaApp = new VistaApp();
					if (tipus.equals("admin")) {
						model.TancarConexio();
						model.AbrirConexio("admin.xml");
					}
					vistaApp.getLblTipusUsuari().setText(tipus.toUpperCase());
					RealitzarConsulta();
					TancarSessioControlador();
					InicialitzaRellotge();
					Eixir();
				}
			}
		});
	}

	public void TancarSessioControlador() {
		vistaApp.getBtnTancarSessio().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcio = JOptionPane.showConfirmDialog(null, "¿Estás segur de que vols tancar la sessio?",
						"Confirmació", JOptionPane.YES_NO_OPTION);
				if (opcio == JOptionPane.YES_OPTION) {
					if (tipus.equals("admin") || tipus.equals("client")) {
						vistaLogIn.setVisible(true);
						vistaApp.dispose();
						if (tipus.equals("admin")) {
							model.TancarConexio();
							model.AbrirConexio("client.xml");
						}
					}
				}

			}
		});
	}

	public void RealitzarConsulta() {
		vistaApp.getBtnExecutar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String consulta = vistaApp.getTxtConsultaSQL().getText();
				if (TrobarCoincidencia("SELECT", consulta)) {
					CargarTabla(consulta);
				} else if (TrobarCoincidencia("INSERT", consulta) || TrobarCoincidencia("UPDATE", consulta)
						|| TrobarCoincidencia("DELETE", consulta)) {
					if (tipus.equals("admin")) {
						int opcio = JOptionPane.showConfirmDialog(null,
								"Aquesta consulta realitzará canvis en la vase de dades,\n ¿Estás segur de continuar?",
								"Confirmació", JOptionPane.YES_NO_OPTION);
						if (opcio == JOptionPane.YES_OPTION) {
							model.ConsultaAdministrador(consulta);
						}

					} else {
						JOptionPane.showMessageDialog(null,
								"Per a executar aquesta sentencia inicia sessió com administrador", "Advertencia",
								JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "La consulta introduïda no es válida", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	public void Eixir() {
		vistaApp.getBtnEixir().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcio = JOptionPane.showConfirmDialog(null, "¿Estás segur de que vols eixir de l'aplicació?",
						"Confirmació", JOptionPane.YES_NO_OPTION);
				if (opcio == JOptionPane.YES_OPTION) {
					model.TancarConexio();
					JOptionPane.showMessageDialog(null, "S'ha tancat la conexió, Eixint de l'aplicació...", "Eixida",
							JOptionPane.INFORMATION_MESSAGE);
					vistaApp.dispose();
					vistaLogIn.dispose();
				}
			}
		});
	}

	/**
	 * Métode encarregat de fer funcionar un rellotge digital.
	 */
	public void InicialitzaRellotge() {
		Timer timer = new Timer(1000, e -> {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			vistaApp.getLblRellotge().setText(sdf.format(new Date()));
		});
		timer.start();
	}

	/**
	 * Métode encarregar de carregaer el model de dades dins del JTable y mostrar-ho
	 * a VistaApp.
	 * 
	 * @param consulta
	 */
	private void CargarTabla(String consulta) {
		JTable table = new JTable(model.Visualitzar(consulta));
		Font fuente = new Font("Arial", Font.PLAIN, 12);
		table.setFont(fuente);
		table.setRowSelectionAllowed(false);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(83, 195, 700, 350);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		vistaApp.getContentPane().add(scrollPane);
	}

	/**
	 * Métode encarregat de trobar si la linia conté la paraula proporcionada.
	 * 
	 * @param text
	 * @param linea
	 * @return true si es troben coincidencies. false si no es troben coincidencies
	 */
	private boolean TrobarCoincidencia(String paraula, String linea) {

		String regex = Pattern.quote(paraula);

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(linea.toUpperCase());

		return matcher.find();

	}

}
