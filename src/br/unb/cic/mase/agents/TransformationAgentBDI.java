package br.unb.cic.mase.agents;

//import java.awt.Color;
import java.util.Map;
import java.util.Random;

import br.unb.cic.mase.Util.Printer;
import br.unb.cic.mase.gui.ControllerPanel;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.fipa.SFipa;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.types.message.MessageType;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.*;

@Agent
@Arguments({ @Argument(name = "positionX", clazz = Integer.class), @Argument(name = "positionY", clazz = Integer.class),
		@Argument(name = "explorationLevel", clazz = Integer.class), @Argument(name = "index", clazz = Integer.class),
		@Argument(name = "type", clazz = Integer.class) })
@Service
@ProvidedServices(@ProvidedService(type = IDeliberately.class))
public class TransformationAgentBDI implements IDeliberately {
	private final int BAIXA_FI = 1;
	private final int BAIXA_FI_COOP = 2;
	private final int BAIXA_FC = 3;
	private final int BAIXA_FC_COOP = 4;
	private final int BAIXA_MC = 5;
	private final int BAIXA_MC_COOP = 6;
	private final int BAIXA_SC = 7;
	private final int BAIXA_SC_COOP = 8;

	
	@Agent
	protected IInternalAccess thisAgent;

	@AgentFeature
	protected IBDIAgentFeature bdiFeature;

	@AgentFeature
	protected IExecutionFeature execFeature;

	@Belief
	private int positionX;
	@Belief
	private int positionY;
	@Belief
	private int explorationLevel;
	@Belief
	private double currentExploration;
	@Belief
	private int type;
	@Belief
	private int index;
	@Belief
	private boolean ready;
	@Belief
	private boolean drySeason;

	/************************** AGENT **************************/
	
	@AgentCreated
	public void created() {
		Printer.print("created: " + this);

		positionX = (Integer) thisAgent.getComponentFeature(IArgumentsResultsFeature.class).getArguments()
				.get("positionX");
		positionY = (Integer) thisAgent.getComponentFeature(IArgumentsResultsFeature.class).getArguments()
				.get("positionY");
		explorationLevel = (Integer) thisAgent.getComponentFeature(IArgumentsResultsFeature.class).getArguments()
				.get("explorationLevel");
		index = (Integer) thisAgent.getComponentFeature(IArgumentsResultsFeature.class).getArguments().get("index");
		type = (Integer) thisAgent.getComponentFeature(IArgumentsResultsFeature.class).getArguments().get("type");
//		currentExploration = explorationLevel;
		
		
		ControllerPanel.getInstance().addTAIdentification(index, thisAgent.getComponentIdentifier());
	}

	@AgentMessageArrived
	public void messageArrived(Map<String, Object> msg, final MessageType mt) {
		if (msg != null) {
			Printer.print("" + index);
			if (((String) msg.get(SFipa.PERFORMATIVE)).equals(SFipa.REQUEST)) {
				ready = true;
			}
		}
	}

	/*********************** OBJECTIVES *************************/
	
	@Goal
	public class Deliberate {

	}
	
	@Goal
	public class SaveRainySeason {

	}

	@Goal
	public class WasteRainySeason {

	}

	@Goal
	public class SaveDrySeason {

	}
	
	@Goal
	public class WasteDrySeason {

	}

	@Goal
	public class SaveRainyEducation {

	}

	@Goal
	public class WasteRainyEducation {

	}

	@Goal
	public class SaveDryEducation {

	}
	
	@Goal
	public class WasteDryEducation {

	}
	
	@Goal
	public class SaveRainyTax {

	}

	@Goal
	public class WasteRainyTax {

	}

	@Goal
	public class SaveDryTax {

	}
	
	@Goal
	public class WasteDryTax {

	}

	@Goal
	public class SaveRainyEducationTax {

	}

	@Goal
	public class WasteRainyEducationTax {

	}

	@Goal
	public class SaveDryEducationTax {

	}
	
	@Goal
	public class WasteDryEducationTax {

	}
	
	
	/**************** METODOS AUX/CHECAGEM DE CRENÇAS ******************/
	
	public int getPositionX(){
		return this.positionX;
	}
	
	public int getPositionY(){
		return this.positionY;
	}
	
	public int getType(){
		return this.type;
	}
		
	private synchronized int generateRandom()
	{
		Random rand = new Random();
		return (rand.nextInt(100));
	}
	
