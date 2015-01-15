<%@ Page Language="C#" AutoEventWireup="true" MasterPageFile="~/MasterPage.master" CodeFile="Register.aspx.cs" Inherits="Register" %>

  
<asp:Content ID="HeaderContent" runat="server" ContentPlaceHolderID="HeadContent">
</asp:Content>

<asp:Content ID="BodyContent" runat="server" ContentPlaceHolderID="MainContent">
     
 
    <div style="height: 291px; width:900px; background-image :url('img/login.jpg'); background-repeat :repeat-x; text-align : center;  margin-left :220px; margin-top:40px">
        
        <div style="height: 69px; width: 100%">
          <p style="font-family : @Adobe 楷体 Std R  ; font-size : x-large ; font-weight :bold ; color:  #0000cc; margin-top :5px">注册信息</p>
        </div>
     
        <div>
    <table  style=" margin-left:100px">
        <tr>
            <td class="style4">
                <asp:Label ID="L2" runat="server" Text="用户名"></asp:Label>
            </td>
            <td class="style5">
                <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td class="style4">
                <asp:Label ID="L3" runat="server" Text="手机"></asp:Label>
            </td>
            <td class="style5">
                <asp:TextBox ID="TextBox2" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td class="style6">
                <asp:Label ID="L4" runat="server" Text="邮箱"></asp:Label>
            </td>
            <td class="style7">
                <asp:TextBox ID="TextBox3" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td class="style4">
                <asp:Label ID="L5" runat="server" Text="密码"></asp:Label>
            </td>
            <td class="style5">
                <asp:TextBox ID="TextBox4" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td class="style4">
                <asp:Label ID="L6" runat="server" Text="重复密码"></asp:Label>
            </td>
            <td class="style5">
                &nbsp;&nbsp;
                <asp:TextBox ID="TextBox5" runat="server"></asp:TextBox>
                &nbsp;<asp:Label ID="Label1" runat="server"></asp:Label>
            </td>
        </tr>
        <tr>
            <td>
                &nbsp;</td>
        </tr>
    </table>
    </div>
    <p style=" margin-left:0px">

        <asp:Button ID="Button1" runat="server" Height="30px"  Width="56px" onclick="Button1_Click" Text="提交" />
    &nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Button ID="Button2" runat="server" Height="30px"  Width="56px" Text="重置" 
            onclick="Button2_Click" />
    </p>
    </div>
    <asp:SqlDataSource ID="SqlDataSource1" runat="server" 
        ConnectionString="<%$ ConnectionStrings:gowConnectionString %>" 
        SelectCommand="SELECT * FROM [register]"></asp:SqlDataSource>
  </asp:Content>