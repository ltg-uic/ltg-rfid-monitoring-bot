package ltg.commons.rfid;
import java.util.ArrayList;
import java.net.*;

public class RFIDReader {
	private JavaClient javaclient;
	private byte id;

	public RFIDReader(String ip_, int port_, byte id_){
		javaclient = new JavaClient(ip_, port_);
		id = id_;
		System.out.println("Java Client..");
	}

	public String ip(){
		return this.javaclient.ip();
	}

	public int available(){
		return this.javaclient.available();
	}


	public boolean setReaderID(byte newID_) {
		System.out.println("SETTING NEW READER ID");
		this.id = newID_;
		ArrayList<Byte> command;
		command = new ArrayList<Byte>();
		command.add((byte)(0xAA));
		command.add((byte)(0x01));
		command.add(this.id);
		command.add((byte)(0x00));
		command.add((byte)(0x00));
		command.add((byte)(0x05));
		command.add(newID_);

		byte b2, b3, b4, b5, b6, b7;
		b2 = command.get(1);
		b3 = command.get(2);
		b4 = command.get(3);
		b5 = command.get(4);
		b6 = command.get(5);
		b7 = command.get(6);

		byte checkSumByte = b2^=b3^=b4^=b5^=b6^=b7;
		command.add(checkSumByte);
		byte[] byteCommand = new byte[command.size()];
		for (int i=0; i<command.size(); i++) {
			byteCommand[i] = command.get(i);
		}
		return writeCommand7(byteCommand);
	}


	public boolean setNetworkID(byte newID_, byte readerID_) {
		System.out.println("SETTING NEW NETWORK ID");
		ArrayList<Byte> command;
		command = new ArrayList<Byte>();
		command.add((byte)(0xAA));
		command.add((byte)(0x01));
		command.add(this.id);
		command.add((byte)(0x00));
		command.add((byte)(0x00));
		command.add((byte)(0x04));
		command.add(newID_);

		byte b2, b3, b4, b5, b6, b7;
		b2 = command.get(1);
		b3 = command.get(2);
		b4 = command.get(3);
		b5 = command.get(4);
		b6 = command.get(5);
		b7 = command.get(6);

		byte checkSumByte = b2^=b3^=b4^=b5^=b6^=b7;
		command.add(checkSumByte);
		byte[] byteCommand = new byte[command.size()];
		for (int i=0; i<command.size(); i++) {
			byteCommand[i] = command.get(i);
		}
		return writeCommand7(byteCommand);
	}

	public boolean setRSSIThreshold(byte rssi_) {
		System.out.println("SETTING NEW RSSI THRESHOLD");
		ArrayList<Byte> command;
		command = new ArrayList<Byte>();
		command.add((byte)(0xAA));
		command.add((byte)(0x01));
		command.add(this.id);
		command.add((byte)(0x00));
		command.add((byte)(0x00));
		command.add((byte)(0x07));
		command.add(rssi_);

		byte b2, b3, b4, b5, b6, b7;
		b2 = command.get(1);
		b3 = command.get(2);
		b4 = command.get(3);
		b5 = command.get(4);
		b6 = command.get(5);
		b7 = command.get(6);

		byte checkSumByte = b2^=b3^=b4^=b5^=b6^=b7;
		command.add(checkSumByte);
		byte[] byteCommand = new byte[command.size()];
		for (int i=0; i<command.size(); i++) {
			byteCommand[i] = command.get(i);
		}
		return writeCommand7(byteCommand);
	}

	public boolean disableAutopolling() {
		System.out.println("DISABLING AUTOPOLLING");
		ArrayList<Byte> command;
		command = new ArrayList<Byte>();
		command.add((byte)(0xAA));
		command.add((byte)(0x00));
		command.add((byte)(0x01));
		command.add(this.id);
		command.add((byte)(0x00));
		command.add((byte)(0x02));

		byte b2, b3, b4, b5, b6;
		b2 = command.get(1);
		b3 = command.get(2);
		b4 = command.get(3);
		b5 = command.get(4);
		b6 = command.get(5);

		byte checkSumByte = b2^=b3^=b4^=b5^=b6;
		command.add(checkSumByte);
		byte[] byteCommand = new byte[command.size()];
		for (int i=0; i<command.size(); i++) {
			byteCommand[i] = command.get(i);
		}
		return writeCommand7(byteCommand);
	}

