package ltg.commons.rfid;
public class RFIDTag {
	private String reader;
	private int tagID;
	private int life;
	private int lifeDec;
	private int rssi;

	RFIDTag(int _id, String _reader, int _rssi, int _life, int _lifeDec) {   
		tagID = _id;
		reader = _reader;
		life = _life;
		rssi = _rssi;
		lifeDec = _lifeDec;
	} 

	public void updateLife(int _rssi, int _life) {
		life = _life;
		rssi = _rssi;
	}
	
	public int getRSSI(){
		return this.rssi;
	}
	
	public String getReader(){
		return this.reader;
	}
	
	public int getID(){
		return this.tagID;
	}

	public boolean checkLife() {
		if (life < 0) {
			return true;
		}
		else {
			life -= lifeDec;   
			return false;
		}
	}
}