<%@ Page Language="C#" AutoEventWireup="true" MasterPageFile="~/MasterPage.master" CodeFile="car_update.aspx.cs" Inherits="car_update" %>

  
<asp:Content ID="HeaderContent" runat="server" ContentPlaceHolderID="HeadContent">
</asp:Content>

<asp:Content ID="BodyContent" runat="server" ContentPlaceHolderID="MainContent">
   <div style=" margin-top:50px; text-align:center">

   <div style=" font-weight:bolder; margin-top:10px; font-size:larger">
   信息填写
   </div>
    <div style="height: 162px; margin-top:40px">

    
        <asp:Label ID="Label5" runat="server" Text="用户名"></asp:Label>
        <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
        <br />
        <asp:Label ID="Label6" runat="server" Text="联系电话"></asp:Label>
        <asp:TextBox ID="TextBox2" runat="server"></asp:TextBox>
        <br />
        <br />
        <asp:Label ID="Label7" runat="server" Text="订票数"></asp:Label>
        <asp:TextBox ID="TextBox3" runat="server" AutoPostBack="True" 
            ontextchanged="TextBox3_TextChanged"></asp:TextBox>
        <br />
        <br />
        总金额<asp:Label ID="Label8" runat="server"></asp:Label>

    
    </div>
    <div>
        <asp:Button ID="Button1" runat="server" Text="生成订单" onclick="Button1_Click" />
     
    </div>
    </div>
  </asp:Content>