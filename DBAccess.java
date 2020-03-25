/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mainserver;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBAccess {
    public static Connection getConnection(int dbnum){
        Connection con=null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            if(dbnum==1)
                con=DriverManager.getConnection("jdbc:mysql://localhost:3306/access?zeroDateTimeBehavior=convertToNull","root","");
            else
                con=DriverManager.getConnection("jdbc:mysql://localhost:3306/capedcrusader?zeroDateTimeBehavior=convertToNull","root","");
        }
        catch(Exception e){
            System.out.println(e);
        }
        return con;
    }
}
