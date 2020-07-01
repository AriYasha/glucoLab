package com.comPort;

import com.controllers.Controller;
import com.entity.MeasurementSetup;
import com.entity.PolySetup;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.validation.DataFromComPortValidation;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


public class Control extends Thread implements SerialPortDataListener {

    final static Logger logger = Logger.getLogger(Control.class);

    private boolean dataReady;
    private byte[] bytesFromFile;
    private int counterBytes;
    private byte[] command = {START_CMD, 0, 0, 0, 0, 0, 0, 0, END_CMD};

    private ComPortConnection comPortConnection;
    private SerialPort userPort;

    private TextArea textArea;

    private ArrayList<Byte> bytesReceived;
    private LinkedList<Integer> listIntegers = new LinkedList<>();

    private Controller controller;
    private DataFromComPortValidation validation;
    private MeasurementSetup setup;
    private PolySetup polySetup;

    public Control() {

    }

    public Control(ComPortConnection comPortConnection, Controller controller, MeasurementSetup setup, PolySetup polySetup) {
        this.comPortConnection = comPortConnection;
        this.controller = controller;
        this.userPort = comPortConnection.getUserPort();
        this.setup = setup;
        this.polySetup = polySetup;
        bytesReceived = new ArrayList<>();
        validation = new DataFromComPortValidation(controller, this);
        //addListener();
    }

    public void setComPortConnection(ComPortConnection comPortConnection) {
        this.comPortConnection = comPortConnection;
        this.userPort = comPortConnection.getUserPort();
        bytesReceived = new ArrayList<>();
    }

    public void sendByte(byte byteToSend) {
        byte[] bytesToSend = {byteToSend};
        userPort.writeBytes(bytesToSend, 1);
    }

    public SerialPort getUserPort() {
        return userPort;
    }

    public void sendByteArray(byte[] bytesToSend) {
        userPort.writeBytes(bytesToSend, bytesToSend.length);
    }

    public void sendTest() {
        command[1] = O_CMD;
        command[2] = k_CMD;
        command[7] = (byte) (O_CMD + k_CMD);
        userPort.writeBytes(command, command.length);
    }

    public void sendSetupRequest() {
        command[1] = CMD;
        command[2] = 0x00;
        command[7] = (byte) (command[1] + command[2]);
        userPort.writeBytes(command, command.length);
    }

    public void sendSmoothRequest() {
        command[1] = CMD;
        command[2] = 0x02;
        command[7] = (byte) (command[1] + command[2]);
        userPort.writeBytes(command, command.length);
    }

    public void sendPolySetupRequest() {
        command[1] = CMD;
        command[2] = 0x01;
        command[7] = (byte) (command[1] + command[2]);
        userPort.writeBytes(command, command.length);
    }

