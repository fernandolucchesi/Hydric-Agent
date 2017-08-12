package br.unb.cic.mase.agents;

import java.awt.Color;
import java.util.Map;

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
	private final int POOR = 1;
	private final int POOR_COOPERATIVE = 2;
	private final int MIDDLECLASS = 3;
	private final int MIDDLECLASS_COOPERATIVE = 4;
	private final int RICH = 5;
	private final int RICH_COOPERATIVE = 6;

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
	private int currentExploration;
	@Belief
	private int type;
	@Belief
	private int index;
	@Belief
	private boolean ready;

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
		currentExploration = explorationLevel;
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

	@Goal
	public class Deliberate {

	}

	@Goal
	public class UseWater {

	}

	@Goal
	public class SaveWater {

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
	
	private boolean checkNeighbourhood (int neighbourhood) 
	{
		//System.out.println(neighbourhood);
		if (neighbourhood < -3)
		{
			bdiFeature.dispatchTopLevelGoal(new UseWater()).get();
			return true;
		} else if (neighbourhood > 3)
		{
			bdiFeature.dispatchTopLevelGoal(new SaveWater()).get();
			return true;
		} else if (neighbourhood < -2 && (type != RICH && type != RICH_COOPERATIVE))
		{
			bdiFeature.dispatchTopLevelGoal(new UseWater()).get();
			return true;
		} else if (neighbourhood > 2 && (type != RICH && type != RICH_COOPERATIVE))
		{
			bdiFeature.dispatchTopLevelGoal(new SaveWater()).get();
			return true;
		} else if (neighbourhood < -1 && (type != MIDDLECLASS && type != MIDDLECLASS_COOPERATIVE) && (type != RICH && type != RICH_COOPERATIVE))
		{
			bdiFeature.dispatchTopLevelGoal(new UseWater()).get();
			return true;
		} else if (neighbourhood > 1 && (type != MIDDLECLASS && type != MIDDLECLASS_COOPERATIVE) && (type != RICH && type != RICH_COOPERATIVE))
		{
			bdiFeature.dispatchTopLevelGoal(new SaveWater()).get();
			return true;
		}else {
			return false;
		}
	}
	
	private boolean checkTax()
	{
		if (ControllerPanel.getInstance().isR1Active()) {
			if (this.type == RICH) {
				bdiFeature.dispatchTopLevelGoal(new UseWater()).get();
			} else {
				bdiFeature.dispatchTopLevelGoal(new SaveWater()).get();
			}
			return true;
		}
		return false;
	}
	
	private boolean checkRain (boolean drySeason) {
		if ((type == POOR || type == POOR_COOPERATIVE)  && drySeason == true)
			bdiFeature.dispatchTopLevelGoal(new SaveWater()).get();	
		else
			bdiFeature.dispatchTopLevelGoal(new UseWater()).get();	
		return true;
	}
	
	public IFuture<Void> deliberate(boolean drySeason, int neighbourhood) {
		int aux = ControllerPanel.getInstance().selectedPriority;
		Printer.print("transformationAgent" + index + " is deliberating...");
		//System.out.println(neighbourhood);
		
		switch (aux)
		{
			case 1: //NTR
				if (!checkNeighbourhood (neighbourhood))
					if (!checkTax())
						if (!checkRain(drySeason))
						{}
				break;
			
			case 2: //NRT
				if (!checkNeighbourhood (neighbourhood))
					if (!checkRain(drySeason))
						if (!checkTax()){}
				break;
			
			case 3: //TNR
				if (!checkTax())
					if (!checkNeighbourhood (neighbourhood))
						if (!checkRain(drySeason)){}
				break;
				
			case 4: //TRN
				if (!checkTax())
					if (!checkRain(drySeason))
						if (!checkNeighbourhood (neighbourhood)){}
				break;
			case 5: //RNT
				if (!checkRain(drySeason))
					if (!checkNeighbourhood (neighbourhood))
						if (!checkTax()){}						
				break;
			case 6: //RTN
				if (!checkRain(drySeason))
					if (!checkTax())
						if (!checkNeighbourhood (neighbourhood)){}
				break;
			default:
				
		}
				
				
		return new Future<Void>();
	}

	@Plan(trigger = @Trigger(goals = (UseWater.class)))
	public void useWaterNormally() {
		Printer.print("transformationAgent" + index + " decided to use water normally.");
		if (this.type == POOR_COOPERATIVE) {
			this.type = POOR;
			currentExploration = 9;
		} else if (this.type == MIDDLECLASS_COOPERATIVE){
			this.type = MIDDLECLASS;
			currentExploration = 75;
		} else if (this.type == RICH_COOPERATIVE){
			this.type = RICH;
			currentExploration = 150;
		}
		ControllerPanel.getInstance().diminishWaterLevel(currentExploration);

	}

	@Plan(trigger = @Trigger(goals = (SaveWater.class)))
	public void useWaterSparingly() {
		Printer.print("transformationAgent" + index + " decided to use water sparingly.");
		if (this.type == POOR) {
			this.type = POOR_COOPERATIVE;
			currentExploration = 3;
		} else if (this.type == MIDDLECLASS){
			this.type = MIDDLECLASS_COOPERATIVE;
			currentExploration = 37;
		} else if (this.type == RICH)
		{
			this.type = RICH_COOPERATIVE;
			currentExploration = 75;
		}
		ControllerPanel.getInstance().diminishWaterLevel(currentExploration);
		
	}

}
