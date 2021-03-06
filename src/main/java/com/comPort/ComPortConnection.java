package com.comPort;

import com.fazecast.jSerialComm.SerialPort;
import com.exception.ComPortException;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ComPortConnection implements AutoCloseable {

    final static Logger logger = Logger.getLogger(ComPortConnection.class);

    private static ComPortConnection comPortConnection;

    private SerialPort userPort;

    private ComPortConnection(String comPortName) {
        if (comPortName != null) {
            userPort = SerialPort.getCommPort(comPortName);
           // userPort.addDataListener(this);
        }
    }

    public SerialPort getUserPort() {
        return userPort;
    }

    public static synchronized ComPortConnection getInstance(String comPortName) throws ComPortException {
        //if (comPortConnection == null) {
            comPortConnection = new ComPortConnection(comPortName);
        //}
        return comPortConnection;
    }

    public static String[] getPortNames() {
        SerialPort[] ports = SerialPort.getCommPorts();
        String[] result = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            //result[i] = ports[i].getDescriptivePortName();
            result[i] = ports[i].getSystemPortName();
        }
        return result;
    }

    public static SerialPort[] getsPortNames(){
        SerialPort[] ports = SerialPort.getCommPorts();
        return ports;
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

    public void openPort() throws ComPortException {
        userPort.setBaudRate(38400);
        userPort.setNumDataBits(8);
        userPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
        userPort.setParity(SerialPort.NO_PARITY);
        userPort.openPort();
    }

    public void openPort(int baudRate, int numDataBits, int numStopBits, int parity) throws ComPortException {
        userPort.setBaudRate(baudRate);
        userPort.setNumDataBits(numDataBits);
        userPort.setNumStopBits(numStopBits);
        userPort.setParity(parity);
        userPort.openPort();
    }

    public void closePort() {
        if (userPort.isOpen()) {
            userPort.closePort();
            logger.debug(userPort.getSystemPortName());
            logger.debug("portClosed");
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
            logger.debug(userPort.getSystemPortName());
            logger.debug("portClosed");
        }
    }

    @Override
    public String toString() {
        return "ComPortConnection{" +
                "userPort=" + userPort.toString() +
                '}';
    }
}
