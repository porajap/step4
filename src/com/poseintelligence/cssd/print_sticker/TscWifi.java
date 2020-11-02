package com.poseintelligence.cssd.print_sticker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TscWifi {

    private static final String TAG = "THINBTCLIENT";
    private static final boolean D = true;
    private InputStream InStream = null;
    private OutputStream OutStream = null;
    private Socket socket = null;
    private String printerstatus = "";
    private int last_bytes = 0;
    private byte[] buffer = new byte[1024];
    private byte[] readBuf = new byte[1024];


    public TscWifi() {

    }

    public void openport(String ipaddress, int portnumber) {
        try {
            this.socket = new Socket(ipaddress, portnumber);
            this.InStream = this.socket.getInputStream();
            this.OutStream = this.socket.getOutputStream();
        } catch (Exception var4) {
            ;
        }
    }

    public void sendcommand(String message) {
        byte[] msgBuffer = message.getBytes();

        try {
            this.OutStream.write(msgBuffer);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public void sendcommand(byte[] message) {
        try {
            this.OutStream.write(message);
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public String status() {
        byte[] message = new byte[]{(byte) 27, (byte) 33, (byte) 63};

        try {
            this.OutStream.write(message);
        } catch (IOException var4) {
            ;
        }

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException var3) {
            var3.printStackTrace();
        }

        int tim;
        try {
            while (this.InStream.available() > 0) {
                this.readBuf = new byte[1024];
                tim = this.InStream.read(this.readBuf);
            }
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        if (this.readBuf[0] == 2 && this.readBuf[5] == 3) {
            for (tim = 0; tim <= 7; ++tim) {
                if (this.readBuf[tim] == 2 && this.readBuf[tim + 1] == 64 && this.readBuf[tim + 2] == 64 && this.readBuf[tim + 3] == 64 && this.readBuf[tim + 4] == 64 && this.readBuf[tim + 5] == 3) {
                    this.printerstatus = "Ready";
                    this.readBuf = new byte[1024];
                    break;
                }

                if (this.readBuf[tim] == 2 && this.readBuf[tim + 1] == 69 && this.readBuf[tim + 2] == 64 && this.readBuf[tim + 3] == 64 && this.readBuf[tim + 4] == 96 && this.readBuf[tim + 5] == 3) {
                    this.printerstatus = "Head Open";
                    this.readBuf = new byte[1024];
                    break;
                }

                if (this.readBuf[tim] == 2 && this.readBuf[tim + 1] == 64 && this.readBuf[tim + 2] == 64 && this.readBuf[tim + 3] == 64 && this.readBuf[tim + 4] == 96 && this.readBuf[tim + 5] == 3) {
                    this.printerstatus = "Head Open";
                    this.readBuf = new byte[1024];
                    break;
                }

                if (this.readBuf[tim] == 2 && this.readBuf[tim + 1] == 69 && this.readBuf[tim + 2] == 64 && this.readBuf[tim + 3] == 64 && this.readBuf[tim + 4] == 72 && this.readBuf[tim + 5] == 3) {
                    this.printerstatus = "Ribbon Jam";
                    this.readBuf = new byte[1024];
                    break;
                }

                if (this.readBuf[tim] == 2 && this.readBuf[tim + 1] == 69 && this.readBuf[tim + 2] == 64 && this.readBuf[tim + 3] == 64 && this.readBuf[tim + 4] == 68 && this.readBuf[tim + 5] == 3) {
                    this.printerstatus = "Ribbon Empty";
                    this.readBuf = new byte[1024];
                    break;
                }

                if (this.readBuf[tim] == 2 && this.readBuf[tim + 1] == 69 && this.readBuf[tim + 2] == 64 && this.readBuf[tim + 3] == 64 && this.readBuf[tim + 4] == 65 && this.readBuf[tim + 5] == 3) {
                    this.printerstatus = "No Paper";
                    this.readBuf = new byte[1024];
                    break;
                }

                if (this.readBuf[tim] == 2 && this.readBuf[tim + 1] == 69 && this.readBuf[tim + 2] == 64 && this.readBuf[tim + 3] == 64 && this.readBuf[tim + 4] == 66 && this.readBuf[tim + 5] == 3) {
                    this.printerstatus = "Paper Jam";
                    this.readBuf = new byte[1024];
                    break;
                }

                if (this.readBuf[tim] == 2 && this.readBuf[tim + 1] == 69 && this.readBuf[tim + 2] == 64 && this.readBuf[tim + 3] == 64 && this.readBuf[tim + 4] == 65 && this.readBuf[tim + 5] == 3) {
                    this.printerstatus = "Paper Empty";
                    this.readBuf = new byte[1024];
                    break;
                }

                if (this.readBuf[tim] == 2 && this.readBuf[tim + 1] == 67 && this.readBuf[tim + 2] == 64 && this.readBuf[tim + 3] == 64 && this.readBuf[tim + 4] == 64 && this.readBuf[tim + 5] == 3) {
                    this.printerstatus = "Cutting";
                    this.readBuf = new byte[1024];
                    break;
                }

                if (this.readBuf[tim] == 2 && this.readBuf[tim + 1] == 75 && this.readBuf[tim + 2] == 64 && this.readBuf[tim + 3] == 64 && this.readBuf[tim + 4] == 64 && this.readBuf[tim + 5] == 3) {
                    this.printerstatus = "Waiting to Press Print Key";
                    this.readBuf = new byte[1024];
                    break;
                }

                if (this.readBuf[tim] == 2 && this.readBuf[tim + 1] == 76 && this.readBuf[tim + 2] == 64 && this.readBuf[tim + 3] == 64 && this.readBuf[tim + 4] == 64 && this.readBuf[tim + 5] == 3) {
                    this.printerstatus = "Waiting to Take Label";
                    this.readBuf = new byte[1024];
                    break;
                }

                if (this.readBuf[tim] == 2 && this.readBuf[tim + 1] == 80 && this.readBuf[tim + 2] == 64 && this.readBuf[tim + 3] == 64 && this.readBuf[tim + 4] == 64 && this.readBuf[tim + 5] == 3) {
                    this.printerstatus = "Printing Batch";
                    this.readBuf = new byte[1024];
                    break;
                }

                if (this.readBuf[tim] == 2 && this.readBuf[tim + 1] == 96 && this.readBuf[tim + 2] == 64 && this.readBuf[tim + 3] == 64 && this.readBuf[tim + 4] == 64 && this.readBuf[tim + 5] == 3) {
                    this.printerstatus = "Pause";
                    this.readBuf = new byte[1024];
                    break;
                }

                if (this.readBuf[tim] == 2 && this.readBuf[tim + 1] == 69 && this.readBuf[tim + 2] == 64 && this.readBuf[tim + 3] == 64 && this.readBuf[tim + 4] == 64 && this.readBuf[tim + 5] == 3) {
                    this.printerstatus = "Pause";
                    this.readBuf = new byte[1024];
                    break;
                }
            }
        }

        return this.printerstatus;
    }

    public String batch() {
        boolean printvalue = false;
        String printbatch = "";
        String stringbatch = "0";
        String message = "~HS";
        this.readBuf = new byte[1024];
        byte[] batcharray = new byte[]{(byte) 48, (byte) 48, (byte) 48, (byte) 48, (byte) 48, (byte) 48, (byte) 48, (byte) 48};
        byte[] msgBuffer = message.getBytes();

        try {
            this.OutStream.write(msgBuffer);
        } catch (IOException var9) {

        }

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException var8) {
            var8.printStackTrace();
        }

        int i;
        try {
            while (this.InStream.available() > 50) {
                this.readBuf = new byte[1024];
                i = this.InStream.read(this.readBuf);
            }
        } catch (IOException var10) {
            var10.printStackTrace();
        }

        if (this.readBuf[0] == 2) {
            System.arraycopy(this.readBuf, 55, batcharray, 0, 8);

            for (i = 0; i <= 7; ++i) {
                if (batcharray[i] == 44) {
                    batcharray = new byte[]{(byte) 57, (byte) 57, (byte) 57, (byte) 57, (byte) 57, (byte) 57, (byte) 57, (byte) 57};
                }
            }

            stringbatch = new String(batcharray);
            int var11 = Integer.parseInt(stringbatch);
            printbatch = Integer.toString(var11);
            if (printbatch == "99999999") {
                printbatch = "";
            }
        }

        return printbatch;
    }

    public void closeport() {
        try {
            this.socket.close();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public void setup(int width, int height, int speed, int density, int sensor, int sensor_distance, int sensor_offset) {
        String message = "";
        String size = "SIZE " + width + " mm" + ", " + height + " mm";
        String speed_value = "SPEED " + speed;
        String density_value = "DENSITY " + density;
        String sensor_value = "";
        if (sensor == 0) {
            sensor_value = "GAP " + sensor_distance + " mm" + ", " + sensor_offset + " mm";
        } else if (sensor == 1) {
            sensor_value = "BLINE " + sensor_distance + " mm" + ", " + sensor_offset + " mm";
        }

        message = size + "\n" + speed_value + "\n" + density_value + "\n" + sensor_value + "\n";
        byte[] msgBuffer = message.getBytes();

        try {
            this.OutStream.write(msgBuffer);
        } catch (IOException var15) {
            var15.printStackTrace();
        }

    }

    public void clearbuffer() {
        String message = "CLS\n";
        byte[] msgBuffer = message.getBytes();

        try {
            this.OutStream.write(msgBuffer);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public void barcode(int x, int y, String type, int height, int human_readable, int rotation, int narrow, int wide, String string) {
        String message = "";
        String barcode = "BARCODE ";
        String position = x + "," + y;
        String mode = "\"" + type + "\"";
        String height_value = "" + height;
        String human_value = "" + human_readable;
        String rota = "" + rotation;
        String narrow_value = "" + narrow;
        String wide_value = "" + wide;
        String string_value = "\"" + string + "\"";
        message = barcode + position + " ," + mode + " ," + height_value + " ," + human_value + " ," + rota + " ," + narrow_value + " ," + wide_value + " ," + string_value + "\n";
        byte[] msgBuffer = message.getBytes();

        try {
            this.OutStream.write(msgBuffer);
        } catch (IOException var22) {
            var22.printStackTrace();
        }

    }
    
    /*
    public String printerfont(int x, int y, String size, int rotation, int x_multiplication, int y_multiplication, String string) {
    	try {
            String message = "";
            String text = "TEXT ";
            String position = x + "," + y;
            String size_value = "\"" + size + "\"";
            String rota = "" + rotation;
            String x_value = "" + x_multiplication;
            String y_value = "" + y_multiplication;
            String string_value = "\"" + string + "\"";
            message = text + position + " ," + size_value + " ," + rota + " ," + x_value + " ," + y_value + " ," + string_value + "\r\n";
            byte[] msgBuffer = message.getBytes("UTF-8");
            this.sendcommand(msgBuffer);
    	} catch (IOException var6) {
            var6.printStackTrace();
        }
            
    	return "1";
        
    }
	*/
  
    public void printerfont(int x, int y, String size, int rotation, int x_multiplication, int y_multiplication, String string) {
    	//String font_ = "COUR.TTF";
        String message = "";
        String text = "TEXT ";
        String position = x + "," + y;
        String size_value = "\"" + size + "\"";
        String rota = "" + rotation;
        String x_value = "" + x_multiplication;
        String y_value = "" + y_multiplication;
        String string_value = "\"" + string + "\"";
        //String font = "\"" + font_ + "\"";
        //message = text + position + ",\"ARIAL.TTF\" ," + size_value + " ," + rota + " ," + x_value + " ," + y_value + " ," + string_value + "\n";
        message = text + position + " ," + size_value + " ," + rota + " ," + x_value + " ," + y_value + " ," + string_value + "\n";
        byte[] msgBuffer = message.getBytes();

        try {
            this.OutStream.write(msgBuffer);
        } catch (IOException var18) {
            var18.printStackTrace();
        }

    }
    

    public void printlabel(int quantity, int copy) {
        String message = "";
        message = "PRINT " + quantity + ", " + copy + "\n";
        byte[] msgBuffer = message.getBytes();

        try {
            this.OutStream.write(msgBuffer);
        } catch (IOException var6) {
            var6.printStackTrace();
        }

    }

    public void formfeed() {
        String message = "";
        message = "FORMFEED\n";
        byte[] msgBuffer = message.getBytes();

        try {
            this.OutStream.write(msgBuffer);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public void nobackfeed() {
        String message = "";
        message = "SET TEAR OFF\n";
        byte[] msgBuffer = message.getBytes();

        try {
            this.OutStream.write(msgBuffer);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public void sendfile(String filename) {
        try {
            FileInputStream fis = new FileInputStream("/sdcard/Download/" + filename);
            byte[] data = new byte[fis.available()];

            while (fis.read(data) != -1) {
                ;
            }

            this.OutStream.write(data);
            fis.close();
        } catch (Exception var4) {
            ;
        }

    }

    public void downloadpcx(String filename) {
        try {
            FileInputStream fis = new FileInputStream("/sdcard/Download/" + filename);
            byte[] data = new byte[fis.available()];
            String download = "DOWNLOAD F,\"" + filename + "\"," + data.length + ",";
            byte[] download_head = download.getBytes();

            while (fis.read(data) != -1) {
                ;
            }

            this.OutStream.write(download_head);
            this.OutStream.write(data);
            fis.close();
        } catch (Exception var6) {
            ;
        }

    }

    public String qrcode(int x, int y, String ecc, String cell, String mode, String rotation, String model, String mask, String content) {
        String message = "QRCODE " + x + "," + y + "," + ecc + "," + cell + "," + mode + "," + rotation + "," + model + "," + mask + "," + "\"" + content + "\"" + "\r\n";
        byte[] msgBuffer = message.getBytes();
        this.sendcommand(msgBuffer);
        return "1";
    }

    public String bar(String x, String y, String width, String height) {
        String message = "BAR " + x + "," + y + "," + width + "," + height + "\r\n";
        byte[] msgBuffer = message.getBytes();
        this.sendcommand(msgBuffer);
        return "1";
    }

}