	private boolean checkTax()
	{
		if (ControllerPanel.getInstance().isR1Active()) return true;
		else return false;
	}
	
	private boolean checkEducation()
	{
		if (ControllerPanel.getInstance().isR2Active()) return true;
		else return false;		
	}
	
	/************************** COGNIÇÃO *******************************/		
	
	public IFuture<Void> deliberate(boolean drySeason) {
		
		//Printer.print("transformationAgent" + index + " is deliberating...");
		this.drySeason = drySeason;
		int rand = generateRandom();
		
		/***************** INICIALIZACAO **************/
		if (this.type > 0 && this.type <= 8) currentExploration = 16.2;
			

		/***************** PERMUTAÇÕES ***************/
		//CHUVA
		if (!this.drySeason && !checkEducation() && !checkTax())
		{		
			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP )
				if (rand < 75) bdiFeature.dispatchTopLevelGoal(new SaveRainySeason()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteRainySeason()).get();
			
			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP )
				if (rand < 83) bdiFeature.dispatchTopLevelGoal(new SaveRainySeason()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteRainySeason()).get();
			
			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP )
				if (rand < 75) bdiFeature.dispatchTopLevelGoal(new SaveRainySeason()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteRainySeason()).get();
			
			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP )
				if (rand < 75) bdiFeature.dispatchTopLevelGoal(new SaveRainySeason()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteRainySeason()).get();
		}
		//SECA
		else if (this.drySeason && !checkEducation() && !checkTax())
		{
			
			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP )
				if (rand < 37) bdiFeature.dispatchTopLevelGoal(new SaveDrySeason()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDrySeason()).get();
				
			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP )
				if (rand < 17) bdiFeature.dispatchTopLevelGoal(new SaveDrySeason()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDrySeason()).get();
				
			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP )
				if (rand < 37) bdiFeature.dispatchTopLevelGoal(new SaveDrySeason()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDrySeason()).get();
				
			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP )
				if (rand < 37) bdiFeature.dispatchTopLevelGoal(new SaveDrySeason()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDrySeason()).get();
				
		}
		//CHUVA + EDUCACAO
		else if (drySeason==false && checkEducation() && !checkTax())
		{
			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP )
				if (rand < 71) bdiFeature.dispatchTopLevelGoal(new SaveRainyEducation()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteRainyEducation()).get();
					
			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP )
				if (rand < 101) bdiFeature.dispatchTopLevelGoal(new SaveRainyEducation()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteRainyEducation()).get();
				
			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP )
				if (rand < 71) bdiFeature.dispatchTopLevelGoal(new SaveRainyEducation()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteRainyEducation()).get();
				
			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP )
				if (rand < 71) bdiFeature.dispatchTopLevelGoal(new SaveRainyEducation()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteRainyEducation()).get();
		}
		//SECA + EDUCACAO
		else if (drySeason==true && checkEducation() && !checkTax())
		{
			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP )
				if (rand < 40) bdiFeature.dispatchTopLevelGoal(new SaveDryEducation()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryEducation()).get();
						
			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP )
				if (rand < 60) bdiFeature.dispatchTopLevelGoal(new SaveDryEducation()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryEducation()).get();
				
			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP )
				if (rand < 40) bdiFeature.dispatchTopLevelGoal(new SaveDryEducation()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryEducation()).get();
				
			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP )
				if (rand < 40) bdiFeature.dispatchTopLevelGoal(new SaveDryEducation()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryEducation()).get();
		}
		//CHUVA + TAXA
		else if (drySeason==false && !checkEducation() && checkTax())
		{
			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP )
				if (rand < 60) bdiFeature.dispatchTopLevelGoal(new SaveRainyTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();
						
			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP )
				if (rand < 100) bdiFeature.dispatchTopLevelGoal(new SaveRainyTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();
				
			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP )
				if (rand < 60) bdiFeature.dispatchTopLevelGoal(new SaveRainyTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();
				
			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP )
				if (rand < 60) bdiFeature.dispatchTopLevelGoal(new SaveRainyTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();
			
		}
		//SECA + TAXA
		else if (drySeason==true && !checkEducation() && checkTax())
		{
			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP )
				if (rand < 35) bdiFeature.dispatchTopLevelGoal(new SaveDryTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();
							
			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP )
				if (rand < 50) bdiFeature.dispatchTopLevelGoal(new SaveDryTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();
					
			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP )
				if (rand < 35) bdiFeature.dispatchTopLevelGoal(new SaveDryTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();
					
			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP )
				if (rand < 35) bdiFeature.dispatchTopLevelGoal(new SaveDryTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();
			}
		//CHUVA + EDUCACAO + TAXA
		else if (drySeason==false && checkEducation() && checkTax())
		{
			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP )
				if (rand < 90) bdiFeature.dispatchTopLevelGoal(new SaveRainyEducationTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteRainyEducationTax()).get();
							
			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP )
				if (rand < 100) bdiFeature.dispatchTopLevelGoal(new SaveRainyEducationTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteRainyEducationTax()).get();
					
			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP )
				if (rand < 90) bdiFeature.dispatchTopLevelGoal(new SaveRainyEducationTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteRainyEducationTax()).get();
					
			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP )
				if (rand < 90) bdiFeature.dispatchTopLevelGoal(new SaveRainyEducationTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteRainyEducationTax()).get();
			
		}
		//SECA + EDUCACAO + TAXA
		else //(drySeason==true && checkEducation() && checkTax())
		{
			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP )
				if (rand < 50) bdiFeature.dispatchTopLevelGoal(new SaveDryEducationTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryEducationTax()).get();
							
			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP )
				if (rand < 67) bdiFeature.dispatchTopLevelGoal(new SaveDryEducationTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryEducationTax()).get();
					
			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP )
				if (rand < 50) bdiFeature.dispatchTopLevelGoal(new SaveDryEducationTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryEducationTax()).get();
					
			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP )
				if (rand < 50) bdiFeature.dispatchTopLevelGoal(new SaveDryEducationTax()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteDryEducationTax()).get();
		}
		
		return new Future<Void>();
	}

	/*************************** AÇÕES *********************************/
	private void hoseSave()
	{
		this.currentExploration -= 1.54;
	}
	
	private void hoseWaste()
	{
		this.currentExploration += 1.54;
	}
	
	private void washingMachine()
	{
		this.currentExploration -= 1.2;
	}
	
	private void washingMachineClothes()
	{
		this.currentExploration += 0.65;	
	}
	
	private void washingMachineWaste()
	{
		if (checkTax())
			this.currentExploration += 1.5;
		else
			this.currentExploration += 2.25;
	}
	
	private void rainWater()
	{
		this.currentExploration -= 0.3;
	}
	
	private void tap()
	{
		this.currentExploration -= 4.4;
	}
	
	private void longShower()
	{
		if(this.drySeason == true)
			this.currentExploration += 3.2;
		else
			this.currentExploration += 2.25;
	}
	
	private void shortShower()
	{
		if(this.drySeason == true)
			this.currentExploration -= 3.2;
		else
			this.currentExploration -= 2.25;
	}
	
	
	
	
	/************************** PLANOS *********************************/	

	/***** CHUVA *****/
	
	@Plan(trigger = @Trigger(goals = (SaveRainySeason.class)))
	public void saveRSWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			hoseSave();
			washingMachine();
			tap();
			if (this.type == BAIXA_FI) this.type = BAIXA_FI_COOP;
		}
		else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			hoseSave();
			washingMachine();
			tap();
			if (this.type == BAIXA_FC) this.type = BAIXA_FC_COOP;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
	@Plan(trigger = @Trigger(goals = (WasteRainySeason.class)))
	public void wasteRSWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			longShower();
			if (this.type == BAIXA_FI_COOP) this.type = BAIXA_FI;
		} 
		else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			longShower();
			if (this.type == BAIXA_FC_COOP) this.type = BAIXA_FC;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
	/***** SECA *****/
	
	@Plan(trigger = @Trigger(goals = (SaveDrySeason.class)))
	public void saveDSWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			washingMachine();
			tap();
			washingMachineClothes();
			if (this.type == BAIXA_FI) this.type = BAIXA_FI_COOP;
		}
		else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			washingMachine();
			tap();
			washingMachineClothes();
			if (this.type == BAIXA_FC) this.type = BAIXA_FC_COOP;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
	@Plan(trigger = @Trigger(goals = (WasteDrySeason.class)))
	public void wasteDSWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			hoseWaste();
			longShower();
			if (this.type == BAIXA_FI_COOP) this.type = BAIXA_FI;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			washingMachineWaste();
			hoseWaste();
			if (this.type == BAIXA_FC_COOP) this.type = BAIXA_FC;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
	/***** CHUVA + EDUCAÇÃO *****/
	
	@Plan(trigger = @Trigger(goals = (SaveRainyEducation.class)))
	public void saveREWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			hoseSave();
			washingMachineClothes();
			tap();
			if (this.type == BAIXA_FI) this.type = BAIXA_FI_COOP;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			hoseSave();
			washingMachineClothes();
			tap();
			if (this.type == BAIXA_FC) this.type = BAIXA_FC_COOP;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
	@Plan(trigger = @Trigger(goals = (WasteRainyEducation.class)))
	public void wasteREWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			longShower();
			washingMachineWaste();
			if (this.type == BAIXA_FI_COOP) this.type = BAIXA_FI;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
		
		}
		
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
	/***** SECA + EDUCAÇÃO *****/
	
	@Plan(trigger = @Trigger(goals = (SaveDryEducation.class)))
	public void saveDEWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			hoseSave();
			washingMachine();
			tap();
			washingMachineClothes();
			if (this.type == BAIXA_FI) this.type = BAIXA_FI_COOP;
			
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			washingMachine();
			tap();
			washingMachineClothes();
			if (this.type == BAIXA_FC) this.type = BAIXA_FC_COOP;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
	@Plan(trigger = @Trigger(goals = (WasteDryEducation.class)))
	public void wasteDEWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			longShower();
			washingMachineWaste();
			if (this.type == BAIXA_FI_COOP) this.type = BAIXA_FI;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			longShower();
			if (this.type == BAIXA_FC_COOP) this.type = BAIXA_FC;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
	/***** CHUVA + TAX *****/
	
	@Plan(trigger = @Trigger(goals = (SaveRainyTax.class)))
	public void saveRTWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			washingMachine();
			tap();
			if (this.type == BAIXA_FI) this.type = BAIXA_FI_COOP;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			tap();
			hoseSave();
			shortShower();
			if (this.type == BAIXA_FC) this.type = BAIXA_FC_COOP;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
	@Plan(trigger = @Trigger(goals = (WasteRainyTax.class)))
	public void wasteRTWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			hoseWaste();
			washingMachineWaste();
			if (this.type == BAIXA_FI_COOP) this.type = BAIXA_FI;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {

		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
	/***** SECA + TAX *****/
	
	@Plan(trigger = @Trigger(goals = (SaveDryTax.class)))
	public void saveDTWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			washingMachine();
			tap();
			if (this.type == BAIXA_FI) this.type = BAIXA_FI_COOP;
		}else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			washingMachine();
			tap();
			shortShower();
			washingMachineClothes();
			if (this.type == BAIXA_FC) this.type = BAIXA_FC_COOP;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
	@Plan(trigger = @Trigger(goals = (WasteDryTax.class)))
	public void wasteDTWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			hoseWaste();
			longShower();
			if (this.type == BAIXA_FI_COOP) this.type = BAIXA_FI;
		}else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			hoseWaste();
			if (this.type == BAIXA_FC_COOP) this.type = BAIXA_FC;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	/***** CHUVA + EDUCATION + TAX *****/
	
	@Plan(trigger = @Trigger(goals = (SaveRainyEducationTax.class)))
	public void saveRETWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			washingMachineClothes();
			rainWater();
			tap();
			shortShower();
			if (this.type == BAIXA_FI) this.type = BAIXA_FI_COOP;
		}else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			washingMachineClothes();
			rainWater();
			tap();
			shortShower();
			if (this.type == BAIXA_FC) this.type = BAIXA_FC_COOP;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
	@Plan(trigger = @Trigger(goals = (WasteRainyEducationTax.class)))
	public void wasteRETWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			washingMachineWaste();
			if (this.type == BAIXA_FI_COOP) this.type = BAIXA_FI;
		}else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
	/***** SECA + EDUCATION + TAX *****/
	
	@Plan(trigger = @Trigger(goals = (SaveDryEducationTax.class)))
	public void saveDETWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			washingMachine();
			tap();
			if (this.type == BAIXA_FI) this.type = BAIXA_FI_COOP;
		}else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			washingMachine();
			washingMachineClothes();
			tap();
			if (this.type == BAIXA_FC) this.type = BAIXA_FC_COOP;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
	@Plan(trigger = @Trigger(goals = (WasteDryEducationTax.class)))
	public void wasteDETWater() 
	{
		//CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			longShower();
			if (this.type == BAIXA_FI_COOP) this.type = BAIXA_FI;
		}else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			longShower();
			if (this.type == BAIXA_FC_COOP) this.type = BAIXA_FC;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
}
