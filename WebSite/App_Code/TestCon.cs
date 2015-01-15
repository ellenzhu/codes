using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.SqlClient;

/// <summary>
///TestCon 的摘要说明
/// </summary>
public class TestCon
{
    static string sqlCon = @"server =.;database=ASPNETcourse;Integrated Security=True";
    
    //public static string ASPNETcourseConnectionString = @"server =.;database=ASPNETcourse;Integrated Security=True";
    public static string ASPNETcourseConnectionString = "Data Source=1DV4OHGVETMMSIF\\SQLEXPRESS;Initial Catalog=gow;User ID=sa;Password=sa123456";
                                                         
	//public TestCon()
    public static void TestCon1()
	{
        //
		//TODO: 在此处添加构造函数逻辑
		//
	}
    /// <summary>  
    /// 连接测试  
    /// </summary>  
    public static void TestConnection()
    {
        //string conStr = @"server=.;database=strike;Integrated Security=True;";  
        SqlConnection conn = new SqlConnection(ASPNETcourseConnectionString);
        conn.Open();
        conn.Close();
        Console.WriteLine("连接对象新建成功。");
        //Console.ReadKey();
    }  
}