	public boolean enableAutopolling() {
		System.out.println("ENABLING AUTOPOLLING");
		ArrayList<Byte> command;
		command = new ArrayList<Byte>();
		command.add((byte)(0xAA));
		command.add((byte)(0x00));
		command.add((byte)(0x01));
		command.add(this.id);
		command.add((byte)(0x00));
		command.add((byte)(0x01));

		byte b2, b3, b4, b5, b6;
		b2 = command.get(1);
		b3 = command.get(2);
		b4 = command.get(3);
		b5 = command.get(4);
		b6 = command.get(5);

		byte checkSumByte = b2^=b3^=b4^=b5^=b6;
		command.add(checkSumByte);
		byte[] byteCommand = new byte[command.size()];
		for (int i=0; i<command.size(); i++) {
			byteCommand[i] = command.get(i);
		}
		return writeCommand7(byteCommand);
	}


	public String getTagDataPacket() {
		  ArrayList<Byte> command;
		  ArrayList<Byte> response;
		  command = new ArrayList();
		  command.add((byte)(0xAA));
		  command.add((byte)(0x00));
		  command.add((byte)(0x01));
		  command.add(this.id);
		  command.add((byte)(0x00));
		  command.add((byte)(0x06));

		  byte b2, b3, b4, b5, b6;
		  b2 = command.get(1);
		  b3 = command.get(2);
		  b4 = command.get(3);
		  b5 = command.get(4);
		  b6 = command.get(5);

		  byte checkSumByte = b2^=b3^=b4^=b5^=b6;
		  command.add(checkSumByte);
		  byte[] byteCommand = new byte[command.size()];
		  for (int i=0; i<command.size(); i++) {
		    byteCommand[i] = command.get(i);
		  }
		  javaclient.write(byteCommand);

		  while (javaclient.available ()>0) {

		    byte[] bufferArray = javaclient.readBytes();
		    byte[] tagPacket = new byte[39];


		    if ((bufferArray.length > 0) && (bufferArray.length % 39 == 0)) {
		      int count = 0;   

		      for (int i = 0; i < bufferArray.length / 39; i++) {
		        for (int j = 0; j < 39; j++) {
		          tagPacket[j] = bufferArray[i*39+j];
		        }
		        int numOfBytes = tagPacket[1];
		        if (numOfBytes == 32) {
		          //          println("Interval = " + tagPacket[9]);
		          //          println("ReaderID = " + tagPacket[3]);
		          //          println("NodeID = " + tagPacket[4]);
		          //          println("TagRSSI = " + tagPacket[28]);
		          //          println("ReaderSSI = " + tagPacket[34]);

		          int rssi = tagPacket[28];
		          int tagID = ((tagPacket[22] << 24) & 0xFFFFFFFF) 
		            +((tagPacket[23] << 16) & 0xFFFFFF)
		              +((tagPacket[24] << 8) & 0xFFFF)
		                +((tagPacket[25]) & 0xFF);

		          //println("RSSI " + tagPacket[28] + " Reader: " + reader.ip() + " ID" + tagID);

		          byte tagCRC = (byte)((tagPacket[9]+tagPacket[10]+tagPacket[14]+tagPacket[15]
		            +tagPacket[16]+tagPacket[17]+tagPacket[18]+tagPacket[19]
		            +tagPacket[20]+tagPacket[21]+tagPacket[22]+tagPacket[23]
		            +tagPacket[24]+tagPacket[25]+tagPacket[26]));

		          byte tagCRC29 = tagPacket[29];
		          //println(binary(tagCRC));
		          //println(binary(tagPacket[29]));


		          byte readerCRC=(tagPacket[1]^=tagPacket[2]^=tagPacket[3]^=tagPacket[4]^=tagPacket[5]
		            ^=tagPacket[6]^=tagPacket[7]^=tagPacket[8]^=tagPacket[9]^=tagPacket[10]
		            ^=tagPacket[11]^=tagPacket[12]^=tagPacket[13]^=tagPacket[14]^=tagPacket[15]
		            ^=tagPacket[16]^=tagPacket[17]^=tagPacket[18]^=tagPacket[19]^=tagPacket[20]
		            ^=tagPacket[21]^=tagPacket[22]^=tagPacket[23]^=tagPacket[24]^=tagPacket[25]
		            ^=tagPacket[26]^=tagPacket[27]^=tagPacket[28]^=tagPacket[29]^=tagPacket[30]
		            ^=tagPacket[31]^=tagPacket[32]^=tagPacket[33]^=tagPacket[34]^=tagPacket[35]
		            ^=tagPacket[36]^=tagPacket[37]);

		          byte readerCRC38 = tagPacket[38];
		          //        println(readerCRC);
		          //        println(tagPacket[38]);


		          if ((tagCRC == tagCRC29) && (readerCRC == readerCRC38)) {
		        	  //System.out.println(tagID + '\t' + rssi);
		        	  return tagID + "'\t'" + rssi;
		            //println(getPatchNumber(reader_.ip())+ "  " + tagID);
		            //updateTagObject(tagID, ip, rssi );
		          }
		        }
		      }
		    }
		  }
		  //this.clearBuffer();
		  return null;
		}

