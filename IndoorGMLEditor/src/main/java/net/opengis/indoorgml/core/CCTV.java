package net.opengis.indoorgml.core;

import java.io.Serializable;
import java.security.Timestamp;

import net.opengis.indoorgml.geometry.Point;

public class CCTV implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3841903718698462744L;
	
	private Timestamp installationTime;
	private String sourceLocation;
	private double width;
	private double height;
	private double framerate;
	private String id;
	private String password;
	private String ip;
	private String cctvID;
	
	private Point location;
	private double orientation;
	private double fov;
	private double aspect;
	private Timestamp time2;
	
	private State mappedState;

	public CCTV() {
		sourceLocation = null;
		width = 0;
		height = 0;
		framerate = 0;
		id = null;
		password = null;
		ip = null;
		cctvID = null;
		location = null;
		orientation = 0;
		fov = 0;
		aspect = 0;
		time2 = null;		
	}

	public Timestamp getInstallationTime() {
		return installationTime;
	}

	public void setInstallationTime(Timestamp installationTime) {
		this.installationTime = installationTime;
	}

	public String getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(String sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getFramerate() {
		return framerate;
	}

	public void setFramerate(double framerate) {
		this.framerate = framerate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCctvID() {
		return cctvID;
	}

	public void setCctvID(String cctvID) {
		this.cctvID = cctvID;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public double getOrientation() {
		return orientation;
	}

	public void setOrientation(double orientation) {
		this.orientation = orientation;
	}

	public double getFov() {
		return fov;
	}

	public void setFov(double fov) {
		this.fov = fov;
	}

	public double getAspect() {
		return aspect;
	}

	public void setAspect(double aspect) {
		this.aspect = aspect;
	}

	public Timestamp getTime2() {
		return time2;
	}

	public void setTime2(Timestamp time2) {
		this.time2 = time2;
	}

	public State getMappedState() {
		return mappedState;
	}

	public void setMappedState(State mappedState) {
		this.mappedState = mappedState;
	}

	
}
