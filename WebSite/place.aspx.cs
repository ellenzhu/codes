using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data.SqlClient;
using System.Data;
using System.Web.UI.HtmlControls;

public partial class place : System.Web.UI.Page
{
    private DataTable dt = null;

    protected void Page_Load(object sender, EventArgs e)
    {

        string id = Session["location"].ToString();

        SqlConnection MyConnection = new SqlConnection(TestCon.ASPNETcourseConnectionString);
        //产生select语句
        string sqlstr = string.Format("select * from jd where j_place like '%{0}%' ", id);
        //连接数据库，进行判断
        string[] col = new string[] { "j_id", "j_name", "j_place", "j_price", "j_img", "jd_img", "j_ind", "j_opendate", "j_phone" };
        dt = SQLCommand.dSqlCommand(sqlstr, MyConnection, col);
        
        int num = 0;
        foreach (DataRow dr in dt.Rows)
        {
            if (num >= 5)
                break;
            TableRow tr = new TableRow();
           
            TableCell tc = new TableCell();
            tc.Style.Add(HtmlTextWriterStyle.TextAlign, "left");
            Label lab = new Label();
            string image = dr["j_img"].ToString().Trim();
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
            lab.Text = place;
            tc.Controls.Add(lab);
            tr.Controls.Add(tc);

            tc = new TableCell();
            tc.Style.Add(HtmlTextWriterStyle.Width, "150px");
            tc.Style.Add(HtmlTextWriterStyle.FontSize, "30px");
            tc.Style.Add(HtmlTextWriterStyle.TextAlign, "right");
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

   }


}