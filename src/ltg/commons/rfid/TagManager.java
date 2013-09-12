package ltg.commons.rfid;

import java.util.ArrayList;
import java.util.HashMap;

import ltg.commons.rfid.MonitoringBot;
import processing.core.*;

public class TagManager {


	private ArrayList <RFIDTag> tags = null;
	private int tagLife;
	private int lifeDec;
	private MonitoringBot parent;
	private HashMap<Integer, Integer> tagsThPatch1;
	private HashMap<Integer, Integer> tagsThPatch2;
	private HashMap<Integer, Integer> tagsThPatch3;
	private HashMap<Integer, Integer> tagsThPatch4;
	private HashMap<Integer, Integer> tagsThPatch5;
	private HashMap<Integer, Integer> tagsThPatch6;
	HashMap <String, HashMap> patchHashMap = new HashMap<String, HashMap>();

	
	public TagManager(MonitoringBot parent_, int tagLife_, int lifeDec_){
		tags = new ArrayList <RFIDTag> ();
		tagLife = tagLife_;
		lifeDec = lifeDec_;
		parent = parent_;
		tagsThPatch1 = new HashMap<Integer, Integer>();
		tagsThPatch2 = new HashMap<Integer, Integer>();
		tagsThPatch3 = new HashMap<Integer, Integer>();
		tagsThPatch4 = new HashMap<Integer, Integer>();
		tagsThPatch5 = new HashMap<Integer, Integer>();
		tagsThPatch6 = new HashMap<Integer, Integer>();
		this.initHashMaps();
	}

