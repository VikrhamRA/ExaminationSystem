/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testclient;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import java.net.*; 
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 *
 * @author Vikrham RA
 */
 public class TestClient extends JFrame implements ActionListener{
    JLabel label,label1,label2;
    JRadioButton jb[]=new JRadioButton[5];
    JButton b1,b2,bcnf,bexit;
    ButtonGroup bg;
    JTextField textfield1;
    int count=0,current=0,x=1,y=1,now=0;
    int m[]=new int[10];
    private Socket socket= null;
    final DataInputStream dis;
    final DataOutputStream dos;
    JFrame frame = new JFrame("Log In");
    String Username="";
    JPasswordField textfield2;
    TestClient(String address, int port, String s) throws IOException{
        super(s);
        try
        { 
            socket = new Socket(address, port); 
            System.out.println("Connected");
            
        }catch(IOException i) 
        { 
            System.out.println(i); 
        }
        this.dis =new DataInputStream(socket.getInputStream());
        this.dos =new DataOutputStream(socket.getOutputStream());
    }
    public void authenticate()
    {
        label1 = new JLabel();
        label2 = new JLabel();
        label1.setText("Username :");
        label2.setText("Password :");
        textfield1 = new JTextField("",10);
        textfield2 = new JPasswordField("",10);
        textfield2.setEchoChar('*');
        bcnf = new JButton("Confirm");
        bexit = new JButton("Exit");
        bcnf.addActionListener(this);
        bexit.addActionListener(this);
        frame.add(label1);
        frame.add(label2);
        frame.add(bcnf);
        frame.add(bexit);
        frame.add(textfield1);
        frame.add(textfield2);
        label1.setBounds(100,90,200,50);
        label2.setBounds(100,140,200,50);
        textfield1.setBounds(200,100,200,30);
        textfield2.setBounds(200,150,200,30);
	bcnf.setBounds(150,240,100,30);
	bexit.setBounds(320,240,100,30);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setSize(600,350);
    }
    public void OnlineTest(){
        label = new JLabel();
        add(label);
        bg = new ButtonGroup();
        for(int i=0;i<5;i++){
            jb[i] = new JRadioButton();
            add(jb[i]);
            bg.add(jb[i]);
        }
        b1 = new JButton("Next");
        b2 = new JButton("Bookmark");
        b1. addActionListener(this);
        b2.addActionListener(this);
        add(b1);add(b2);
        set();
	b1.setBounds(100,240,100,30);
	b2.setBounds(270,240,100,30);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(600,350);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==bcnf){
            String s;
            try {
                dos.writeUTF("Confirm");
                Username=textfield1.getText();
                dos.writeUTF(textfield1.getText());
                dos.writeUTF(String.valueOf(textfield2.getPassword()));
                s=dis.readUTF();
                System.out.print(s);
                if(s.equals("Granted")){
                        frame.dispose();
                        OnlineTest();
                }
                else{
                    JOptionPane.showMessageDialog(this,"Wrong user name or password");
                }
            } catch (IOException ex) {
            }
            
        }
            
        if(e.getSource()==bexit){
                System.exit(0);
        }
        if(e.getSource()==b1){
            if(check())
                count=count+1;
            current++;
            set();
            if(current==9){
                b1.setEnabled(false);
                b2.setText("Result");
            }
        }
        if(e.getSource()==b2){
            JButton bk = new JButton("Bookmark "+x);
            bk.setBounds(450,20+30*x,160,30);
            add(bk);
            bk.addActionListener(this);
            m[x]=current;
            x++;
            current++;
            set();	
            if(current==9)
		b2.setText("Result");
            setVisible(false);
            setVisible(true);
        }
        for(int i=0,y=1;i<x;i++,y++)
        {
            if(e.getActionCommand().equals("Bookmark "+y)){
                    if(check())
                            count=count+1;
                    now=current;
                    current=m[y];
                    System.out.println("he");
                    set();
                    ((JButton)e.getSource()).setEnabled(false);
                    current=now;
            }
        }

        if(e.getActionCommand().equals("Result")){
                if(check())
                        count=count+1;
                current++;
                try {
                    dos.writeUTF("Result");
                    dos.writeUTF("'"+Username+"'");
                    dos.writeUTF(Integer.toString(count));
                } catch (IOException ex) {}
                JOptionPane.showMessageDialog(this,"Your Score is : "+count);
                System.exit(0);
        }
    }
    void set()
	{
		jb[4].setSelected(true);
                Connection con = DB.getConnection();
                int ques = current+1;
                String str = "select Question,OptionA,OptionB,OptionC,OptionD from test where Ques_Num="+ques;
                try{
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(str);
                    rs.next();
                        label.setText("Question "+ques+" : "+rs.getString("Question"));
                    jb[0].setText(rs.getString("OptionA"));
                    jb[1].setText(rs.getString("OptionB"));
                    jb[2].setText(rs.getString("OptionC"));
                    jb[3].setText(rs.getString("OptionD")); 
		label.setBounds(30,40,450,20);
		for(int i=0,j=0;i<=90;i+=30,j++)
			jb[j].setBounds(50,80+i,200,20);
                }catch(SQLException e){
                    System.out.println();
                }
	}
        boolean check()
	{
            Connection con = DB.getConnection();
            int ques = current+1;
            String str = "select Answer from test where Ques_Num="+ques;
            try{
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(str);
                rs.next();
                return(jb[rs.getInt("Answer")-1].isSelected());
            }catch(SQLException e){
                System.out.println();
            }
            return false;
	}
    
    public static void main(String[] args) throws IOException {
        TestClient client = new TestClient("127.0.0.1", 5005, "Online Exam");
        client.authenticate();
    }

    
}
