using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;


public partial class car_update : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        //Label8.Text = Convert.ToInt32(Session["Price"].ToString());
    }
    protected void TextBox3_TextChanged(object sender, EventArgs e)
    {

        int i;
        int s;
        int l;
        i = Convert.ToInt32(TextBox3.Text);
        s = Convert.ToInt32(Session["Price"].ToString());
        l  = s * i;
        Label8.Text = l.ToString();
       
    }
    protected void Button1_Click(object sender, EventArgs e)
    {
        string MySQL = "INSERT INTO car (c_username,c_phone,c_num,c_m) VALUES('";

        MySQL += this.TextBox1.Text + "','";
        MySQL += this.TextBox2.Text + "','";
        MySQL += this.TextBox3.Text + "','";
        MySQL += this.Label8.Text + "');";


        SqlConnection MyConnection = new SqlConnection(TestCon.ASPNETcourseConnectionString);
        MyConnection.Open();
        SqlCommand MyCommand = MyConnection.CreateCommand();
        MyCommand.CommandText = MySQL;
        MyCommand.ExecuteNonQuery();
        MyConnection.Close();

        //重定位页面
        Response.Redirect("order.aspx");
    }
    
}