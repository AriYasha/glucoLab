package comPort;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import control.Control;
import exception.ComPortException;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

public class ComPortConnectivity implements SerialPortDataListener, AutoCloseable {

    private static ComPortConnectivity comPortConnectivity;

    private SerialPort userPort;
    private Control control;
    private LinkedList<Byte> bytesReceived = new LinkedList<>();

    private ComPortConnectivity() {
    }

    private ComPortConnectivity(String comPortName) {
        if (comPortName != null) {
            userPort = SerialPort.getCommPort(comPortName);
            userPort.addDataListener(this);
        }
    }

    public static synchronized ComPortConnectivity getInstance(String comPortName) {
        if (comPortConnectivity == null) {
            comPortConnectivity = new ComPortConnectivity(comPortName);
        }
        return comPortConnectivity;
    }

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public SerialPort getUserPort() {
        return this.userPort;
    }

    public static String[] getPortNames() {
        SerialPort[] ports = SerialPort.getCommPorts();
        String[] result = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            result[i] = ports[i].getDescriptivePortName();
        }
        return result;
    }

    public static String getPortName() throws ComPortException {
        SerialPort[] ports = SerialPort.getCommPorts();
        String result = null;
        for (int i = 0; i < ports.length; i++) {
            if (ports[i].getDescriptivePortName().contains("USB-SERIAL CH340"))
                result = ports[i].getSystemPortName();
        }
        if (result == null) {
            throw new ComPortException("There are no COM-port with name: USB-SERIAL CH340");
        }
        return result;
    }

    public void openPort() {
        userPort.setBaudRate(9600);
        userPort.setNumDataBits(8);
        userPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
        userPort.setParity(SerialPort.NO_PARITY);
        //userPort.addDataListener(this);
        userPort.openPort();
    }

    public void openPort(int baudRate, int numDataBits, int numStopBits, int parity) {
        userPort.setBaudRate(baudRate);
        userPort.setNumDataBits(numDataBits);
        userPort.setNumStopBits(numStopBits);
        userPort.setParity(parity);
        userPort.openPort();
    }

    public void closePort() {
        if (userPort.isOpen()) {
            userPort.closePort();
        }
    }

    public boolean isBusy(){
        boolean isBusy = false;
        if(userPort.isOpen()){
            isBusy = true;
        }
        return isBusy;
    }

    public void sendString(String string) {

    }

    public void sendStartBytes(byte[] bytes) {
        userPort.writeBytes(bytes, bytes.length);

    }

    public void sendEndBytes(byte[] bytes) {
        userPort.writeBytes(bytes, bytes.length);

    }

    public void sendBytes(byte[] bytes) {
        userPort.writeBytes(bytes, bytes.length);
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
            return;
        if (userPort.bytesAvailable() > 1) {
            int counterBytes;
            do {
                counterBytes = userPort.bytesAvailable();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (counterBytes < userPort.bytesAvailable());
            byte[] newData = new byte[userPort.bytesAvailable()];
            int numRead = userPort.readBytes(newData, newData.length);
            System.out.println("Read " + numRead + " bytes.");
            System.out.println(Hex.encodeHexString(newData));
            for (byte aNewData : newData) {
                bytesReceived.add(aNewData);
            }
            System.out.println(bytesReceived);
            control.addToList(bytesReceived);
            bytesReceived.clear();
        }
    }

    @Override
    public void close() throws IOException {
        if (userPort.isOpen()) {
            userPort.closePort();
        }
    }
}
