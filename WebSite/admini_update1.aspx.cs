using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;

public partial class admini_update1 : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        
    }
        protected void Button10_Click(object sender, EventArgs e)
        {
        //数据库存的是filepath：图片相对路径
        //真正的图片上传到pics文件夹下
        FileUpload1.SaveAs(Server.MapPath("~/images/") + "\\" + FileUpload1.FileName);
        string filepath = @"~/images/" + FileUpload1.FileName;
        Image1.ImageUrl = filepath; //将filepath添加到数据库中

        InsertPic();
    }
        private void InsertPic()
        {
            SqlConnection MyConnection = new SqlConnection(SQLCommand.ASPNETcourseConnectionString);

            Label10.Text = "";
            //添加一行到数据库中
            string MySQL = string.Format("INSERT INTO goods(ID,price,name,introduction,imageurl) values({0},'{1}','{2}','{3}','{4}')", TextBox1.Text, TextBox3.Text, TextBox2.Text, TextBox4.Text, Image1.ImageUrl);
            //VALUES('"TextBox1.text"','"TextBox2.text"','"TextBox4.text"','";
           // MySQL += Image1.ImageUrl + "','"TextBox3.Text"');";

            if (SQLCommand.iSqlCommand(MySQL, MyConnection) == 1)
            {
                //重定向到自己，相当于刷新网页
                Response.Write("添加成功，点击查看按钮查看添加结果");
                Image2.ImageUrl = Image1.ImageUrl;
                //如果网页有绑定的控件，可以用下面的命令自动刷新页面，实现数据的自动显示
                //Response.Redirect("CommandForDataBase.aspx");
               /* TextBox1.Text = "";
                TextBox2.Text = "";
                TextBox3.Text = "";
                TextBox4.Text = "";
                Image2.ImageUrl = "";
                Image1.ImageUrl = "";*/
            }
            else
                Label10.Text = "添加失败";

        }



        protected void GridView1_SelectedIndexChanged(object sender, EventArgs e)
        {

        }
       
}