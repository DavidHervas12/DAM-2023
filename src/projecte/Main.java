package projecte;

import projecte.controladors.Controlador;
import projecte.models.Model;
import projecte.vistes.VistaLogIn;

public class Main {

	public static void main(String[] args) {
		Model model = new Model();
		VistaLogIn vistaLogIn = new VistaLogIn();
		Controlador controlador = new Controlador(model, vistaLogIn);
		

	}

}
