package client;

import java.io.*;
import java .net.*;
import java. awt.event.*;
import java.awt.*;
import javax. swing.*;

public class chatclient implements ActionListener,Runnable{
	JTextArea showArea;
    JTextField msgText,ipText;
    JFrame mainJframe;
    JButton Enter,start;
    JScrollPane Scroll;
    JPanel pane;
    Container con;
    Thread thread=null;
    Socket c1;
    DataInputStream din;
    DataOutputStream dout;
    public String nickname;
    public String iP;


    public chatclient  (){
    	nickname=JOptionPane.showInputDialog("�п�J�ʺ�");
    	selectc.nickname=nickname;
    	
    	gamec.nickname=nickname;
        mainJframe=new JFrame(nickname+"����ѵ���");     
        con=mainJframe.getContentPane();
        
        showArea=new JTextArea();
        showArea.setEditable(false);
        showArea.setLineWrap(true);
        Scroll=new JScrollPane(showArea);
        msgText=new JTextField();
        msgText.setColumns(30);
        msgText.addActionListener(this);
        Enter=new JButton("Enter");
        Enter.addActionListener(this);
        
        ipText=new JTextField("IP�a�}");
        ipText.setColumns(20);
        ipText.addActionListener(this);
        
        start=new JButton("start");
        start.addActionListener(this);

        pane=new JPanel();
        pane.setLayout(new FlowLayout());
        pane.add(msgText);
        pane.add(Enter);
        

        con.add(ipText, BorderLayout.NORTH);
        con.add(start, BorderLayout.EAST);
        con.add(Scroll, BorderLayout.CENTER);
        con.add(pane, BorderLayout.SOUTH);
        mainJframe.setSize (500 ,400);
        mainJframe.setVisible (true);
        mainJframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //�s��
        try{
        	c1=new Socket("localhost",5500);
        	din=new DataInputStream(c1.getInputStream());
        	dout=new DataOutputStream(c1 .getOutputStream());
            showArea.append(nickname+"���\�[�J��ѫ�...\n");
           
            
            //�B�z������
            thread=new Thread(this);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        } catch (UnknownHostException e1){
            e1.printStackTrace();
        } catch (IOException e1){
            showArea.append("Error\n");
            msgText.setEditable(false);
            Enter.setEnabled(false);
        }
    }

    public static void main(String[] args){
        new chatclient();
    }



  //�����s�A�ǰT��
    public void actionPerformed(ActionEvent e){
        String s=msgText.getText();
        String cmd = e.getActionCommand();
        if (s.length()>0){
            try{
            	dout.writeUTF(s);
            	dout.flush();
                showArea.append("��"+"�G "+msgText. getText()+"\n");
                msgText.setText(null);
            } catch (IOException e1){
                showArea.append("�A���T���G��"+msgText.getText()+"���L�k�o�e\n");
            }
        }
        if (cmd=="start") {
        	//iP=ipText.getText();
        	iP="localhost";
            gamec.ip=iP;
            new selectc();
        
    }

    }

    //�N�A�ȺݶǨӪ�������ܦb��ܰϰ�
    public void run(){
        try{
            while (true){
                showArea.append("���G"+din.readUTF()+"\n");
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

