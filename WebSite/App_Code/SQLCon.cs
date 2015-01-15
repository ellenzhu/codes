using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.SqlClient;
using System.Data;

/// <summary>
///SQLCon 的摘要说明
/// </summary>
public class SQLCon
{
    static string sqlCon = @"server =.;database=db2_Tome2;Integrated Security=True";  
	public SQLCon()
	{
		//
		//TODO: 在此处添加构造函数逻辑
		//
	}
    /// <summary>  
    /// 新增记录  
    /// </summary>  
    public static bool AddRecord(int sid, string name,string tid,string cag,int total)
    {
        bool flag = false;
        //string sqlCon = "server =.;database=strike;Integrated Security=True";  
        try
        {
            SqlConnection conn = new SqlConnection();
            conn.ConnectionString = sqlCon;
            string sqlStr2 = string.Format("insert 高考成绩表(编号,姓名,考生编号,考生类别,总成绩)" +
                                                            "values({0},'{1}','{2}','{3}',{4})", sid, name, tid, cag, total);
            SqlCommand cmd = new SqlCommand();
            cmd.Connection = conn;
            cmd.CommandText = sqlStr2;
            conn.Open();
            cmd.ExecuteNonQuery();
            conn.Close();
            flag = true;
        }
        catch (Exception ex)
        { }
        return flag;
    }
    public static int iSqlCommand(string sqlstr, SqlConnection con)
    {
        int flag = 0;
        try
        {
            SqlCommand cmd = new SqlCommand(sqlstr, con);
            con.Open();
            flag = cmd.ExecuteNonQuery();
            con.Close();
        }
        catch (Exception ex)
        {

        }
        return flag;
    }

    /// <summary>  
    /// 删除数据  
    /// </summary>  
    public static bool DelRecord(int id)
    {
        bool flag = false;
        //string sqlCon = "server =.;database=strike;Integrated Security=True";  
        try
        {
            SqlConnection conn = new SqlConnection();
            conn.ConnectionString = sqlCon;
            string sqlStr = "delete 高考成绩表 where 编号 = " + id.ToString();
            SqlCommand cmd = new SqlCommand(sqlStr, conn);
            conn.Open();
            cmd.ExecuteNonQuery();
            conn.Close();
            flag = true;
        }
        catch (Exception ex)
        {
 
        }
        return flag;
    }

    public static bool UpdateRecord(int sid, string name,string tid,string cag,int total)
    {
        bool flag = false;
        //  string sqlCon = @"server =.;database=strike;Integrated Security=True";  
        try
        {
            SqlConnection conn = new SqlConnection();
            conn.ConnectionString = sqlCon;
            string sqlStr = string.Format("update 高考成绩表 set 姓名='{0}' , 考生编号='{1}' , 考生类别='{2}' , 总成绩={3})  where 编号={4}", name, tid, cag, total, sid);
            SqlCommand cmd = new SqlCommand(sqlStr, conn);
            conn.Open();
            cmd.ExecuteNonQuery();
            conn.Close();
            flag = true;
        }
        catch (Exception ex)
        { }
        return flag;
    }

    /// <summary>  
    /// 查询多行数据---DataReader逐行读取,每次读一行  
    /// </summary>  
    public static void QueryList( ref DataTable dt)
    {
        try
        {
            dt.Clear();
            SqlConnection con = new SqlConnection(sqlCon);
            string sqlStr = "select * from 高考成绩表";
            SqlCommand cmd = new SqlCommand(sqlStr, con);
            con.Open();
            SqlDataReader dr = cmd.ExecuteReader();
            if (dr.HasRows)
            {
                while (dr.Read())//dr每read（）一次就读取一行  
                {
                    DataRow dr_dt = dt.NewRow();
                    for (int i = 0; i < dr.FieldCount; i++)
                    {
                        dr_dt[i] = dr[i].ToString();
                    }
                    dt.Rows.Add(dr_dt);
                }
                // Console.WriteLine("有数据");  
            }
            else
            {

                //Console.WriteLine("无数据");
            }
        }
        catch (Exception ex)
        { 
        }
    }


    public static bool Login (string sqlstr,SqlConnection con)
    {
       
        try
        {
            
            SqlCommand cmd = new SqlCommand(sqlstr, con);
            con.Open();
            SqlDataReader dr = cmd.ExecuteReader();
            if (dr.HasRows)
            {
                return true;
                //dt = new DataTable();
                //while (dr.Read())//dr每read（）一次就读取一行  
                //{
                //    DataRow dr_dt = dt.NewRow();
                //    for (int i = 0; i < dr.FieldCount; i++)
                //    {
                //        dr_dt[i] = dr[i].ToString();
                //    }
                //    dt.Rows.Add(dr_dt);
                //}
                //return dt;
                // Console.WriteLine("有数据");  
            }
            
        }
        catch (Exception ex)
        {
            
        }
            
        return false;
    }


    public static DataTable CreatDateTable(string[] col)
    {
        DataTable dt = new DataTable();
        dt.Clear();
        for (int i = 0; i < col.Length; i++)
        {
            dt.Columns.Add(col[i]);
        }
        return dt;
    }

    public static DataTable dSqlCommand(string sqlstr, SqlConnection con, string[] col)
    {
        DataTable dt = null;
        try
        {
            SqlCommand cmd = new SqlCommand(sqlstr, con);
            con.Open();
            SqlDataReader dr = cmd.ExecuteReader();
            if (dr.HasRows)
            {
                dt = CreatDateTable(col);
                while (dr.Read())//dr每read（）一次就读取一行  
                {
                    DataRow dr_dt = dt.NewRow();
                    for (int i = 0; i < dr.FieldCount; i++)
                    {
                        dr_dt[i] = dr[i].ToString();
                    }
                    dt.Rows.Add(dr_dt);
                }
            }
        }
        catch (Exception ex)
        {
        }
        return dt;
    }
}