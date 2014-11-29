/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

checkCookieForRedirect();


var formValues = [];
formValues["action-poo"] = "false";
formValues["action-pee"] = "true";
formValues["radio-status"] = "0";
formValues["radio-kid-init"] = "false";
formValues["radio-open-door"] = "fullHelp";
formValues["radio-close-door"] = "fullHelp";
formValues["radio-drop-pants"] = "fullHelp";
formValues["radio-drop-underware"] = "fullHelp";
formValues["radio-wipe"] = "fullHelp";
formValues["radio-lift-underware"] = "fullHelp";
formValues["radio-lift-pants"] = "fullHelp";
formValues["radio-flush"] = "fullHelp";
formValues["radio-wash-hands"] = "fullHelp";
formValues["radio-wipe-hands"] = "fullHelp";


function getAndFixTime() {
    $.ajax({
        url: SERVER_URL + "/getTime",
        method: 'GET',
        success: function(resData) {
            var data = JSON.parse(resData);
            var time = data.hour;
            var date = data.date;

            $("#currentTime").val(time);
            $("#currentDate").val(date);
        },
        error: function() {
            alert("server error");
        }
    });
}

function buildJSONObject() {
    var obj = {
        insertingUserId: UID,
        kidID: QueryString.id,
        dateTime: $("#currentTime").val() + " " + $("#currentDate").val(),
        successResult: formValues["radio-status"],
        createdIndependenceStages: [
            {independenceStage: "pantsUp", assistantLevel: formValues["radio-lift-pants"]},
            {independenceStage: "pantiesDown", assistantLevel: formValues["radio-drop-underware"]},
            {independenceStage: "pantiesUp", assistantLevel: formValues["radio-lift-underware"]},
            {independenceStage: "doorOpen", assistantLevel: formValues["radio-open-door"]},
            {independenceStage: "doorClose", assistantLevel: formValues["radio-close-door"]},
            {independenceStage: "handsWashed", assistantLevel: formValues["radio-wash-hands"]},
            {independenceStage: "wipe", assistantLevel: formValues["radio-wipe"]},
            {independenceStage: "flushWater", assistantLevel: formValues["radio-flush"]},
            {independenceStage: "handsDry", assistantLevel: formValues["radio-wipe-hands"]},
            {independenceStage: "pantsDown", assistantLevel: formValues["radio-drop-pants"]}
        ],
        comments: document.getElementById("comment").value,
        kidIsInitiator: formValues["radio-kid-init"],
        isKaki: formValues["action-poo"],
        isPipi: formValues["action-pee"]
    };
    return obj;
}

function fixElementsApperance() {
        
        $("#profilePicture").width($("#profilePicture").height());
        $(".time_box").css("margin-top", "-" + ($(".time_box").height() / 2) + "px");
        $(".burger_btn").width($(".burger_btn").height() * 1.91);
        $(".burger_btn").css("margin-top", "-" + ($(".burger_btn").height() / 2) + "px");
        $(".pee").width($(".burger_btn").height() * 1);
        $(".poo").width($(".burger_btn").height() * 1.30);
        $(".profile_picture").css("margin-top", "-" + ($(".profile_picture").height() / 2) + "px");
        $(".profile_name").css("margin-top", "-" + ($(".profile_name").height() / 2) + "px");
        $(".profile_name").css("right", (Math.ceil($(".profile_picture").width() * 1.7)) + "px");
        $("[name=checkbox]").width($("[name=checkbox]").height());
        $(".radio_btn").width($(".radio_btn").height());
        $(".radio_btn_small").width($(".radio_btn_small").height());
        $("textarea").click(function() {
            $("textarea").html("");
        });
    }
    
$(document).ready(function() {

    getAndFixTime();
    
    function setRadios() {
        var allCB = document.getElementsByName("checkbox");
        for (var i = 0; i < allCB.length; i++) {
            var id = allCB[i].id;
            //$("#"+id).bind('mousedown touchstart', getRadioDownFunction(id));
            //$("#"+id).bind('mouseup touchstart', getRadioDownFunction(id));
            //.on('click touchstart'
            allCB[i].onclick = getCBClickFunction(id);
        }
    }



    function setRadios() {
        var allRadios = document.getElementsByClassName("radio_btn");
        for (var i = 0; i < allRadios.length; i++) {
            var name = allRadios[i].getAttribute("name");
            var id = allRadios[i].id;
            //$("#"+id).bind('mousedown touchstart', getRadioDownFunction(id));
            //$("#"+id).bind('mouseup touchstart', getRadioDownFunction(id));
            //.on('click touchstart'
            allRadios[i].onclick = getRadioClickFunction(name, id);
        }

        var allRadios = document.getElementsByClassName("radio_btn_small");
        for (var i = 0; i < allRadios.length; i++) {
            var name = allRadios[i].getAttribute("name");
            var id = allRadios[i].id;
            //$("#"+id).bind('mousedown touchstart', getRadioDownFunction(id));
            //$("#"+id).bind('mouseup touchstart', getRadioDownFunction(id));
            //.on('click touchstart'
            allRadios[i].onclick = getSmallRadioClickFunction(name, id);
        }
    }

    function getRadioClickFunction(name, id) {
        return function() {
            $('[name="' + name + '"]').removeClass("radio_on").addClass("radio_off");
            $('#' + id).removeClass("radio_off").addClass("radio_on");
            formValues[name] = document.getElementById(id).getAttribute("value");
        };
    }

    function getSmallRadioClickFunction(name, id) {
        return function() {
            $('[name="' + name + '"]').removeClass("radio_small_on").addClass("radio_small_off");
            $('#' + id).removeClass("radio_small_off").addClass("radio_small_on");
            formValues[name] = document.getElementById(id).getAttribute("value");
        };
    }

    function getRadioDownFunction(id) {
        return function(event) {
            $('#' + id).removeClass("radio_off").addClass("radio_hover");
        };
    }

    $("#independentLine").click(function(){
        if ($("#arrow").hasClass("up")) {
            $("#arrow").removeClass("up").addClass("down");
        } else {
            $("#arrow").removeClass("down").addClass("up");
        }
      $("#bottom_table").slideToggle();
    });

    fixElementsApperance();
     $("#bottom_table").hide();
    
    setRadios();
    document.getElementById("childName").innerHTML = decodeURI(QueryString.name);
    document.getElementById("profilePicture").src = "images/" + QueryString.img;


});


function postToServer() {
    var data = buildJSONObject();
    $.ajax({
        url: SERVER_URL + "/newEvent",
        method: 'POST',
        data: JSON.stringify(data),
        success: function(resData) {
            var data = JSON.parse(resData);
            if (data.success == -1 || data.success == "-1") {
                alert("Server Error");
            } else {
                window.location = SERVER_URL + "/child.html?id=" + QueryString.id + "&name=" + QueryString.name + "&img=" + QueryString.img;
            }
        },
        error: function() {
            alert("server error");
        }
    });
}


function cbClick(id) {
    var cb = document.getElementById("action-"+id);
    
    if (cb.className == "checkbox_on") {
        cb.className = "checkbox_off";
    } else {
        cb.className = "checkbox_on";
    }
    formValues[cb.id] = !formValues[cb.id];
}