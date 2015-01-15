using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.SqlClient;
using System.Data;


/// <summary>
///SQLCommand 的摘要说明
/// </summary>
public class SQLCommand
{
    //数据库连接字符串
    public static string ASPNETcourseConnectionString = TestCon.ASPNETcourseConnectionString;

	public SQLCommand()
	{
		//
		//TODO: 在此处添加构造函数逻辑
		//
	}

    /// <summary>
    /// 数据库查询命令，返回DataTable类型数据
    /// </summary>
    /// <param name="sqlstr">数据库字符串</param>
    /// <param name="con">数据库连接字符串</param>
    /// <param name="col">格式化控件</param>
    /// <returns></returns>
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
   
    /// <summary>
    /// 初始化返回dt的格式
    /// </summary>
    /// <param name="col">要显示的字段</param>
    /// <returns></returns>
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

    /// <summary>
    /// 执行删除和添加任务，如果执行成功，返回int类型：0不成功，1为成功
    /// </summary>
    /// <param name="sqlstr"></param>
    /// <param name="con"></param>
    /// <returns></returns>
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
}