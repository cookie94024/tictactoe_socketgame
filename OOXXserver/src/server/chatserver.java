package server;

import java.io.*;
import java.net. *;
import java. awt.event. *;
import java.awt.*;
import javax. swing.*;


public class chatserver implements ActionListener, Runnable {
	JTextArea showArea;
    JTextField msgText,ipText;
    JFrame mainJframe;
    JButton Enter,start;
    JScrollPane Scroll;
    JPanel pane;
    Container con;
    Thread thread = null;
    ServerSocket server;
    Socket s1;
    DataInputStream din;
    DataOutputStream dout;
    public String nickname;
    public String iP;
    
    public chatserver() {
        //設置介面
    	nickname=JOptionPane.showInputDialog("請輸入暱稱");
        mainJframe = new JFrame(nickname+"的聊天視窗");
        selects.nickname=nickname;
    	
    	games.nickname=nickname;
        con = mainJframe.getContentPane();
        showArea = new JTextArea();
        showArea.setEditable(false);
        showArea.setLineWrap(true);
        Scroll = new JScrollPane(showArea);
        msgText = new JTextField();
        msgText.setColumns(30);
        msgText.addActionListener(this);
        Enter = new JButton("Enter");
        Enter.addActionListener(this);
        start = new JButton("start");
        start.addActionListener(this);
        pane = new JPanel();
        pane.setLayout(new FlowLayout());
        pane.add(msgText);
        pane.add(Enter);
        
        ipText=new JTextField("IP地址");
        ipText.setColumns(20);
        ipText.addActionListener(this);
        
        con.add(ipText, BorderLayout.NORTH);
        con.add(start, BorderLayout.EAST);
        con.add(Scroll, BorderLayout.CENTER);
        con.add(pane, BorderLayout.SOUTH);
        mainJframe.setSize(500, 400);
        mainJframe.setVisible(true);
        mainJframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ;
        try {
            //創建服務端
            server = new ServerSocket(5500);
            showArea.append("Waiting for another player...\n");//等待客戶端連接
            s1 = server.accept();
            din = new DataInputStream(s1.getInputStream());
            dout = new DataOutputStream(s1.getOutputStream());
            //後台接收對方訊息
            thread = new Thread(this);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        } catch (IOException e) {
            showArea.append("Error\n");
            msgText.setEditable(false);
            Enter.setEnabled(false);
        }
    }

    public static void main(String[] args){
        new chatserver();
    }


    //按按鈕，傳訊息
    public void actionPerformed(ActionEvent e) {
        String s = msgText.getText();
        String cmd = e.getActionCommand();
        if (s.length() > 0) {
            try {
                dout.writeUTF(s);
                dout.flush();
                showArea.append( "我"+" : " + msgText.getText() + "\n");
                msgText.setText(null);
            } catch (IOException el) {
                showArea.append("你的訊息：“" + msgText.getText() + "”發送錯誤!\n");
            }
        }
        if (cmd=="start") {
        	//iP=ipText.getText();
        	iP="localhost";
        	
            games.ip=iP;
            new selects();
        }
    }


    //將客戶端傳來的消息顯示在對話區域
    public void run() {
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