    public boolean isTestOk(){
        Runnable task = () -> {
            sendTest();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        return DataFromComPortValidation.isTest;
    }

    public void addListener() {
        userPort.addDataListener(this);
    }

    public void removeListener() {
        userPort.removeDataListener();
    }

    public byte[] readBytes() {
        byte[] bytes = new byte[9];
        userPort.readBytes(bytes, bytes.length);
        return bytes;
    }

    public byte[] readSmoothBytes() {
        byte[] bytes = new byte[23];
        userPort.readBytes(bytes, bytes.length);
        return bytes;
    }

    public int getIntFromArray(byte[] bytes) {
        String binaryString = "";
        for(int i = bytes.length - 1; i >= 0; i--){
            binaryString += dataToBin(bytes[i]);
        }
        //return fromBinToInt(dataToBin(bytes[1]) + dataToBin(bytes[0]));
        return fromBinToInt(binaryString);
    }


    private String dataToBin(byte data) {
        String bs = null;
        if (data >= 0) {
            bs = Integer.toBinaryString(data);
            while (bs.length() < 8) {
                bs = "0" + bs;
            }
        } else {
            bs = Integer.toBinaryString(data);
            bs = bs.substring(24, 32);
        }
        //System.out.println(bs);
        return bs;
    }

    private int fromBinToInt(String binaryString) {
        int decDigit = 0;

        if (binaryString.length() < 32) {
            StringBuilder sb = new StringBuilder(binaryString);
            sb.insert(0, "0");
            binaryString = sb.toString();
        }
        char[] binStringToArray = binaryString.toCharArray(); // Преобразуем строку в массив символов
        //System.out.println(Arrays.toString(binStringToArray));
        if (binStringToArray[0] != '1') {
            int j = 0;
            for (int i = binStringToArray.length - 1; i >= 0; i--) {
                decDigit += Character.getNumericValue(binStringToArray[i]) * Math.pow(2, j);
                j++;
            }
        } else {
            for (int i = binStringToArray.length - 1; i >= 0; i--) {
                binStringToArray[i] = binStringToArray[i] == '1' ? '0' : '1';

            }
            int j = 0;
            for (int i = binStringToArray.length - 1; i >= 0; i--) {
                decDigit += Character.getNumericValue(binStringToArray[i]) * Math.pow(2, j);
                j++;

            }
            decDigit = (decDigit + 1) * (-1);
        }
        //System.out.println(Arrays.toString(binStringToArray));

        //System.out.println(decDigit);
        return decDigit;
    }

    private int addByteToList(int numBytes){
        byte[] setupData = new byte[numBytes];
        while (userPort.bytesAvailable() < numBytes) ;
        userPort.readBytes(setupData, numBytes);
        for (byte aNewData : setupData) {
            bytesReceived.add(aNewData);
        }
        logger.debug(bytesReceived);
        return userPort.bytesAvailable();
    }

    public int bytesAvailable() {
        return userPort.bytesAvailable();
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        boolean isGood = true;
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
            return;
        int numRead = userPort.bytesAvailable();
        while (numRead >= 9) {
            byte[] cmdData = new byte[8];
            byte[] firstByte = new byte[1];
            userPort.readBytes(firstByte, 1);
            if (firstByte[0] == START_CMD) {
                bytesReceived.add(firstByte[0]);
                numRead = userPort.readBytes(cmdData, 8);
                System.out.println("Read " + numRead + " bytes.");
                System.out.println("available " + numRead);
                for (byte aNewData : cmdData) {
                    bytesReceived.add(aNewData);
                }
                logger.debug(bytesReceived);
                numRead = userPort.bytesAvailable();
                if (userPort.bytesAvailable() >= 8) {
                    //logger.debug("available " + numRead);
                }

            } else if (firstByte[0] == SETUP_CMD) {
                bytesReceived.add(firstByte[0]);
                numRead = addByteToList(29);
                //logger.debug("available " + numRead);
            } else if (firstByte[0] == END_CMD){
                bytesReceived.add(firstByte[0]);
                numRead = addByteToList(11);
            } else if(firstByte[0] == POLY_SETUP_CMD){
                bytesReceived.add(firstByte[0]);
                numRead = addByteToList(25);
            } else if(firstByte[0] == POLY_DATA_CMD){
                bytesReceived.add(firstByte[0]);
                numRead = addByteToList(11);
            } else{
                Platform.runLater(() -> controller.comPortStatus.setText(String.valueOf(userPort.bytesAvailable())));
                byte[] notRecognize = new byte[userPort.bytesAvailable()];
                userPort.readBytes(notRecognize, userPort.bytesAvailable());
                logger.warn("notRecognize : ");
                logger.warn(firstByte[0]);
                logger.warn(Arrays.toString(notRecognize));
                isGood = false;
                numRead = userPort.bytesAvailable();
            }
            if(isGood) {
                try {
                    validation.checkCmd(bytesReceived, setup, polySetup);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            //logger.debug("byte received :" + bytesReceived);

            bytesReceived.clear();
        }
    }

    static final public byte START_CMD = 0x55;          // 0x55
    static final public byte END_CMD = (byte) 0xAA;      // 0xAA
    static final public byte SETUP_CMD = (byte) 0xCC;      // 0xCC
    static final public byte STAT_CMD = (byte) 153;     // 0x99
    static final public byte STRIP_NUMBER_CMD = 83;     // 0x53
    static final public byte DEVICE_MODE_CMD = 0x4D;     // 0x4D
    static final public byte ERR_CMD = 69;              // 0x45
    static final public byte CMD = 0x64;                 // 0x64
    static final public byte MEASURE_CMD = (byte) 0x30;   // 0x30
    static final public byte O_CMD = 79;                // 0x4F
    static final public byte k_CMD = 104;               // 0x68
    static final public byte SMOOTH_CMD = (byte) 0xEE;               // 0xEE
    static final public byte NOT_FOUND_CMD = 0x20;               // 0x20
    static final public byte POLY_SETUP_CMD = (byte)0xDD;               // 0xDD
    static final public byte POLY_DATA_CMD = (byte)0xBB;               // 0xBB
    static final public byte[] CONTROL_ARRAY = {Control.START_CMD,
            Control.O_CMD,
            Control.k_CMD,
            0,
            0,
            0,
            0,
            (byte) (Control.O_CMD + Control.k_CMD),
            Control.END_CMD};

    //    DEVICE STATUS TYPES
    static final public byte NOT_CONNECTED_STAT = 0;
    static final public byte CONNECTED_STAT = 1;
    static final public byte STRIP_WAITING_STAT = 2;
    static final public byte STRIP_INSERTED_STAT = 3;
    static final public byte DROP_WAITING_STAT = 4;
    static final public byte DROP_DETECTED_STAT = 5;
    static final public byte LEAK_WAITING_STAT = 6;
    static final public byte LEAKING_STAT = 7;
    static final public byte FAST_POLARITY_BEGIN_STAT = 8;
    static final public byte FAST_POLARITY_END_STAT = 9;
    static final public byte START_MEASURE_STAT = 10;
    static final public byte POLARITY_CHANGED_STAT = 11;
    static final public byte END_MEASURE_STAT = 12;
    static final public byte START_POLY_STAT = 13;
    static final public byte END_POLY_STAT = 14;
    static final public byte PAUSE_BEGIN_STAT = 15;
    static final public byte PAUSE_END_STAT = 16;

    //    STRIP TYPES
    static final public byte FIRST_STRIP_TYPE = 1;
    static final public byte SECOND_STRIP_TYPE = 2;
    static final public byte THIRD_STRIP_TYPE = 3;
    static final public byte FOURTH_STRIP_TYPE = 4;
    static final public byte ZERO_STRIP_TYPE = 5;

    //    DEVICE MODE
    static final public byte MEASURE_MODE = 1;
    static final public byte POLY_MODE = 2;
    static final public byte SMOOTH_MODE = 3;

    //    ERROR TYPES
    static final public byte STRIP_TYPE_ERROR = 1;
    static final public byte VOLTAGE_ERROR = 10;
    static final public byte LEAKING_ERROR = 11;
    static final public byte E50_ERROR = 50;
    static final public byte E51_ERROR = 51;
    static final public byte E52_ERROR = 52;
    static final public byte E53_ERROR = 53;
    static final public byte E54_ERROR = 54;
    static final public byte E55_ERROR = 55;
    static final public byte E80_ERROR = 80;
    static final public byte E81_ERROR = 81;
    static final public byte E82_ERROR = 82;
    static final public byte E83_ERROR = 83;
    static final public byte E84_ERROR = 84;
    static final public byte E85_ERROR = 85;
    static final public byte E86_ERROR = 86;
    static final public byte E87_ERROR = 87;
    static final public byte E88_ERROR = 88;

    public void sendOnExit() {
    }

    public void sendStatusRequest() {
        command[1] = STAT_CMD;
        command[2] = 0x00;
        command[3] = (byte) (command[1] + command[2]);
        userPort.writeBytes(command, command.length);
    }

    public void closeConnection(){
        comPortConnection.closePort();
    }

//    DEVICE ERROR TYPES


}