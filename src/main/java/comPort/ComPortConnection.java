package comPort;

import com.fazecast.jSerialComm.SerialPort;
import exception.ComPortException;

import java.io.IOException;

public class ComPortConnection implements AutoCloseable {

    private static ComPortConnection comPortConnection;

    private SerialPort userPort;

    private ComPortConnection(String comPortName) {
        if (comPortName != null) {
            userPort = SerialPort.getCommPort(comPortName);
           // userPort.addDataListener(this);
        }
    }

    public static synchronized ComPortConnection getInstance(String comPortName) {
        if (comPortConnection == null) {
            comPortConnection = new ComPortConnection(comPortName);
        }
        return comPortConnection;
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

    @Override
    public void close() throws IOException {
        if (userPort.isOpen()) {
            userPort.closePort();
        }
    }
}
