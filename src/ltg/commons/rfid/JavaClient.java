package ltg.commons.rfid;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;

public class JavaClient implements Runnable {
	Thread thread;
	Socket socket;
	String ip;
	String host;
	int port;

	public InputStream input;
	public OutputStream output;

	byte buffer[] = new byte[32768];
	int bufferIndex;
	int bufferLast;

	public JavaClient(String host, int port) {
		this.host = host;
		this.port = port;

		try {
			System.out.println("Connecting client...");
			socket = new Socket(this.host, this.port);
			input = socket.getInputStream();
			output = socket.getOutputStream();
			thread = new Thread(this);
			thread.start();
			
		} catch (ConnectException ce) {
			ce.printStackTrace();
			dispose();
		} catch (IOException e) {
			e.printStackTrace();
			dispose();
		}
	}

	public void dispose() {
		thread = null;
		try {
			if (input != null) {
				input.close();
				input = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (output != null) {
				output.close();
				output = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (socket != null) {
				socket.close();
				socket = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void run() {
		while (Thread.currentThread() == thread) {
			try {
				while (input != null) {
					int value;
					// try to read a byte using a blocking read. 
					// An exception will occur when the sketch is exits.
					try {
						value = input.read();
					} catch (SocketException e) {
						System.err.println("Client SocketException: " + e.getMessage());
						// the socket had a problem reading so don't try to read from it again.
						stop();
						return;
					}

					// read returns -1 if end-of-stream occurs (for example if the host disappears)
					if (value == -1) {
						System.err.println("Client got end-of-stream.");
						stop();
						return;
					}

					synchronized (buffer) {
						// todo: at some point buffer should stop increasing in size, 
						// otherwise it could use up all the memory.
						if (bufferLast == buffer.length) {
							byte temp[] = new byte[bufferLast << 1];
							System.arraycopy(buffer, 0, temp, 0, bufferLast);
							buffer = temp;
						}
						buffer[bufferLast++] = (byte)value;
					}
				}
			} catch (IOException e) {
				//errorMessage("run", e);
				e.printStackTrace();
			}
		}
	}

	public void stop() {    
		dispose();
	}


	/**
	 * Return true if this client is still active and hasn't run
	 * into any trouble.
	 */
	public boolean active() {
		return (thread != null);
	}

	public String ip() {
		return socket.getInetAddress().getHostAddress();
	}

	public int available() {
		return (bufferLast - bufferIndex);
	}

	public void clear() {
		bufferLast = 0;
		bufferIndex = 0;
	}


	public int read() {
		if (bufferIndex == bufferLast) return -1;
		synchronized (buffer) {
			int outgoing = buffer[bufferIndex++] & 0xff;
			if (bufferIndex == bufferLast) {  // rewind
				bufferIndex = 0;
				bufferLast = 0;
			}
			return outgoing;
		}
	}


	public char readChar() {
		if (bufferIndex == bufferLast) return (char)(-1);
		return (char) read();
	}


	public byte[] readBytes() {
		if (bufferIndex == bufferLast) return null;

		synchronized (buffer) {
			int length = bufferLast - bufferIndex;
			byte outgoing[] = new byte[length];
			System.arraycopy(buffer, bufferIndex, outgoing, 0, length);

			bufferIndex = 0;  // rewind
			bufferLast = 0;
			return outgoing;
		}
	}

	public int readBytes(byte bytebuffer[]) {
		if (bufferIndex == bufferLast) return 0;

		synchronized (buffer) {
			int length = bufferLast - bufferIndex;
			if (length > bytebuffer.length) length = bytebuffer.length;
			System.arraycopy(buffer, bufferIndex, bytebuffer, 0, length);

			bufferIndex += length;
			if (bufferIndex == bufferLast) {
				bufferIndex = 0;  // rewind
				bufferLast = 0;
			}
			return length;
		}
	}

	public byte[] readBytesUntil(int interesting) {
		if (bufferIndex == bufferLast) return null;
		byte what = (byte)interesting;

		synchronized (buffer) {
			int found = -1;
			for (int k = bufferIndex; k < bufferLast; k++) {
				if (buffer[k] == what) {
					found = k;
					break;
				}
			}
			if (found == -1) return null;

			int length = found - bufferIndex + 1;
			byte outgoing[] = new byte[length];
			System.arraycopy(buffer, bufferIndex, outgoing, 0, length);

			bufferIndex += length;
			if (bufferIndex == bufferLast) {
				bufferIndex = 0; // rewind
				bufferLast = 0;
			}
			return outgoing;
		}
	}

	public int readBytesUntil(int interesting, byte byteBuffer[]) {
		if (bufferIndex == bufferLast) return 0;
		byte what = (byte)interesting;

		synchronized (buffer) {
			int found = -1;
			for (int k = bufferIndex; k < bufferLast; k++) {
				if (buffer[k] == what) {
					found = k;
					break;
				}
			}
			if (found == -1) return 0;

			int length = found - bufferIndex + 1;
			if (length > byteBuffer.length) {
				System.err.println("readBytesUntil() byte buffer is" +
						" too small for the " + length +
						" bytes up to and including char " + interesting);
				return -1;
			}
			//byte outgoing[] = new byte[length];
			System.arraycopy(buffer, bufferIndex, byteBuffer, 0, length);

			bufferIndex += length;
			if (bufferIndex == bufferLast) {
				bufferIndex = 0;  // rewind
				bufferLast = 0;
			}
			return length;
		}
	}

	public String readString() {
		if (bufferIndex == bufferLast) return null;
		return new String(readBytes());
	}

	public String readStringUntil(int interesting) {
		byte b[] = readBytesUntil(interesting);
		if (b == null) return null;
		return new String(b);
	}
	
	public void write(int data) {  // will also cover char
		try {
			output.write(data & 0xff);  // for good measure do the &
			output.flush();   // hmm, not sure if a good idea

		} catch (Exception e) { // null pointer or serial port dead
			//errorMessage("write", e);
			//e.printStackTrace();
			//dispose();
			//disconnect(e);
			e.printStackTrace();
			stop();
		}
	}


	public void write(byte data[]) {
		try {
			output.write(data);
			//output.flush();   // hmm, not sure if a good idea

		} catch (Exception e) { // null pointer or serial port dead
			//errorMessage("write", e);
			//e.printStackTrace();
			//disconnect(e);
			e.printStackTrace();
			stop();
		}
	}


	/**
	 * <h3>Advanced</h3>
	 * Write a String to the output. Note that this doesn't account
	 * for Unicode (two bytes per char), nor will it send UTF8
	 * characters.. It assumes that you mean to send a byte buffer
	 * (most often the case for networking and serial i/o) and
	 * will only use the bottom 8 bits of each char in the string.
	 * (Meaning that internally it uses String.getBytes)
	 *
	 * If you want to move Unicode data, you can first convert the
	 * String to a byte stream in the representation of your choice
	 * (i.e. UTF8 or two-byte Unicode data), and send it as a byte array.
	 */
	public void write(String data) {
		write(data.getBytes());
	}


	/**
	 * Handle disconnect due to an Exception being thrown.
	 */
	/*
    protected void disconnect(Exception e) {
    dispose();
    if (e != null) {
    e.printStackTrace();
    }
    }
	 */


	/**
	 * General error reporting, all corralled here just in case
	 * I think of something slightly more intelligent to do.
	 */
	//public void errorMessage(String where, Exception e) {
	//parent.die("Error inside Client." + where + "()", e);
	//e.printStackTrace(System.err);
	//}
}