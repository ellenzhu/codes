using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;


public partial class update_pwd: System.Web.UI.Page
{
    private DataTable dt = null;
    protected void Page_Load(object sender, EventArgs e)
    {
        SqlConnection MyConnection = new SqlConnection(TestCon.ASPNETcourseConnectionString);
        //产生select语句
        string sqlstr = string.Format("select * from register where id='{0}'", Session["UserID"].ToString());
        //连接数据库，进行判断
        string[] col = new string[] { "id", "name", "phone", "email", "pwd", "repwd" };
        dt = SQLCommand.dSqlCommand(sqlstr, MyConnection, col);
        TextBox3.Text = dt.Rows[0]["name"].ToString();
    }
   
    protected void Button1_Click(object sender, EventArgs e)
    {
        
        string name = TextBox3.Text.Trim();
        string pass = TextBox2.Text.Trim();
        
        string MySQL = "update register set pwd='" + pass + "',repwd='" + pass + "' where name='" + name + "'";
        SqlConnection MyConnection = new SqlConnection(TestCon.ASPNETcourseConnectionString);

        MyConnection.Open();
        SqlCommand MyCommand = MyConnection.CreateCommand();
        MyCommand.CommandText = MySQL;
        MyCommand.ExecuteNonQuery();
        MyConnection.Close();

        Response.Redirect("main.aspx");
    }
}