<%@ Page Language="C#" AutoEventWireup="true" MasterPageFile="~/MasterPage.master" CodeFile="pwd_update.aspx.cs" Inherits="update_pwd" %>

  
<asp:Content ID="HeaderContent" runat="server" ContentPlaceHolderID="HeadContent">
</asp:Content>

<asp:Content ID="BodyContent" runat="server" ContentPlaceHolderID="MainContent">
 <div style=" margin-top:92px; text-align:center"> 
   <div>
    用户密码修改
    </div>
    <div style="height: 82px">
    <div>用户名<asp:TextBox ID="TextBox3" runat="server"></asp:TextBox></div>
    <div>新密码<asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
        </div>
    <div>确认密码<asp:TextBox ID="TextBox2" runat="server"></asp:TextBox>
        </div>
     
    <div> 
          
         <asp:Button ID="Button2" runat="server" onclick="Button1_Click" Text="确认" />
        </div>

    </div>
    </div>
</asp:Content>