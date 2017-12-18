package br.unb.cic.mase.gui;

//import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import javax.swing.ButtonGroup;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
import java.io.PrintWriter;

import java.awt.event.ItemEvent;

import java.awt.Dimension;


import javax.swing.JLabel;
import javax.swing.JOptionPane;

import br.unb.cic.mase.Util.Printer;
import br.unb.cic.mase.agents.IDeliberately;
import br.unb.cic.mase.agents.PlatformFactory;
import jadex.bridge.IComponentIdentifier;

import jadex.bridge.IExternalAccess;


import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import java.io.IOException;


public class ControllerPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static ControllerPanel instance;
	private String platformStatusInfo;
	private double waterLevel;
	private int currentStep;
	private int currentMonth;
	private boolean isR1active;
	private boolean isR2active;
	private JLabel platformStatus;
	private JLabel waterLevelInfo;
	private JLabel currentStepInfo;
	private JLabel isR1activeInfo;
	private JLabel isR2activeInfo;
	private JPanel buttons;
	private JButton startStop;
	private JButton nextStep;
	private JButton toggleR1;
	private JButton toggleR2;
	private JButton report;
	private JRadioButton NTR, NRT, TNR, TRN, RNT, RTN;
	private IExternalAccess platform;
	private IComponentIdentifier TAID[];
	private ButtonGroup buttonGroup;
	private RadioButtonHandler handler;
	private JButton configureGrid;

	public int selectedPriority = 1;
	private int flag = 0;

	public double consumption_BAIXA_FI = 0;
	public double consumption_BAIXA_FC = 0;
	public double consumption_BAIXA_MC = 0;
	public double consumption_BAIXA_SC = 0;
	public double consumption_MEDIA_FC = 0;
	public double consumption_MEDIA_MC = 0;
	public double consumption_MEDIA_SC = 0;
	public double consumption_ALTA_MC = 0;
	public double consumption_ALTA_SC = 0;
	

	public int totalCooperation_BAIXA_FI = 0;
	public int totalCooperation_BAIXA_FC = 0;
	public int totalCooperation_BAIXA_MC = 0;
	public int totalCooperation_BAIXA_SC = 0;
	
	public int totalCooperation_MEDIA_FC = 0;
	public int totalCooperation_MEDIA_MC = 0;
	public int totalCooperation_MEDIA_SC = 0;
	
	public int totalCooperation_ALTA_MC = 0;
	public int totalCooperation_ALTA_SC = 0;

	
	public static int agentsCount = 0;
	private static int barrier = 0;

	private final int BAIXA_FI = 1;
	private final int BAIXA_FI_COOP = 2;
	private final int BAIXA_FC = 3;
	private final int BAIXA_FC_COOP = 4;
	private final int BAIXA_MC = 5;
	private final int BAIXA_MC_COOP = 6;
	private final int BAIXA_SC = 7;
	private final int BAIXA_SC_COOP = 8;
	
	private final int MEDIA_FC = 9;
	private final int MEDIA_FC_COOP = 10;
	private final int MEDIA_MC = 11;
	private final int MEDIA_MC_COOP = 12;
	private final int MEDIA_SC = 13;
	private final int MEDIA_SC_COOP = 14;
	
	private final int ALTA_MC = 15;
	private final int ALTA_MC_COOP = 16;
	private final int ALTA_SC = 17;
	private final int ALTA_SC_COOP = 18;
	

	public ControllerPanel() {
		waterLevel = 10000;
		currentStep = 0;
		isR1active = false;
		isR2active = false;
		
		waterLevelInfo = new JLabel(
				"<html><p style=\"color:green;font-size:17px;padding-top:3px;padding-bottom:3px;\">Agent-Hydric</p></html>");
	
		
		platformStatusInfo = "<html><p style=\"color:red;font-size:14px;padding-top:6px;\">" + "Platform is stopped"
				+ "</p></html>";


		
		platformStatus = new JLabel(
				"<html><p style=\"color:red;font-size:14px;padding-top:6px;\">" + platformStatusInfo + "</p></html>");



		currentStepInfo = new JLabel(
				"<html><p style=\"color:black;font-size:14px;padding-top:3px;padding-bottom:3px;\">Current Step:"
						+ currentStep + "</p></html>");

		isR1activeInfo = new JLabel(
				"<html><p style=\"color:black;font-size:14px;padding-top:3px;padding-bottom:3px;\">Tax active:"
						+ isR1active + "</p></html>");

		isR2activeInfo = new JLabel(
				"<html><p style=\"color:black;font-size:14px;padding-top:3px;padding-bottom:3px;\">Education active:"
						+ isR2active + "</p></html>");

		buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.PAGE_AXIS));

		startStop = new JButton("Start");
		startStop.addActionListener(this);
		startStop.setEnabled(true);

		nextStep = new JButton("Next step");
		nextStep.addActionListener(this);
		nextStep.setEnabled(false);

		toggleR1 = new JButton("Toggle tax");
		toggleR1.addActionListener(this);
		toggleR1.setEnabled(true);

		toggleR2 = new JButton("Toggle education");
		toggleR2.addActionListener(this);
		toggleR2.setEnabled(true);

		report = new JButton("Generate Report");
		report.addActionListener(this);
		report.setEnabled(true);

		configureGrid = new JButton("Configure Grid");
		configureGrid.addActionListener(this);
		configureGrid.setEnabled(true);

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(waterLevelInfo);
		this.add(platformStatus);
		this.add(currentStepInfo);
		this.add(isR1activeInfo);
		this.add(Box.createRigidArea(new Dimension(5, 5)));
		this.add(isR2activeInfo);
		this.add(Box.createRigidArea(new Dimension(5, 5)));

		buttons.add(startStop);
		buttons.add(Box.createRigidArea(new Dimension(5, 5)));
		buttons.add(nextStep);
		buttons.add(Box.createRigidArea(new Dimension(5, 5)));
		buttons.add(toggleR1);
		buttons.add(Box.createRigidArea(new Dimension(5, 5)));
		buttons.add(toggleR2);
		buttons.add(Box.createRigidArea(new Dimension(5, 5)));
		buttons.add(configureGrid);
		buttons.add(report);
		buttons.add(Box.createRigidArea(new Dimension(5, 5)));

		this.add(buttons);
		// RadioButton();

	}

	public static ControllerPanel getInstance() {
		if (instance == null) {
			instance = new ControllerPanel();
		}
		return instance;
	}

	public void actionPerformed(ActionEvent arg0) {

		boolean drySeason;

		if (arg0.getSource().equals(startStop)) {
			if (platform == null) {
				updatePlatformStatusInfo(
						"<html><p style=\"color:red;font-size:14px;padding-top:6px;\">Please set agents...</p></html>");
				ArrayList<HashMap<String, Object>> arguments = buildArguments();
				if (arguments != null) {
					updatePlatformStatusInfo(
							"<html><p style=\"color:red;font-size:14px;padding-top:6px;\">Agents loaded.</p></html>");
					platform = PlatformFactory.getPlatForm();
					setIsPlatformStarted(true);

					IComponentManagementService icms = SServiceProvider
							.getService(platform, IComponentManagementService.class).get();

					for (int i = 0; i < arguments.size(); i++) {
						icms.createComponent("transformationAgent" + i,
								"bin/br/unb/cic/mase/agents/TransformationAgentBDI.class",
								new CreationInfo(null, arguments.get(i))).getFirstResult();
					}
					agentsCount = arguments.size();
				}
			} else {
				platform.killComponent();
				platform = null;
				setIsPlatformStarted(false);
			}
		} else if (arg0.getSource().equals(nextStep)) {
			//DESABILITA O BOTAO
			
			nextStep.setEnabled(false);
			
			Printer.print("Next Step");
			currentStep++;
			updateCurrentStepInfo();
			currentMonth = currentStep % 12;

			if ((currentMonth >= 0 && currentMonth <= 4) || (currentMonth >= 10))
				drySeason = false;
			else
				drySeason = true;

//			System.out.println (drySeason);
			
			switch (currentMonth) {
			case 1: // January
				updatePlatformStatusInfo(
						"<html><p style=\"color:red;font-size:14px;padding-top:6px;\">January</p></html>");
				// increaseWaterLevel(2000);
				break;
			case 2: // February
				updatePlatformStatusInfo(
						"<html><p style=\"color:red;font-size:14px;padding-top:6px;\">February</p></html>");
				// increaseWaterLevel(2000);
				break;
			case 3: // March
				updatePlatformStatusInfo(
						"<html><p style=\"color:red;font-size:14px;padding-top:6px;\">March</p></html>");
				// increaseWaterLevel(2000);
				break;
			case 4: // April
				updatePlatformStatusInfo(
						"<html><p style=\"color:red;font-size:14px;padding-top:6px;\">April</p></html>");
				// increaseWaterLevel(2000);
				break;
			case 5: // May
				updatePlatformStatusInfo("<html><p style=\"color:red;font-size:14px;padding-top:6px;\">May</p></html>");
				// increaseWaterLevel(2000);
				break;
			case 6: // June
				updatePlatformStatusInfo(
						"<html><p style=\"color:red;font-size:14px;padding-top:6px;\">June</p></html>");
				// increaseWaterLevel(300);
				break;
			case 7: // July
				updatePlatformStatusInfo(
						"<html><p style=\"color:red;font-size:14px;padding-top:6px;\">July</p></html>");
				// increaseWaterLevel(300);
				break;
			case 8: // August
				updatePlatformStatusInfo(
						"<html><p style=\"color:red;font-size:14px;padding-top:6px;\">August</p></html>");
				// increaseWaterLevel(300);
				break;
			case 9: // September
				updatePlatformStatusInfo(
						"<html><p style=\"color:red;font-size:14px;padding-top:6px;\">September</p></html>");
				// increaseWaterLevel(300);
				break;
			case 10: // October
				updatePlatformStatusInfo(
						"<html><p style=\"color:red;font-size:14px;padding-top:6px;\">October</p></html>");
				// increaseWaterLevel(300);
				break;
			case 11: // November
				updatePlatformStatusInfo(
						"<html><p style=\"color:red;font-size:14px;padding-top:6px;\">November</p></html>");
				// increaseWaterLevel(300);
				break;
			default: // December
				updatePlatformStatusInfo(
						"<html><p style=\"color:red;font-size:14px;padding-top:6px;\">December</p></html>");
				// increaseWaterLevel(2000);
				break;
			}

			Collection<IDeliberately> serviceTransformationAgent = SServiceProvider
					.getServices(platform, IDeliberately.class, RequiredServiceInfo.SCOPE_GLOBAL).get();
			for (IDeliberately actualService : serviceTransformationAgent) {
				// neighbourhood =
				// checkNeighbourhood(actualService.getPositionX(),
				// actualService.getPositionY());
				// actualService.deliberate(drySeason, neighbourhood);
				actualService.deliberate(drySeason);
				updateSpaces(actualService.getPositionX(), actualService.getPositionY(), actualService.getType());
			}
			// for(IDeliberately actualService:serviceTransformationAgent){
			//
			// }

//			generateReport();

		} else if (arg0.getSource().equals(toggleR1)) {
			toggleR1();
		} else if (arg0.getSource().equals(toggleR2)) {
			toggleR2();
		} else if (arg0.getSource().equals(configureGrid)) {
			configureGrid();
		} else if (arg0.getSource().equals(report)) {
//			generateReport();
		}
	}

	private void updateSpaces(int i, int j, int newValue) {
		SimulationScreen.getInstance().setSpaces(i, j, newValue);
		SimulationScreen.getInstance().repaint();
	}

	private void updatePlatformStatusInfo(String platformStatusInfo) {
		this.platformStatusInfo = platformStatusInfo;
		setPlatformStatus();
	}

	private void updateCurrentStepInfo() {
		currentStepInfo.setText(
				("<html><p style=\"font-size:14px;padding-top:6px;\">Current Step: " + currentStep + "</p></html>"));

	}

	private void setPlatformStatus() {
		platformStatus.setText(platformStatusInfo);
		this.repaint();
	}

	private void setIsPlatformStarted(boolean isPlatFormStarted) {
		if (isPlatFormStarted == true) {
			
			nextStep.setEnabled(true);
			toggleR1.setEnabled(true);
			toggleR2.setEnabled(true);
			configureGrid.setEnabled(false);
			SimulationScreen.getInstance().printSpaces();
			currentStep = 0;
			startStop.setText("Stop");
			
		} else {
			startStop.setText("Start");
			nextStep.setEnabled(false);
			toggleR1.setEnabled(false);
			toggleR2.setEnabled(false);
			configureGrid.setEnabled(true);
		}
	}

	private void configureGrid() {
		int rows = Integer.parseInt(JOptionPane.showInputDialog("rows: "));
		int columns = Integer.parseInt(JOptionPane.showInputDialog("columns: "));

		String arr = JOptionPane.showInputDialog("grid: ");

		if (arr != null && !arr.isEmpty()) {
			String[] items = arr.replaceAll("\\[", "").replaceAll("\\]", "").split(",");

			int[] results = new int[items.length];

			for (int i = 0; i < items.length; i++) {
				try {
					results[i] = Integer.parseInt(items[i]);
				} catch (NumberFormatException nfe) {
					// NOTE: write something here if you need to recover from
					// formatting errors
				}

			}

			SimulationScreen.getInstance().setColumnsAndRows(rows, columns);

			int z = 0;

			for (int i = 0; i < columns; i++) {
				for (int j = 0; j < rows; j++) {
					// System.out.print(z + ", ");
					SimulationScreen.getInstance().setSpaces(i, j, results[z]);
					z++;
				}
			}
			this.repaint();

		} else {
			// setIsPlatformStarted(false);
			SimulationScreen.getInstance().setColumnsAndRows(rows, columns);
			for (int i = 0; i < columns; i++) {
				for (int j = 0; j < rows; j++) {
					SimulationScreen.getInstance().setSpaces(i, j, 0);
				}
			}
			currentStep = 0;
			waterLevel = 10000;
			updateWaterLevelInfo();
			SimulationScreen.getInstance().repaint();
		}
	}

	public boolean isR1Active() {
		return isR1active;
	}

	public boolean isR2Active() {
		return isR2active;
	}

	private void toggleR1() {
		isR1active = !isR1active;
		updateIsR1ActiveInfo();
	}

	private void toggleR2() {
		isR2active = !isR2active;
		updateIsR2ActiveInfo();
	}

	private void updateIsR1ActiveInfo() {
		isR1activeInfo
				.setText("<html><p style=\"color:black;font-size:14px;padding-top:3px;padding-bottom:3px;\">Tax active:"
						+ isR1active + "</p></html>");
		this.repaint();
	}

	private void updateIsR2ActiveInfo() {
		isR2activeInfo.setText(
				"<html><p style=\"color:black;font-size:14px;padding-top:3px;padding-bottom:3px;\">Education active:"
						+ isR2active + "</p></html>");
		this.repaint();
	}

	public synchronized void addTAIdentification(int index, IComponentIdentifier id) {
		TAID[index] = id;
	}

	private ArrayList<HashMap<String, Object>> buildArguments() {
		ArrayList<HashMap<String, Object>> arguments = null;
		int agentsCount = 0;
		int[][] spaces = SimulationScreen.getInstance().getSpaces();
		int rows = SimulationScreen.getInstance().getRows();
		int columns = SimulationScreen.getInstance().getColumns();

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (spaces[i][j] != 0) {
					agentsCount++;
				}
			}
		}

		if (agentsCount != 0) {
			arguments = new ArrayList<HashMap<String, Object>>();
			TAID = new IComponentIdentifier[agentsCount];
			int indexCount = 0;
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					if (spaces[i][j] != 0) {

						int positionX = i;
						int positionY = j;
						int explorationLevel = 100;
						int index = indexCount;
						int type = spaces[i][j];

						HashMap<String, Object> args = new HashMap<String, Object>();
						args.put("positionX", positionX);
						args.put("positionY", positionY);
						args.put("index", index);
						args.put("explorationLevel", explorationLevel);
						args.put("type", type);
						arguments.add(args);
						indexCount++;

					}
				}
			}
			return arguments;
		} else {
			Printer.print("No agent was placed on the grid");
			return null;
		}
	}

	public synchronized void updateDataForReport(double amount, int agentType) {
		if (agentType == BAIXA_FI) {
			consumption_BAIXA_FI += amount;
		} else if (agentType == BAIXA_FI_COOP) {
			consumption_BAIXA_FI += amount;
			totalCooperation_BAIXA_FI++;
		} else if (agentType == BAIXA_FC) {
			consumption_BAIXA_FC += amount;
		} else if (agentType == BAIXA_FC_COOP) {
			consumption_BAIXA_FC += amount;
			totalCooperation_BAIXA_FC++;
		} else if (agentType == BAIXA_MC) {
			consumption_BAIXA_MC += amount;
		} else if (agentType == BAIXA_MC_COOP) {
			consumption_BAIXA_MC += amount;
			totalCooperation_BAIXA_MC++;
		} else if (agentType == BAIXA_SC) {
			consumption_BAIXA_SC += amount;
		} else if (agentType == BAIXA_SC_COOP) {
			consumption_BAIXA_SC += amount;
			totalCooperation_BAIXA_SC++;
		}//media
		else if (agentType == MEDIA_FC) {
			consumption_MEDIA_FC += amount;
		} else if (agentType == MEDIA_FC_COOP) {
			consumption_BAIXA_FC += amount;
			totalCooperation_MEDIA_FC++;
		} else if (agentType == MEDIA_MC) {
			consumption_MEDIA_MC += amount;
		} else if (agentType == MEDIA_MC_COOP) {
			consumption_MEDIA_MC += amount;
			totalCooperation_MEDIA_MC++;
		} else if (agentType == MEDIA_SC) {
			consumption_MEDIA_SC += amount;
		} else if (agentType == MEDIA_SC_COOP) {
			consumption_MEDIA_SC += amount;
			totalCooperation_MEDIA_SC++;
		}//alta
		else if (agentType == ALTA_MC) {
			consumption_ALTA_MC += amount;
		} else if (agentType == ALTA_MC_COOP) {
			consumption_ALTA_MC += amount;
			totalCooperation_ALTA_MC++;
		} else if (agentType == ALTA_SC) {
			consumption_ALTA_SC += amount;
		} else if (agentType == ALTA_SC_COOP) {
			consumption_ALTA_SC += amount;
			totalCooperation_ALTA_SC++;
		}
		
		if (++barrier == agentsCount) {
			generateReport();
			
			consumption_BAIXA_FI = 0;
			consumption_BAIXA_FC = 0;
			consumption_BAIXA_MC = 0;
			consumption_BAIXA_SC = 0;
			consumption_MEDIA_FC = 0;
			consumption_MEDIA_MC = 0;
			consumption_MEDIA_SC = 0;
			consumption_ALTA_MC = 0;
			consumption_ALTA_SC = 0;
			
			totalCooperation_BAIXA_FI = 0;
			totalCooperation_BAIXA_FC = 0;
			totalCooperation_BAIXA_MC = 0;
			totalCooperation_BAIXA_SC = 0;
			totalCooperation_MEDIA_FC = 0;
			totalCooperation_MEDIA_MC = 0;
			totalCooperation_MEDIA_SC = 0;
			totalCooperation_ALTA_MC = 0;
			totalCooperation_ALTA_SC = 0;
			
			barrier = 0;
			nextStep.setEnabled(true);
		}
	}

	private synchronized void generateReport() {

		try {
			File file = new File("report.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			// This will add a new line to the file content
			
			if (flag == 0) {
				pw.println("Mês\t"
						+ "\tB_FI\tCOOP"
						+ "\tB_FC\tCOOP"
						+ "\tB_MC\tCOOP"
						+ "\tB_SC\tCOOP"
						+ "\tM_FC\tCOOP"
						+ "\tM_MC\tCOOP"
						+ "\tM_SC\tCOOP"	
						+ "\tA_MC\tCOOP"
						+ "\tA_SC\tCOOP");
				flag = 1;
				pw.println("");
			}
			pw.println("");
			
			/*
			 * Below three statements would add three mentioned Strings to the
			 * file in new lines.
			 */
			switch (currentMonth) {
			case 1:
				pw.print("Jan");
				break;
			case 2:
				pw.print("Fev");
				break;
			case 3:
				pw.print("Mar");
				break;
			case 4:
				pw.print("Abr");
				break;
			case 5:
				pw.print("Mai");
				break;
			case 6:
				pw.print("Jun");
				break;
			case 7:
				pw.print("Jul");
				break;
			case 8:
				pw.print("Ago");
				break;
			case 9:
				pw.print("Set");
				break;
			case 10:
				pw.print("Out");
				break;
			case 11:
				pw.print("Nov");
				break;
			default:
				pw.print("Dez");
				break;
			}
			
			
				
				
			pw.print("\t\t" + String.format("%.02f", consumption_BAIXA_FI) + "\t" + totalCooperation_BAIXA_FI + "\t");
			pw.print(String.format("%.02f", consumption_BAIXA_FC) + "\t" + totalCooperation_BAIXA_FC + "\t");
			pw.print(String.format("%.02f", consumption_BAIXA_MC) + "\t" + totalCooperation_BAIXA_MC + "\t");
			pw.print(String.format("%.02f", consumption_BAIXA_SC) + "\t" + totalCooperation_BAIXA_SC + "\t");
			
			pw.print(String.format("%.02f", consumption_MEDIA_FC) + "\t" + totalCooperation_MEDIA_FC + "\t");
			pw.print(String.format("%.02f", consumption_MEDIA_MC) + "\t" + totalCooperation_MEDIA_MC + "\t");
			pw.print(String.format("%.02f", consumption_MEDIA_SC) + "\t" + totalCooperation_MEDIA_SC + "\t");
			
			pw.print(String.format("%.02f", consumption_ALTA_MC) + "\t" + totalCooperation_ALTA_MC + "\t");
			pw.println(String.format("%.02f", consumption_ALTA_SC) + "\t" + totalCooperation_ALTA_SC);
			pw.close();

			System.out.println("Data successfully appended at the end of file");

		} catch (IOException ioe) {
			System.out.println("Exception occurred:");
			ioe.printStackTrace();
		}
		/*
		System.out.println("Total consumptionP: " + consumption_BAIXA_FI);
		System.out.println("Total consumptionM: " + consumption_BAIXA_FC);
		System.out.println("Total consumptionR: " + consumption_BAIXA_MC);
		System.out.println("Total cooperationP: " + totalCooperation_BAIXA_FI);
		System.out.println("Total cooperationM: " + totalCooperation_BAIXA_FC);
		System.out.println("Total cooperationR: " + totalCooperation_BAIXA_MC);*/
		
	}

	public void RadioButton() {
		handler = new RadioButtonHandler();

		NTR = new JRadioButton("NTR", false);
		NRT = new JRadioButton("NRT", false);
		TNR = new JRadioButton("TNR", false);
		TRN = new JRadioButton("TRN", false);
		RNT = new JRadioButton("RNT", false);
		RTN = new JRadioButton("RTN", false);

		add(NTR);
		add(NRT);
		add(TNR);
		add(TRN);
		add(RNT);
		add(RTN);

		buttonGroup = new ButtonGroup();
		buttonGroup.add(NTR);
		buttonGroup.add(NRT);
		buttonGroup.add(TNR);
		buttonGroup.add(TRN);
		buttonGroup.add(RNT);
		buttonGroup.add(RTN);

		NTR.addItemListener(handler);
		NRT.addItemListener(handler);
		TNR.addItemListener(handler);
		TRN.addItemListener(handler);
		RNT.addItemListener(handler);
		RTN.addItemListener(handler);

	}

	private class RadioButtonHandler implements ItemListener {

		public void itemStateChanged(ItemEvent event) {
			if (NTR.isSelected()) {
				JOptionPane.showMessageDialog(null, "Priority Selected: Neighbourhood -> Tax -> Rain");
				selectedPriority = 1;
			}
			if (NRT.isSelected()) {
				JOptionPane.showMessageDialog(null, "Priority Selected: Neighbourhood -> Rain -> Tax");
				selectedPriority = 2;
			}
			if (TNR.isSelected()) {
				JOptionPane.showMessageDialog(null, "Priority Selected: Tax -> Neighbourhood -> Rain");
				selectedPriority = 3;
			}
			if (TRN.isSelected()) {
				JOptionPane.showMessageDialog(null, "Priority Selected: Tax -> Rain -> Neighbourhood");
				selectedPriority = 4;
			}
			if (RNT.isSelected()) {
				JOptionPane.showMessageDialog(null, "Priority Selected: Rain -> Neighbourhood -> Tax");
				selectedPriority = 5;
			}
			if (RTN.isSelected()) {
				JOptionPane.showMessageDialog(null, "Priority Selected: Rain -> Tax -> Neighbourhood");
				selectedPriority = 6;
			}

		}

	}

	public synchronized void diminishWaterLevel(double amount) {

		waterLevel = waterLevel - amount;
		// updateWaterLevelInfo();

	}

	public synchronized void increaseWaterLevel(double amount) {

		waterLevel = waterLevel + amount;
		updateWaterLevelInfo();

	}

	private void updateWaterLevelInfo() {
		waterLevelInfo.setText(
				"<html><p style=\"color:green;font-size:14px;padding-top:3px;padding-bottom:3px;\">Water Level:"
						+ Math.round(waterLevel) + "</p></html>");
		this.repaint();
	}


}
