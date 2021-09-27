package client;

import javax.swing.*;
import java.awt.*;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class gamec extends JFrame implements Runnable{

    public static int signal=0;  //�O�@�}�l�Ҧ����s�ҥ�Ĳ�o
    public static  String ip;    //�O�q��IP
    public static  String nickname;
    Container con;
    private  JButton[]buttons=new JButton[9]; 
      /*  /**
         * 0 ��Ĳ�o���s
         * 1 �ڤ�Ĳ�o���s
         * 2 �Ĥ�Ĳ�o���s
         */
        private int[]boom=new int[9];
        private int judge=2; 
        private ImageIcon a;  //oo���Ϲ�
        private ImageIcon b;  //xx���Ϲ�

    private DatagramSocket client;
    private int serverPort=6666;
    private int clientPort=8888;
    private int outPort=9999;
    //6666
    private DatagramSocket server;

    public gamec() throws SocketException {
        a=new ImageIcon("D:\\eclipse-workspace\\0611\\images\\redCircle.png");
        b=new ImageIcon("D:\\eclipse-workspace\\0611\\images\\blueX.png");
        //��ڵoĲ�o���s�A����t�@�Ӫ��a
        
        if(signal==1){  
            serverPort=7777;
            clientPort=9999;
            outPort=8888;
            //�洫
            ImageIcon c=a;
            a=b;
            b=c;
        }
        this.setVisible(true);
        this.setLayout(null);
        this.setTitle(nickname+"���C������");
        this.setSize(500,500);
        con=this.getContentPane();
        con.setLayout(new GridLayout(3,3,5,5));
        con.setBackground(Color.black);
        this.setButtons();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void setButtons(){
        for(int i=0;i<9;i++){
            buttons[i]=new JButton();
            con.add(buttons[i]); 
        }
       
        for(int i=0;i<9;i++){
            int j=i;
            buttons[i].addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if(myJudge()&&boom[j]==0){
                        buttons[j].setIcon(a);
                        boom[j]=1;

                        try {
                            server=new DatagramSocket(serverPort);

                            //2.�ǳƼƾ�
                            String msg;
                            if(win()){
                              msg=j+","+1;
                            }else if(noWin()){
                                msg=j+","+2;
                            }else{
                                msg=j+","+0;
                            }

                            byte[] data =msg.getBytes();
                            //3�B���]�]�o�e�a�I �Ψ��^
                            DatagramPacket packet=new DatagramPacket(data,data.length,new InetSocketAddress(ip,outPort));
                            //4�B�o�e
                            server.send(packet);
                            System.out.println("O��o�e���\");
                            if(win()){
                                JOptionPane.showConfirmDialog(null, "����~~Ĺ�o!�ӱj��!",nickname+"���C������", JOptionPane.CLOSED_OPTION);
                                boom=new int[9];
                                for(JButton jButton:buttons){
                                    jButton.setIcon(null);
                                }
                                repaint();

                            }
                            server.close();
                        }catch(Exception ec) {
                            System.out.println("O�貧�`");
                        }
                        judge++;
                        if(noWin()){
                            JOptionPane.showConfirmDialog(null, "����", nickname+"���C������", JOptionPane.CLOSED_OPTION);
                            judge=2;
                            boom=new int[9];
                            for(JButton jButton:buttons){
                                jButton.setIcon(null);
                            }
                        }

                    }



                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            this.add(buttons[i]);

        }


    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(myJudge()) {
                continue;
            }
            try {
                client=new DatagramSocket(clientPort);
                //2�B�ǳƱ����e��
                byte[] container=new byte[1024];
                //3�B�ʸ˦��]DatagramPacket(byte[] buf,int length)
                DatagramPacket packet=new DatagramPacket(container,container.length);
                //4�B����
                client.receive(packet);
                byte[] data=packet.getData();
                int len=packet.getLength();
                String str=new String(data,0,len);
                Integer integer=new Integer(str.split(",")[0]);
                Integer integer1=new Integer(str.split(",")[1]);
                boom[integer]=5;
                buttons[integer].setIcon(b);
                System.out.println(integer1);
                judge++;
                repaint();
                client.close();
                if(integer1==1){
                    JOptionPane.showConfirmDialog(null, "��F!!�U���A���A�y",nickname+"���C������", JOptionPane.CLOSED_OPTION);
                    boom=new int[9];
                    for(JButton jButton:buttons){
                        jButton.setIcon(null);
                        judge=2;
                    }
                    repaint();
                }else if(integer1==2){
                    JOptionPane.showConfirmDialog(null, "�M��", null, JOptionPane.CLOSED_OPTION);
                    boom=new int[9];
                    for(JButton jButton:buttons){
                        jButton.setIcon(null);
                        judge=2;
                    }
                    repaint();
                }
            }catch(Exception ec) {
                System.out.println("Error");
            }
        }

    }
    public boolean win() {
        if(boom[0]+boom[1]+boom[2]==3||
           boom[3]+boom[4]+boom[5]==3||
           boom[6]+boom[7]+boom[8]==3)
            return true;
        if(boom[0]+boom[3]+boom[6]==3||
           boom[1]+boom[4]+boom[7]==3||
           boom[2]+boom[5]+boom[8]==3)
            return true;
        if(boom[0]+boom[4]+boom[8]==3||
           boom[2]+boom[4]+boom[6]==3)
            return true;

       return false;
    }
    public  boolean noWin() {
        for (int i : boom) {
            if (i == 0) {
                return false;
            }
        }
        return true;
    }
    public boolean myJudge(){
        if(signal==0){
            if(judge%2==0){
                return true;
            }else{
                return false;
            }
        }else{
            if(judge%2!=0){
                return true;
            }else{
                return false;
            }
        }
    }

}
