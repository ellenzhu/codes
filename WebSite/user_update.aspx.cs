using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data.SqlClient;
using System.Data;


public partial class user_update : System.Web.UI.Page
{
    private DataTable dt = null;
    protected void Page_Load(object sender, EventArgs e)
    {
        if (!IsPostBack)
        {
            SqlConnection MyConnection = new SqlConnection(TestCon.ASPNETcourseConnectionString);
            //产生select语句
            string sqlstr = string.Format("select * from register where id='{0}'", Session["UserID"].ToString());
            //连接数据库，进行判断
            string[] col = new string[] { "id", "name", "phone", "email", "pwd", "repwd" };
            dt = SQLCommand.dSqlCommand(sqlstr, MyConnection, col);
            TextBox1.Text = dt.Rows[0]["name"].ToString().Trim();
            TextBox2.Text = dt.Rows[0]["email"].ToString().Trim();
            TextBox3.Text = dt.Rows[0]["phone"].ToString().Trim();
        }
     }
   
    protected void Button1_Click(object sender, EventArgs e)
    {
        string name  = TextBox1.Text.Trim();
        string email = TextBox2.Text.Trim();
        string phone = TextBox3.Text.Trim();


        string MySQL = "update register set email='" + email + "',phone='" + phone + "',name='" + name + "'where name='" + name + "'";
        SqlConnection MyConnection = new SqlConnection(TestCon.ASPNETcourseConnectionString);

        MyConnection.Open();
        SqlCommand MyCommand = MyConnection.CreateCommand();
        MyCommand.CommandText = MySQL;
        MyCommand.ExecuteNonQuery();
        MyConnection.Close();
        
        Response.Redirect("main.aspx");
        
        
        
        
    }
}