	//////////////////////////////////////////
	/////HASH MAPS////////////////////////////
	//////////////////////////////////////////
	private void initHashMaps() {
		int gn=0;
		int t1=-5+gn;
		int t2=-5+gn;
		int t3=-5+gn;
		int t4=-5+gn;
		int t5=-5+gn;
		int t6=-5+gn;

		tagsThPatch1.put(1623110, 89+t1);
		tagsThPatch1.put(1623115, 90+t1);
		tagsThPatch1.put(1623126, 89+t1);
		tagsThPatch1.put(1623257, 86+t1);
		tagsThPatch1.put(1623302, 89+t1);
		tagsThPatch1.put(1623303, 86+t1);
		tagsThPatch1.put(1623305, 90+t1);
		tagsThPatch1.put(1623373, 85+t1);
		tagsThPatch1.put(1623386, 87+t1);
		tagsThPatch1.put(1623454, 85+t1);
		tagsThPatch1.put(1623624, 86+t1);
		tagsThPatch1.put(1623641, 86+t1);
		tagsThPatch1.put(1623663, 86+t1);
		tagsThPatch1.put(1623667, 85+t1);
		tagsThPatch1.put(1623683, 85+t1);
		tagsThPatch1.put(1623728, 90+t1);
		tagsThPatch1.put(1623972, 89+t1);
		tagsThPatch1.put(1623678, 89+t1);
		tagsThPatch1.put(1623352, 90+t1);
		tagsThPatch1.put(1623365, 89+t1);

		tagsThPatch2.put(1623110, 90+t2);
		tagsThPatch2.put(1623115, 90+t2);
		tagsThPatch2.put(1623126, 94+t2);
		tagsThPatch2.put(1623257, 89+t2);
		tagsThPatch2.put(1623302, 94+t2);
		tagsThPatch2.put(1623303, 89+t2);
		tagsThPatch2.put(1623305, 95+t2);
		tagsThPatch2.put(1623373, 88+t2);
		tagsThPatch2.put(1623386, 94+t2);
		tagsThPatch2.put(1623454, 88+t2);
		tagsThPatch2.put(1623624, 95+t2);
		tagsThPatch2.put(1623641, 89+t2);
		tagsThPatch2.put(1623663, 89+t2);
		tagsThPatch2.put(1623667, 88+t2);
		tagsThPatch2.put(1623683, 88+t2);
		tagsThPatch2.put(1623728, 95+t2);
		tagsThPatch2.put(1623972, 94+t2);
		tagsThPatch2.put(1623678, 94+t2);
		tagsThPatch2.put(1623352, 90+t2);
		tagsThPatch2.put(1623365, 95+t2);

		tagsThPatch3.put(1623110, 82+t3);
		tagsThPatch3.put(1623115, 90+t3);
		tagsThPatch3.put(1623126, 85+t3);
		tagsThPatch3.put(1623257, 83+t3);
		tagsThPatch3.put(1623302, 85+t3);
		tagsThPatch3.put(1623303, 83+t3);
		tagsThPatch3.put(1623305, 82+t3);
		tagsThPatch3.put(1623373, 83+t3);
		tagsThPatch3.put(1623386, 85+t3);
		tagsThPatch3.put(1623454, 83+t3);
		tagsThPatch3.put(1623624, 82+t3);
		tagsThPatch3.put(1623641, 83+t3);
		tagsThPatch3.put(1623663, 83+t3);
		tagsThPatch3.put(1623667, 83+t3);
		tagsThPatch3.put(1623683, 83+t3);
		tagsThPatch3.put(1623728, 82+t3);
		tagsThPatch3.put(1623972, 85+t3);
		tagsThPatch3.put(1623678, 83+t3);
		tagsThPatch3.put(1623352, 84+t3);
		tagsThPatch3.put(1623365, 85+t3);

		tagsThPatch4.put(1623110, 92+t4);
		tagsThPatch4.put(1623115, 93+t4);
		tagsThPatch4.put(1623126, 89+t4);
		tagsThPatch4.put(1623257, 90+t4);
		tagsThPatch4.put(1623302, 89+t4);
		tagsThPatch4.put(1623303, 91+t4);
		tagsThPatch4.put(1623305, 92+t4);
		tagsThPatch4.put(1623373, 93+t4);
		tagsThPatch4.put(1623386, 89+t4);
		tagsThPatch4.put(1623454, 93+t4);
		tagsThPatch4.put(1623624, 92+t4);
		tagsThPatch4.put(1623641, 90+t4);
		tagsThPatch4.put(1623663, 90+t4);
		tagsThPatch4.put(1623667, 93+t4);
		tagsThPatch4.put(1623683, 93+t4);
		tagsThPatch4.put(1623728, 92+t4);
		tagsThPatch4.put(1623972, 90+t4);
		tagsThPatch4.put(1623678, 89+t4);
		tagsThPatch4.put(1623352, 90+t4);
		tagsThPatch4.put(1623365, 89+t4);

		tagsThPatch5.put(1623110, 88+t5);
		tagsThPatch5.put(1623115, 90+t5);
		tagsThPatch5.put(1623126, 86+t5);
		tagsThPatch5.put(1623257, 82+t5);
		tagsThPatch5.put(1623302, 86+t5);
		tagsThPatch5.put(1623303, 82+t5);
		tagsThPatch5.put(1623305, 88+t5);
		tagsThPatch5.put(1623373, 80+t5);
		tagsThPatch5.put(1623386, 86+t5);
		tagsThPatch5.put(1623454, 86+t5);
		tagsThPatch5.put(1623624, 89+t5);
		tagsThPatch5.put(1623641, 83+t5);
		tagsThPatch5.put(1623663, 83+t5);
		tagsThPatch5.put(1623667, 86+t5);
		tagsThPatch5.put(1623683, 83+t5);
		tagsThPatch5.put(1623728, 80+t5);
		tagsThPatch5.put(1623972, 86+t5);
		tagsThPatch5.put(1623678, 85+t5);
		tagsThPatch5.put(1623352, 83+t5);
		tagsThPatch5.put(1623365, 89+t5);

		tagsThPatch6.put(1623110, 87+t6);
		tagsThPatch6.put(1623115, 90+t6);
		tagsThPatch6.put(1623126, 80+t6);
		tagsThPatch6.put(1623257, 80+t6);
		tagsThPatch6.put(1623302, 84+t6);
		tagsThPatch6.put(1623303, 87+t6);
		tagsThPatch6.put(1623305, 84+t6);
		tagsThPatch6.put(1623373, 82+t6);
		tagsThPatch6.put(1623386, 84+t6);
		tagsThPatch6.put(1623454, 87+t6);
		tagsThPatch6.put(1623624, 80+t6);
		tagsThPatch6.put(1623641, 80+t6);
		tagsThPatch6.put(1623663, 80+t6);
		tagsThPatch6.put(1623667, 85+t6);
		tagsThPatch6.put(1623683, 83+t6);
		tagsThPatch6.put(1623728, 87+t6);
		tagsThPatch6.put(1623972, 87+t6);
		tagsThPatch6.put(1623678, 85+t6);
		tagsThPatch6.put(1623352, 85+t6);
		tagsThPatch6.put(1623365, 85+t6);

		patchHashMap.put("fg-patch-1", tagsThPatch1);
		patchHashMap.put("fg-patch-2", tagsThPatch2);
		patchHashMap.put("fg-patch-3", tagsThPatch3);
		patchHashMap.put("fg-patch-4", tagsThPatch4);
		patchHashMap.put("fg-patch-5", tagsThPatch5);
		patchHashMap.put("fg-patch-6", tagsThPatch6);
	}
	//////////////////////////////////////////
	//////////////////////////////////////////
	//////////////////////////////////////////
	
	
	//////////////////////////////////////////
	//////TAG FUNCTIONS///////////////////////
	//////////////////////////////////////////
	private void addTag(int id, String _reader, int _rssi) {
		tags.add(new RFIDTag(id, _reader, _rssi, tagLife, lifeDec));
		String patch = parent.getPatchName(_reader);
		String jsonMessage =  "{\"event\":\"rfid_update\", \"destination\":\"" + patch + "\", \"payload\":{\"arrivals\" : [\""+id+"\"], \"departures\" :   [] }}";
		if(parent.sendJSON())parent.sendXMPPMessage(jsonMessage);
		//if(logDATA)AppendToLog(patch+", "+ id +", arrival, "+ _rssi);
		System.out.println("RSSI " + _rssi + " Patch: " + patch + " ID:" + id + " Add " + "Time: " + PApplet.nf(PApplet.hour(), 2) + ":" + PApplet.nf(PApplet.minute(), 2) + ":" + PApplet.nf(PApplet.second(), 2));
	}

