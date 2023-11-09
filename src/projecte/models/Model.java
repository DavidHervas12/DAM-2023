package projecte.models;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Model {

	private static Connection con;

	/**
	 * Métode encarregat d' obrir la conexio amb un usuari específic.
	 * 
	 * @param ruta pot ser client.xml o admin.xml.
	 */
	public void AbrirConexio(String ruta) {
		Usuari usuari = ObtenirUsuari(ruta);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(usuari.getUrl(), usuari.getUsername(), usuari.getPassword());
			if (con != null && !con.isClosed()) {
				System.out.println("La conexion se ha establecido con exito");
			}

		} catch (SQLException | ClassNotFoundException e) {
			MostrarMisstageError(e.getMessage());
		}
	}

	/**
	 * Métode per a tancar la conexió.
	 */
	public void TancarConexio() {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Aquest métode comprova que el nom d'usuari y la contrasenya estiguen
	 * registrats a la base de dades.
	 * 
	 * @param nom_usuario
	 * @param contrasenya
	 * @return el tipus d'usuari: client o admin
	 */
	public String ComprobarCredencials(String nom_usuari, String contrasenya) {
		String sql = "SELECT * FROM users WHERE user = ? AND pass = ?";

		try (PreparedStatement psSelect = con.prepareStatement(sql)) {
			psSelect.setString(1, nom_usuari);
			psSelect.setString(2, EncriptarHash(contrasenya));

			try (ResultSet rs = psSelect.executeQuery()) {
				if (rs.next()) {
					return rs.getString(4);
				} else {
					MostrarMisstageError("La contraseña o el usuario son incorrectos.");
				}
			} catch (SQLException e) {
				MostrarMisstageError("Error al ejecutar la consulta: " + e.getMessage());
			}
		} catch (SQLException e) {
			MostrarMisstageError("Error al preparar la consulta: " + e.getMessage());
		}

		return "";
	}

	/**
	 * Aquest métode te la funció d'omplir el model de dades per a omplir el
	 * JTable de la Vista quan es realitza una consulta SELECT.
	 * 
	 * @param consulta SQL
	 * @return Model de la tabla
	 */
	public DefaultTableModel Visualitzar(String consulta) {

		DefaultTableModel tabla = null;
		ArrayList<Object[]> llistaDades = new ArrayList<Object[]>();
		String[] nomsColumnes;

		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(consulta)) {

			int nombreColumnes = rs.getMetaData().getColumnCount();
			nomsColumnes = new String[nombreColumnes];

			for (int i = 0; i < nombreColumnes; i++) {
				nomsColumnes[i] = rs.getMetaData().getColumnName(i + 1).toUpperCase();
			}

			while (rs.next()) {
				Object[] fila = new Object[nombreColumnes];
				for (int i = 1; i <= nombreColumnes; i++) {
					int tipus = rs.getMetaData().getColumnType(i);
					if (tipus == java.sql.Types.VARCHAR) {
						fila[i - 1] = rs.getString(i);

					} else if (tipus == java.sql.Types.INTEGER) {
						fila[i - 1] = rs.getInt(i);
					}
				}
				llistaDades.add(fila);
			}

			// Transforma la llista a array bidimensional.
			Object[][] arrayDades = new Object[llistaDades.size()][];
			for (int i = 0; i < arrayDades.length; i++) {
				arrayDades[i] = llistaDades.get(i);
			}

			tabla = new DefaultTableModel(arrayDades, nomsColumnes);
		} catch (SQLException e) {
			MostrarMisstageError("La consulta introduida no es válida: \n" + e.getMessage());
		}

		return tabla;
	}
	
	/**
	 * Métode encarregat de realitzar consultes de modificació (INSERT, UPDATE, DELETE)
	 *
	 * @param consulta
	 */
	public void ConsultaAdministrador(String consulta) {
		try (PreparedStatement psConsulta = con.prepareStatement(consulta);) {
			int affectedRows = psConsulta.executeUpdate();
			JOptionPane.showMessageDialog(null, affectedRows + " Filas Afectadas", "EXITO",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			MostrarMisstageError("La consulta introduida no es válida: \n" + e.getMessage());
		}
	}

	/**
	 * Métode encarregat d'obtenir les dades d'usuari d'un fitxer xml
	 * segons la ruta proporcionada per paràmetre.
	 * 
	 * @param ruta de l'arxiu xml
	 * @return
	 */
	private Usuari ObtenirUsuari(String ruta) {

		Usuari usuari = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(ruta));

			Element raiz = doc.getDocumentElement();
			String url = raiz.getElementsByTagName("url").item(0).getTextContent();
			String username = raiz.getElementsByTagName("username").item(0).getTextContent();
			String password = raiz.getElementsByTagName("password").item(0).getTextContent();
			System.out.println(url + username + password);
			usuari = new Usuari(url, username, password);

		} catch (ParserConfigurationException | SAXException | IOException e) {
			MostrarMisstageError(e.getMessage());
		}

		return usuari;

	}

	/**
	 * Métode encarregat de pasar la contrasenya a hash per a compararla
	 * amb la de la base de dades en el métode ComprovarCredencials.
	 * @param text
	 * @return el text encriptat en MD5.
	 */
	private String EncriptarHash(String text) {
		String hashText = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(text.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			hashText = number.toString(16);
			while (hashText.length() < 32) {
				hashText = "0" + hashText;
			}

		} catch (NoSuchAlgorithmException e) {
			System.err.println("Error: Algoritmo MD5 no encontrado.");
		}
		return hashText;
	}

	private void MostrarMisstageError(String missatje) {
		JOptionPane.showMessageDialog(null, missatje, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
