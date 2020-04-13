package comPort;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import comPort.ComPortConnection;
import javafx.scene.control.TextArea;
import org.apache.commons.codec.binary.Hex;

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

    private ComPortConnection comPortConnection;

    private TextArea textArea;

    private ArrayList<Byte> bytesReceived;
    private LinkedList<Integer> listIntegers = new LinkedList<>();

    public Control(ComPortConnection comPortConnection) {
        //comPortConnection = ComPortConnection.getInstance();
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
//        if (userPort.bytesAvailable() > 1) {
//            int counterBytes;
//            do {
//                counterBytes = userPort.bytesAvailable();
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            } while (counterBytes < userPort.bytesAvailable());
//            byte[] newData = new byte[userPort.bytesAvailable()];
//            int numRead = userPort.readBytes(newData, newData.length);
//            System.out.println("Read " + numRead + " bytes.");
//            System.out.println(Hex.encodeHexString(newData));
//            for (byte aNewData : newData) {
//                bytesReceived.add(aNewData);
//            }
//            System.out.println(bytesReceived);
//            control.addToList(bytesReceived);
//            bytesReceived.clear();
//        }
    }
}