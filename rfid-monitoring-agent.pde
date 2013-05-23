import processing.net.*;

long lastTime;
// Decrement in life whenever it doesn't get detected
// The best way to remove timeout for tags is to put 
// this to 0 so tags will never be killed
int lifeDecrement = 50;
// One minute. This is the uncertainy zone. 
int TagLife = 110000;
// Filters nothing
int RSSI=0;
int gainMode=1;
int AlarmFilter=0;
// Readers
Client reader_1, reader_2, reader_3, reader_4;
XMPPConnection conn; 
ConnectionConfiguration config;
String dataIn;
String roomID= "fg-pilot-oct12@conference.ltg.evl.uic.edu";
String ip1, ip2, ip3, ip4;
int c1, c2, c3, c4;

PrintWriter logger;
// Stores all the tags in the system. Initialized only the first time they are read
ArrayList <Tag> tags = new ArrayList <Tag> ();
boolean isConfiguring = false;
// These hashmaps are used to do filtering on individual tags per patch
HashMap  <Integer, Integer>tagsThPatch1 = new HashMap<Integer, Integer>();
HashMap  <Integer, Integer>tagsThPatch2 = new HashMap<Integer, Integer>();
HashMap  <Integer, Integer>tagsThPatch3 = new HashMap<Integer, Integer>();
HashMap  <Integer, Integer>tagsThPatch4 = new HashMap<Integer, Integer>();
HashMap  <Integer, Integer>tagsThPatch5 = new HashMap<Integer, Integer>();
HashMap  <Integer, Integer>tagsThPatch6 = new HashMap<Integer, Integer>();
HashMap <String, HashMap> patchHashMap = new HashMap<String, HashMap>();


void setup() {
  size(900, 700);
  frameRate(60);
  initHashMaps(); 
  CreateLogFile();
  background(0);
  // We've got 121-128
  ip1 = "131.193.161.121"; 
  ip2 = "131.193.161.122"; 
  ip3 = "131.193.161.123"; 
  ip4 = "131.193.161.124";
  //configXMPP();
  System.out.println("-----");
  System.out.println("All messages will be sent to " + roomID);
  // Port for connection , can be confiured on readers
  reader_1 = new Client(this, ip1, 9001);
  println("+");
  reader_2 = new Client(this, ip2, 9001);
  println("+");
  reader_3 = new Client(this, ip3, 9001);
  println("+");
  reader_4 = new Client(this, ip4, 9001);
  println("+");
  delay(5000);
  // These are the software/network/connection buffer...shouldn't be necessary but doesn't hurt
  clearConnectionBuffers();
  // Disable polling for configuration
  disableAllAutopolling();
  delay(1000);
  // Just to be safe and avoid tags from last time we were using these...
  clearAllBuffers();
  // Re-enable auto polling after configuration to read tags automatically as they come in
  enableAllAutopolling();
} 


void draw() { 
  background(0);
  boolean over;
  // Empty connection buffer and read the tags
  if (!isConfiguring) {
    if (reader_1.available()>0) {
      getTagDataPacket(reader_1);
    }
    if (reader_2.available()>0) {
      getTagDataPacket(reader_2);
    }

    if (reader_3.available()>0) {
      getTagDataPacket(reader_3);
    }
    
    if (reader_4.available()>0) {
      getTagDataPacket(reader_4);
    }
    // This is to handle the 15 seconds delay to detect departures
    // If we just want to detect the patch switches we have to turn this off
    for (int i=0; i < tags.size(); i++) {
      over = tags.get(i).checkLife();
      if (over) {
        println("sustracting " + tags.get(i).id);
        removeTagObject(tags.get(i).id);
      }
    }
    // Get reader tags count
    c1=getTagsOnReader(ip1);
    c2=getTagsOnReader(ip2);
    c3=getTagsOnReader(ip3);
    c4=getTagsOnReader(ip4);
  }
  // Display tag counts
  fill(255);
  textSize(140);
  textAlign(CENTER);
  text(c1, 0.25*width, 0.25*height);
  text(c2, 0.75*width, 0.25*height);
  text(c3, 0.25*width, 0.75*height);
  text(c4, 0.75*width, 0.75*height);
  //print(".");
}

