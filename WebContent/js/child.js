    /* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
checkCookieForRedirect();

var pageLoaded = false;

var rowTemplate =   "<td>[DATE_TIME]</td>"+
                    "<td class='details'>"+
                        "<div class='[INIT]'>הליכה יזומה</div>"+
                        "<div class='spacer'>&nbsp;</div>"+
                        "<div class='[MISS]'>פספוס</div>"+
                    "</td>"+
                    "<td class='sign'>[PIPI]</td>"+
                    "<td class='sign'>[KAKI]</td>";
            
var childData = null;

function getChildActivityByDays(days) {
    var data = {
        kidId: QueryString.id,
        daysFromToday: days
    };
    $.ajax({
        url: SERVER_URL + "/viewKid",
        method: 'POST',
        data: JSON.stringify(data),
        success: function(resData) {
            childData = JSON.parse(resData);
            addChildInfo();
        },
        error: function() {
            alert("server error");
        }
    });
}


var userType = getCookie("type");

getChildActivityByDays(1);

function addChildInfo() {
    if (!pageLoaded) {
        setTimeout("addChildInfo()",1000);
        return;
    }
    
    var rows = childData.data.arrayValues;
    var mainTable = document.getElementById("mainTable");
    for (var i = 0; i < rows.length; i++) {
        var tr = document.createElement("tr");
        var inner = rowTemplate;
        inner = inner.replace("[DATE_TIME]", rows[i].dateTime);
        if (rows[i].isKaki){
            inner = inner.replace("[KAKI]","&nbsp;+");
        } else {
            inner = inner.replace("[KAKI]","&nbsp;-");
        }
        
        if (rows[i].isPipi) {
            inner= inner.replace("[PIPI]","&nbsp;+");
        } else {
            inner = inner.replace("[PIPI]","&nbsp;+");
        }
             
        if (rows[i].kidIsInitiator) {
            inner = inner.replace("[INIT]","cb_v");
        } else {
            inner = inner.replace("[INIT]","cb");
        }
   
        if (rows[i].successResult) {
            inner = inner.replace("[MISS]","cb_v");
        } else {
            inner = inner.replace("[MISS]","cb");
        }
        
        tr.innerHTML = inner;
        mainTable.appendChild(tr);
    }
    
    
    fixElementsApperance();
}

function fixElementsApperance() {
    $(".profile_picture").width($(".profile_picture").height());
    $(".burger_btn").width($(".burger_btn").height() * 1.91);
    $(".back_btn").css("margin-top", "-" + ($(".back_btn").height() / 2) + "px");
    $(".burger_btn").css("margin-top", "-" + ($(".burger_btn").height() / 2) + "px");
    $(".profile_picture").css("margin-top", "-" + ($(".profile_picture").height() / 2) + "px");
    $(".profile_name").css("margin-top", "-" + ($(".profile_name").height() / 2) + "px");
    //$(".profile_name").css("right", (Math.ceil($(".profile_picture").width() * 1.7)) + "px");
    $(".radio_btn").width($(".radio_btn").height());
    $(".radio_btn_small").width($(".radio_btn_small").height());

}

function refreshList(days) {
    var mainTable = document.getElementById("mainTable");
    var allTr = mainTable.getElementsByTagName("tr");
    for (var i = 1; i < allTr.length; i++) {
        mainTable.removeChild(allTr[i]);
    }
    
    getChildActivityByDays(days);
    
}

$(document).ready(function() {
    pageLoaded = true;
    function getRadioClickFunction(name, id) {
        return function() {
            $('[name="' + name + '"]').removeClass("radio_on").addClass("radio_off");
            $('#' + id).removeClass("radio_off").addClass("radio_on");
            refreshList(document.getElementById(id).getAttribute("value"));
        };
    }
    
    document.getElementById("childName").innerHTML = decodeURI(QueryString.name);
    document.getElementById("profilePicture").src = "images/" + QueryString.img;
    document.getElementById("newEventLink").href = "add-event.html?id=" + QueryString.id + "&name=" +  QueryString.name + "&img=" +  QueryString.img;
    document.getElementById("statsLink").href = "stats.html?id=" + QueryString.id + "&name=" +  QueryString.name + "&img=" +  QueryString.img;
    
    var allRadios = document.getElementsByClassName("radio_btn");
    for (var i = 0; i < allRadios.length; i++) {
        var name = allRadios[i].getAttribute("name");
        var id = allRadios[i].id;
        //$("#"+id).bind('mousedown touchstart', getRadioDownFunction(id));
        //$("#"+id).bind('mouseup touchstart', getRadioDownFunction(id));
        //.on('click touchstart'
        allRadios[i].onclick = getRadioClickFunction(name, id);
    }
        
    addChildInfo();
});




