package control;

import comPort.ComPortConnectivity;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class Control extends Thread {

    private boolean dataReady;
   // private Data data;
    private byte[] bytesFromFile;
    private int counterBytes;

    private ComPortConnectivity connectivity;

    private TextArea textArea;

    private ArrayList<Byte> bytesRiesived;
    private LinkedList<Integer> listIntegers = new LinkedList<>();

    public Control(TextArea textArea) {
        bytesRiesived = new ArrayList<>();
        this.textArea = textArea;
        try {
            bytesFromFile = Files.readAllBytes(Paths.get("one-ten.wav"));
            System.out.println(bytesFromFile.length);
            System.out.println(Arrays.toString(bytesFromFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        bytesFromFile = new byte[7];
//        bytesFromFile[0] = 11;
//        bytesFromFile[1] = 11;
//        bytesFromFile[2] = 11;
//        bytesFromFile[3] = 11;
//        bytesFromFile[4] = 11;
//        bytesFromFile[5] = 11;
//        bytesFromFile[6] = 11;
        counterBytes = 20000;
    }

    public Control() {
        bytesRiesived = new ArrayList<>();
        try {
            bytesFromFile = Files.readAllBytes(Paths.get(/*"one_8kHz_8bits.wav"*/"translate_tts_11kHz_16bits.wav"));
            System.out.println(bytesFromFile.length);
            System.out.println(Arrays.toString(bytesFromFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> parceFromFile() {
        ArrayList<Integer> datas = new ArrayList<>();
        int i = 0;
        byte a, b;
        while(i<bytesFromFile.length){
            a = bytesFromFile[i];
            i++;
            b = bytesFromFile[i];
            i++;
            System.out.println(fromBinToInt(dataToBin(b) + dataToBin(a)));
           // Write.writing("notes3.txt", Integer.toString(fromBinToInt(dataToBin(b) + dataToBin(a))));
            datas.add(fromBinToInt(dataToBin(b) + dataToBin(a)));
        }
        //for(int i = 0; i < bytesFromFile.length; i++){

        //}
        return datas;
    }

    public void setConnectivity(ComPortConnectivity connectivity) {
        this.connectivity = connectivity;
    }

    public boolean isDataReady() {
        return dataReady;
    }

    private void setDataReady(boolean dataReady) {
        this.dataReady = dataReady;
    }



    public void addToList(LinkedList<Byte> addBytes) {
        setDataReady(false);
        bytesRiesived.addAll(addBytes);
        //System.out.println("add to list = " + bytesRiesived.toString());
        //parseArray(bytesRiesived);

        //parseByTest(bytesRiesived);
        //Write.writing("notes3.txt",listIntegers);
        delete();

    }

//    private void parseArray(ArrayList<Byte> bytesRiesived) {
//        System.out.println("received bytes " + bytesRiesived.size());
//        if (bytesRiesived.size() >= 15) {
//
//           /* data.setIdHigh(fromBinToInt(dataToBin(bytesRiesived.get(0))));
//            data.setIdLow(fromBinToInt(dataToBin(bytesRiesived.get(2))
//                    + dataToBin(bytesRiesived.get(1))));*/
//            for (int i = 3; i < bytesRiesived.size(); i += 12) {
//                int j = i;
//                Data data = new Data();
//                LocalDate localDate = LocalDate.of(fromBinToInt(dataToBin(bytesRiesived.get(i + 3))
//                                + dataToBin(bytesRiesived.get(i + 2))),
//                        fromBinToInt(dataToBin(bytesRiesived.get(i + 1))), fromBinToInt(dataToBin(bytesRiesived.get(i))));
//                data.setDate(localDate);
//                LocalTime localTime = LocalTime.of(fromBinToInt(dataToBin(bytesRiesived.get(i + 4))),
//                        fromBinToInt(dataToBin(bytesRiesived.get(i + 5))));
//                data.setTime(localTime);
//                data.setDiary(fromBinToInt(dataToBin(bytesRiesived.get(i + 6))));
//                data.setEat(fromBinToInt(dataToBin(bytesRiesived.get(i + 7))));
//
//                data.setGlucose((float) fromBinToInt(dataToBin(bytesRiesived.get(i + 9)) + dataToBin(bytesRiesived.get(i + 8))) / 100);
//                data.setHct((float) fromBinToInt(dataToBin(bytesRiesived.get(i + 11)) + dataToBin(bytesRiesived.get(i + 10))) / 100);
//                Platform.runLater(() -> textArea.appendText(data.toString()));
//                Write.writing(data);
//                setDataReady(true);
//                setData(data);
//            }
//        } else if (bytesRiesived.size() == 2 || bytesRiesived.size() == 4) {
//            if (bytesRiesived.get(0) == 79 && bytesRiesived.get(1) == 107) {
//                //System.out.println("ok");
//                Platform.runLater(() -> textArea.appendText("ok"));
////                for(int i = 1; i <= 255; i++){
////                    byte[] byteToTransmit = {(byte)64};
////                    connectivity.sendBytes(byteToTransmit);
////                }
////                byte[] byteToTransmit = {(byte)-59};
////                connectivity.sendBytes(byteToTransmit);
//                if (bytesFromFile.length > counterBytes) {
////                    byte[] bytes = {92};
////                    if(counterBytes > 200){
////                        connectivity.sendBytes(bytes);
////                    }
////                    byte[] byteToTransmit = new byte[256];
////                    int transmitCounter = 0;
////
////                    do{
////                        byteToTransmit[transmitCounter] = (byte) counterBytes/*bytesFromFile[counterBytes]*/;
////                        counterBytes++;
////                        transmitCounter++;
////                    }while( (counterBytes % 255) != 0 && (transmitCounter % 255) != 0);
////                    byteToTransmit[transmitCounter] = (byte) -59;
////                    System.out.println(counterBytes);
////                    Platform.runLater(() -> textArea.appendText(Integer.toString(counterBytes)));
////                    System.out.println(Arrays.toString(byteToTransmit));
////                    /*for (byte one:byteToTransmit) {
////                      byte transmit[] = {one};
////                        System.out.println(one);
////                      connectivity.sendBytes(transmit);
////                    }*/
////                    connectivity.sendBytes(byteToTransmit);
//                    byte[] byteToTransmit = {bytesFromFile[counterBytes]};
//                    //System.out.println(counterBytes);
//                    Platform.runLater(() -> textArea.appendText(Integer.toString(counterBytes) + " "));
//                    counterBytes++;
//                    connectivity.sendBytes(byteToTransmit);
//
//
//
//
//                } else {
//                    System.out.println(counterBytes);
//                    Platform.runLater(() -> textArea.appendText(Integer.toString(counterBytes)));
//                    byte[] byteToTransmit = {-1};
//                    //connectivity.sendBytes(byteToTransmit);
//                }
//            }
//        }
////        for (byte byteReceived : bytesRiesived) {
////            listIntegers.add(fromBinToInt(dataToBin(byteReceived)));
////        }
////        System.out.println("listIntegers : " + listIntegers);
//
//    }


    public void parseByTest(LinkedList<Byte> bytesRiesived) {
        for (int i = 0; i < bytesRiesived.size(); i++) {
            if (bytesRiesived.get(i) == 92) {
                bytesRiesived.remove(i);
            }
            if (bytesRiesived.get(i) == -59) {
                bytesRiesived.remove(i);
                i -= 2;

            }
        }
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


    public void delete() {
        bytesRiesived.removeAll(bytesRiesived);
        listIntegers.clear();
    }
}