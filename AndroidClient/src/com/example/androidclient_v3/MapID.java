package com.example.androidclient_v3;

public class MapID {
	public String buildingID;
	public String floorID;
	
	public MapID(){
		buildingID=null;
		floorID=null;
	}
	public boolean equals(MapID id){
		if(this.buildingID.equals(id.buildingID)&& this.floorID.equals(id.floorID)){
			return true;
		}else
			return false;
	}

}
