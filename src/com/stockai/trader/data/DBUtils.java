package com.stockai.trader.data;

import com.mchange.v2.c3p0.*;

import java.beans.PropertyVetoException;
import java.sql.*;

public class DBUtils
{
    public static void main(String[] args)
    {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try
        {
            cpds.setDriverClass("org.sqlite.JDBC");
            cpds.setJdbcUrl("jdbc:sqlite:D:/database/stockai.db");
            Connection conn = cpds.getConnection();
            cpds.setMaxPoolSize(10);
            System.out.println(conn);

            String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	name text NOT NULL,\n"
                    + "	capacity real\n"
                    + ");";
            Statement stat = conn.createStatement();
            stat.execute(sql);

//            stat.execute("INSERT INTO warehouses(name,capacity) VALUES('xxxyyy',10)");
//            stat.execute("INSERT INTO warehouses(name,capacity) VALUES('zzzjjj',50)");
            ResultSet rs = stat.executeQuery("select * from warehouses");
            while(rs.next())
            {
                System.out.println(rs.getInt("id"));
                System.out.println(rs.getString("name"));
                System.out.println(rs.getDouble("capacity"));
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