	private void updateTagObject (int id, String _reader, int _rssi) {
		String patch = parent.getPatchName(_reader);
		Integer tagThObj = (Integer)patchHashMap.get(patch).get(id);
		if (tagThObj != null) {
			int tagTh = tagThObj.intValue();
			if (_rssi > tagTh) {
				RFIDTag b = findTag(id);
				if (b==null) {
					addTag(id, _reader, _rssi);
				}
				else {
					if (b.getReader().equals(_reader)) {
						patch = parent.getPatchName(_reader);
						b.updateLife(_rssi, this.tagLife);
						System.out.println("RSSI " + _rssi + " Patch: " + patch + " ID:" + id + " Update" + "Time: " + PApplet.nf(PApplet.hour(), 2) + ":" + PApplet.nf(PApplet.minute(), 2) + ":" + PApplet.nf(PApplet.second(), 2));
						//if(logDATA)AppendToLog(patch+", "+ id +", update, "+ b.rssi);
					}
					else {
						if (parent.getPatchName(b.getReader()).equals("fg-den") && parent.getPatchName(_reader).equals("fg-den")) {
						}
						else {
							removeTagObject(id);
							addTag(id, _reader, _rssi);
						}
						b.updateLife(b.getRSSI(), this.tagLife);
					}
				}
			}
		}
	}

	private void removeTagObject(int id_) {
		RFIDTag b = findTag(id_);
		if (b != null) {
			String patch = parent.getPatchName(b.getReader());
			String jsonMessage =  "{\"event\":\"rfid_update\", \"destination\":\"" + patch + "\", \"payload\":{\"arrivals\" : [], \"departures\" :   [\""+ id_ +"\"] }}";
			if(parent.sendJSON())parent.sendXMPPMessage(jsonMessage);
			//if(logDATA)appendToLog(patch+", "+ id_ +", departure, "+ b.getRSSI());
			System.out.println("RSSI " + b.getRSSI() + " Patch: " + patch + " ID:" + id_ + " Departure " + "Time: " + PApplet.nf(PApplet.hour(), 2) + ":" + PApplet.nf(PApplet.minute(), 2) + ":" + PApplet.nf(PApplet.second(), 2));
			tags.remove(b);
		}
	}

	private RFIDTag findTag (int id_) {
		for (int i=0; i < tags.size(); i++) {
			if ( tags.get(i).getID() == id_) {
				return tags.get(i);
			}
		}
		return null;
	}

	
}
