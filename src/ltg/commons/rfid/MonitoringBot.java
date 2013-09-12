package ltg.commons.rfid;
import java.util.ArrayList;
import java.util.HashMap;

import processing.core.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class MonitoringBot implements Runnable{

	protected boolean looping;
	public volatile boolean finished;
	public volatile boolean paused;
	protected boolean exitCalled;
	Object pauseObject = new Object();
	Thread thread;
	protected long frameRatePeriod = 1000000000L / 60L;

	public String[] args;
	private XMPPClient xmpp;
	private TagManager tagmanager;
	private String chatRoom;
	private String botUsername = null;
	private String botPassword = null;
	private ArrayList <RFIDTag> tags = new ArrayList <RFIDTag> ();
	private int tagLife = 110000;
	private int lifeDec = 50;
	private boolean sendJSON = true;
	private boolean logDATA = false;
	private boolean useXMPP = true;
	private boolean useRFID = true;
	private boolean isConfiguring = false;
	private String ip1 = "131.193.161.121"; 
	private RFIDReader reader1;
	private HashMap  <Integer, Integer>tagsThPatch1, tagsThPatch2, tagsThPatch3, tagsThPatch4, tagsThPatch5, tagsThPatch6; 
	HashMap <String, HashMap> patchHashMap = new HashMap<String, HashMap>();

	public static void main(String[] args) {
		MonitoringBot mbot = new MonitoringBot();
		if (!mbot.parseCLIArgs(args)) {
			System.out.println("-----\nUsage\n-----\njava -jar <archive.jar> " + "<XMPP_username> <chatRoom>\n");
			System.exit(0);
		}
		mbot.startBot();
	}

	//////////////////////////////////////////
	//////START BOT //////////////////////////
	//////////////////////////////////////////
	private void startBot() {
		System.out.println("STARTING RFID BOT...");
		tagmanager = new TagManager(this, tagLife, lifeDec);
		if(useXMPP)this.configXMPP();
		if(useRFID){
			this.setupRFID();
			this.reader1.enableAutopolling();
			this.reader1.setAlarmFilter(0);
		}
		finished = false; 
		looping = true;
		thread = new Thread(this, "RFID Thread");
		thread.start();
	}

	private boolean parseCLIArgs(String[] args) {
		if (args.length < 2 && useXMPP)
			return false;
		if(args.length < 2 && !useXMPP)
			return true;
		if (nullOrEmpty(args[0]) || nullOrEmpty(args[1]))
			return false;
		if(args.length == 2 && useXMPP){
			botUsername = args[0];
			botPassword = botUsername;
			chatRoom = args[1];
			return true;
		}
		return false;
	}

	//////////////////////////////////////////
	//////SETUP RFID//////////////////////////
	//////////////////////////////////////////
	private void setupRFID(){
		System.out.println("CONFIGURING RFID...");
		tagsThPatch1 = new HashMap<Integer, Integer>();
		tagsThPatch2 = new HashMap<Integer, Integer>();
		tagsThPatch3 = new HashMap<Integer, Integer>();
		tagsThPatch4 = new HashMap<Integer, Integer>();
		tagsThPatch5 = new HashMap<Integer, Integer>();
		tagsThPatch6 = new HashMap<Integer, Integer>();
		this.initHashMaps();
		System.out.println("CONNECTING TO READERS...");
		reader1 = new RFIDReader(ip1, 9001, (byte) (1));
	}
	//////////////////////////////////////////
	//////////////////////////////////////////
	//////////////////////////////////////////

	//////////////////////////////////////////
	//////THREAD RUN//////////////////////////
	//////////////////////////////////////////
	public void run() { 
		long beforeTime = System.nanoTime();
		long overSleepTime = 0L;
		int noDelays = 0;
		final int NO_DELAYS_PER_YIELD = 15;

		while ((Thread.currentThread() == thread) && !finished) {
			if (paused) {
				synchronized (pauseObject) {
					try {
						pauseObject.wait();
					} catch (InterruptedException e) {
					}
				}
			}
			if(useRFID){
				this.pollReaders();
			}
			long afterTime = System.nanoTime();
			long timeDiff = afterTime - beforeTime;
			long sleepTime = (frameRatePeriod - timeDiff) - overSleepTime;

			if (sleepTime > 0) {  
				try {
					Thread.sleep(sleepTime / 1000000L, (int) (sleepTime % 1000000L));
					noDelays = 0; 
				} catch (InterruptedException ex) { }

				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			} else {    
				overSleepTime = 0L;
				noDelays++;

				if (noDelays > NO_DELAYS_PER_YIELD) {
					Thread.yield();  
					noDelays = 0;
				}
			}
			beforeTime = System.nanoTime();
		}
	}
	//////////////////////////////////////////
	//////////////////////////////////////////
	//////////////////////////////////////////

	//////////////////////////////////////////
	//////POLL READERS////////////////////////
	//////////////////////////////////////////
	private void pollReaders(){
		boolean over;
		if (!isConfiguring) {
			if(this.reader1.available()>0) {
				String s = this.reader1.getTagDataPacket();	
				if(s != null){
					String [] parts = s.split("'\t'");
					int id = Integer.parseInt(parts[0]);
					int rssi = Integer.parseInt(parts[1]);
					this.updateTagObject(id, this.ip1, rssi);
					System.out.println(parts[0] + " " + parts[1]);
				}
			}
			for (int i=0; i < tags.size(); i++) {
				over = tags.get(i).checkLife();
				if (over) {
					System.out.println("sustracting " + tags.get(i).getID());
					removeTagObject(tags.get(i).getID());
				}
			}
			int c1=getTagsOnReader(ip1);
		}
	}
	//////////////////////////////////////////
	//////////////////////////////////////////
	//////////////////////////////////////////

	//////////////////////////////////////////
	//////XMPP////////////////////////////////
	//////////////////////////////////////////
	private void configXMPP() {
		
		xmpp = new XMPPClient(botUsername, botPassword, "localhost", this.chatRoom);
		
		xmpp.registerEventListener(new MessageListener() {
			@Override
			public void processMessage(Message m) {
				String message = m.getBody();
				if(message.equals("rfid#stop")) sendJSON = false;
				else if(message.equals("rfid#start")) sendJSON = true;
				System.out.println(m.getBody());
			}
		});
		System.out.println("XMPPClient connected...");
		System.out.println("Entering chatRoom...");
	}

	public void sendXMPPMessage(String messg, String to) {
		xmpp.sendMessage(to, messg);
	}
	
	public void sendXMPPMessage(String messg){
		xmpp.sendMessage(this.chatRoom,messg);
	}
	//////////////////////////////////////////
	//////////////////////////////////////////
	//////////////////////////////////////////


	//////////////////////////////////////////
	//////TAG FUNCTIONS///////////////////////
	//////////////////////////////////////////
	private void addTag(int id, String _reader, int _rssi) {
		tags.add(new RFIDTag(id, _reader, _rssi, tagLife, lifeDec));
		String patch = getPatchName(_reader);
		String jsonMessage =  "{\"event\":\"rfid_update\", \"destination\":\"" + patch + "\", \"payload\":{\"arrivals\" : [\""+id+"\"], \"departures\" :   [] }}";
		if(sendJSON)sendXMPPMessage(jsonMessage, this.chatRoom);
		//if(logDATA)AppendToLog(patch+", "+ id +", arrival, "+ _rssi);
		System.out.println("RSSI " + _rssi + " Patch: " + patch + " ID:" + id + " Add " + "Time: " + PApplet.nf(PApplet.hour(), 2) + ":" + PApplet.nf(PApplet.minute(), 2) + ":" + PApplet.nf(PApplet.second(), 2));
	}

	private void updateTagObject (int id, String _reader, int _rssi) {
		String patch = getPatchName(_reader);
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
						patch = getPatchName(_reader);
						b.updateLife(_rssi, this.tagLife);
						System.out.println("RSSI " + _rssi + " Patch: " + patch + " ID:" + id + " Update" + "Time: " + PApplet.nf(PApplet.hour(), 2) + ":" + PApplet.nf(PApplet.minute(), 2) + ":" + PApplet.nf(PApplet.second(), 2));
						//if(logDATA)AppendToLog(patch+", "+ id +", update, "+ b.rssi);
					}
					else {
						if (getPatchName(b.getReader()).equals("fg-den") && getPatchName(_reader).equals("fg-den")) {
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
			String patch = getPatchNameForTag(b);
			String jsonMessage =  "{\"event\":\"rfid_update\", \"destination\":\"" + patch + "\", \"payload\":{\"arrivals\" : [], \"departures\" :   [\""+ id_ +"\"] }}";
			if(sendJSON)sendXMPPMessage(jsonMessage, this.chatRoom);
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

	private String getPatchNameForTag(RFIDTag b) {
		return (getPatchName(b.getReader()));
	}

	public String getPatchName(String _reader) {
		String _patch = "";

		if (_reader.equals(ip1)) {
			_patch = "fg-patch-1";
		}
		//		else if (_reader.equals(ip2)) {
		//			_patch = "fg-patch-2";
		//		}
		//		else if (_reader.equals(ip3)) {
		//			_patch = "fg-patch-3";
		//		}
		//		else if (_reader.equals(ip4)) {
		//			_patch = "fg-patch-4";
		//		}
		//		else if (_reader.equals(ip5)) {
		//			_patch = "fg-patch-5";
		//		}
		//		else if (_reader.equals(ip6)) {
		//			_patch = "fg-patch-6";
		//		}  
		return _patch;
	}

	private int getTagsOnReader(String _reader) {
		String reader = " ";
		int count=0;

		for (int i=0; i < tags.size(); i++) {
			reader = tags.get(i).getReader();   
			if (reader.equals(_reader)) {
				count++;
			}
		}
		return count;
	}
	//////////////////////////////////////////
	//////////////////////////////////////////
	//////////////////////////////////////////

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

	public static boolean nullOrEmpty(String s) {
		return (s==null || s=="");
	}

	public boolean sendJSON(){
		return this.sendJSON;
	}
}