void keyPressed() {
  if (key == 'c') { 
    clearAllBuffers();
  }
  // This resets the counts
  else if (key == 'N') { 
    resetEverything();
  }
  else if (key == '7') { 
    AppendToLog("hola");
  }
  // It disables/enables auto-polling, also a good way of finding out if everything is alive
  else if (key == ' ') { 
    isConfiguring = !isConfiguring;
    if (isConfiguring == true) {
      disableAllAutopolling();
    }
    else {
      clearAllBuffers();
      delay(200);
      enableAllAutopolling();
    }
  }
  else if (key == 'o') {
    disableAllAutopolling();
  }
  else if (key == 'a') {
    enableAllAutopolling();
  }
  else if (key == 'd') {
    int netID = 1;
    setNetworkID(reader_1, byte(netID));
    setNetworkID(reader_2, byte(netID));
    setNetworkID(reader_3, byte(netID));
    setNetworkID(reader_4, byte(netID));
  }
  else if (key == 'n') {
    setReaderID(reader_1, byte(1));
    setReaderID(reader_2, byte(2));
    setReaderID(reader_3, byte(3));
    setReaderID(reader_4, byte(4));
    
  }
  else if (key == 'i') { 
    setGainMode(reader_1, byte(gainMode));
    setGainMode(reader_2, byte(gainMode));
    setGainMode(reader_3, byte(gainMode));
    setGainMode(reader_4, byte(gainMode));
  }
  // This hardcodes in the reader the noise level of the new environment
  else if (key == '3') { 
    evaluateNoise(reader_1);
    evaluateNoise(reader_2);
    evaluateNoise(reader_3);
    evaluateNoise(reader_4);
  }
  // This kills the hardware treshold, sets it to 0 so everything gets detected and then we filter 
  // all the tags via software
  else if (key == 'r') {
    setRSSI(reader_1, byte(RSSI));
    setRSSI(reader_2, byte(RSSI));
    setRSSI(reader_3, byte(RSSI));
    setRSSI(reader_4, byte(RSSI));
  }
  else if (key == 'q') {
    getRSSI(reader_1);
    getRSSI(reader_2);
    getRSSI(reader_3);
    getRSSI(reader_4);
  }
  // Sets the alarm filter (movement) for a reader
  // 0 reads tags both moving and not moving  
  // 1 reads only tags that are moving
  // 2 reads only tags that are not moving
  else if (key == '2') { 
    setAlarmFilter(reader_1, AlarmFilter);
    setAlarmFilter(reader_2, AlarmFilter);
    setAlarmFilter(reader_3, AlarmFilter);
    setAlarmFilter(reader_4, AlarmFilter);
  }
}

void disableAllAutopolling() {
  disableAutopolling(reader_1);
  disableAutopolling(reader_2);
  disableAutopolling(reader_3);
  disableAutopolling(reader_4);
}

void enableAllAutopolling() {
  enableAutopolling(reader_1);
  enableAutopolling(reader_2);
  enableAutopolling(reader_3);
  enableAutopolling(reader_4);
}

void clearAllBuffers() {
  clearBuffer(reader_1);
  clearBuffer(reader_2);
  clearBuffer(reader_3);
  clearBuffer(reader_4);
}

void delay(int d)
{
  int l = millis();
  int c = millis();
  while ( c - l < d) {
    c = millis();
  }
}

void resetEverything() {
  tags.clear();
}


void clearConnectionBuffers() {
  reader_1.clear();
  reader_2.clear();
  reader_3.clear();
  reader_4.clear();
}

// This kills the app in a clean way, disabling autopolling
// DON'T kill the app regularly!
void mouseClicked(){
 if(mouseX < 20 && mouseY < 20){
   finish();
 } 
}

void finish(){ 
  disableAllAutopolling();
  delay(1000);
  clearConnectionBuffers();
  delay(1000); 
  reader_4.stop();
  reader_3.stop();
  reader_2.stop();
  reader_1.stop();
  delay(1000);
  CloseLogFile();  
  delay(2000);
  exit();
}

