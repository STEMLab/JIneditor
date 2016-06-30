package net.opengis.indoorgml.core;

import java.io.Serializable;

public abstract class AbstractFeature implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6664125324203036883L;
	private String gmlID;
	private String name;
	private String description;
	
	public AbstractFeature() {
	}
	
	public AbstractFeature(AbstractFeature other) {
		gmlID = other.gmlID;
		name = other.name;
		description = other.description;
	}
	
	public String getGmlID() {
		return gmlID;
	}
	public void setGmlID(String gmlID) {
		this.gmlID = gmlID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public String getDescription() {
        return description;
    }
    public String getDescription(String type) {
    	if (description == null) {
    		description = "";
    	}
    	String[] splitByType = description.split(":");
    	
    	for (int i = 0; i < splitByType.length; i++) {
    		String[] splits = splitByType[i].split("=");
    		
    		if (splits[0].equals(type)) {
    			return splits[1];
    		}
    	}
    	
    	return null;
    }
    public void setDescription(String description) {
        //this.description = description;
    	setDescription("Description", description);
    }
    public void setDescription(String type, String value) {
    	if (value == null || value.equals("")) {
    		return;
    	}
    	if (description == null) {
    		description = "";
    	}
    	String[] splitByType = description.split(":");
    	boolean isAssign = false;
    	
    	for (int i = 0; i < splitByType.length; i++) {
    		String[] splits = splitByType[i].split("=");
    		if (splits[0].equals(type)) {
    			splits[1] = value;
    			splitByType[i] = splits[0] + "=" + splits[1];
    			isAssign = true;
    			break;
    		}
    	}
    	
    	if (isAssign) {
    		StringBuffer sb = new StringBuffer("");
    		for (int i = 0; i < splitByType.length; i++) {
    			if (i != 0) {
    				sb.append(":");
    			}
    			
    			sb.append(splitByType[i]);
    		}
    		
    		description = sb.toString();
    	} else {
    		if (description == null || description.equals("")) {
    			description = type + "=" + value;
    		} else {
    			description += ":" + type + "=" + value;
    		}
    	}
    }
}
