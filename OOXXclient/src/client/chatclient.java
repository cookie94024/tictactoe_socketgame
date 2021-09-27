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
    	nickname=JOptionPane.showInputDialog("請輸入暱稱");
    	selectc.nickname=nickname;
    	
    	gamec.nickname=nickname;
        mainJframe=new JFrame(nickname+"的聊天視窗");     
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
        
        ipText=new JTextField("IP地址");
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

        //連接
        try{
        	c1=new Socket("localhost",5500);
        	din=new DataInputStream(c1.getInputStream());
        	dout=new DataOutputStream(c1 .getOutputStream());
            showArea.append(nickname+"成功加入聊天室...\n");
           
            
            //處理對方消息
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



  //按按鈕，傳訊息
    public void actionPerformed(ActionEvent e){
        String s=msgText.getText();
        String cmd = e.getActionCommand();
        if (s.length()>0){
            try{
            	dout.writeUTF(s);
            	dout.flush();
                showArea.append("我"+"： "+msgText. getText()+"\n");
                msgText.setText(null);
            } catch (IOException e1){
                showArea.append("你的訊息：“"+msgText.getText()+"”無法發送\n");
            }
        }
        if (cmd=="start") {
        	//iP=ipText.getText();
        	iP="localhost";
            gamec.ip=iP;
            new selectc();
        
    }

    }

    //將服務端傳來的消息顯示在對話區域
    public void run(){
        try{
            while (true){
                showArea.append("對方："+din.readUTF()+"\n");
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