	public boolean setAlarmFilter(int _filter) {
		System.out.println("SETTING ALARM FILTER TO: " + _filter);
		ArrayList<Byte> command;
		command = new ArrayList<Byte>();
		command.add((byte)(0xAA));
		command.add((byte)(0x01));
		command.add((byte)(0x01));
		command.add(this.id);
		command.add((byte)(0x00));
		command.add((byte)(0x0D));
		command.add((byte)(_filter));

		byte b2, b3, b4, b5, b6, b7;
		b2 = command.get(1);
		b3 = command.get(2);
		b4 = command.get(3);
		b5 = command.get(4);
		b6 = command.get(5);
		b7 = command.get(6);

		byte checkSumByte = b2^=b3^=b4^=b5^=b6^=b7;
		command.add(checkSumByte);
		byte[] byteCommand = new byte[command.size()];
		for (int i=0; i<command.size(); i++) {
			byteCommand[i] = command.get(i);
		}
		return writeCommand7(byteCommand);
	}

	public boolean setGainMode(byte gain_) {
		System.out.println("SETTING GAIN MODE TO: " + gain_);
		ArrayList<Byte> command;
		command = new ArrayList<Byte>();
		command.add((byte)(0xAA));
		command.add((byte)(0x01));
		command.add((byte)(0x01));
		command.add(this.id);
		command.add((byte)(0x00));
		command.add((byte)(0x0B));
		command.add(gain_);

		byte b2, b3, b4, b5, b6, b7;
		b2 = command.get(1);
		b3 = command.get(2);
		b4 = command.get(3);
		b5 = command.get(4);
		b6 = command.get(5);
		b7 = command.get(6);

		byte checkSumByte = b2^=b3^=b4^=b5^=b6^=b7;
		command.add(checkSumByte);
		byte[] byteCommand = new byte[command.size()];
		for (int i=0; i<command.size(); i++) {
			byteCommand[i] = command.get(i);
		}
		return writeCommand7(byteCommand);
	}

	public boolean clearBuffer() {
		System.out.println("CLEARING BUFFERS");
		ArrayList<Byte> command;
		command = new ArrayList<Byte>();
		command.add((byte)(0xAA));
		command.add((byte)(0x00));
		command.add((byte)(0x01));
		command.add(this.id);
		command.add((byte)(0x00));
		command.add((byte)(0x13));

		byte b2, b3, b4, b5, b6;
		b2 = command.get(1);
		b3 = command.get(2);
		b4 = command.get(3);
		b5 = command.get(4);
		b6 = command.get(5);

		byte checkSumByte = b2^=b3^=b4^=b5^=b6;
		command.add(checkSumByte);
		byte[] byteCommand = new byte[command.size()];
		for (int i=0; i<command.size(); i++) {
			byteCommand[i] = command.get(i);
		}
		return writeCommand7(byteCommand);
	}


	public boolean evaluateNoise() {
		System.out.println("EVALUATING NOISE LEVELS");
		ArrayList<Byte> command;
		command = new ArrayList<Byte>();
		command.add((byte)(0xAA));
		command.add((byte)(0x00));
		command.add((byte)(0x01));
		command.add(this.id);
		command.add((byte)(0x00));
		command.add((byte)(0x11));

		byte b2, b3, b4, b5, b6;
		b2 = command.get(1);
		b3 = command.get(2);
		b4 = command.get(3);
		b5 = command.get(4);
		b6 = command.get(5);

		byte checkSumByte = b2^=b3^=b4^=b5^=b6;
		command.add(checkSumByte);
		byte[] byteCommand = new byte[command.size()];
		for (int i=0; i<command.size(); i++) {
			byteCommand[i] = command.get(i);
		}
		return writeCommand7(byteCommand);
	}

