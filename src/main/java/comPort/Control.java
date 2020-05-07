package comPort;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import comPort.ComPortConnection;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.apache.commons.codec.binary.Hex;
import sample.Controller;
import validation.DataFromComPortValidation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class Control extends Thread implements SerialPortDataListener {

    private boolean dataReady;
    private byte[] bytesFromFile;
    private int counterBytes;
    private byte[] command = {START_CMD, 0, 0, 0, END_CMD};

    private ComPortConnection comPortConnection;
    private SerialPort userPort;

    private TextArea textArea;

    private ArrayList<Byte> bytesReceived;
    private LinkedList<Integer> listIntegers = new LinkedList<>();

    private Controller controller;

    public Control(){

    }

    public Control(ComPortConnection comPortConnection, Controller controller) {
        this.comPortConnection = comPortConnection;
        this.controller = controller;
        this.userPort = comPortConnection.getUserPort();
        bytesReceived = new ArrayList<>();
        //addListener();
    }

    public void setComPortConnection(ComPortConnection comPortConnection) {
        this.comPortConnection = comPortConnection;
        this.userPort = comPortConnection.getUserPort();
        bytesReceived = new ArrayList<>();
    }

    public void sendByte(byte byteToSend){
        byte[] bytesToSend = {byteToSend};
        userPort.writeBytes(bytesToSend, 1);
    }

    public void sendByteArray(byte[] bytesToSend){
        userPort.writeBytes(bytesToSend, bytesToSend.length);
    }

    public void sendTest(){
        command[1] = O_CMD;
        command[2] = k_CMD;
        command[3] = (byte) (O_CMD + k_CMD);
        userPort.writeBytes(command, command.length);
    }

    public void addListener(){
        userPort.addDataListener(this);
    }

    public void removeListener(){
        userPort.removeDataListener();
    }

    public byte[] readBytes(){
        byte[] bytes = new byte[5];
        userPort.readBytes(bytes, bytes.length);
        return bytes;
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

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
//        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
//            return;
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
            System.out.println("Read " + counterBytes + " bytes.");
//            System.out.println(Hex.encodeHexString(newData));
            for (byte aNewData : newData) {
                bytesReceived.add(aNewData);
            }

            if (counterBytes == 5){
                DataFromComPortValidation validation = new DataFromComPortValidation(controller);
                try {
                    validation.checkCmd(bytesReceived);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(bytesReceived);
            }
//            control.addToList(bytesReceived);
            bytesReceived.clear();
        }
    }

    static final public byte START_CMD = 0x55;          // 0x55
    static final public byte END_CMD = (byte) 0xAA;      // 0xAA
    static final public byte STAT_CMD = (byte) 153;     // 0x99
    static final public byte STRIP_NUMBER_CMD = 83;     // 0x53
    static final public byte ERR_CMD = 69;              // 0x45
    static final public byte CMD = 0x64;                 // 0x64
    static final public byte O_CMD = 79;                // 0x4F
    static final public byte k_CMD = 104;               // 0x68
    static final public byte[] CONTROL_ARRAY = {Control.START_CMD,
            Control.O_CMD,
            Control.k_CMD,
            (byte)(Control.O_CMD + Control.k_CMD),
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

//    STRIP TYPES
    static final public byte FIRST_STRIP_TYPE = 1;
    static final public byte SECOND_STRIP_TYPE = 2;
    static final public byte THIRD_STRIP_TYPE = 3;
    static final public byte FOURTH_STRIP_TYPE = 4;

//    DEVICE ERROR TYPES


}