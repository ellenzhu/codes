using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data.SqlClient;
using System.Data;
using System.Web.UI.HtmlControls;


public partial class main : System.Web.UI.Page
{   
    private DataTable dt = null;
 
    

    protected void Page_Load(object sender, EventArgs e)
    {


        if (Session["Name"] == null)
        {
            Label1.Text = "<a href='login.aspx'>登录</a>";
        }
        else
        {
            Label1.Text = "<a href='update.aspx'>" + "您好 , " + Session["Name"].ToString() + "</a>";
            //Label1.BackColor = System.Drawing.Color.Pink;

        }
        
        
     
        SqlConnection MyConnection = new SqlConnection(TestCon.ASPNETcourseConnectionString);
        //产生select语句
        string sqlstr = string.Format("select * from jd");
        //连接数据库，进行判断
        string[] col = new string[] { "j_id", "j_name", "j_place", "j_price", "j_img", "jd_img", "j_ind", "j_opendate", "j_phone" };
        dt = SQLCommand.dSqlCommand(sqlstr, MyConnection, col);
        int num = 0;

        
        
        
        foreach (DataRow dr in dt.Rows)
        {
            if(num>=7)
                break;
            TableRow tr = new TableRow();
            //tr.Style.Add(HtmlTextWriterStyle.BackgroundColor, "#d9f4fb");


            TableCell tc = new TableCell();
            tc.Style.Add(HtmlTextWriterStyle.TextAlign, "left");
            Label lab = new Label();
            string image=dr["j_img"].ToString().Trim();
            lab.Text = "<a href=\"detail.aspx?ID=" + dr["j_id"].ToString().Trim() + "\"><img src=\"" + image + "\" /></a> ";
            tc.Controls.Add(lab);
            tr.Controls.Add(tc);

            tc = new TableCell();
            tc.Style.Add(HtmlTextWriterStyle.FontFamily, "楷体");
            tc.Style.Add(HtmlTextWriterStyle.FontSize, "25px");
            tc.Style.Add(HtmlTextWriterStyle.FontWeight, "900");
            lab = new Label();
            string name = dr["j_name"].ToString().Trim();
            lab.Text = name;
            tc.Controls.Add(lab);
            tr.Controls.Add(tc);

            tc = new TableCell();
            tc.Style.Add(HtmlTextWriterStyle.FontFamily, "楷体");
            tc.Style.Add(HtmlTextWriterStyle.FontSize, "20px");
            lab = new Label();
            string place = dr["j_place"].ToString().Trim();
            lab.Text =  place;
            tc.Controls.Add(lab);
            tr.Controls.Add(tc);

            tc = new TableCell();
            tc.Style.Add(HtmlTextWriterStyle.Width, "150px");
            tc.Style.Add(HtmlTextWriterStyle.FontSize, "30px");
            tc.Style.Add(HtmlTextWriterStyle.TextAlign , "right");
            lab = new Label();
            string price = dr["j_price"].ToString().Trim();
            lab.Text = price + "￥";
            tc.Controls.Add(lab);
            tr.Controls.Add(tc);

            tc = new TableCell();
            lab = new Label();
            lab.Text = "<a href=\"detail.aspx?ID=" + dr["j_id"].ToString().Trim() + "\">查看详情</a>";
            tc.Controls.Add(lab);
            tr.Controls.Add(tc);

            Table1.Controls.Add(tr);
            num++;
        }
        
        
        MyConnection.Close();    
       
        /*
          Label5.Text = dt.Rows[0]["j_name"].ToString().Trim();
          Label6.Text = dt.Rows[0]["j_place"].ToString().Trim();
          Label7.Text = dt.Rows[0]["j_price"].ToString().Trim();
          
          Label8.Text="<img src=\""+image+"\" />";
         */



        if (!IsPostBack)
        { 
           if(Application["count"]==null)
            {
                Application["count"] = 0;

            }
           Application["count"] = (int)Application["count"] + 1;
           Response.Write("网站访问量：" + Application["count"]);
        }
        
    }


    protected void LinkButton1_Click(object sender, EventArgs e)
    {
        Session["location"] = "北碚";
        Response.Redirect( "place.aspx");
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