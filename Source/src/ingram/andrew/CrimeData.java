package ingram.andrew;

class CrimeData {
	public int invid;
	public String starttime;
	public String id;
	public String agency;
	public String service;
	public String nature;
	public String address;
	public double geox;
	public double geoy;
	public String marker_details_xml;
	public String rec_key;
	public String icon_url;
	public String icon;
	
	public CrimeData(CrimeData copiedData) {
		this.invid = copiedData.invid;
		this.starttime = copiedData.starttime;
		this.id = copiedData.id;
		this.agency = copiedData.agency;
		this.service = copiedData.service;
		this.nature = copiedData.nature;
		this.address = copiedData.address;
		this.geox = copiedData.geox;
		this.geoy = copiedData.geoy;
		this.marker_details_xml = copiedData.marker_details_xml;
		this.rec_key = copiedData.rec_key;
		this.icon_url = copiedData.icon_url;
		this.icon = copiedData.icon;
	}
	
	public String toString() {
		
		String returnString = "[";
		returnString = returnString + "starttime:"+starttime+",";
		returnString = returnString + "id:"+id+",";
		returnString = returnString + "agency:"+agency+",";
		returnString = returnString + "service:"+service+",";
		returnString = returnString + "nature:"+nature+",";
		returnString = returnString + "address:"+address;
		returnString = returnString +"]";
		return returnString;
		
	}
	
	public boolean equals(CrimeData comparedData) {
		
		if (id.equals(comparedData.id)) return true;
		
		return false;
	}
}