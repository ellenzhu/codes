using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;


public partial class login : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {

    }
    protected void Login1_Authenticate(object sender, AuthenticateEventArgs e)
    {

    }
    protected void Button1_Click(object sender, EventArgs e)
    {
        //连接数据库字符串
        SqlConnection MyConnection = new SqlConnection(TestCon.ASPNETcourseConnectionString);
        //产生select语句
        string sqlstr = string.Format("select * from register where name='{0}' and pwd='{1}'", TextBox1.Text, TextBox2.Text);
        //连接数据库，进行判断
        if (SQLCon.Login(sqlstr, MyConnection))
        {
            MyConnection.Close();
            string[] col = new string []{ "id","name","phone","email","pwd","repwd"};
            DataTable dt=SQLCommand.dSqlCommand(sqlstr,MyConnection,col);
            Session["UserID"]=dt.Rows[0]["id"];
            Session["Name"]=dt.Rows[0]["name"];

           // if(dt.Rows[0]["pwd"]==TextBox2.Text)
            Response.Redirect("~/loginmain.aspx");
            
        }
        else
        {
            Page.ClientScript.RegisterStartupScript(GetType(), "success", "alert('用户名或密码错误!')", true);
        }
    
    }
}