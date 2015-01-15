using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;

public partial class admini_update2 : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (!IsPostBack)
        {
            Bind();
        }
    
        //SqlConnection MyConnection = new SqlConnection(TestCon.ASPNETcourseConnectionString);

        //string sqlstr = string.Format("select * from goods ");
        //string[] col = new[] { "ID", "price", "name", "introduction", "imageurl" };
        //DataTable dt = SQLCon.dSqlCommand(sqlstr, MyConnection, col);
        //GridView1.DataSource = dt;
        //GridView1.DataBind();
    }
    private void Bind()
    {
        SqlConnection MyConnection = new SqlConnection(TestCon.ASPNETcourseConnectionString);
        //        string sqlstr = string.Format("delete from passwords where UserName='{0}'", TextBox1.Text);
        string sqlstr = string.Format("select * from goods ");
        string[] col = new[] { "ID", "price", "name", "introduction", "imageurl" };
        DataTable dt = SQLCon.dSqlCommand(sqlstr, MyConnection, col);
        GridView1.DataKeyNames = new string[] { "ID" };
        GridView1.DataSource = dt;
        GridView1.DataBind();
        //for (int i = 0; i < GridView1.Rows.Count; i++)
        //{
        //    Label l = (Label)GridView1.Rows[i].FindControl("Label3");
        //    if (l != null)
        //        l.Text = (Convert.ToInt32(dt.Rows[GridView1.PageIndex * GridView1.PageCount + i][1].ToString()) * Convert.ToInt32(dt.Rows[GridView1.PageIndex * GridView1.PageCount + i][2].ToString())).ToString();
        //}
    }
    protected void GridView1_RowDeleting(object sender, GridViewDeleteEventArgs e)
    {
        SqlConnection MyConnection = new SqlConnection(TestCon.ASPNETcourseConnectionString);
        //        string sqlstr = string.Format("delete from passwords where UserName='{0}'", TextBox1.Text);
        string sqlstr = string.Format("delete  from goods where ID=");
        sqlstr += GridView1.DataKeys[e.RowIndex].Value.ToString();
        if (1 != SQLCon.iSqlCommand(sqlstr, MyConnection))
        {
            Response.Write("<script>alert('删除失败！')</script>");
        }
        else
        {
            Bind();
        }

    }
    protected void GridView1_RowEditing(object sender, GridViewEditEventArgs e)
    {
        GridView1.EditIndex = e.NewEditIndex;
        Bind();
    }
    protected void GridView1_RowUpdating(object sender, GridViewUpdateEventArgs e)
    {
        SqlConnection MyConnection = new SqlConnection(TestCon.ASPNETcourseConnectionString);
        //        string sqlstr = string.Format("delete from passwords where UserName='{0}'", TextBox1.Text);
        string MySQL = "update goods set price='" + ((TextBox)GridView1.Rows[e.RowIndex].Cells[3].Controls[0]).Text
            + "' ,  name='" + ((TextBox)GridView1.Rows[e.RowIndex].Cells[4].Controls[0]).Text + "' ,  introduction='" + ((TextBox)GridView1.Rows[e.RowIndex].Cells[5].Controls[0]).Text
             + "' ,  imageurl='" + ((TextBox)GridView1.Rows[e.RowIndex].Cells[6].Controls[0]).Text
            + "' where ID=" + GridView1.DataKeys[e.RowIndex].Value.ToString();
        /*string MySQL =string.Format("update goods set ID='{0}' , ='{1}' , price='{2}' , name='{3}', introduction='{4}')  where imageurl={5}")*/
       
        if (1 != SQLCon.iSqlCommand(MySQL, MyConnection))
        {
            Response.Write("<script>alert('更新失败！')</script>");
        }
        else
        {
            GridView1.EditIndex = -1;
            Bind();
        }
    }
    protected void GridView1_RowCancelingEdit(object sender, GridViewCancelEditEventArgs e)
    {
        GridView1.EditIndex = -1;
        Bind();
    }

    protected void GridView1_PageIndexChanging(object sender, GridViewPageEventArgs e)
    {
        GridView1.PageIndex = e.NewPageIndex;
        Bind();
    }
    protected void GridView1_SelectedIndexChanged(object sender, EventArgs e)
    {

    }

}