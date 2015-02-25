//$(function() {
//
////    //  $('#search').keyup(function() {
////    $('#search').keypress(function (e) {
////        if (e.which == 13)
////        {
////            //$.ajax({
////            //    type: "POST",
////            //    url: "/games/search/",
////            //    data: {
////            //        //'search_text' : document.getElementById("#search")
////            //        'search_text': $('#search').val()
////            //     //   'csrfmiddlewaretoken': $("input[name=csrfmiddlewaretoken]").val()
////            //    },
////            //    success: searchSuccess,
////            //    dataType: 'html'
////            //});
////setTimeout(function() {
////		window.location.href = "http://127.0.0.1:8000/games/";
////	}, 5000);
////
////       }
////
////    });
//    $('#search').keypress(function (e) {
//        var str = $('#search').val();
//        var domain = "http://www.yourdomain.com";
//        var url = domain + "default.aspx?search=" + str;
//        if (e.keyCode == 13) {
//            location.href = url;
//        }
//    });
//});
////function searchSuccess(data, textStatus, jqXHR)
////{
////    //$('#search-results').html(data);
////};