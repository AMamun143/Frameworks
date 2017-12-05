package utility;


import java.sql.*;

public class ConnectDB {

    Connection con;
    Statement smt;
    ResultSet rs;

    public void connectToDB() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "root");

    }

    public void executeSQL(String SQL, String ReturnAs, String ReturnColumn) throws SQLException {
        smt = con.createStatement();
        rs= smt.executeQuery(SQL);

        while (rs.next()) {

            ReturnAs = rs.getString(ReturnColumn);
        }
    }

    public void executeSQL(String SQL, String ReturnAs, String ReturnColumn, String SecondColumn) throws SQLException {
        smt = con.createStatement();
        rs= smt.executeQuery(SQL);

        while (rs.next()) {

            ReturnAs = rs.getString(ReturnColumn);
            ReturnAs = rs.getString(SecondColumn);
        }
    }

    public void executeSQL(String SQL, String ReturnAs, String ReturnColumn, String SecondColumn, String ThirdColumn) throws SQLException {
        smt = con.createStatement();
        rs= smt.executeQuery(SQL);

        while (rs.next()) {

            ReturnAs = rs.getString(ReturnColumn);
            ReturnAs = rs.getString(SecondColumn);
            ReturnAs = rs.getString(ThirdColumn);
        }
    }

    public void executeSQL(String SQL, String ReturnAs, String ReturnColumn, String SecondColumn, String ThirdColumn, String FourthColumn) throws SQLException {
        smt = con.createStatement();
        rs= smt.executeQuery(SQL);

        while (rs.next()) {

            ReturnAs = rs.getString(ReturnColumn);
            ReturnAs = rs.getString(SecondColumn);
            ReturnAs = rs.getString(ThirdColumn);
            ReturnAs = rs.getString(FourthColumn);
        }
    }
}
