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

	/************************** Agente **************************/
	
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

	/**************************** Objetivos **********************************/
	
	@Goal
	public class Deliberate {

	}

	@Goal
	public class UseWater {

	}

	@Goal
	public class SaveWater {

	}
	
	@Goal
	public class SaveRainySeason {

	}

	@Goal
	public class WasteRainySeason {

	}
	
	public int getPositionX(){
		return this.positionX;
	}
	
	public int getPositionY(){
		return this.positionY;
	}
	
	public int getType(){
		return this.type;
	}
	
	/**************** METODOS AUX/CHECAGEM DE CRENÇAS *********************/
	
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
	
	/************************** COGNIÇÃO *********************************/		
	
	public IFuture<Void> deliberate(boolean drySeason) {
		
		Printer.print("transformationAgent" + index + " is deliberating...");
		
		int rand = generateRandom();
		
		//CLASSE BAIXA
		if (this.type > 0 && this.type <= 8) {
			
			currentExploration = 16.2;
			
			//CHUVA
			if (!drySeason && !checkEducation() && !checkTax())
			{
				if (rand < 75) bdiFeature.dispatchTopLevelGoal(new SaveRainySeason()).get();
				else bdiFeature.dispatchTopLevelGoal(new WasteRainySeason()).get();
			}
		}
		return new Future<Void>();
	}

/******************************* AÇÕES *********************************/
	private void hose()
	{
		//CLASSE BAIXA
		if (this.type > 0 && this.type <= 8) {
			//currentExploration -= 
		}
		
	}
	
/******************************* PLANOS *********************************/	
	
	@Plan(trigger = @Trigger(goals = SaveRainySeason.class))
	public void saveFewWater() 
	{
		hose();
		//washingMachine();
		//tap();
		
	}
	
	@Plan(trigger = @Trigger(goals = (UseWater.class)))
	public void useWaterNormally() {
		Printer.print("transformationAgent" + index + " decided to use water normally.");
		if (this.type == BAIXA_FI_COOP && this.currentExploration >= 16.2) {
			this.type = BAIXA_FI;
		} else if (this.type == BAIXA_FC_COOP && this.currentExploration >= 17.5){
			this.type = BAIXA_FC;
		} else if (this.type == BAIXA_MC_COOP && this.currentExploration >= 17.4){
			this.type = BAIXA_MC;
		}
		ControllerPanel.getInstance().diminishWaterLevel(this.currentExploration);
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
	
	
	@Plan(trigger = @Trigger(goals = (SaveWater.class)))
	public void useWaterSparingly() {
		Printer.print("transformationAgent" + index + " decided to use water sparingly.");
		if (this.type == BAIXA_FI) {
			this.type = BAIXA_FI_COOP;
		} else if (this.type == BAIXA_FC){
			this.type = BAIXA_FC_COOP;
		} else if (this.type == BAIXA_MC)
		{
			this.type = BAIXA_MC_COOP;
		}
		ControllerPanel.getInstance().diminishWaterLevel(currentExploration);
		ControllerPanel.getInstance().updateDataForReport(currentExploration, this.type);
	}
}
