/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mainserver;

import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

class Handler extends Thread
{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    public Handler(DataInputStream dis, DataOutputStream dos, Socket s) {
        this.dis = dis;
        this.dos = dos;
        this.s = s;
    }
    
    @Override
    public void run()
    {
        String received,received1,received2;
        
        while(true)
        {
            try{
            received=dis.readUTF();
            System.out.println(received);
            Connection con = DBAccess.getConnection(1);
            if(received.equals("Exit")) 
                    {  
                        System.out.println("Client " + this.s + " sends exit..."); 
                        System.out.println("Closing this connection."); 
                        this.s.close(); 
                        System.out.println("Connection closed"); 
                        break; 
                    }
            else if(received.equals("Confirm")) {
                received1=dis.readUTF();
                received2=dis.readUTF();
                System.out.println(received2);
                String str = "select password from access where User_name='"+received1+"'";
                try{
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(str);
                    if(rs.next())
                    {
                        String temp = rs.getString("password");
                        if(temp.equals(received2))
                        {
                            dos.writeUTF("Granted");
                        }
                        else
                            dos.writeUTF("Wrong password");
                    }
                    else{
                        dos.writeUTF("Wrong User");
                    }
                
                } catch (SQLException ex) {
                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);}
            }
            else if(received.equals("Result")) {
                received1=dis.readUTF();
                received2=dis.readUTF();
                String str="Update access set Score="+ Integer.valueOf(received2)+" where User_name="+received1;
                try{
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(str);
                }catch(SQLException e){
                }
            }
            } catch (IOException e) {
                System.out.println("Waiting For Connection!!");
            }
        }
    }
}
class Awt extends JFrame implements ActionListener {
    JLabel label,label1;
    JTextField text1,text2,text3,text4,text5,text6,text7;
    JButton b1,b2,bok;
    JTextField textfield1,textfield2;
    JFrame frame=new JFrame();
    JLabel labelqn,labelA,labelB,labelC,labelD,labelAns,labelno;
    public Awt() {
        super("Examination System");
        Awtfun();
    }
    
    public void Awtfun(){
        b1 = new JButton("Start");
        b2 = new JButton("Update");
        label1=new JLabel();
        Font font = new Font("Verdana", Font.BOLD, 32);
        label1.setFont(font);
        label1.setText("Examination System");
        add(b1);add(b2);
        add(label1);
        b1.addActionListener(this);
        b2.addActionListener(this);
        label1.setBounds(100,100,500,70);
	b1.setBounds(140,240,100,30);
	b2.setBounds(330,240,100,30);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(600,350);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==b1){
            ServerSocket ss = null;
            try {
                ss = new ServerSocket(5005);
            } catch (IOException ex) {
                Logger.getLogger(Awt.class.getName()).log(Level.SEVERE, null, ex);
            }
        System.out.println("Server started"); 
        System.out.println("Waiting for a client ...");
        while(true)
        {
            Socket s=null;
            try{
            s=ss.accept();
            System.out.println("New Client Connected");
            
            DataInputStream dis=new DataInputStream(s.getInputStream());
            DataOutputStream dos=new DataOutputStream(s.getOutputStream());
            Thread t=new Handler(dis,dos,s);
            t.start();
            }catch(IOException e1){
                try {
                    s.close();
                } catch (IOException ex) {
                    Logger.getLogger(Awt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
            
        }
        else if(e.getSource()==b2){
            labelqn = new JLabel();
            labelno = new JLabel();
            labelA = new JLabel();
            labelB = new JLabel();
            labelC = new JLabel();
            labelD = new JLabel();
            text1 = new JTextField("",10);
            text2 = new JTextField("",10);
            text3 = new JTextField("",10);
            text4 = new JTextField("",10);
            text5 = new JTextField("",10);
            text6 = new JTextField("",10);
            text7 = new JTextField("",10);
            
            bok = new JButton("OK");
            frame.add(bok);
            frame.add(text1);
            frame.add(text2);
            frame.add(text3);
            frame.add(text4);
            frame.add(text5);
            frame.add(text6);
            frame.add(text7);
            
            labelAns = new JLabel();
            labelno.setText("No :");
            frame.add(labelno);
            labelqn.setText("Question :");
            frame.add(labelqn);
            labelA.setText("A :");
            frame.add(labelA);
            labelB.setText("B :");
            frame.add(labelB);
            labelC.setText("C :");
            frame.add(labelC);
            labelD.setText("D :");
            frame.add(labelD);
            labelAns.setText("Ans :");
            frame.add(labelAns);
            text1.setBounds(70,30,50,30);
            text2.setBounds(200,30,300,30);
            text3.setBounds(150,70,200,30);
            text4.setBounds(150,110,200,30);
            text5.setBounds(150,150,200,30);
            text6.setBounds(150,190,200,30);
            text7.setBounds(150,230,200,30);
            labelno.setBounds(40,20,200,50);
            labelqn.setBounds(140,20,200,50);
            labelA.setBounds(100,60,200,50);
            labelB.setBounds(100,100,200,50);
            labelC.setBounds(100,140,200,50);
            labelD.setBounds(100,180,200,50);
            labelAns.setBounds(100,220,200,50);
            bok.setBounds(150,270,70,30);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(null);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setSize(600,350);
            bok.addActionListener(this);
        }
        else if(e.getSource()==bok){
            try{
            Connection conn = DBAccess.getConnection(2);
            Statement stmt = conn.createStatement();
            String query;
            query = "Update test set Question=\""+text2.getText()+"\",OptionA=\""+text3.getText()+"\",OptionB=\""+text4.getText()+"\",OptionC=\""+text5.getText()+"\",OptionD=\""+text6.getText()+"\",Answer="+text7.getText()+" where Ques_Num ="+text1.getText();
            stmt.executeUpdate(query);
            }catch(SQLException s){
                System.out.println(s);
            }
        }
}
}
public class MainServer {
    
    
    public static void main(String[] args) throws Exception {
        Awt ser_awt=new Awt();
        }
    }