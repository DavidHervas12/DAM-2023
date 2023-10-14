package mvc;

public class Principal {

	public static void main(String[] args) {
		// TODO Comentar el código con javadoc.
		Model model = new Model();
		Vista vista = new Vista();
		Controlador controlador = new Controlador(vista, model);

	}

}
