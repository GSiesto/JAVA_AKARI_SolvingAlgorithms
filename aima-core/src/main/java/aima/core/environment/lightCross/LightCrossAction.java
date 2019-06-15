package aima.core.environment.lightCross;

import aima.core.agent.impl.DynamicAction;
import aima.core.util.datastructure.XYLocation;

/**
@author aeprieto
*/


public class LightCrossAction extends DynamicAction {
	
	//Action names
	public static final String INCLUIR_FUENTE_LUZ = "incluirFuenteLuz";

	//Action params
	public static final String ATRIBUTO_COORDENADAS = "coordenadas";
	
	public LightCrossAction(String name, XYLocation coordenadas) {
		super(name);
		this.setAttribute(ATRIBUTO_COORDENADAS, coordenadas);
	}
	
	public XYLocation getCoordenadas() {
		return (XYLocation) getAttribute(ATRIBUTO_COORDENADAS);
	}
	
	
	
}
