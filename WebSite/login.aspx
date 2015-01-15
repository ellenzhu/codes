<%@ Page Language="C#" AutoEventWireup="true" MasterPageFile="~/MasterPage.master" CodeFile="login.aspx.cs" Inherits="login" %>

  
<asp:Content ID="HeaderContent" runat="server" ContentPlaceHolderID="HeadContent">
</asp:Content>

<asp:Content ID="BodyContent" runat="server" ContentPlaceHolderID="MainContent">
   
    
    <div style="height: 100%; width:900px; background-image :url(img/login.jpg); background-repeat :repeat-x; text-align : center;  margin-left :220px; margin-top:40px ">
        <div style="height: 69px; width: 100%">
          <p style="font-family : @Adobe 楷体 Std R ;font-size : x-large; font-weight :bold; color:  #0000cc; height: 43px; margin-top :5px">欢迎登陆</p>
        </div>
        <div style="height: 233px">
            <div style="height: 43px">
                 
                <p style="font-size :medium ; display : inline ">用户名&nbsp; </p>  
                <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
            </div>
            <div style="height: 43px">
                 
                <p style="font-size :medium ; display : inline ">密码&nbsp;&nbsp; </p> 
                <asp:TextBox ID="TextBox2" runat="server" TextMode="Password"></asp:TextBox>
            </div>
            <div style="height: 43px">
                
                <asp:Button ID="Button1" runat="server" Height="30px" Text="登陆" Width="56px" 
                    onclick="Button1_Click" />
                </div>
            <div>
                <p style =" font-size : small ">您还没有账号，<a href ="Register.aspx">点击注册</a></p>
            </div>
        </div>
    </div>
    
    <asp:SqlDataSource ID="SqlDataSource1" runat="server" 
        ConnectionString="<%$ ConnectionStrings:gowConnectionString2 %>" 
        SelectCommand="SELECT [name], [pwd] FROM [password]"></asp:SqlDataSource>
    
  </asp:Content>
