<%@ Page Language="C#" AutoEventWireup="true" CodeFile="admini_update2.aspx.cs" Inherits="admini_update2" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body style="height: 3421px; width: 1360px" background="images\slight.jpg">
    <form id="form1" runat="server" 
  >
    <div >
    <asp:GridView ID="GridView1" runat="server" 
            onrowcancelingedit="GridView1_RowCancelingEdit" 
            onrowdeleting="GridView1_RowDeleting" onrowediting="GridView1_RowEditing" 
            onrowupdating="GridView1_RowUpdating">
        <Columns>
            <asp:CommandField ShowEditButton="True" />
            <asp:CommandField ShowDeleteButton="True" />
        </Columns>
    </asp:GridView>
     </div>
    </form>
</body>
</html>
