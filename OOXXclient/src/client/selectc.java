package client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.SocketException;

public class selectc  {
	JFrame select;
	JButton Enter;
	Container con;
	public static  String nickname;
	private JComboBox<String>faceCombo;
	public selectc(){
		select=new JFrame(nickname+"請選擇");
		
		
        con=select.getContentPane();
        
        faceCombo=new JComboBox<>();  //下拉bar
        faceCombo.addItem("我方先攻");
        faceCombo.addItem("敵方先攻");
        Enter=new JButton("開始遊戲");
        
        con.add(faceCombo, BorderLayout.NORTH);
        con.add(Enter, BorderLayout.SOUTH);
        select.setSize (300 ,150);
		select.setVisible(true);
		
        select.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        faceCombo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	gamec.signal=faceCombo.getSelectedIndex();
            }
        });
       
        
     
        
        Enter.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                try {

                    new Thread(new gamec()).start();
                    select.setVisible(false);
                    
                } catch (SocketException e1) {
                    e1.printStackTrace();
                }


            }

            @Override
            public void mousePressed(MouseEvent e) {

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


    }

}
