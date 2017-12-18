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

	/*********************** GOALS *************************/

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

	public int getPositionX() {
		return this.positionX;
	}

	public int getPositionY() {
		return this.positionY;
	}

	public int getType() {
		return this.type;
	}

	private synchronized int generateRandom() {
		Random rand = new Random();
		return (rand.nextInt(100));
	}

	private boolean checkTax() {
		if (ControllerPanel.getInstance().isR1Active())
			return true;
		else
			return false;
	}

	private boolean checkEducation() {
		if (ControllerPanel.getInstance().isR2Active())
			return true;
		else
			return false;
	}

	/************************** COGNIÇÃO *******************************/

	public IFuture<Void> deliberate(boolean drySeason) {

		this.drySeason = drySeason;
		int rand = generateRandom();

		/***************** INICIALIZACAO **************/
		if (this.type > 0 && this.type <= 8) currentExploration = 16.2;
		else if (this.type > 8 && this.type <= 14) currentExploration = 17.5;
		else currentExploration = 17.41;

		/***************** COMBINAÇÕES ***************/
		// SECA
		if (this.drySeason && !checkEducation() && !checkTax()) {
			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP)
				if (rand < 14) //14% 
					bdiFeature.dispatchTopLevelGoal(new SaveDrySeason()).get();
				else if (rand > 61) //38% 
					bdiFeature.dispatchTopLevelGoal(new WasteDrySeason()).get();
			
			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP)
				if (rand < 14) //14%
					bdiFeature.dispatchTopLevelGoal(new SaveDrySeason()).get();
				else if (rand > 82) //17%
					bdiFeature.dispatchTopLevelGoal(new WasteDrySeason()).get();

			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP)
				if (rand < 3) //3%
					bdiFeature.dispatchTopLevelGoal(new SaveDrySeason()).get();
				else if (rand > 71) //28%
					bdiFeature.dispatchTopLevelGoal(new WasteDrySeason()).get();

			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP)
				if (rand < 12)
					bdiFeature.dispatchTopLevelGoal(new SaveDrySeason()).get();
				else if (rand > 73) //26%
					bdiFeature.dispatchTopLevelGoal(new WasteDrySeason()).get();

			// MEDIA

			else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP)
				if (rand < 19) //18.75
					bdiFeature.dispatchTopLevelGoal(new SaveDrySeason()).get();
				else //o que sobra nao economiza..  
					bdiFeature.dispatchTopLevelGoal(new WasteDrySeason()).get();

			else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP)
				if (rand < 6) //6%
					bdiFeature.dispatchTopLevelGoal(new SaveDrySeason()).get();
				else if (rand > 79) //20% 
					bdiFeature.dispatchTopLevelGoal(new WasteDrySeason()).get();

			else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP)
				if (rand < 15) //15%
					bdiFeature.dispatchTopLevelGoal(new SaveDrySeason()).get();
				else if (rand > 78) //21%
					bdiFeature.dispatchTopLevelGoal(new WasteDrySeason()).get();

			// ALTA

			else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP)
				if (rand < 25) //25%
					bdiFeature.dispatchTopLevelGoal(new SaveDrySeason()).get();
				else if (rand > 81) //18%
					bdiFeature.dispatchTopLevelGoal(new WasteDrySeason()).get();

			else // if (this.type == ALTA_SC || this.type == ALTA_SC_COOP )
				if (rand < 9) //9%
					bdiFeature.dispatchTopLevelGoal(new SaveDrySeason()).get();
				else if (rand > 58) //41%
					bdiFeature.dispatchTopLevelGoal(new WasteDrySeason()).get();

		}

		// CHUVA
		else if (!this.drySeason && !checkEducation() && !checkTax()) {

			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP)
				if (rand < 57)
					bdiFeature.dispatchTopLevelGoal(new SaveRainySeason()).get();
				else if (rand > 85) //14%
					bdiFeature.dispatchTopLevelGoal(new WasteRainySeason()).get();

			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP)
				if (rand < 50)
					bdiFeature.dispatchTopLevelGoal(new SaveRainySeason()).get();
				else if (rand > 91) //8%
					bdiFeature.dispatchTopLevelGoal(new WasteRainySeason()).get();

			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP)
				if (rand < 22) //22
					bdiFeature.dispatchTopLevelGoal(new SaveRainySeason()).get();
				else if (rand > 89) //10
					bdiFeature.dispatchTopLevelGoal(new WasteRainySeason()).get();

			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP)
				if (rand < 58)
					bdiFeature.dispatchTopLevelGoal(new SaveRainySeason()).get();
				else if (rand > 87) //12
					bdiFeature.dispatchTopLevelGoal(new WasteRainySeason()).get();

			// MEDIA

			else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP)
				if (rand < 50)
					bdiFeature.dispatchTopLevelGoal(new SaveRainySeason()).get();
				else if (rand > 80) //19
					bdiFeature.dispatchTopLevelGoal(new WasteRainySeason()).get();

			else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP)
				if (rand < 58)
					bdiFeature.dispatchTopLevelGoal(new SaveRainySeason()).get();
				else if (rand > 81) //18
					bdiFeature.dispatchTopLevelGoal(new WasteRainySeason()).get();

			else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP)
				if (rand < 69)
					bdiFeature.dispatchTopLevelGoal(new SaveRainySeason()).get();
				else if (rand > 81) //18
					bdiFeature.dispatchTopLevelGoal(new WasteRainySeason()).get();

			// ALTA

			else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP)
				if (rand < 25)
					bdiFeature.dispatchTopLevelGoal(new SaveRainySeason()).get();
				else if (rand > 61) //38
					bdiFeature.dispatchTopLevelGoal(new WasteRainySeason()).get();

			else // if (this.type == ALTA_SC || this.type == ALTA_SC_COOP )
				if (rand < 36)
					bdiFeature.dispatchTopLevelGoal(new SaveRainySeason()).get();
				else if (rand > 85) //14
					bdiFeature.dispatchTopLevelGoal(new WasteRainySeason()).get();
		}
		// CHUVA + EDUCACAO
		else if (drySeason == false && checkEducation() && !checkTax()) {
			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP)
				if (rand < 48)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducation()).get();
				else if (rand > 94) //5
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducation()).get();

			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP)
				if (rand < 47)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducation()).get();
				else if (rand > 96) //3
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducation()).get();

			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP)
				if (rand < 25)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducation()).get();
				else if (rand > 95) //4
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducation()).get();

			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP)
				if (rand < 68)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducation()).get();
				else if (rand > 93) //6
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducation()).get();

			// MEDIA

			else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP)
				if (rand < 50)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducation()).get();
				else if (rand > 86) //13
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducation()).get();

			else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP)
				if (rand < 34)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducation()).get();
				else if (rand > 93) //6
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducation()).get();

			else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP)
				if (rand < 49)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducation()).get();
				else if (rand > 92) //7
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducation()).get();

			// ALTA

			else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP)
				if (rand < 25)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducation()).get();
				else if (rand > 61) //38
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducation()).get();

			else // if (this.type == ALTA_SC || this.type == ALTA_SC_COOP )
				if (rand < 32)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducation()).get();
				else if (rand > 85) //14
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducation()).get();

		}
		
		// SECA + EDUCACAO
		else if (drySeason == true && checkEducation() && !checkTax()) {
			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP)
				if (rand < 10)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducation()).get();
				else if (rand > 89) //10
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducation()).get();

			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP)
				if (rand < 14)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducation()).get();
				else if (rand > 93) //6
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducation()).get();

			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP)
				if (rand < 4)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducation()).get();
				else if (rand > 90) //9
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducation()).get();

			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP)
				if (rand < 6)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducation()).get();
				else if (rand > 90) //9
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducation()).get();

			// MEDIA

			else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP)
				if (rand < 6)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducation()).get();
				else if (rand > 80) //19
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducation()).get();

			else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP)
				if (rand < 6)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducation()).get();
				else if (rand > 85) //14 
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducation()).get();

			else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP)
				if (rand < 10)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducation()).get();
				else if (rand > 89) //10
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducation()).get();

			// ALTA

			else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP)
				if (rand < 13)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducation()).get();
				else if (rand > 74) //25
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducation()).get();

			else // if (this.type == ALTA_SC || this.type == ALTA_SC_COOP )
				if (rand < 9)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducation()).get();
				else if (rand > 85) //14
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducation()).get();

		}
		// CHUVA + TAXA
		else if (drySeason == false && !checkEducation() && checkTax()) {
			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP)
				if (rand < 57)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyTax()).get();
				else if (rand > 89) //10
					bdiFeature.dispatchTopLevelGoal(new WasteRainyTax()).get();

			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP)
				if (rand < 44)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyTax()).get();
				else if (rand > 96) //3
					bdiFeature.dispatchTopLevelGoal(new WasteRainyTax()).get();

			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP)
				if (rand < 52)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyTax()).get();
				else if (rand > 92) //7
					bdiFeature.dispatchTopLevelGoal(new WasteRainyTax()).get();

			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP)
				if (rand < 35)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyTax()).get();
				else if (rand > 90) //9
					bdiFeature.dispatchTopLevelGoal(new WasteRainyTax()).get();

			// MEDIA

			else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP)
				if (rand < 44)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyTax()).get();
				else if (rand > 86) //13
					bdiFeature.dispatchTopLevelGoal(new WasteRainyTax()).get();

			else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP)
				if (rand < 14)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyTax()).get();
				else if (rand > 93) //6 
					bdiFeature.dispatchTopLevelGoal(new WasteRainyTax()).get();

			else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP)
				if (rand < 48)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyTax()).get();
				else if (rand > 95) //4
					bdiFeature.dispatchTopLevelGoal(new WasteRainyTax()).get();

			// ALTA

			else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP)
				if (rand < 0 ) // nao vai rolar nunca
					bdiFeature.dispatchTopLevelGoal(new SaveRainyTax()).get();
				else //nao vai rolar nunca
					bdiFeature.dispatchTopLevelGoal(new WasteRainyTax()).get(); // não
																				// existe.
			else // if (this.type == ALTA_SC || this.type == ALTA_SC_COOP )
				if (rand < 23)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyTax()).get();
				else if (rand > 85) //14
					bdiFeature.dispatchTopLevelGoal(new WasteRainyTax()).get();
		}
		// SECA + TAXA
		else if (drySeason == true && !checkEducation() && checkTax()) {
			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP)
				if (rand < 35)
					bdiFeature.dispatchTopLevelGoal(new SaveDryTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();

			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP)
				if (rand < 50)
					bdiFeature.dispatchTopLevelGoal(new SaveDryTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();

			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP)
				if (rand < 22)
					bdiFeature.dispatchTopLevelGoal(new SaveDryTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();

			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP)
				if (rand < 25)
					bdiFeature.dispatchTopLevelGoal(new SaveDryTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();

			// MEDIA

			else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP)
				if (rand < 34)
					bdiFeature.dispatchTopLevelGoal(new SaveDryTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();

			else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP)
				if (rand < 19)
					bdiFeature.dispatchTopLevelGoal(new SaveDryTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();

			else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP)
				if (rand < 29)
					bdiFeature.dispatchTopLevelGoal(new SaveDryTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();

			// ALTA

			else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP)
				if (rand < 50)
					bdiFeature.dispatchTopLevelGoal(new SaveDryTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();

			else // if (this.type == ALTA_SC || this.type == ALTA_SC_COOP )
			if (rand < 50)
				bdiFeature.dispatchTopLevelGoal(new SaveDryTax()).get();
			else
				bdiFeature.dispatchTopLevelGoal(new WasteDryTax()).get();

		}
		// CHUVA + EDUCACAO + TAXA
		else if (drySeason == false && checkEducation() && checkTax()) {
			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP)
				if (rand < 90)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducationTax()).get();

			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP)
				if (rand < 94)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducationTax()).get();

			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP)
				if (rand < 91)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducationTax()).get();

			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP)
				if (rand < 94)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducationTax()).get();

			// MEDIA

			else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP)
				if (rand < 55)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducationTax()).get();

			else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP)
				if (rand < 94)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducationTax()).get();

			else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP)
				if (rand < 91)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducationTax()).get();

			// ALTA

			else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP)
				if (rand < 101)
					bdiFeature.dispatchTopLevelGoal(new SaveRainyEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteRainyEducationTax()).get(); // não
																							// existe.

			else // if (this.type == ALTA_SC || this.type == ALTA_SC_COOP )
			if (rand < 80)
				bdiFeature.dispatchTopLevelGoal(new SaveRainyEducationTax()).get();
			else
				bdiFeature.dispatchTopLevelGoal(new WasteRainyEducationTax()).get();

		}
		// SECA + EDUCACAO + TAXA
		else // (drySeason==true && checkEducation() && checkTax())
		{
			if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP)
				if (rand < 50)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducationTax()).get();

			else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP)
				if (rand < 67)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducationTax()).get();

			else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP)
				if (rand < 67)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducationTax()).get();

			else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP)
				if (rand < 50)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducationTax()).get();

			// MEDIA

			else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP)
				if (rand < 50)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducationTax()).get();

			else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP)
				if (rand < 50)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducationTax()).get();

			else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP)
				if (rand < 57)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducationTax()).get();

			// ALTA

			else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP)
				if (rand < 101)
					bdiFeature.dispatchTopLevelGoal(new SaveDryEducationTax()).get();
				else
					bdiFeature.dispatchTopLevelGoal(new WasteDryEducationTax()).get();

			else // if (this.type == ALTA_SC || this.type == ALTA_SC_COOP )
			if (rand < 50)
				bdiFeature.dispatchTopLevelGoal(new SaveDryEducationTax()).get();
			else
				bdiFeature.dispatchTopLevelGoal(new WasteDryEducationTax()).get();

		}

		return new Future<Void>();
	}

	/*************************** AÇÕES *********************************/
	private void hoseSave() {
		this.currentExploration -= 1.54;
	}

	private void hoseWaste() {
		this.currentExploration += 1.54;
	}

	private void washingMachine() {
		this.currentExploration -= 1.2;
	}

	private void rainWater() {
		this.currentExploration -= 0.3;
	}

	private void tap() {
		this.currentExploration -= 4.4;
	}

	private void washingMachineSave() {
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP)
			if (checkTax()) this.currentExploration -= 1.17;
			else this.currentExploration -= 0.58;
		else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP)
			if (checkTax()) this.currentExploration -= 1.02;
			else this.currentExploration -= 0.84;
		else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP)
			if (checkTax()) this.currentExploration -= 0.74;
			else this.currentExploration -= 0.85;
		else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP)
			if (checkTax()) this.currentExploration -= 0.85;
			else this.currentExploration -= 0.93;
		
		//MEDIA
		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP)
			if (checkTax()) this.currentExploration -= 0.92;
			else this.currentExploration -= 0.6;
		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP)
			if (checkTax()) this.currentExploration -= 0.83;
			else this.currentExploration -= 0.71;
		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP)
			if (checkTax()) this.currentExploration -= 0.82;
			else this.currentExploration -= 0.76;

		//ALTA
		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP 
				|| this.type == ALTA_SC || this.type == ALTA_SC_COOP)
			if (checkTax()) this.currentExploration -= 0.5;
			else this.currentExploration -= 1.25;
		
	}

	private void washingMachineWaste() {
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP)
			if (checkTax()) this.currentExploration += 1.35;
			else this.currentExploration += 0.67;
		else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP)
			if (checkTax()) this.currentExploration += 0.37;
			else this.currentExploration += 0.65;
		else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP)
			if (checkTax()) this.currentExploration += 0.92;
			else this.currentExploration += 0.78;
		else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP)
			if (checkTax()) this.currentExploration += 0.56;
			else this.currentExploration += 0.22;

		//MEDIA
		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP)
			if (checkTax()) this.currentExploration += 0.92;
			else this.currentExploration += 0.6;
		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP)
			if (checkTax()) this.currentExploration += 0.83;
			else this.currentExploration += 0.71;
		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP)
			if (checkTax()) this.currentExploration += 0.82;
			else this.currentExploration += 0.76;
		
		//ALTA
		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP 
				|| this.type == ALTA_SC || this.type == ALTA_SC_COOP)
			if (checkTax()) this.currentExploration += 0.5;
			else this.currentExploration += 1.25;
	}
	
	private void shortShower() {
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP)
			if (this.drySeason == true) this.currentExploration -= 1.87;
			else this.currentExploration -= 1.6;
		else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP)
			if (this.drySeason == true) this.currentExploration -= 1.08;
			else this.currentExploration -= 3.04;
		else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP)
			if (this.drySeason == true) this.currentExploration -= 2.98;
			else this.currentExploration -= 0.9;
		else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP)
			if (this.drySeason == true) this.currentExploration -= 2.27;
			else this.currentExploration -= 2.4;
		
		//MEDIA

		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP)
			if (this.drySeason == true) this.currentExploration -= 1.9;
			else this.currentExploration -= 0.675;
		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP)
			if (this.drySeason == true) this.currentExploration -= 2.5;
			else this.currentExploration -= 0.675;
		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP)
			if (this.drySeason == true) this.currentExploration -= 2.2;
			else this.currentExploration -= 0.45;

		//ALTA
		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP 
				|| this.type == ALTA_SC || this.type == ALTA_SC_COOP)
			if (this.drySeason == true) this.currentExploration -= 2.92;
			else this.currentExploration -= 0.675;
	}
	
	private void longShower() {
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP)
			if (this.drySeason == true) this.currentExploration += 1.37;
			else this.currentExploration += 2.5;
		else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP)
			if (this.drySeason == true) this.currentExploration += 0.99;
			else this.currentExploration += 1.02;
		else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP)
			if (this.drySeason == true) this.currentExploration += 1.01;
			else this.currentExploration += 0.9;
		else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP)
			if (this.drySeason == true) this.currentExploration += 1.48;
			else this.currentExploration += 1.35;
		
		//MEDIA

		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP)
			if (this.drySeason == true) this.currentExploration += 2.08;
			else this.currentExploration += 1.3;
		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP)
			if (this.drySeason == true) this.currentExploration += 1.7;
			else this.currentExploration += 1.3;
		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP)
			if (this.drySeason == true) this.currentExploration += 1.4;
			else this.currentExploration += 0.9;
		
		//ALTA
		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP 
				|| this.type == ALTA_SC || this.type == ALTA_SC_COOP)
			if (this.drySeason == true) this.currentExploration += 1.95;
			else this.currentExploration += 1.3;
	}

	/************************** PLANOS *********************************/

	/***** CHUVA *****/

	@Plan(trigger = @Trigger(goals = (SaveRainySeason.class)))
	public void saveRSWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			hoseSave();
			washingMachine();
			tap();
			if (this.type == BAIXA_FI)
				this.type = BAIXA_FI_COOP;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			hoseSave();
			washingMachine();
			tap();
			if (this.type == BAIXA_FC)
				this.type = BAIXA_FC_COOP;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			hoseSave();
			washingMachine();
			tap();
			if (this.type == BAIXA_MC)
				this.type = BAIXA_MC_COOP;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			hoseSave();
			shortShower();
			washingMachine();
			tap();
			if (this.type == BAIXA_SC)
				this.type = BAIXA_SC_COOP;
		}

		// MEDIA

		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			shortShower();
			washingMachine();
			tap();
			if (this.type == MEDIA_FC)
				this.type = MEDIA_FC_COOP;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			hoseSave();
			shortShower();
			washingMachine();
			tap();
			if (this.type == MEDIA_MC)
				this.type = MEDIA_MC_COOP;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			washingMachine();
			tap();
			if (this.type == MEDIA_SC)
				this.type = MEDIA_SC_COOP;
		}

		// ALTA

		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {
			hoseSave();
			washingMachine();
			tap();
			if (this.type == ALTA_MC)
				this.type = ALTA_MC_COOP;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			hoseSave();
			washingMachineSave();
			if (this.type == ALTA_SC)
				this.type = ALTA_SC_COOP;
		}

		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	@Plan(trigger = @Trigger(goals = (WasteRainySeason.class)))
	public void wasteRSWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			longShower();
			if (this.type == BAIXA_FI_COOP)
				this.type = BAIXA_FI;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			longShower();
			if (this.type == BAIXA_FC_COOP)
				this.type = BAIXA_FC;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			longShower();
			washingMachineWaste();
			if (this.type == BAIXA_MC_COOP)
				this.type = BAIXA_MC;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			washingMachineWaste();
			if (this.type == BAIXA_SC_COOP)
				this.type = BAIXA_SC;
		}

		// MEDIA
		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			washingMachineWaste();
			if (this.type == MEDIA_FC_COOP)
				this.type = MEDIA_FC;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			washingMachineWaste();
			if (this.type == MEDIA_MC_COOP)
				this.type = MEDIA_MC;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			washingMachineWaste();
			longShower();
			if (this.type == MEDIA_SC_COOP)
				this.type = MEDIA_SC;
		}

		// ALTA
		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {
			washingMachineWaste();
			if (this.type == ALTA_MC_COOP)
				this.type = ALTA_MC;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			hoseWaste();
			if (this.type == ALTA_SC_COOP)
				this.type = ALTA_SC;
		}

		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	/***** SECA *****/

	@Plan(trigger = @Trigger(goals = (SaveDrySeason.class)))
	public void saveDSWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			hoseSave();
			washingMachine();
			tap();
			washingMachineSave();
			if (this.type == BAIXA_FI)
				this.type = BAIXA_FI_COOP;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			washingMachine();
			tap();
			washingMachineSave();
			if (this.type == BAIXA_FC)
				this.type = BAIXA_FC_COOP;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			hoseSave();
			washingMachine();
			tap();
			washingMachineSave();
			if (this.type == BAIXA_MC)
				this.type = BAIXA_MC_COOP;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			shortShower();
			washingMachine();
			tap();
			if (this.type == BAIXA_SC)
				this.type = BAIXA_SC_COOP;
		}
		// MEDIA

		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			hoseSave();
			washingMachine();
			tap();
			if (this.type == MEDIA_FC)
				this.type = MEDIA_FC_COOP;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			hoseSave();
			washingMachine();
			shortShower();
			tap();
			if (this.type == MEDIA_MC)
				this.type = MEDIA_MC_COOP;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			washingMachine();
			tap();
			if (this.type == MEDIA_SC)
				this.type = MEDIA_SC_COOP;
		}

		// ALTA

		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {
			shortShower();
			washingMachine();
			tap();
			if (this.type == ALTA_MC)
				this.type = ALTA_MC_COOP;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			shortShower();
			washingMachine();
			tap();
			washingMachineSave();
			if (this.type == ALTA_SC)
				this.type = ALTA_SC_COOP;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	@Plan(trigger = @Trigger(goals = (WasteDrySeason.class)))
	public void wasteDSWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			hoseWaste();
			longShower();
			if (this.type == BAIXA_FI_COOP)
				this.type = BAIXA_FI;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			washingMachineWaste();
			hoseWaste();
			if (this.type == BAIXA_FC_COOP)
				this.type = BAIXA_FC;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			longShower();
			if (this.type == BAIXA_MC_COOP)
				this.type = BAIXA_MC;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			hoseWaste();
			if (this.type == BAIXA_SC_COOP)
				this.type = BAIXA_SC;
		} // MEDIA
		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			longShower();
			if (this.type == MEDIA_FC_COOP)
				this.type = MEDIA_FC;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			longShower();
			hoseWaste();
			if (this.type == MEDIA_MC_COOP)
				this.type = MEDIA_MC;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			longShower();
			hoseWaste();
			washingMachineWaste();
			if (this.type == MEDIA_SC_COOP)
				this.type = MEDIA_SC;
		}

		// ALTA
		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {
			longShower();
			washingMachineWaste();
			if (this.type == ALTA_MC_COOP)
				this.type = ALTA_MC;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			longShower();
			if (this.type == ALTA_SC_COOP)
				this.type = ALTA_SC;
		}

		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	/***** CHUVA + EDUCAÇÃO *****/

	@Plan(trigger = @Trigger(goals = (SaveRainyEducation.class)))
	public void saveREWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			hoseSave();
			washingMachineSave();
			tap();
			washingMachine();
			if (this.type == BAIXA_FI)
				this.type = BAIXA_FI_COOP;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			hoseSave();
			washingMachineSave();
			tap();
			if (this.type == BAIXA_FC)
				this.type = BAIXA_FC_COOP;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			rainWater();
			washingMachine();
			tap();
			if (this.type == BAIXA_MC)
				this.type = BAIXA_MC_COOP;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			shortShower();
			washingMachine();
			tap();
			if (this.type == BAIXA_SC)
				this.type = BAIXA_SC_COOP;
		}

		// MEDIA

		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			hoseSave();
			tap();
			washingMachineSave();
			if (this.type == MEDIA_FC)
				this.type = MEDIA_FC_COOP;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			hoseSave();
			tap();
			rainWater();
			shortShower();
			washingMachine();
			if (this.type == MEDIA_MC)
				this.type = MEDIA_MC_COOP;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			washingMachine();
			tap();
			if (this.type == MEDIA_SC)
				this.type = MEDIA_SC_COOP;
		}

		// ALTA

		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {
			tap();
			rainWater();
			if (this.type == ALTA_MC)
				this.type = ALTA_MC_COOP;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			washingMachine();
			tap();
			hoseSave();
			if (this.type == ALTA_SC)
				this.type = ALTA_SC_COOP;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	@Plan(trigger = @Trigger(goals = (WasteRainyEducation.class)))
	public void wasteREWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			longShower();
			washingMachineWaste();
			if (this.type == BAIXA_FI_COOP)
				this.type = BAIXA_FI;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			longShower();
			if (this.type == BAIXA_FC_COOP)
				this.type = BAIXA_FC;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			washingMachineWaste();
			hoseWaste();
			if (this.type == BAIXA_MC_COOP)
				this.type = BAIXA_MC;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			washingMachineWaste();
			if (this.type == BAIXA_SC_COOP)
				this.type = BAIXA_SC;
		} // MEDIA
		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			hoseWaste();
			washingMachineWaste();
			if (this.type == MEDIA_FC_COOP)
				this.type = MEDIA_FC;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			washingMachineWaste();
			if (this.type == MEDIA_MC_COOP)
				this.type = MEDIA_MC;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			washingMachineWaste();
			if (this.type == MEDIA_SC_COOP)
				this.type = MEDIA_SC;
		}

		// ALTA
		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {
			washingMachineWaste();
			longShower();
			if (this.type == ALTA_MC_COOP)
				this.type = ALTA_MC;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			washingMachineWaste();
			if (this.type == ALTA_SC_COOP)
				this.type = ALTA_SC;
		}

		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	/***** SECA + EDUCAÇÃO *****/

	@Plan(trigger = @Trigger(goals = (SaveDryEducation.class)))
	public void saveDEWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			hoseSave();
			washingMachine();
			tap();
			washingMachineSave();
			if (this.type == BAIXA_FI)
				this.type = BAIXA_FI_COOP;

		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			washingMachine();
			tap();
			washingMachineSave();
			if (this.type == BAIXA_FC)
				this.type = BAIXA_FC_COOP;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			washingMachine();
			tap();
			hoseSave();
			if (this.type == BAIXA_MC)
				this.type = BAIXA_MC_COOP;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			washingMachine();
			tap();
			hoseSave();
			if (this.type == BAIXA_SC)
				this.type = BAIXA_SC_COOP;
		}
		// MEDIA

		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			hoseSave();
			washingMachine();
			tap();
			if (this.type == MEDIA_FC)
				this.type = MEDIA_FC_COOP;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			washingMachine();
			tap();
			if (this.type == MEDIA_MC)
				this.type = MEDIA_MC_COOP;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			washingMachine();
			tap();
			if (this.type == MEDIA_SC)
				this.type = MEDIA_SC_COOP;
		}

		// ALTA

		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {
			washingMachine();
			shortShower();
			hoseSave();
			tap();
			if (this.type == ALTA_MC)
				this.type = ALTA_MC_COOP;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			washingMachine();
			shortShower();
			washingMachineSave();
			tap();
			if (this.type == ALTA_SC)
				this.type = ALTA_SC_COOP;
		}

		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	@Plan(trigger = @Trigger(goals = (WasteDryEducation.class)))
	public void wasteDEWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			longShower();
			washingMachineWaste();
			if (this.type == BAIXA_FI_COOP)
				this.type = BAIXA_FI;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			longShower();
			if (this.type == BAIXA_FC_COOP)
				this.type = BAIXA_FC;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			longShower();
			washingMachineWaste();
			if (this.type == BAIXA_MC_COOP)
				this.type = BAIXA_MC;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			longShower();
			if (this.type == BAIXA_SC_COOP)
				this.type = BAIXA_SC;
		} // MEDIA
		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			longShower();
			hoseWaste();
			if (this.type == MEDIA_FC_COOP)
				this.type = MEDIA_FC;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			longShower();
			if (this.type == MEDIA_MC_COOP)
				this.type = MEDIA_MC;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			hoseWaste();
			longShower();
			if (this.type == MEDIA_SC_COOP)
				this.type = MEDIA_SC;
		}

		// ALTA
		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {
			longShower();
			washingMachineWaste();
			if (this.type == ALTA_MC_COOP)
				this.type = ALTA_MC;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			hoseWaste();
			longShower();
			if (this.type == ALTA_SC_COOP)
				this.type = ALTA_SC;
		}

		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	/***** CHUVA + TAX *****/

	@Plan(trigger = @Trigger(goals = (SaveRainyTax.class)))
	public void saveRTWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			washingMachine();
			tap();
			if (this.type == BAIXA_FI)
				this.type = BAIXA_FI_COOP;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			tap();
			hoseSave();
			shortShower();
			if (this.type == BAIXA_FC)
				this.type = BAIXA_FC_COOP;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			tap();
			washingMachine();
			if (this.type == BAIXA_MC)
				this.type = BAIXA_MC_COOP;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			tap();
			washingMachine();
			rainWater();
			if (this.type == BAIXA_SC)
				this.type = BAIXA_SC_COOP;
		}

		// MEDIA

		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			tap();
			washingMachineSave();
			shortShower();
			if (this.type == MEDIA_FC)
				this.type = MEDIA_FC_COOP;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			tap();
			hoseSave();
			washingMachine();
			rainWater();
			if (this.type == MEDIA_MC)
				this.type = MEDIA_MC_COOP;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			tap();
			washingMachine();
			if (this.type == MEDIA_SC)
				this.type = MEDIA_SC_COOP;
		}

		// ALTA

		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {
			 tap();
			 shortShower();
			 hoseSave();
			 washingMachine();
			 if (this.type == ALTA_MC) this.type = ALTA_MC_COOP;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			tap();
			washingMachine();
			if (this.type == ALTA_SC)
				this.type = ALTA_SC_COOP;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	@Plan(trigger = @Trigger(goals = (WasteRainyTax.class)))
	public void wasteRTWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			hoseWaste();
			washingMachineWaste();
			if (this.type == BAIXA_FI_COOP)
				this.type = BAIXA_FI;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			hoseWaste();
			if (this.type == BAIXA_FC_COOP)
				this.type = BAIXA_FC;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			longShower();
			washingMachineWaste();
			if (this.type == BAIXA_MC_COOP)
				this.type = BAIXA_MC;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			washingMachineWaste();
			if (this.type == BAIXA_SC_COOP)
				this.type = BAIXA_SC;
		} // MEDIA
		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			longShower();
			if (this.type == MEDIA_FC_COOP)
				this.type = MEDIA_FC;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			longShower();
			washingMachineWaste();
			if (this.type == MEDIA_MC_COOP)
				this.type = MEDIA_MC;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			hoseWaste();
			longShower();
			washingMachineWaste();
			if (this.type == MEDIA_SC_COOP)
				this.type = MEDIA_SC;
		}

		// ALTA
		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {
			// longShower();
			// washingMachineWaste();
			if (this.type == ALTA_MC_COOP)
				this.type = ALTA_MC;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			longShower();
			if (this.type == ALTA_SC_COOP)
				this.type = ALTA_SC;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	/***** SECA + TAX *****/

	@Plan(trigger = @Trigger(goals = (SaveDryTax.class)))
	public void saveDTWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			washingMachine();
			tap();
			if (this.type == BAIXA_FI)
				this.type = BAIXA_FI_COOP;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			washingMachine();
			tap();
			shortShower();
			washingMachineSave();
			if (this.type == BAIXA_FC)
				this.type = BAIXA_FC_COOP;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			washingMachine();
			hoseSave();
			tap();
			if (this.type == BAIXA_MC)
				this.type = BAIXA_MC_COOP;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			hoseSave();
			washingMachineSave();
			if (this.type == BAIXA_SC)
				this.type = BAIXA_SC_COOP;
		}
		// MEDIA

		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			washingMachine();
			tap();
			if (this.type == MEDIA_FC)
				this.type = MEDIA_FC_COOP;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			washingMachine();
			washingMachineSave();
			shortShower();
			tap();
			if (this.type == MEDIA_MC)
				this.type = MEDIA_MC_COOP;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			washingMachine();
			tap();
			if (this.type == MEDIA_SC)
				this.type = MEDIA_SC_COOP;
		}

		// ALTA

		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {
			washingMachine();
			hoseSave();
			shortShower();
			tap();
			if (this.type == ALTA_MC)
				this.type = ALTA_MC_COOP;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			hoseSave();
			shortShower();
			if (this.type == ALTA_SC)
				this.type = ALTA_SC_COOP;
		}

		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	@Plan(trigger = @Trigger(goals = (WasteDryTax.class)))
	public void wasteDTWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			hoseWaste();
			longShower();
			if (this.type == BAIXA_FI_COOP)
				this.type = BAIXA_FI;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			hoseWaste();
			if (this.type == BAIXA_FC_COOP)
				this.type = BAIXA_FC;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			longShower();
			washingMachineWaste();
			if (this.type == BAIXA_MC_COOP)
				this.type = BAIXA_MC;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			longShower();
			washingMachineWaste();
			if (this.type == BAIXA_SC_COOP)
				this.type = BAIXA_SC;
		} // MEDIA
		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			hoseWaste();
			longShower();
			if (this.type == MEDIA_FC_COOP)
				this.type = MEDIA_FC;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			longShower();
			washingMachineWaste();
			if (this.type == MEDIA_MC_COOP)
				this.type = MEDIA_MC;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			hoseWaste();
			longShower();
			washingMachineWaste();
			if (this.type == MEDIA_SC_COOP)
				this.type = MEDIA_SC;
		}

		// ALTA
		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {
			longShower();
			washingMachineWaste();
			if (this.type == ALTA_MC_COOP)
				this.type = ALTA_MC;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			hoseWaste();
			if (this.type == ALTA_SC_COOP)
				this.type = ALTA_SC;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	/***** CHUVA + EDUCATION + TAX *****/

	@Plan(trigger = @Trigger(goals = (SaveRainyEducationTax.class)))
	public void saveRETWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			washingMachineSave();
			rainWater();
			tap();
			shortShower();
			if (this.type == BAIXA_FI)
				this.type = BAIXA_FI_COOP;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			washingMachineSave();
			rainWater();
			tap();
			shortShower();
			if (this.type == BAIXA_FC)
				this.type = BAIXA_FC_COOP;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			washingMachine();
			rainWater();
			tap();
			shortShower();
			hoseSave();
			if (this.type == BAIXA_MC)
				this.type = BAIXA_MC_COOP;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			washingMachine();
			tap();
			hoseSave();
			if (this.type == BAIXA_SC)
				this.type = BAIXA_SC_COOP;
		}
		// MEDIA

		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			washingMachineSave();
			tap();
			shortShower();
			if (this.type == MEDIA_FC)
				this.type = MEDIA_FC_COOP;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			washingMachine();
			hoseSave();
			tap();
			rainWater();
			shortShower();
			if (this.type == MEDIA_MC)
				this.type = MEDIA_MC_COOP;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			washingMachine();
			hoseSave();
			tap();
			shortShower();
			if (this.type == MEDIA_SC)
				this.type = MEDIA_SC_COOP;
		}

		// ALTA

		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {

			// if (this.type == ALTA_MC) this.type = ALTA_MC_COOP;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			washingMachine();
			tap();
			if (this.type == ALTA_SC)
				this.type = ALTA_SC_COOP;
		}

		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	@Plan(trigger = @Trigger(goals = (WasteRainyEducationTax.class)))
	public void wasteRETWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			washingMachineWaste();
			if (this.type == BAIXA_FI_COOP)
				this.type = BAIXA_FI;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			hoseWaste();
			if (this.type == BAIXA_FC_COOP)
				this.type = BAIXA_FC;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			washingMachineWaste();
			if (this.type == BAIXA_MC_COOP)
				this.type = BAIXA_MC;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			washingMachineWaste();
			if (this.type == BAIXA_SC_COOP)
				this.type = BAIXA_SC;
		} // MEDIA
		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			washingMachineWaste();
			if (this.type == MEDIA_FC_COOP)
				this.type = MEDIA_FC;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			longShower();
			if (this.type == MEDIA_MC_COOP) this.type = MEDIA_MC;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			washingMachineWaste();
			if (this.type == MEDIA_SC_COOP)
				this.type = MEDIA_SC;
		}

		// ALTA
		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {

			// if (this.type == ALTA_MC_COOP) this.type = ALTA_MC;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			hoseWaste();
			washingMachineWaste();
			if (this.type == ALTA_SC_COOP)
				this.type = ALTA_SC;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	/***** SECA + EDUCATION + TAX *****/

	@Plan(trigger = @Trigger(goals = (SaveDryEducationTax.class)))
	public void saveDETWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			washingMachine();
			tap();
			if (this.type == BAIXA_FI)
				this.type = BAIXA_FI_COOP;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			washingMachine();
			washingMachineSave();
			tap();
			if (this.type == BAIXA_FC)
				this.type = BAIXA_FC_COOP;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			washingMachine();
			tap();
			if (this.type == BAIXA_MC)
				this.type = BAIXA_MC_COOP;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			washingMachine();
			hoseSave();
			shortShower();
			tap();
			if (this.type == BAIXA_SC)
				this.type = BAIXA_SC_COOP;
		}
		// MEDIA

		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			washingMachine();
			hoseSave();
			tap();
			if (this.type == MEDIA_FC)
				this.type = MEDIA_FC_COOP;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			tap();
			shortShower();
			if (this.type == MEDIA_MC)
				this.type = MEDIA_MC_COOP;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			washingMachine();
			tap();
			shortShower();
			if (this.type == MEDIA_SC)
				this.type = MEDIA_SC_COOP;
		}

		// ALTA

		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {
			tap();
			shortShower();
			washingMachine();
			hoseSave();
			if (this.type == ALTA_MC)
				this.type = ALTA_MC_COOP;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			washingMachine();
			tap();
			washingMachineSave();
			shortShower();
			if (this.type == ALTA_SC)
				this.type = ALTA_SC_COOP;
		}

		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}

	@Plan(trigger = @Trigger(goals = (WasteDryEducationTax.class)))
	public void wasteDETWater() {
		// CLASSE BAIXA
		if (this.type == BAIXA_FI || this.type == BAIXA_FI_COOP) {
			longShower();
			if (this.type == BAIXA_FI_COOP)
				this.type = BAIXA_FI;
		} else if (this.type == BAIXA_FC || this.type == BAIXA_FC_COOP) {
			longShower();
			if (this.type == BAIXA_FC_COOP)
				this.type = BAIXA_FC;
		} else if (this.type == BAIXA_MC || this.type == BAIXA_MC_COOP) {
			longShower();
			washingMachineWaste();
			hoseWaste();
			if (this.type == BAIXA_MC_COOP)
				this.type = BAIXA_MC;
		} else if (this.type == BAIXA_SC || this.type == BAIXA_SC_COOP) {
			longShower();
			washingMachineWaste();
			if (this.type == BAIXA_SC_COOP)
				this.type = BAIXA_SC;
		} // MEDIA
		else if (this.type == MEDIA_FC || this.type == MEDIA_FC_COOP) {
			washingMachineWaste();
			longShower();
			if (this.type == MEDIA_FC_COOP)
				this.type = MEDIA_FC;
		}

		else if (this.type == MEDIA_MC || this.type == MEDIA_MC_COOP) {
			longShower();
			if (this.type == MEDIA_MC_COOP)
				this.type = MEDIA_MC;
		}

		else if (this.type == MEDIA_SC || this.type == MEDIA_SC_COOP) {
			washingMachineWaste();
			longShower();
			hoseWaste();
			if (this.type == MEDIA_SC_COOP)
				this.type = MEDIA_SC;
		}

		// ALTA
		else if (this.type == ALTA_MC || this.type == ALTA_MC_COOP) {
			washingMachineWaste();
			longShower();
			if (this.type == ALTA_MC_COOP)
				this.type = ALTA_MC;
		}

		else if (this.type == ALTA_SC || this.type == ALTA_SC_COOP) {
			longShower();
			hoseWaste();
			if (this.type == ALTA_SC_COOP)
				this.type = ALTA_SC;
		}
		ControllerPanel.getInstance().updateDataForReport(this.currentExploration, this.type);
	}
}