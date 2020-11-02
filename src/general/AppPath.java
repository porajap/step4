package general;

import org.zkoss.zk.ui.Executions;

public class AppPath {
	public String setAppPath(){
		String ap = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
		return ap;
	}
}
