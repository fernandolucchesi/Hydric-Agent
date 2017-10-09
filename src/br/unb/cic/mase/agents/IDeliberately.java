package br.unb.cic.mase.agents;

import jadex.commons.future.IFuture;

public interface IDeliberately {
	public IFuture<Void> deliberate(boolean drySeason);
	public int getPositionX();
	public int getPositionY();
	public int getType();	
}
