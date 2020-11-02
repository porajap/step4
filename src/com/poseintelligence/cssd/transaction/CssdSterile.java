package com.poseintelligence.cssd.transaction;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import com.poseintelligence.cssd.print_sticker.Sticker_Non_Indicator_Cerulean_50x45_2;

@SuppressWarnings("rawtypes")
public class CssdSterile extends GenericForwardComposer{
	
	private static final long serialVersionUID = -8716887210135693330L;

	
	public void onClick$Button_Print(Event event) throws Exception {
		try{ 
			Sticker_Non_Indicator_Cerulean_50x45_2 n = new Sticker_Non_Indicator_Cerulean_50x45_2(); 
			n.print();
			
		}catch(Exception e){
			
		}
	}
	
}
