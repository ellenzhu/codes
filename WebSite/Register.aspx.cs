using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;

public partial class Register : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
       
    }
    protected void Button1_Click(object sender, EventArgs e)
    {
        Label1.Text = "";
        if (TextBox4.Text != TextBox5.Text)
            Label1.Text = "密码不同";
        else
        {
            
            string MySQL = "INSERT INTO register (name,phone,email,pwd,repwd) VALUES('";
            
            MySQL += this.TextBox1.Text + "','";
            MySQL += this.TextBox2.Text + "','";
            MySQL += this.TextBox3.Text + "','";
            MySQL += this.TextBox4.Text + "','";
            MySQL += this.TextBox5.Text + "');";

           
            SqlConnection MyConnection = new SqlConnection(TestCon.ASPNETcourseConnectionString);
            MyConnection.Open();
            SqlCommand MyCommand = MyConnection.CreateCommand();
            MyCommand.CommandText = MySQL;
            MyCommand.ExecuteNonQuery();
            MyConnection.Close();

            //重定位页面
            Response.Redirect("login.aspx");
        }
    }
    protected void Button2_Click(object sender, EventArgs e)
    {
        TextBox1.Text = "";
        TextBox2.Text = "";
        TextBox3.Text = "";
        TextBox4.Text = "";
        TextBox5.Text = "";
    }
}