	public int getGainMode() {
		int retGain = 0;
		System.out.println("GETTING GAIN MODE");
		ArrayList<Byte> command;
		command = new ArrayList<Byte>();
		command.add((byte)(0xAA));
		command.add((byte)(0x00));
		command.add((byte)(0x01));
		command.add(this.id);
		command.add((byte)(0x00));
		command.add((byte)(0x0C));

		byte b2, b3, b4, b5, b6;
		b2 = command.get(1);
		b3 = command.get(2);
		b4 = command.get(3);
		b5 = command.get(4);
		b6 = command.get(5);

		byte checkSumByte = b2^=b3^=b4^=b5^=b6;
		command.add(checkSumByte);
		byte[] byteCommand = new byte[command.size()];
		for (int i=0; i<command.size(); i++) {
			byteCommand[i] = command.get(i);
		}

		this.javaclient.write(byteCommand);

		while (this.javaclient.available() < 8) {
		}
		if (this.javaclient.available()>0) {
			byte[] responseArray = this.javaclient.readBytes();
			if (responseArray.length == 8) {
				for (int i=0; i < responseArray.length; i++) {
					//System.out.print(PApplet.hex(responseArray[i]));
				}
				retGain = responseArray[7];
				return retGain;
			}
		}
		System.out.println();
		return -1;
	}

	public int getRSSIThreshold() {
		int retRSSI = 0;
		System.out.println("REQUESTING RSSI THRESHOLD");
		ArrayList<Byte> command;
		command = new ArrayList<Byte>();
		command.add((byte)(0xAA));
		command.add((byte)(0x00));
		command.add((byte)(0x01));
		command.add(this.id);
		command.add((byte)(0x00));
		command.add((byte)(0x08));

		byte b2, b3, b4, b5, b6;
		b2 = command.get(1);
		b3 = command.get(2);
		b4 = command.get(3);
		b5 = command.get(4);
		b6 = command.get(5);

		byte checkSumByte = b2^=b3^=b4^=b5^=b6;
		command.add(checkSumByte);
		byte[] byteCommand = new byte[command.size()];
		for (int i=0; i<command.size(); i++) {
			byteCommand[i] = command.get(i);
		}

		this.javaclient.write(byteCommand);
		while(this.javaclient.available () < 8) {
		}
		if (this.javaclient.available()>0) {
			byte[] responseArray = this.javaclient.readBytes();
			if (responseArray.length == 8) {  
				for (int i=0; i < responseArray.length; i++) {
					//System.out.print(PApplet.hex(responseArray[i]));
				}
				System.out.println("RSSI:" + (int)responseArray[6]);
				retRSSI = (int)responseArray[6];
			}
		}
		System.out.println();
		return retRSSI;
	}

	public boolean ping() {
		System.out.println("PINGING.." + this.ip());
		ArrayList<Byte> command;
		command = new ArrayList<Byte>();
		command.add((byte)(0xAA));
		command.add((byte)(0x00));
		command.add((byte)(0x01));
		command.add(this.id);
		command.add((byte)(0x00));
		command.add((byte)(0x03));

		byte b2, b3, b4, b5, b6;
		b2 = command.get(1);
		b3 = command.get(2);
		b4 = command.get(3);
		b5 = command.get(4);
		b6 = command.get(5);

		byte checkSumByte = b2^=b3^=b4^=b5^=b6;
		command.add(checkSumByte);
		byte[] byteCommand = new byte[command.size()];
		for (int i=0; i<command.size(); i++) {
			byteCommand[i] = command.get(i);
		}
		this.javaclient.write(byteCommand);

		while (this.javaclient.available () < 8) {
		}
		if (this.javaclient.available()>0) {
			byte[] responseArray = this.javaclient.readBytes();
			if (responseArray.length == 8) {
				for (int i=0; i < responseArray.length; i++) {
					//System.out.print(PApplet.hex(responseArray[i]));
				}
			}
		}
		System.out.println();
		return true;
	}

	private boolean writeCommand7(byte[] byteCommand_){
		System.out.println("Writing command to java client...");
		this.javaclient.write(byteCommand_);
		int read = 0;
		while(read<7){
			int r = this.javaclient.read();
			if(r==-1){
			}
			else{
				System.out.print((byte)r);
				read++;
			}
		}
		System.out.println();
		return true;
	}

}
