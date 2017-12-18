package br.unb.cic.mase.gui;

import javax.swing.JFrame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class MainScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private static MainScreen instance;
	private SimulationScreen simulationScr;
	private ControllerPanel controllerPan;

	private MainScreen() {
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.9;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;

		configureCanvas();

		this.getContentPane().add(simulationScr, c);

		configureControllers();

		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.1;
		c.weighty = 1;
		c.gridx = 1;
		c.gridy = 0;
		this.getContentPane().add(controllerPan, c);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public static MainScreen getInstance() {
		if (instance == null) {
			instance = new MainScreen();
		}
		return instance;
	}

	public void configureCanvas(){
		int rows = 20;
		int columns = 20;

		simulationScr = SimulationScreen.getInstance();
		simulationScr.setColumnsAndRows(columns, rows);
	}

	public void configureControllers() {
		controllerPan = ControllerPanel.getInstance();
	}

	public static void main(String[] args) {
		MainScreen.getInstance();
	}
}
