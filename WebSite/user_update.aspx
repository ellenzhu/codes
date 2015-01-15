<%@ Page Language="C#" AutoEventWireup="true" MasterPageFile="~/MasterPage.master" CodeFile="user_update.aspx.cs" Inherits="user_update" %>

  
<asp:Content ID="HeaderContent" runat="server" ContentPlaceHolderID="HeadContent">
</asp:Content>

<asp:Content ID="BodyContent" runat="server" ContentPlaceHolderID="MainContent">
  <div style=" margin-top:60px; text-align:center">
    <div>
    用户信息修改
    </div>
    <div style="height: 102px">
    <div>用户名<asp:TextBox ID="TextBox1"  runat="server" ></asp:TextBox>
        </div>
    <div>邮箱<asp:TextBox ID="TextBox2" runat="server"></asp:TextBox>
        </div>
    <div>电话<asp:TextBox ID="TextBox3" runat="server"></asp:TextBox>
        </div>
    <div> 
        
        </div>

    </div>
    <p>
        
         <asp:Button ID="Button2" runat="server" onclick="Button1_Click" Text="确认" /></asp:Button>
        </p>
  </div>
  </asp:Content>