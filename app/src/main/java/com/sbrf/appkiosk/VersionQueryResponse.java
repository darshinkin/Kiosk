package com.sbrf.appkiosk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VersionQueryResponse implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String version;
	private boolean mandatory;
	private List<String> releaseNotes = new ArrayList<>();
	private String status;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	
	public List<String> getReleaseNotes() {
		return releaseNotes;
	}
	public void setReleaseNotes(List<String> releaseNotes) {
		this.releaseNotes = releaseNotes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
}
