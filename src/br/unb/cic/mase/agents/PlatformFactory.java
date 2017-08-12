package br.unb.cic.mase.agents;

import br.unb.cic.mase.Util.Printer;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.commons.future.IFuture;

public class PlatformFactory {
	public static IExternalAccess getPlatForm() {
		String[] defaultPlatformArgs = new String[] { "-gui", "false", "-welcome", "false", "-cli", "false",
				"-printpass", "false", "-awareness", "false" };
		IFuture<IExternalAccess> futurePlatform = Starter.createPlatform(defaultPlatformArgs);
		IExternalAccess platform = futurePlatform.get();
		Printer.print("Started platform: "+platform+" "+futurePlatform);
		return platform;
	}
}
