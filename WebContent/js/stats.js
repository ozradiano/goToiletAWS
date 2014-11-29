/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
checkCookieForRedirect();

var allData = null;

google.load("visualization", "1", {packages: ["corechart"]});

function getData(type) {
    var data = {
        kidId: QueryString.id,
        statisticType: type,
        daysFromToday: 30
     };
    $.ajax({
        url: SERVER_URL + "/viewStatistics",
        method: 'GET',
        data: JSON.stringify(data),
        success: function(resData) {
            allData = JSON.parse(resData);
            
        },
        error: function() {
            alert("server error");
        }
    });
}


function getDataFirstTime() {
    var data = {
        kidId: QueryString.id,
        daysFromToday: 30
     };
    $.ajax({
        url: SERVER_URL + "/viewAllStatistics",
        method: 'GET',
        data: JSON.stringify(data),
        success: function(resData) {
            var parsedData = JSON.parse(resData);
            setTableData(parsedData.data.arrayValues);
        },
        error: function() {
            alert("server error");
        }
    });
}

function fixElementsApperance() {
    $(".profile_picture").width($(".profile_picture").height());
    $(".burger_btn").width($(".burger_btn").height() * 1.91);
    $(".back_btn").css("margin-top", "-" + ($(".back_btn").height() / 2) + "px");
    $(".burger_btn").css("margin-top", "-" + ($(".burger_btn").height() / 2) + "px");
    $(".profile_picture").css("margin-top", "-" + ($(".profile_picture").height() / 2) + "px");
    $(".profile_name").css("margin-top", "-" + ($(".profile_name").height() / 2) + "px");
    $(".profile_name").css("right", (Math.ceil($(".profile_picture").width() * 1.7)) + "px");

}

function setTableData() {
    if (allData == null) {
        setTimeout("setTableData()",1000);
        return;
    }
    
    google.setOnLoadCallback(drawChart);
    function drawChart() {
        var parsedData =  new Array();
        parsedData[0] = ['הצלחות', 'ימים'];
        for (var i = 0; i < allData.data.arrayValues.length; i++) {
            parsedData[i+1] = [allData.data.arrayValues[i].value, allData.data.arrayValues[i].date];
        }
        var tableDataObject = google.visualization.arrayToDataTable(parsedData);

        var tableOptions = {
            title: 'הצלחות',
            hAxis: {title: 'הצלחות', titleTextStyle: {color: '#333'}},
            vAxis: {title: 'ימים', titleTextStyle: {color: '#333'}, minValue: 0},
            'width': $("#mainContent").width() * 0.75,
            'height': $("#mainContent").height() * 0.65
        };

        var chart1 = new google.visualization.AreaChart(document.getElementById('chart_div1'));
        //var chart2 = new google.visualization.AreaChart(document.getElementById('chart_div2'));
        chart1.draw(tableDataObject, tableOptions);
        //chart2.draw(data, options);
    }
}

getDataFirstTime();

$(document).ready(function() {
    setTableData();
    //alert(document.getElementById("childName") + ":::" + QueryString.name);
    document.getElementById("childName").innerHTML = decodeURI(QueryString.name);
    document.getElementById("profilePicture").src = "images/" + QueryString.img;

    $("#success").click(function() {
        if ($("#success_arrow").hasClass("up")) {
            $("#success_arrow").removeClass("up").addClass("down");
        } else {
            $("#success_arrow").removeClass("down").addClass("up");
        }
        $("#chart_div1").slideToggle();
    });

    $("#times").click(function() {
        if ($("#times_arrow").hasClass("up")) {
            $("#times_arrow").removeClass("up").addClass("down");
        } else {
            $("#times_arrow").removeClass("down").addClass("up");
        }
        $("#chart_div2").slideToggle();
    });
    setTimeout("fixTables();", 1000);





});

function fixTables() {
    $("#chart_div2").hide();
}






