using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data.SqlClient;
using System.Data;
using System.Web.UI.HtmlControls;

public partial class detail : System.Web.UI.Page
{
    private DataTable dt = null;
    private DataRow dr = null;

 
    protected void Page_Load(object sender, EventArgs e)
    {
        
        if (!IsPostBack)
        {
            
            string id=Request.QueryString["ID"].ToString();
            if (string.IsNullOrEmpty(id))
            {
                Response.Redirect("main.aspx");
            }
            else
            {
                SqlConnection MyConnection = new SqlConnection(TestCon.ASPNETcourseConnectionString);
                //产生select语句
                string sqlstr = string.Format("select * from jd where j_id='{0}'", id);
                //连接数据库，进行判断
                string[] col = new string[] { "j_id", "j_name", "j_place", "j_price", "j_img", "jd_img", "j_ind", "j_opendate", "j_phone" };
                dt = SQLCommand.dSqlCommand(sqlstr, MyConnection, col);
                Session["Price"] = dt.Rows[0]["j_price"].ToString();
                //Label5.Text =  Session["Price"].ToString();
                

                foreach (DataRow dr in dt.Rows)
                {

                    string image = dr["jd_img"].ToString().Trim();
                    imgLable.Text = "<img src=\"" + image + "\" src=\"detail.aspx?ID=" + dr["j_id"].ToString().Trim() + "\" />";

                    TableRow tr = new TableRow();
                    
                    TableCell tc = new TableCell();
                   
                   
                    tc = new TableCell();
                    //tc.Style.Add(HtmlTextWriterStyle.BackgroundColor, "green");   //name
                    tc.Width = 300;
                    tc.Height = 40;
                    Label lab = new Label();
                    string name = dr["j_name"].ToString().Trim();
                    lab.Text ="景点：" + name;
                    tc.Controls.Add(lab);
                    tr.Controls.Add(tc);
                    Table1.Controls.Add(tr);

                    tr = new TableRow();
                    tc = new TableCell();
                    //tc.Style.Add(HtmlTextWriterStyle.BackgroundColor, "green");   //place
                    tc.Style.Add(HtmlTextWriterStyle.FontSize, "15px");
                    tc.Width = 300;
                    tc.Height = 40;
                    lab = new Label();
                    string place = dr["j_place"].ToString().Trim();
                    lab.Text = "位置：" + place;
                    tc.Controls.Add(lab);
                    tr.Controls.Add(tc);
                    Table1.Controls.Add(tr);

                    tr = new TableRow();
                    tc = new TableCell();
                    //tc.Style.Add(HtmlTextWriterStyle.BackgroundColor, "green");  //ind
                    tc.Width = 460;
                    tc.Height = 40;
                    lab = new Label();
                    string ind = dr["j_ind"].ToString().Trim();
                    lab.Text = "景点简介：" + ind;
                    tc.Controls.Add(lab);
                    tr.Controls.Add(tc);
                    Table1.Controls.Add(tr);

                    tr = new TableRow();
                    tc = new TableCell();
                    //tc.Style.Add(HtmlTextWriterStyle.BackgroundColor, "green");  //open
                    tc.Width = 600;
                    tc.Height = 60;
                    lab = new Label();
                    string open = dr["j_opendate"].ToString().Trim();
                    string phone = dr["j_phone"].ToString().Trim();

                    string nbsp = "";

                    for (int i = 0; i < 32; i++)
                        nbsp += "&nbsp;";

                        lab.Text = "开放时间：" + open + nbsp + "联系电话：" + phone;
                    tc.Controls.Add(lab);
                    tr.Controls.Add(tc);
                    Table1.Controls.Add(tr);

                    tr = new TableRow();
                    tc = new TableCell();
                    //tc.Style.Add(HtmlTextWriterStyle.BackgroundColor, "green");  //price
                    tc.Style.Add(HtmlTextWriterStyle.FontSize, "20px");
                    tc.Width = 460;
                    tc.Height = 55;
                    lab = new Label();
                    string price = dr["j_price"].ToString().Trim();

                    nbsp = "";

                    for (int i = 0; i < 38; i++)
                        nbsp += "&nbsp;";
                    lab.Text = "票价：" + price + "￥"+ nbsp + "<a href=\"car_update.aspx \">加入购物车 </a>";
                    tc.Controls.Add(lab);
                    tr.Controls.Add(tc);
                    Table1.Controls.Add(tr);

                }


            }

        }
         
        
    }
}