<%@ Page Language="C#" AutoEventWireup="true" CodeFile="main.aspx.cs" Inherits="main" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
    </head>
<body style="background-image:url(img/bg.jpg)">
    <form id="form1" runat="server" >
    <div style="height: 60px; width:900px;margin-left :162px; ">
       
        
        
        <div style=" float :left; margin-top :0px;   width: 900px; height: 10px; margin-right: 10px;">
          <ul style="margin-top: 0px; margin-left :650px; width: 350px; font-size:16px; margin-bottom: 0px;">
            
            <li style =" display :inline ; margin-right :20px">
              
                <asp:Label ID="Label1" runat="server" Text="登录"></asp:Label>
              
            </li>
            
            <li style =" display :inline ; margin-right :20px">
              <a href ="Register.aspx" >
                <asp:Label ID="Label2" runat="server" Text="注册"></asp:Label>
              </a>
            </li>
            
            <li style =" display :inline ; margin-right :20px">
              <a href ="update.aspx" >
                <asp:Label ID="Label3" runat="server" Text="个人信息"></asp:Label>
              </a>
            </li>
            
            <li style =" display :inline ; margin-right :20px">
              <a href ="car_update.aspx" >
                <asp:Label ID="Label4" runat="server" Text="购物车"></asp:Label>
              </a>
            </li>
          </ul>
        </div>
        
        <div style=" float :left; margin-top :30px; margin-left: 0px; width:900px; height: 40px;">
        <ul style="margin-top: 0px; margin-left:210px; font-size:20px; font-weight :bolder">
         <li style =" display :inline ; margin-right :50px; font-size:25px"><a href ="main.aspx">首页</a></li>

         <li style =" display :inline ; margin-right :50px">
         <asp:LinkButton ID="LinkButton1" runat="server" onclick="LinkButton1_Click">北碚</asp:LinkButton>
         </li>

         <li style =" display :inline ; margin-right :50px"><asp:LinkButton ID="LinkButton2" 
                 runat="server" onclick="LinkButton2_Click">永川</asp:LinkButton></li>
         <li style =" display :inline ; margin-right :50px"><asp:LinkButton ID="LinkButton3" 
                 runat="server" onclick="LinkButton3_Click">奉节</asp:LinkButton></li>
         <li style =" display :inline ; margin-right :50px"><asp:LinkButton ID="LinkButton4" 
                 runat="server" onclick="LinkButton4_Click">武隆</asp:LinkButton></li>
         <li style =" display :inline ; margin-right :50px"><a href ="main.aspx">更多>></a></li>
        </ul>
        </div>
        
    </div>

    <hr style=" width:1200px" />

    <div style="height: 1534px; width:900px;margin-left :280px; text-align: center">
  
    
     <div  style=" margin-top:22px">
         
        <asp:Table runat="server" ID="Table1">
            
        </asp:Table>
     
     </div>
    </div>
    <div style="height: 43px; width:900px;margin-left :250px; text-align: center; margin-bottom: 0px;">
      <p><a href ="admini_update1.aspx">商品后台管理</a></p>
     
    </div>
    </form>
    
</body>
</html>
