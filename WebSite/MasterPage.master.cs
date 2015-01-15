using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class MasterPage : System.Web.UI.MasterPage
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (Session["Name"] == null)
        {
            Label1.Text = "<a href='login.aspx'>登录</a>";
        }
        else
        {
            Label1.Text = "<a href='update.aspx'>"+"您好 , " + Session["Name"].ToString()+"</a>";
            //Label1.BackColor = System.Drawing.Color.Pink;
           
        }
    }
    protected void LinkButton1_Click(object sender, EventArgs e)
    {
        Session["location"] = "北碚";
        Response.Redirect("place.aspx");
    }


    protected void LinkButton2_Click(object sender, EventArgs e)
    {
        Session["location"] = "永川";
        Response.Redirect("place.aspx");
    }
    protected void LinkButton3_Click(object sender, EventArgs e)
    {
        Session["location"] = "奉节";
        Response.Redirect("place.aspx");
    }
    protected void LinkButton4_Click(object sender, EventArgs e)
    {
        Session["location"] = "武隆";
        Response.Redirect("place.aspx");
    }
}
