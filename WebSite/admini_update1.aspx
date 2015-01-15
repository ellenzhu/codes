<%@ Page Language="C#" AutoEventWireup="true"  MasterPageFile="~/MasterPage.master" CodeFile="admini_update1.aspx.cs" Inherits="admini_update1" %>

<script runat="server">

</script>
<asp:Content ID="HeaderContent" runat="server" ContentPlaceHolderID="HeadContent">
</asp:Content>

<asp:Content ID="BodyContent" runat="server" ContentPlaceHolderID="MainContent">
<div style="background-image: url('images/1.jpg')">
   管理员管理商品页面
   
    添加商品信息:
    <br />
    <br />
    &nbsp;&nbsp
    <asp:Label ID="Label5" runat="server" Text="景区ID"></asp:Label>
    &nbsp;&nbsp;&nbsp
    <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
    <br />
    <br />
    &nbsp;&nbsp;
    <asp:Label ID="Label6" runat="server" Text="景区名"></asp:Label>
    &nbsp;&nbsp;&nbsp
    <asp:TextBox ID="TextBox2" runat="server"></asp:TextBox>
    <br />
    <br />
    &nbsp;&nbsp
    <asp:Label ID="Label7" runat="server" Text="门票价格"></asp:Label>
    &nbsp;&nbsp;
    <asp:TextBox ID="TextBox3" runat="server"></asp:TextBox>
    <br />
    <br />
    
    &nbsp;&nbsp;<asp:Label ID="Label9" runat="server" Text="景区描述"></asp:Label>
&nbsp;&nbsp;&nbsp;
    <asp:TextBox ID="TextBox4" runat="server" 
        TextMode="MultiLine"></asp:TextBox>
    <br />
    <br />
     
        <table border = 1 width=200 height=100 >
        <tr><th>添加景区图片</th><th class="style1">显示景区图片</th>           
        <tr><td><asp:FileUpload ID="FileUpload1" runat="server" />
    
            </td><td class="style1">
                <asp:Image ID="Image1" runat="server" Width="177px" Height="156px" /></td>  
        <tr><td>从数据库中读取的图片</td><td>
            <asp:Image ID="Image2" runat="server" style="margin-left: 0px" />
            </td></tr>
        </table> 

        <asp:Label ID="Label10" runat="server"></asp:Label>
    <br />

        <br />
        
        <asp:Button ID="Button10" runat="server" Height="23px" onclick="Button10_Click" Text="景区信息入库" Width="126px" />
        
        <br />
    <br />
    
    <br />
        <div>
        <a href ="admini_update2.aspx">编辑景区信息</a>
       </div>
        <br />
    </div>
    &nbsp;&nbsp&nbsp;

    </div>
    </asp:Content>
   
    