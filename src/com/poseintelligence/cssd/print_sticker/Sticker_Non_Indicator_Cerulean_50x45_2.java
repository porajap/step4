package com.poseintelligence.cssd.print_sticker;

public class Sticker_Non_Indicator_Cerulean_50x45_2 {

    // Var
    String IP = "192.168.1.61";
    final int PortNumber = 9100;

    private int PSpeed = 6 ;
    private int PDensity = 13;

    // Var Position
    private int x = 0;
    private int y = 0;
    private int h = 48;
    private int w = 53;

    private boolean Print_Manual = false;
    private int PQty = 1;

    public Sticker_Non_Indicator_Cerulean_50x45_2(){
    	
    }

    public Sticker_Non_Indicator_Cerulean_50x45_2(String IP, int Speed, int Density, int h, int w, int x, int y, boolean Print_Manual){
        this.IP = IP;
        this.PSpeed = Speed;
        this.PDensity = Density;
        this.h = h;
        this.w = w;
        this.x = x;
        this.y = y;
        this.Print_Manual = Print_Manual;
    }

    public void print() {

        TscWifi Tsc = new TscWifi();

        Tsc.openport(IP, 9100);

        Tsc.setup(w, h, PSpeed, PDensity, 0, 2, 1);
            Tsc.sendcommand("DIRECTION 1,0\r\n");
            Tsc.clearbuffer();

            try {



                // ---------------------------------------------------------------------------------
                // Part 1 :
                // ---------------------------------------------------------------------------------

                // Item
                Tsc.printerfont(x(3), y(4), "0", 0, 9, 9, "Testxxxxxxxxxxxxxxxxxxxxxxx");
                
                
                // Item
                Tsc.printerfont(x(3), y(10), "0", 0, 9, 9, "เทส xxxxxxxxxxxxxxxxxxxxx");


                //QR_Code
                Tsc.qrcode(x(3), y(20), "H", "5", "A", "0", "M2", "S1", "Test xfvTEstfdrg");
                
                Tsc.printerfont(210, 204, "0", 0, 9, 9, "Test xfvfdrg");

                // ---------------------------------------------------------------------------------
                Tsc.sendcommand("PRINT 1," + 1 + "\r\n");

               

            } catch (Exception e) {
                e.printStackTrace();
            }

        

        Tsc.closeport();

    }

    private int x(double val){
        double data = (val*7.986);
        return (int)data + x;
    }

    private int y(double val){
        double data = (val*7.986);
        return (int)data + y;
    }

}
