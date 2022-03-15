
import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Vit {
    private int state;

    private DatagramSocket sck;
    private int communication_counter = 0;
    private int sourceaddr = 0;
    private int destaddr = 0x0101;
    private int UdpPort;
    private InetAddress IPaddress;
    private int Timeout = 1500;
    private int MultiZone_elements=0;

    public int getTimeout() {
        return Timeout;
    }

    public void setTimeout(int Timeout) {
        this.Timeout = Timeout;
    }

    public int getState() {
        return state;
    }

    public Vit(InetAddress IPaddress, int UdpPort) throws UnknownHostException, SocketException {
        this.UdpPort = UdpPort;
        this.IPaddress = IPaddress; //InetAddress.getByName("169.254.10.49"); //IPaddress
        this.sck = new DatagramSocket();

        state = 0;

    }

    private int Vjt_Command(int command) throws IOException {
        byte[] tx_msg = new byte[1024];
        byte[] received = new byte[1024];
        tx_msg[0] = (byte) 0x55;
        tx_msg[1] = (byte) 0xa7;
//        tx_msg[2] = (byte) 0x00;
//        tx_msg[3] = (byte) 0x00;
        tx_msg[4] = (byte) 0x00;
        tx_msg[5] = (byte) 0x00;
        tx_msg[6] = (byte) (sourceaddr & 0xff);
        tx_msg[7] = (byte) ((sourceaddr >> 8) & 0xff);
        tx_msg[8] = (byte) (destaddr & 0xff);
        tx_msg[9] = (byte) ((destaddr >> 8) & 0xff);

        tx_msg[10] = (byte) (communication_counter & 0xff);
        tx_msg[11] = (byte) ((communication_counter >> 8) & 0xff);
        communication_counter++;
        tx_msg[12] = (byte) ((command >> 8) & 0xff);
        tx_msg[13] = (byte) (command & 0xff);
        tx_msg[14] = (byte) 0x00;
        tx_msg[15] = (byte) 0x00;
        int msg_length = 16;

        int a = 0;
        int i;
        for (i = 4; i < msg_length; i++) {
            a += (((int) tx_msg[i]) & 0xff);
        }
        tx_msg[2] = (byte) (a & 0xff);
        tx_msg[3] = (byte) ((a >> 8) & 0xff);

        sck.setSoTimeout(Timeout);

        DatagramPacket request = new DatagramPacket(tx_msg, msg_length, IPaddress, UdpPort);
        sck.send(request);
        DatagramPacket response = new DatagramPacket(received, received.length);
        sck.receive(response);
        System.out.println("vjt_p2.nc_qw.Vjt_Command()" + received[16]);
        return received[16];
    }

    ///----------------------------------------------
    public int Test_All_Br() throws IOException {
        return Vjt_Command(0x0303);
    }

    public int Test_All_Red() throws IOException {
        return Vjt_Command(0x0304);
    }

    public int Test_All_Green() throws IOException {
        return Vjt_Command(0x0305);
    }

    public int Test_All_Blue() throws IOException {
        return Vjt_Command(0x0306);
    }

    //     public int All_Blue_Test () throws IOException{return  Vjt_Command(0x0306);}
    public int Test_End() throws IOException {
        return Vjt_Command(0x0309);
    }

    //  ---------------------
    public int Reset_System() throws IOException {
        return Vjt_Command(0x0400);
    }

    public int Black_screen_start() throws IOException {
        return Vjt_Command(0x0401);
    }

    public int Black_screen_end() throws IOException {
        return Vjt_Command(0x0402);
    }
//     public int Switch_Led_Off () throws IOException{return  Vjt_Command(0x0403);}
    //    public int Switch_Led_On () throws IOException{return  Vjt_Command(0x0404);}

    //     public int Play_Pause () throws IOException{return  Vjt_Command(0x0603);}
//    public int Play_Continue () throws IOException{return  Vjt_Command(0x0604);}
    private int Delete_file(String File) throws IOException {

        if (File.length() > 0) {

            //   byte[] tx_msg = new byte[File.length() + 22];
            byte[] tx_msg = new byte[1024];
            byte[] received = new byte[1024];
            tx_msg[0] = (byte) 0x55;
            tx_msg[1] = (byte) 0xa7;
//        tx_msg[2] = (byte) 0x00;
//        tx_msg[3] = (byte) 0x00;
            tx_msg[4] = (byte) 0x00;
            tx_msg[5] = (byte) 0x00;
            tx_msg[6] = (byte) (sourceaddr & 0xff);
            tx_msg[7] = (byte) ((sourceaddr >> 8) & 0xff);
            tx_msg[8] = (byte) (destaddr & 0xff);
            tx_msg[9] = (byte) ((destaddr >> 8) & 0xff);

            tx_msg[10] = (byte) (communication_counter & 0xff);
            tx_msg[11] = (byte) ((communication_counter >> 8) & 0xff);
            communication_counter++;
            tx_msg[12] = (byte) 0x07;
            tx_msg[13] = (byte) 0x06;
            tx_msg[14] = (byte) ((File.length() / 4) + 1);//0x04;
            tx_msg[15] = (byte) 0x00;
            int a = 0;
            int i;

            for (i = 0; i < File.length(); i++) {
                tx_msg[i + 16] = (byte) File.charAt(i);
            }
            for (; (i + 16) < 66; i++) {
                tx_msg[i + 16] = 0x00;
            }

            int msg_length = 36;
            //  msg_length = tx_msg.length;

            for (i = 4; i < msg_length; i++) {
                a += (((int) tx_msg[i]) & 0xff);
            }
            tx_msg[2] = (byte) (a & 0xff);
            tx_msg[3] = (byte) ((a >> 8) & 0xff);

            sck.setSoTimeout(Timeout);
            DatagramPacket request = new DatagramPacket(tx_msg, msg_length, IPaddress, UdpPort);
            sck.send(request);
            DatagramPacket response = new DatagramPacket(received, received.length);
            sck.receive(response);

            //    System.out.println("vjt_p2.nc_qw.Delete_file()>: " + received[16] );
            return received[16];
        }
        return -1;
    }

    private int Write_Pict(String Path, String Filename) throws IOException {
        byte[] picturefile = Files.readAllBytes(Paths.get(Path + Filename));
        byte[] tx_msg = new byte[1024];
        byte[] received = new byte[1024];
        int msg_length = 0;
        int a = 0;
        int ft;

        for (ft = 0; (picturefile.length > (ft * 0x300)); ft++) {
//            System.out.println("fap length : " + /*Integer.toHexString*/(fap.length) + "sefds" + ft + ' ' + Integer.toHexString(ft * 0x300));
            tx_msg[0] = (byte) 0x55;
            tx_msg[1] = (byte) 0xa7;

            tx_msg[2] = (byte) 0x00;  //crc/chksum
            tx_msg[3] = (byte) 0x00;

            if (picturefile.length - (0x300 * ft) >= 0x300) {
                tx_msg[4] = (byte) 0x00;
                tx_msg[5] = (byte) 0x03;
            } else {

                tx_msg[4] = (byte) ((picturefile.length - (0x300 * ft)) & 0xff);
                tx_msg[5] = (byte) (((picturefile.length - (0x300 * ft)) >> 8) & 0xff);
            }

            tx_msg[6] = (byte) (sourceaddr & 0xff);
            tx_msg[7] = (byte) ((sourceaddr >> 8) & 0xff);
            tx_msg[8] = (byte) (destaddr & 0xff);
            tx_msg[9] = (byte) ((destaddr >> 8) & 0xff);

            tx_msg[10] = (byte) (communication_counter & 0xff);
            tx_msg[11] = (byte) ((communication_counter >> 8) & 0xff);
            communication_counter++;

            tx_msg[12] = (byte) 0x02;
            tx_msg[13] = (byte) 0x06;     //send picture file

            tx_msg[14] = (byte) 0x06;    //6x4byte   arg length
            tx_msg[15] = (byte) 0x00;   //noflag

            tx_msg[16] = (byte) 'D';  //filename char
            tx_msg[17] = (byte) 0x00;  //filename char

            for (int i = 0; i < 12; i++) {
                if (i < Filename.length()) {
                    tx_msg[18 + i] = (byte) Filename.charAt(i);
                } else {
                    tx_msg[18 + i] = 0x00;
                }
            }

            tx_msg[30] = (byte) (picturefile.length & 0xff);  //file size 0xff
            tx_msg[31] = (byte) ((picturefile.length >> 8) & 0xff);
            tx_msg[32] = (byte) ((picturefile.length >> 16) & 0xff);
            tx_msg[33] = (byte) ((picturefile.length >> 24) & 0xff);

            tx_msg[34] = (byte) 0x00;  //
            tx_msg[35] = (byte) 0x03;  //  size pocket

            tx_msg[36] = (byte) ((((picturefile.length >> 8) / 3) + 1) & 0xff);  //  no. sum  packet.  1 pcket ft max
            tx_msg[37] = (byte) (((((picturefile.length >> 8) / 3) + 1) >> 8) & 0xff);  //  no. packet >> 8

            tx_msg[38] = (byte) ((ft + 1) & 0xff);  //  no. current packet.  1 pcket
            tx_msg[39] = (byte) ((((ft + 1)) >> 8) & 0xff);  //  no. packet >> 8

            msg_length = 40;

            for (a = (ft * 0x300); ((a < picturefile.length) && ((a < (ft + 1) * 0x300))); a++) {
                tx_msg[msg_length++] = (byte) picturefile[a];
            }

            a = 0;
            for (int j = 4; j < msg_length; j++) {
                a += (int) (((int) tx_msg[j]) & 0xff);
            }
            tx_msg[2] = (byte) (a & 0xff);
            tx_msg[3] = (byte) ((a >> 8) & 0xff);

            received[16] = 0x02;
            sck.setSoTimeout(Timeout);
            DatagramPacket request = new DatagramPacket(tx_msg, msg_length, IPaddress, UdpPort);
            sck.send(request);

            DatagramPacket response = new DatagramPacket(received, received.length);
            sck.receive(response);

        }

        System.out.println("write : " + Filename + " :" + Integer.toHexString(received[16]));

        return 0;
    }

    public int Write_NMG(String Path, String Filename) throws IOException {
        byte[] picturefile = Files.readAllBytes(Paths.get(Path + Filename));
        byte[] tx_msg = new byte[1024];
        byte[] received = new byte[1024];
        int msg_length = 0;
        int a = 0;
        int ft;

        for (ft = 0; (picturefile.length > (ft * 0x300)); ft++) {
//            System.out.println("fap length : " + /*Integer.toHexString*/(fap.length) + "sefds" + ft + ' ' + Integer.toHexString(ft * 0x300));
            tx_msg[0] = (byte) 0x55;
            tx_msg[1] = (byte) 0xa7;

            tx_msg[2] = (byte) 0x00;  //crc/chksum
            tx_msg[3] = (byte) 0x00;

            if (picturefile.length - (0x300 * ft) >= 0x300) {
                tx_msg[4] = (byte) 0x00;
                tx_msg[5] = (byte) 0x03;
            } else {

                tx_msg[4] = (byte) ((picturefile.length - (0x300 * ft)) & 0xff);
                tx_msg[5] = (byte) (((picturefile.length - (0x300 * ft)) >> 8) & 0xff);
            }

            tx_msg[6] = (byte) (sourceaddr & 0xff);
            tx_msg[7] = (byte) ((sourceaddr >> 8) & 0xff);
            tx_msg[8] = (byte) (destaddr & 0xff);
            tx_msg[9] = (byte) ((destaddr >> 8) & 0xff);

            tx_msg[10] = (byte) (communication_counter & 0xff);
            tx_msg[11] = (byte) ((communication_counter >> 8) & 0xff);
            communication_counter++;

            tx_msg[12] = (byte) 0x02;
            tx_msg[13] = (byte) 0x04;     //send text file  textfile  > *.nmg

            tx_msg[14] = (byte) 0x06;    //6x4byte   arg length
            tx_msg[15] = (byte) 0x00;   //noflag

            tx_msg[16] = (byte) 'D';  //filename char
            tx_msg[17] = (byte) 0x00;  //filename char

            for (int i = 0; i < 12; i++) {
                if (i < Filename.length()) {
                    tx_msg[18 + i] = (byte) Filename.charAt(i);
                } else {
                    tx_msg[18 + i] = 0x00;
                }
            }

            tx_msg[30] = (byte) (picturefile.length & 0xff);  //file size 0xff
            tx_msg[31] = (byte) ((picturefile.length >> 8) & 0xff);
            tx_msg[32] = (byte) ((picturefile.length >> 16) & 0xff);
            tx_msg[33] = (byte) ((picturefile.length >> 24) & 0xff);

            tx_msg[34] = (byte) 0x00;  //
            tx_msg[35] = (byte) 0x03;  //  size pocket

            tx_msg[36] = (byte) ((((picturefile.length >> 8) / 3) + 1) & 0xff);  //  no. sum  packet.  1 pcket ft max
            tx_msg[37] = (byte) (((((picturefile.length >> 8) / 3) + 1) >> 8) & 0xff);  //  no. packet >> 8

            tx_msg[38] = (byte) ((ft + 1) & 0xff);  //  no. current packet.  1 pcket
            tx_msg[39] = (byte) ((((ft + 1)) >> 8) & 0xff);  //  no. packet >> 8

            msg_length = 40;

            for (a = (ft * 0x300); ((a < picturefile.length) && ((a < (ft + 1) * 0x300))); a++) {
                tx_msg[msg_length++] = (byte) picturefile[a];
            }

            a = 0;
            for (int j = 4; j < msg_length; j++) {
                a += (int) (((int) tx_msg[j]) & 0xff);
            }
            tx_msg[2] = (byte) (a & 0xff);
            tx_msg[3] = (byte) ((a >> 8) & 0xff);

            received[16] = 0x02;
            sck.setSoTimeout(Timeout);
            DatagramPacket request = new DatagramPacket(tx_msg, msg_length, IPaddress, UdpPort);
            sck.send(request);

            DatagramPacket response = new DatagramPacket(received, received.length);
            sck.receive(response);

        }

        System.out.println("write nmg : " + Filename + " :" + Integer.toHexString(received[16]));

        return 0;
    }



    public int Write_StringFile(Byte Textfilename, String Text) throws IOException {

        byte[] tx_msg = new byte[1024];
        byte[] received = new byte[1024];
        int msg_length = 0;
        int a = 0;

        tx_msg[0] = (byte) 0x55;
        tx_msg[1] = (byte) 0xa7;

        tx_msg[2] = (byte) 0x00;  //crc/chksum
        tx_msg[3] = (byte) 0x00;

        tx_msg[4] = (byte) ((Text.length()) & 0xff);
        tx_msg[5] = (byte) (((Text.length()) >> 8) & 0xff);

        tx_msg[6] = (byte) (sourceaddr & 0xff);
        tx_msg[7] = (byte) ((sourceaddr >> 8) & 0xff);
        tx_msg[8] = (byte) (destaddr & 0xff);
        tx_msg[9] = (byte) ((destaddr >> 8) & 0xff);

        tx_msg[10] = (byte) (communication_counter & 0xff);
        tx_msg[11] = (byte) ((communication_counter >> 8) & 0xff);
        communication_counter++;

        tx_msg[12] = (byte) 0x02;
        tx_msg[13] = (byte) 0x05;     //send string file //04text/nmg   05string

        tx_msg[14] = (byte) 0x06;    //6x4byte   arg length
        tx_msg[15] = (byte) 0x00;   //noflag

        tx_msg[16] = (byte) 'D';  //filename char
        tx_msg[17] = (byte) 0x00;  //filename char

        tx_msg[18] = (byte) Textfilename;  //filename char
        for (int i = 0; i < 11; i++) {tx_msg[19 + i] = 0x00; }

        tx_msg[30] = (byte) ((Text.length()) & 0xff);  //file size 0xff
        tx_msg[31] = (byte) (((Text.length()) >> 8) & 0xff);
        tx_msg[32] = (byte) (((Text.length()) >> 16) & 0xff);
        tx_msg[33] = (byte) (((Text.length()) >> 24) & 0xff);

        tx_msg[34] = (byte) 0x00;  //
        tx_msg[35] = (byte) 0x03;  //  size pocket

        tx_msg[36] = (byte) (((((Text.length()) >> 8) / 3) + 1) & 0xff);  //  no. sum  packet.  1 pcket ft max
        tx_msg[37] = (byte) ((((((Text.length()) >> 8) / 3) + 1) >> 8) & 0xff);  //  no. packet >> 8

        tx_msg[38] = (byte) 0x01;  //  no. current packet.  1 pcket
        tx_msg[39] = (byte) 0x00;  //  no. packet >> 8

        msg_length = 40;

        for (a = 0; ((a < (Text.length())) && ((a < 32))); a++) {
            tx_msg[msg_length++] = (byte) Text.charAt(a);
        }



        a = 0;
        for (int j = 4; j < msg_length; j++) {
            a += (int) (((int) tx_msg[j]) & 0xff);
        }
        tx_msg[2] = (byte) (a & 0xff);
        tx_msg[3] = (byte) ((a >> 8) & 0xff);

        received[16] = 0x02;
        sck.setSoTimeout(Timeout);
        DatagramPacket request = new DatagramPacket(tx_msg, msg_length, IPaddress, UdpPort);
        sck.send(request);

        DatagramPacket response = new DatagramPacket(received, received.length);
        sck.receive(response);

        System.out.println("write : " + Text + " :" + Integer.toHexString(received[16]));

        return 0;
    }


    //   private int Check_Pict(String File){return 0;}
//private int Read_Pict(String File){return 0;}
    private int Rewrie_sequence_Pict(String Filename) throws IOException {
        int r = Delete_file("C:\\SEQUENT.SYS");
        System.out.println("del C:\\SEQUENT.SYS :" + r);

        byte[] tx_msg = new byte[1024];
        byte[] received = new byte[1024];
        tx_msg[0] = (byte) 0x55;
        tx_msg[1] = (byte) 0xa7;
//        tx_msg[2] = (byte) 0x00;
//        tx_msg[3] = (byte) 0x00;
        tx_msg[4] = (byte) 44; //sequent_length; //0x00;
        tx_msg[5] = (byte) 0x00;
        tx_msg[6] = (byte) (sourceaddr & 0xff);
        tx_msg[7] = (byte) ((sourceaddr >> 8) & 0xff);
        tx_msg[8] = (byte) (destaddr & 0xff);
        tx_msg[9] = (byte) ((destaddr >> 8) & 0xff);

        tx_msg[10] = (byte) (communication_counter & 0xff);
        tx_msg[11] = (byte) ((communication_counter >> 8) & 0xff);
        communication_counter++;
        tx_msg[12] = (byte) 0x02;
        tx_msg[13] = (byte) 0x02;
        tx_msg[14] = (byte) 0x06;
        tx_msg[15] = (byte) 0x00;

        tx_msg[16] = (byte) 'S';  //filename char
        tx_msg[17] = (byte) 'E';  //filename char
        tx_msg[18] = (byte) 'Q';  //filename char
        tx_msg[19] = (byte) 'U';  //filename char
        tx_msg[20] = (byte) 'E';  //filename char
        tx_msg[21] = (byte) 'N';  //filename char
        tx_msg[22] = (byte) 'T';  //filename char
        tx_msg[23] = (byte) '.';  //filename char
        tx_msg[24] = (byte) 'S';  //filename char
        tx_msg[25] = (byte) 'Y';  //filename char
        tx_msg[26] = (byte) 'S';  //filename char
        tx_msg[27] = (byte) 0x00;  //filename char

        tx_msg[28] = (byte) 44;  //file size 0xff

        tx_msg[29] = (byte) 0x00;  //file size >>8 &0xff
        tx_msg[30] = (byte) 0x00;  //filename char  >>16
        tx_msg[31] = (byte) 0x00;  //filename char  >>24

        tx_msg[32] = (byte) 0x2c;  //fa.length;  //packet size
        tx_msg[33] = (byte) 0x00; // 0x00 //ps >>8

        tx_msg[34] = (byte) 0x01;  // sum no. packet.  1 pcket
        tx_msg[35] = (byte) 0x00;  // sum no. packet >>

        tx_msg[36] = (byte) 0x01;  //  no. packet.  1 pcket
        tx_msg[37] = (byte) 0x00;  //  no. packet >>

        tx_msg[38] = (byte) 0x00;  //  2 byte none
        tx_msg[39] = (byte) 0x00;  //  2 byte none

        tx_msg[40] = 'S';
        tx_msg[41] = 'Q';
        tx_msg[42] = 0x04;
        tx_msg[43] = 0x00;
        tx_msg[44] = 0x01; //no. of file
        tx_msg[45] = 0x00;
        tx_msg[46] = 0x00;
        tx_msg[47] = 0x00;

        tx_msg[48] = 'D';
        tx_msg[49] = 'P';
        tx_msg[50] = 0x0f;
        tx_msg[51] = 0x7f;// //week repeat

        tx_msg[52] = 0x08;  // year  word //begin date .. end date  ..dw shk dw siye  filename
        tx_msg[53] = 0x20;
        tx_msg[54] = 0x01; // januar
        tx_msg[55] = 0x01; // 1. day
        tx_msg[56] = 0x01; //hour
        tx_msg[57] = 0x01; //min
        tx_msg[58] = 0x01; // date valid
        tx_msg[59] = 0x01; //time valid

        tx_msg[60] = 0x08;  // year  word /end date  ..dw shk dw siye  filename
        tx_msg[61] = 0x20;
        tx_msg[62] = 0x01; // januar
        tx_msg[63] = 0x01; // 1. day
        tx_msg[64] = 0x23; //hour
        tx_msg[65] = 0x59; //min
        tx_msg[66] = 0x01; // date valid
        tx_msg[67] = 0x01; //time valid

        tx_msg[68] = (byte) 0xb6; //chk
        tx_msg[69] = 0x01; //chk

        //   tx_msg[68] = (byte) 0x46; //chk
        //   tx_msg[69] = (byte) 0xe1; //chk
        tx_msg[70] = 0x00; //size
        tx_msg[71] = 0x00; // size

        //filename
        int i;
        for (i = 0; ((i < Filename.length() && (i < 12))); i++) {
            tx_msg[72 + i] = (byte) Filename.charAt(i);
        }
        for (; i < 12; i++) {
            tx_msg[72 + i] = 0x00;
        }

        int msg_length = 84;

        int a = 0;
        for (i = 4; i < msg_length; i++) {
            a += (((int) tx_msg[i]) & 0xff);
        }
        tx_msg[2] = (byte) (a & 0xff);
        tx_msg[3] = (byte) ((a >> 8) & 0xff);

        sck.setSoTimeout(Timeout);
        DatagramPacket request = new DatagramPacket(tx_msg, msg_length, IPaddress, UdpPort);
        sck.send(request);
        DatagramPacket response = new DatagramPacket(received, received.length);
        sck.receive(response);

        //        System.out.println("vjt_p2.nc_qw.rewrite seq _file()>: " + received[16] );
        return received[16];
    }

    public int Rewrie_sequence_NMG(String Filename) throws IOException {
        int r = Delete_file("C:\\sequent.sys");
        System.out.println("del C:\\SEQUENT.SYS :" + r);

        byte[] tx_msg = new byte[1024];
        byte[] received = new byte[1024];
        tx_msg[0] = (byte) 0x55;
        tx_msg[1] = (byte) 0xa7;
//        tx_msg[2] = (byte) 0x00;
//        tx_msg[3] = (byte) 0x00;
        tx_msg[4] = (byte) 44; //sequent_length; //0x00;
        tx_msg[5] = (byte) 0x00;
        tx_msg[6] = (byte) (sourceaddr & 0xff);
        tx_msg[7] = (byte) ((sourceaddr >> 8) & 0xff);
        tx_msg[8] = (byte) (destaddr & 0xff);
        tx_msg[9] = (byte) ((destaddr >> 8) & 0xff);

        tx_msg[10] = (byte) (communication_counter & 0xff);
        tx_msg[11] = (byte) ((communication_counter >> 8) & 0xff);
        communication_counter++;
        tx_msg[12] = (byte) 0x02;
        tx_msg[13] = (byte) 0x02;
        tx_msg[14] = (byte) 0x06;
        tx_msg[15] = (byte) 0x00;

        tx_msg[16] = (byte) 'S';  //filename char
        tx_msg[17] = (byte) 'E';  //filename char
        tx_msg[18] = (byte) 'Q';  //filename char
        tx_msg[19] = (byte) 'U';  //filename char
        tx_msg[20] = (byte) 'E';  //filename char
        tx_msg[21] = (byte) 'N';  //filename char
        tx_msg[22] = (byte) 'T';  //filename char
        tx_msg[23] = (byte) '.';  //filename char
        tx_msg[24] = (byte) 'S';  //filename char
        tx_msg[25] = (byte) 'Y';  //filename char
        tx_msg[26] = (byte) 'S';  //filename char
        tx_msg[27] = (byte) 0x00;  //filename char

        tx_msg[28] = (byte) 44;  //file size 0xff

        tx_msg[29] = (byte) 0x00;  //file size >>8 &0xff
        tx_msg[30] = (byte) 0x00;  //filename char  >>16
        tx_msg[31] = (byte) 0x00;  //filename char  >>24

        tx_msg[32] = (byte) 0x2c;  //fa.length;  //packet size
        tx_msg[33] = (byte) 0x00; // 0x00 //ps >>8

        tx_msg[34] = (byte) 0x01;  // sum no. packet.  1 pcket
        tx_msg[35] = (byte) 0x00;  // sum no. packet >>

        tx_msg[36] = (byte) 0x01;  //  no. packet.  1 pcket
        tx_msg[37] = (byte) 0x00;  //  no. packet >>

        tx_msg[38] = (byte) 0x00;  //  2 byte none
        tx_msg[39] = (byte) 0x00;  //  2 byte none

        tx_msg[40] = 'S';
        tx_msg[41] = 'Q';
        tx_msg[42] = 0x04;
        tx_msg[43] = 0x00;
        tx_msg[44] = 0x01; //no. of file
        tx_msg[45] = 0x00;
        tx_msg[46] = 0x00;
        tx_msg[47] = 0x00;

        tx_msg[48] = 'D';
        tx_msg[49] = 't';
        tx_msg[50] = 0x0f;
        tx_msg[51] = 0x7f;// //week repeat

        tx_msg[52] = 0x08;  // year  word //begin date .. end date  ..dw shk dw siye  filename
        tx_msg[53] = 0x20;
        tx_msg[54] = 0x01; // januar
        tx_msg[55] = 0x01; // 1. day
        tx_msg[56] = 0x01; //hour
        tx_msg[57] = 0x01; //min
        tx_msg[58] = 0x01; // date valid
        tx_msg[59] = 0x01; //time valid

        tx_msg[60] = 0x08;  // year  word /end date  ..dw shk dw siye  filename
        tx_msg[61] = 0x20;
        tx_msg[62] = 0x01; // januar
        tx_msg[63] = 0x01; // 1. day
        tx_msg[64] = 0x23; //hour
        tx_msg[65] = 0x59; //min
        tx_msg[66] = 0x01; // date valid
        tx_msg[67] = 0x01; //time valid

        tx_msg[68] = (byte) 0xb6; //chk
        tx_msg[69] = 0x01; //chk

        //   tx_msg[68] = (byte) 0x46; //chk
        //   tx_msg[69] = (byte) 0xe1; //chk
        tx_msg[70] = 0x00; //size
        tx_msg[71] = 0x00; // size

        //filename
        int i;
        for (i = 0; ((i < Filename.length() && (i < 12))); i++) {
            tx_msg[72 + i] = (byte) Filename.charAt(i);
        }
        for (; i < 12; i++) {
            tx_msg[72 + i] = 0x00;
        }

        int msg_length = 84;

        int a = 0;
        for (i = 4; i < msg_length; i++) {
            a += (((int) tx_msg[i]) & 0xff);
        }
        tx_msg[2] = (byte) (a & 0xff);
        tx_msg[3] = (byte) ((a >> 8) & 0xff);

        sck.setSoTimeout(Timeout);
        DatagramPacket request = new DatagramPacket(tx_msg, msg_length, IPaddress, UdpPort);
        sck.send(request);
        DatagramPacket response = new DatagramPacket(received, received.length);
        sck.receive(response);

        //        System.out.println("vjt_p2.nc_qw.rewrite seq _file()>: " + received[16] );
        return received[16];
    }

    public int Display_New_Pict(String Path, String Filename) throws IOException {
        int retval = 0;
        if (Filename.length() <= 12) {
            Black_screen_start();

            Delete_file("D:\\p\\" + Filename);
            Write_Pict(Path, Filename);

            Rewrie_sequence_Pict(Filename);
            Black_screen_end();
//        Play_Continue();
            System.out.println("Display_New_Pict : " + Filename);
        } else {
            retval = -1;
        }
        return retval;
    }

    public int Display_Pict(String Path, String Filename) throws IOException {
        int retval = 0;
        if (Filename.length() <= 12) {
            Black_screen_start();
            Rewrie_sequence_Pict(Filename);
            Black_screen_end();
//        Play_Continue();
            System.out.println("Display_Pict : " + Filename);
        } else {
            retval = -1;
        }
        return retval;
    }

    public int Display_NMG(String Filename) throws IOException {
        int retval = 0;
        if (Filename.length() <= 12) {
            Black_screen_start();

            Rewrie_sequence_NMG(Filename);

            Black_screen_end();
//        Play_Continue();
            System.out.println("Display_Nmg : " + Filename);
        } else {
            retval = -1;
        }
        return retval;   }


    /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */
    /* MultiZone hegesztes*/


    /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */


    public int Write_MultiZone_Bmp_file (String File, int PictNo) throws IOException {
        byte[] picturefile = Files.readAllBytes(Paths.get(File + ".bmp"));
        byte[] tx_msg = new byte[1024];
        byte[] received = new byte[1024];
        int msg_length = 0;
        int a = 0;
        int ft;

        for (ft = 0; (picturefile.length > (ft * 0x300)); ft++) {
//            System.out.println("fap length : " + /*Integer.toHexString*/(fap.length) + "sefds" + ft + ' ' + Integer.toHexString(ft * 0x300));
            tx_msg[0] = (byte) 0x55;
            tx_msg[1] = (byte) 0xa7;

            tx_msg[2] = (byte) 0x00;  //crc/chksum
            tx_msg[3] = (byte) 0x00;

            if (picturefile.length - (0x300 * ft) >= 0x300) {
                tx_msg[4] = (byte) 0x00;
                tx_msg[5] = (byte) 0x03;
            } else {

                tx_msg[4] = (byte) ((picturefile.length - (0x300 * ft)) & 0xff);
                tx_msg[5] = (byte) (((picturefile.length - (0x300 * ft)) >> 8) & 0xff);
            }

            tx_msg[6] = (byte) (sourceaddr & 0xff);
            tx_msg[7] = (byte) ((sourceaddr >> 8) & 0xff);
            tx_msg[8] = (byte) (destaddr & 0xff);
            tx_msg[9] = (byte) ((destaddr >> 8) & 0xff);

            tx_msg[10] = (byte) (communication_counter & 0xff);
            tx_msg[11] = (byte) ((communication_counter >> 8) & 0xff);
            communication_counter++;

            tx_msg[12] = (byte) 0x02;
            tx_msg[13] = (byte) 0x08;     //write file

            tx_msg[14] = (byte) 0x06;    //6x4byte   arg length
            tx_msg[15] = (byte) 0x00;   //noflag

            tx_msg[16] = (byte) 'D';  //filename char
            tx_msg[17] = (byte) ':';  //filename char
            tx_msg[18] = (byte) '\\';  //filename char
            tx_msg[19] = (byte) 'P';  //filename char
            tx_msg[20] = (byte) '\\';  //filename char



/*            for (int i = 0; i < 12; i++) {
                if (i < Filename.length()) {
                    tx_msg[18 + i] = (byte) Filename.charAt(i);
                } else {
                    tx_msg[18 + i] = 0x00;
                }
            }*/

            tx_msg[21] = (byte) ('0'+((PictNo/10)));
            tx_msg[22] = (byte) ('0'+(PictNo%10));
            tx_msg[23] = (byte) '.';  //filename char

            tx_msg[24] = (byte) 'b';  //filename char
            tx_msg[25] = (byte) 'm';  //filename char
            tx_msg[26] = (byte) 'p';  //filename char


            tx_msg[27] = (byte) 0x00;
            tx_msg[28] = (byte) 0x00;
            tx_msg[29] = (byte) 0x00;
            //     tx_msg[30] = (byte) 0x00;






            tx_msg[30] = (byte) (picturefile.length & 0xff);  //file size 0xff
            tx_msg[31] = (byte) ((picturefile.length >> 8) & 0xff);
            tx_msg[32] = (byte) ((picturefile.length >> 16) & 0xff);
            tx_msg[33] = (byte) ((picturefile.length >> 24) & 0xff);

            tx_msg[34] = (byte) 0x00;  //
            tx_msg[35] = (byte) 0x03;  //  size pocket

            tx_msg[36] = (byte) ((((picturefile.length >> 8) / 3) + 1) & 0xff);  //  no. sum  packet.  1 pcket ft max
            tx_msg[37] = (byte) (((((picturefile.length >> 8) / 3) + 1) >> 8) & 0xff);  //  no. packet >> 8

            tx_msg[38] = (byte) ((ft + 1) & 0xff);  //  no. current packet.  1 pcket
            tx_msg[39] = (byte) ((((ft + 1)) >> 8) & 0xff);  //  no. packet >> 8

            msg_length = 40;

            for (a = (ft * 0x300); ((a < picturefile.length) && ((a < (ft + 1) * 0x300))); a++) {
                tx_msg[msg_length++] = (byte) picturefile[a];
            }

            a = 0;
            for (int j = 4; j < msg_length; j++) {
                a += (int) (((int) tx_msg[j]) & 0xff);
            }
            tx_msg[2] = (byte) (a & 0xff);
            tx_msg[3] = (byte) ((a >> 8) & 0xff);

            received[16] = 0x02;
            sck.setSoTimeout(Timeout);
            DatagramPacket request = new DatagramPacket(tx_msg, msg_length, IPaddress, UdpPort);
            sck.send(request);

            DatagramPacket response = new DatagramPacket(received, received.length);
            sck.receive(response);

        }

        System.out.println("write : " + PictNo + ".bmp :" + Integer.toHexString(received[16]));

        return 0;
    }


    public int Write_MultiZone_Bmp_pict (String File, int PictNo) throws IOException {
        byte[] picturefile = Files.readAllBytes(Paths.get(File + ".bmp"));
        byte[] tx_msg = new byte[1024];
        byte[] received = new byte[1024];
        int msg_length = 0;
        int a = 0;
        int ft;

        for (ft = 0; (picturefile.length > (ft * 0x300)); ft++) {
//            System.out.println("fap length : " + /*Integer.toHexString*/(fap.length) + "sefds" + ft + ' ' + Integer.toHexString(ft * 0x300));
            tx_msg[0] = (byte) 0x55;
            tx_msg[1] = (byte) 0xa7;

            tx_msg[2] = (byte) 0x00;  //crc/chksum
            tx_msg[3] = (byte) 0x00;

            if (picturefile.length - (0x300 * ft) >= 0x300) {
                tx_msg[4] = (byte) 0x00;
                tx_msg[5] = (byte) 0x03;
            } else {

                tx_msg[4] = (byte) ((picturefile.length - (0x300 * ft)) & 0xff);
                tx_msg[5] = (byte) (((picturefile.length - (0x300 * ft)) >> 8) & 0xff);
            }

            tx_msg[6] = (byte) (sourceaddr & 0xff);
            tx_msg[7] = (byte) ((sourceaddr >> 8) & 0xff);
            tx_msg[8] = (byte) (destaddr & 0xff);
            tx_msg[9] = (byte) ((destaddr >> 8) & 0xff);

            tx_msg[10] = (byte) (communication_counter & 0xff);
            tx_msg[11] = (byte) ((communication_counter >> 8) & 0xff);
            communication_counter++;

            tx_msg[12] = (byte) 0x02;
            tx_msg[13] = (byte) 0x06;     //send pict file

            tx_msg[14] = (byte) 0x06;    //6x4byte   arg length
            tx_msg[15] = (byte) 0x00;   //noflag

            tx_msg[16] = (byte) 'D';  //filename char
            tx_msg[17] = (byte) 0x00;  //filename char



/*            for (int i = 0; i < 12; i++) {
                if (i < Filename.length()) {
                    tx_msg[18 + i] = (byte) Filename.charAt(i);
                } else {
                    tx_msg[18 + i] = 0x00;
                }
            }*/

            tx_msg[18] = (byte) ('0'+((PictNo/10)));
            tx_msg[19] = (byte) ('0'+(PictNo%10));
            tx_msg[20] = (byte) '.';  //filename char
            tx_msg[21] = (byte) 'b';  //filename char
            tx_msg[22] = (byte) 'm';  //filename char
            tx_msg[23] = (byte) 'p';  //filename char

            //           tx_msg[18] = (byte) '\\';  //filename char
            //     tx_msg[19] = (byte) 'P';  //filename char
            //    tx_msg[20] = (byte) '\\';  //filename char

            tx_msg[24] = (byte) 0x00;
            tx_msg[25] = (byte) 0x00;
            tx_msg[26] = (byte) 0x00;

            tx_msg[27] = (byte) 0x00;
            tx_msg[28] = (byte) 0x00;
            tx_msg[29] = (byte) 0x00;
            //     tx_msg[30] = (byte) 0x00;






            tx_msg[30] = (byte) (picturefile.length & 0xff);  //file size 0xff
            tx_msg[31] = (byte) ((picturefile.length >> 8) & 0xff);
            tx_msg[32] = (byte) ((picturefile.length >> 16) & 0xff);
            tx_msg[33] = (byte) ((picturefile.length >> 24) & 0xff);

            tx_msg[34] = (byte) 0x00;  //
            tx_msg[35] = (byte) 0x03;  //  size pocket

            tx_msg[36] = (byte) ((((picturefile.length >> 8) / 3) + 1) & 0xff);  //  no. sum  packet.  1 pcket ft max
            tx_msg[37] = (byte) (((((picturefile.length >> 8) / 3) + 1) >> 8) & 0xff);  //  no. packet >> 8

            tx_msg[38] = (byte) ((ft + 1) & 0xff);  //  no. current packet.  1 pcket
            tx_msg[39] = (byte) ((((ft + 1)) >> 8) & 0xff);  //  no. packet >> 8

            msg_length = 40;

            for (a = (ft * 0x300); ((a < picturefile.length) && ((a < (ft + 1) * 0x300))); a++) {
                tx_msg[msg_length++] = (byte) picturefile[a];
            }

            a = 0;
            for (int j = 4; j < msg_length; j++) {
                a += (int) (((int) tx_msg[j]) & 0xff);
            }
            tx_msg[2] = (byte) (a & 0xff);
            tx_msg[3] = (byte) ((a >> 8) & 0xff);

            received[16] = 0x02;
            sck.setSoTimeout(Timeout);
            DatagramPacket request = new DatagramPacket(tx_msg, msg_length, IPaddress, UdpPort);
            sck.send(request);

            DatagramPacket response = new DatagramPacket(received, received.length);
            sck.receive(response);

        }

        System.out.println("write : " + PictNo + ".bmp :" + Integer.toHexString(received[16]));

        return 0;
    }




    public int Write_MultiZone_Text(int TextNo, String Text) throws IOException {

        return Write_MultiZone_Text(TextNo, Text, Color.yellow, Color.black);
    }



    public int Write_MultiZone_Text(int TextNo, String Text,Color Textcolor,Color Backcolor) throws IOException {


        MultiZone_elements=2;

        byte[] tx_msg = new byte[1024];
        byte[] received = new byte[1024];
        int msg_length = 0;
        int a = 0;

        tx_msg[0] = (byte) 0x55;
        tx_msg[1] = (byte) 0xa7;

        tx_msg[2] = (byte) 0x00;  //crc/chksum
        tx_msg[3] = (byte) 0x00;

        tx_msg[4] = (byte) ((Text.length()+0x20  ) & 0xff);
        tx_msg[5] = (byte) (((Text.length()+0x20) >> 8) & 0xff);

        tx_msg[6] = (byte) (sourceaddr & 0xff);
        tx_msg[7] = (byte) ((sourceaddr >> 8) & 0xff);
        tx_msg[8] = (byte) (destaddr & 0xff);
        tx_msg[9] = (byte) ((destaddr >> 8) & 0xff);

        tx_msg[10] = (byte) (communication_counter & 0xff);
        tx_msg[11] = (byte) ((communication_counter >> 8) & 0xff);
        communication_counter++;

        tx_msg[12] = (byte) 0x36;
        tx_msg[13] = (byte) 0x03;     //text hegesztes

        tx_msg[14] = (byte) 0x01;    //from wireshark
        tx_msg[15] = (byte) 0x00;   //
        tx_msg[16] = (byte) 0x01;  //

        tx_msg[17] = (byte) MultiZone_elements;  // text mez�k sz�ma �sszesen
//        tx_msg[17] = (byte) 0x03;  // zonak sz�ma �sszesen

        tx_msg[18] = (byte) 0x01;  //
        tx_msg[19] = (byte) 0x00;  //
        tx_msg[20] = (byte) 0x00;  //
        tx_msg[21] = (byte) 0x08;  //
        tx_msg[22] = (byte) (TextNo&0x3f);  //
        tx_msg[23] = (byte) 0x00;  //
        tx_msg[24] = (byte) 0x00;  //

        tx_msg[25] = (byte) 0x00;  //
        tx_msg[26] = (byte) 0x00;  //
        tx_msg[27] = (byte) 0x00;  //

        tx_msg[28] = (byte) 0x7f;  //
        tx_msg[29] = (byte) 0x80;  //
        tx_msg[30] = (byte) 0x00;  //


        tx_msg[31] = (byte) 0x00;  //
        tx_msg[32] = (byte) 0x00;  //
        tx_msg[33] = (byte) 0x80;  //


        tx_msg[28] = (byte) (Textcolor.getRed()&0xff);  //
        tx_msg[29] = (byte) (Textcolor.getGreen()&0xff)     ;  //
        tx_msg[30] = (byte) (Textcolor.getBlue()&0xff);

        tx_msg[31] = (byte) (Backcolor.getRed()&0xff);  //
        tx_msg[32] = (byte) (Backcolor.getGreen()&0xff);  //
        tx_msg[33] = (byte) (Backcolor.getBlue()&0xff);  //





        tx_msg[34] = (byte) 0x00;  //
        tx_msg[35] = (byte) 0x00;  //
        tx_msg[36] = (byte) 0x01;  //
        tx_msg[37] = (byte) 0x00;  //
        tx_msg[38] = (byte) 0x00;  //
        tx_msg[39] = (byte) 0x00;  //
        tx_msg[40] = (byte) 0x3a;  //
        tx_msg[41] = (byte) 0x01;  //
        tx_msg[42] = (byte) 0x01;  //
        tx_msg[43] = (byte) 0x00;  //
        tx_msg[44] = (byte) 0x32;  //
        tx_msg[45] = (byte) 0x00;  //
        tx_msg[46] = (byte) 0x32;  //
        tx_msg[47] = (byte) 0x00;  //
        tx_msg[48] = (byte) Text.length();  //
        tx_msg[49] = (byte) 0x00;  //
        tx_msg[50] = (byte) 0x00;  //
        tx_msg[51] = (byte) 0x00;  //



        msg_length = 52;

        for (a = 0; ((a < (Text.length())) && ((a < 32))); a++) {
            tx_msg[msg_length++] = (byte) Text.charAt(a);
        }



        a = 0;
        for (int j = 4; j < msg_length; j++) {
            a += (int) (((int) tx_msg[j]) & 0xff);
        }
        tx_msg[2] = (byte) (a & 0xff);
        tx_msg[3] = (byte) ((a >> 8) & 0xff);

        received[16] = 0x02;
        sck.setSoTimeout(Timeout);
        DatagramPacket request = new DatagramPacket(tx_msg, msg_length, IPaddress, UdpPort);
        sck.send(request);

        DatagramPacket response = new DatagramPacket(received, received.length);
        sck.receive(response);

        System.out.println("write : " + Text + " :" + Integer.toHexString(received[16]));

        return 0;
    }

    public int Write_MultiZone_Pict(int PictNo, int zoneNo, Color Backcolor ) throws IOException {

        MultiZone_elements=2;

        byte[] tx_msg = new byte[1024];
        byte[] received = new byte[1024];
        int msg_length = 0;
        int a = 0;

        tx_msg[0] = (byte) 0x55;
        tx_msg[1] = (byte) 0xa7;

        tx_msg[2] = (byte) 0x00;  //crc/chksum
        tx_msg[3] = (byte) 0x00;

        tx_msg[4] = (byte) 0x23;//((Text.length()+0x20  ) & 0xff);
        tx_msg[5] = (byte) 0x00;//(((Text.length()+0x20) >> 8) & 0xff);

        tx_msg[6] = (byte) (sourceaddr & 0xff);
        tx_msg[7] = (byte) ((sourceaddr >> 8) & 0xff);
        tx_msg[8] = (byte) (destaddr & 0xff);
        tx_msg[9] = (byte) ((destaddr >> 8) & 0xff);

        tx_msg[10] = (byte) (communication_counter & 0xff);
        tx_msg[11] = (byte) ((communication_counter >> 8) & 0xff);
        communication_counter++;

        tx_msg[12] = (byte) 0x36;
        tx_msg[13] = (byte) 0x03;     //multizone hegesztes

        tx_msg[14] = (byte) 0x01;    //from wireshark
        tx_msg[15] = (byte) 0x00;   //

        tx_msg[16] = (byte) zoneNo;  //
//        tx_msg[17] = (byte) 0x02;  // text mez�k sz�ma �sszesen
        tx_msg[17] = (byte) MultiZone_elements;  // zonak sz�ma �sszesen

        tx_msg[18] = (byte) 0x01;  //
        tx_msg[19] = (byte) 0x00;  //
        tx_msg[20] = (byte) 0x00;  //
        tx_msg[21] = (byte) 0x20;  //

        tx_msg[22] = (byte) 0x01;  //
        tx_msg[23] = (byte) 0x00;  //
        tx_msg[24] = (byte) 0x00;  //
        tx_msg[25] = (byte) 0x00;  //
        tx_msg[26] = (byte) 0x00;  //
        tx_msg[27] = (byte) 0x00;  //

        tx_msg[31] = (byte) (Backcolor.getRed()&0xff);  //
        tx_msg[32] = (byte) (Backcolor.getGreen()&0xff);  //
        tx_msg[33] = (byte) (Backcolor.getBlue()&0xff);  //


        tx_msg[34] = (byte) 0x00;  //
        tx_msg[35] = (byte) 0x00;  //
        tx_msg[36] = (byte) 0x01;  //
        tx_msg[37] = (byte) 0x00;  //
        tx_msg[38] = (byte) 0x00;  //
        tx_msg[39] = (byte) 0x00;  //
        tx_msg[40] = (byte) 0x32;  //
        tx_msg[41] = (byte) 0x00;  //
        tx_msg[42] = (byte) 0x01;  //
        tx_msg[43] = (byte) 0x00;  //
        tx_msg[44] = (byte) 0x32;  //
        tx_msg[45] = (byte) 0x00;  //
        tx_msg[46] = (byte) 0x32;  //
        tx_msg[47] = (byte) 0x00;  //
        tx_msg[48] = (byte) 0x03;////
        tx_msg[49] = (byte) 0x00;  //
        tx_msg[50] = (byte) 0x00;  //
        tx_msg[51] = (byte) 0x00;  //

        tx_msg[52] = (byte) 0x08;  //
        tx_msg[53] = (byte) 0x33;  //
        tx_msg[54] = (byte) PictNo;  //


        msg_length = 55;



        a = 0;
        for (int j = 4; j < msg_length; j++) {
            a += (int) (((int) tx_msg[j]) & 0xff);
        }
        tx_msg[2] = (byte) (a & 0xff);
        tx_msg[3] = (byte) ((a >> 8) & 0xff);

        received[16] = 0x02;
        sck.setSoTimeout(Timeout);
        DatagramPacket request = new DatagramPacket(tx_msg, msg_length, IPaddress, UdpPort);
        sck.send(request);

        DatagramPacket response = new DatagramPacket(received, received.length);
        sck.receive(response);

        System.out.println("write pict:" + PictNo + " :" + Integer.toHexString(received[16]));

        return 0;
    }

    public int Init_HSWIM_zones ()throws IOException {

        byte[] tx_msg = new byte[1024];
        byte[] received = new byte[1024];
        int msg_length = 0;
        int a = 0;
        MultiZone_elements=2;


        tx_msg[0] = (byte) 0x55;
        tx_msg[1] = (byte) 0xa7;

        tx_msg[2] = (byte) 0x00;  //crc/chksum
        tx_msg[3] = (byte) 0x00;

        tx_msg[4] = (byte) 0x20;//((Text.length()+0x20  ) & 0xff);
        tx_msg[5] = (byte) 0x00;//(((Text.length()+0x20) >> 8) & 0xff);

        tx_msg[6] = (byte) (sourceaddr & 0xff);
        tx_msg[7] = (byte) ((sourceaddr >> 8) & 0xff);
        tx_msg[8] = (byte) (destaddr & 0xff);
        tx_msg[9] = (byte) ((destaddr >> 8) & 0xff);

        tx_msg[10] = (byte) (communication_counter & 0xff);
        tx_msg[11] = (byte) ((communication_counter >> 8) & 0xff);
        communication_counter++;

        tx_msg[12] = (byte) 0x36;
        tx_msg[13] = (byte) 0x01;     //multizone hegesztes

        tx_msg[14] = (byte) 0x01;    //from wireshark
        tx_msg[15] = (byte) 0x00;   //

        tx_msg[16] = (byte) 0x01;  //
        tx_msg[17] = (byte) 0x01;   //

        tx_msg[18] = (byte) 0x00;  //
        tx_msg[19] = (byte) 0x00;  //
        tx_msg[20] = (byte) 0x00;  //
        tx_msg[21] = (byte) 0x00;  //

        tx_msg[22] = (byte) 0x00;  //   zona szam
        tx_msg[23] = (byte) 0x02;  //
        tx_msg[24] = (byte) 0x00;  //
        tx_msg[25] = (byte) 0x00;  //
        tx_msg[26] = (byte) 0x00;  //
        tx_msg[27] = (byte) 0x00;  //
//   -----   elemek:


        tx_msg[28] = (byte) 0x01;  // elem szama
        tx_msg[29] = (byte) 0x00;  //
        tx_msg[30] = (byte) 0x00;  //
        tx_msg[31] = (byte) 0x00;  //

        tx_msg[32] = (byte) 0x00;  //  xpos
        tx_msg[33] = (byte) 0x00;  //

        tx_msg[34] = (byte) 0x00;  //  ypos
        tx_msg[35] = (byte) 0x00;  //

        tx_msg[36] = (byte) 0x70;  //
        tx_msg[37] = (byte) 0x00;  // szelesseg

        tx_msg[38] = (byte) 0x40;  //
        tx_msg[39] = (byte) 0x00;  // magassag

//  masodik elem

        tx_msg[40] = (byte) 0x02;  //
        tx_msg[41] = (byte) 0x00;  //
        tx_msg[42] = (byte) 0x00;  //
        tx_msg[43] = (byte) 0x00;  //
        tx_msg[44] = (byte) 0x00;  // xpos L
        tx_msg[45] = (byte) 0x00;  // xpos H
        tx_msg[46] = (byte) 0x40;  // ypos
        tx_msg[47] = (byte) 0x00;  //

        tx_msg[48] = (byte) 0x7f;////
        tx_msg[49] = (byte) 0x00;  //
        tx_msg[50] = (byte) 0x10;  //
        tx_msg[51] = (byte) 0x00;  //




        msg_length = 52;



        a = 0;
        for (int j = 4; j < msg_length; j++) {
            a += (int) (((int) tx_msg[j]) & 0xff);
        }
        tx_msg[2] = (byte) (a & 0xff);
        tx_msg[3] = (byte) ((a >> 8) & 0xff);

        received[16] = 0x02;
        sck.setSoTimeout(Timeout);
        DatagramPacket request = new DatagramPacket(tx_msg, msg_length, IPaddress, UdpPort);
        sck.send(request);

        DatagramPacket response = new DatagramPacket(received, received.length);
        sck.receive(response);

        System.out.println("set hswim zones" +  " :" + Integer.toHexString(received[16]));

        tx_msg[16] = (byte) 0x01;  //
        tx_msg[17] = (byte) 0x00;  //

        a = 0;
        for (int j = 4; j < msg_length; j++) {
            a += (int) (((int) tx_msg[j]) & 0xff);
        }
        tx_msg[2] = (byte) (a & 0xff);
        tx_msg[3] = (byte) ((a >> 8) & 0xff);

        received[16] = 0x02;
        sck.setSoTimeout(Timeout);
        request = new DatagramPacket(tx_msg, msg_length, IPaddress, UdpPort);
        sck.send(request);

        response = new DatagramPacket(received, received.length);
        sck.receive(response);

        System.out.println("set hswim zones" +  " :" + Integer.toHexString(received[16]));

        //  Write_MultiZone_Text((byte) 0x01, " ");
        //s  Write_MultiZone_Text((byte) 0x02, " ");


        return 0;
    }
}