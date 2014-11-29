/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
checkCookieForRedirect();

var kidTemplate = '<img src="[IMG_SRC]" class="profile_picture v_align background_cover"/>' +
        '<div class="profile_name v_align">[NAME_OF_CHILD]</div>';

var childrenData = null;

function getChildrenData() {
    var data = {
        userId: UID
    };
    $.ajax({
        url: SERVER_URL + "/viewGarden",
        method: 'POST',
        data: JSON.stringify(data),
        success: function(resData) {
            childrenData = JSON.parse(resData);
        },
        error: function() {
            alert("server error");
        }
    });
}

function fixElementsApperance() {
    $(".profile_picture").width($(".profile_picture").height());
    $(".burger_btn").width($(".burger_btn").height() * 1.91);
    $(".burger_btn").css("margin-top", "-" + ($(".burger_btn").height() / 2) + "px");
    $(".profile_picture").css("margin-top", "-" + ($(".profile_picture").height() / 2) + "px");
    $(".profile_name").css("margin-top", "-" + ($(".profile_name").height() / 2) + "px");
    //$(".profile_name").css("right", (Math.ceil($(".profile_picture").css('right'); * 1.7)) + "px");
}

function addChildrenToPage() {
    if (childrenData == null) {
         setTimeout("addChildrenToPage()",1000); 
         return;
    }
    
    var gardenName = childrenData.name;
    document.getElementById("header").innerHTML = gardenName;
    var allChildern = data.data.arrayValues;
    var mainDiv = document.getElementById("mainContent");

    if (userType == "2" || userType == 2) {
        window.location = SERVER_URL + "/child.html?id=" + allChildern[0].kidId + "&name=" + allChildern[0].kidName + "&img=" + allChildern[0].imageLink;
    } else {
        for (var i = 0; i < allChildern.length; i++) {
            var newChildDiv = document.createElement("div");
            newChildDiv.className = "kid_header";
            newChildDiv.onclick = getKidFunction(allChildern[i].kidId, allChildern[i].kidName, allChildern[i].imageLink);
            var innerContant = kidTemplate.replace('[NAME_OF_CHILD]', allChildern[i].kidName);
            innerContant = innerContant.replace('[IMG_SRC]', SERVER_URL + '/images/' + allChildern[i].imageLink);
            newChildDiv.innerHTML = innerContant;
            mainDiv.appendChild(newChildDiv);

            var spacer = document.createElement("div");
            spacer.className = "spacer";
            spacer.innerHTML = "&nbsp;";
            mainDiv.appendChild(spacer);
        }
        
        var footerDiv = document.createElement("div");
        //footerDiv.className = "kid_header";
        footerDiv.innerHTML = "<div style='text-align:center;'>All rights reserved to Go Toilet© Application</div>";
        mainDiv.appendChild(footerDiv);
    }

    fixElementsApperance();
    setTimeout("fixElementsApperance();", 2500);
}

function getKidFunction(id, name, img) {
    return function() {
        window.location = SERVER_URL + "/child.html?id=" + id + "&name=" + name + "&img=" + img;
    };
}

getChildrenData();

$(document).ready(function() {
    addChildrenToPage();
});





