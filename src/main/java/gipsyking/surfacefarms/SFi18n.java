package gipsyking.surfacefarms;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class SFi18n {

	public String reason(DenyReason reason, Material type) {
		if (reason == DenyReason.TOO_HIGH) {
			return type + " no crece en estas altitudes";
			
		} else if (reason == DenyReason.TOO_DEEP) {
			return type + " no crece en estas profundidades";
			
		} else if (reason == DenyReason.NO_DIRECT_SUNLIGHT) {
			return type + " no crece sin que reciba luz solar directa";
			
		} else if (reason == DenyReason.NO_GLASSHOUSE) {
			return type + " no crece sin que reciba luz solar directa o través de un cristal";
			
		}
		return type + " no crece aquí , no se ha podido determinar por qué...";
	}

	public String spawnReason(DenyReason reason, EntityType type) {
		if (reason == DenyReason.TOO_HIGH) {
			return type + " no puede criar en estas alturas";
			
		} else if (reason == DenyReason.TOO_DEEP) {
			return type + " no puede criar en estas profundidades";
			
		} else if (reason == DenyReason.NO_DIRECT_SUNLIGHT) {
			return type + " no puede criar sin que reciba luz solar directa";
			
		} else if (reason == DenyReason.NO_GLASSHOUSE) {
			return type + " no puede criar sin que reciba luz solar directa o través de un cristal";
			
		}
		return type + " no puede criar aquí , no se ha podido determinar por qué...";
	}

}
