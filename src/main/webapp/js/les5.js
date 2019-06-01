window.onload = function(){
    initPage();
    $('#city').on( "click", function() {
        showWeather($('#lat').text(), $('#lon').text(), this.textContent);
    });
};


function initPage(){
    fetch('https://ipapi.co/json/')
        .then(function(response) {
            return response.json();
        })
        .then(function(ipData) {
            $('#countryCode').text(ipData['country']);
            $('#country').text(ipData['country_name']);
            $('#region').text(ipData['region']);
            $('#city').text(ipData['city']);
            $('#zip').text(ipData['postal']);
            $('#lat').text(ipData['latitude']);
            $('#lon').text(ipData['longitude']);
            $('#ip').text(ipData['ip']);
            
            showWeather(ipData['latitude'], ipData['longitude'], ipData['city']);

        }).then(function () {
            loadCountries();
        }).then(function () {
            addDeleteandUpdateListener();
        });
}


function showWeather(latitude, longitude, city){
    $('#cityHeader').text("Het weer in " + city);
    var key = 'c4247fb82eb9464c9a2b43b7bc2f28c3';
    fetch('https://api.openweathermap.org/data/2.5/weather?lat=' + latitude + '&lon=' + longitude + '&units=metric'+ '&appid=' + key)
    .then(function(resp) { return resp.json() }) // Convert data to json
    .then(function(data) {
        let weatherData = !(localStorage.getItem("weatherData") == null) ? JSON.parse(localStorage.getItem("weatherData")) : {};
        weatherData[city] = data;
        localStorage.setItem("weatherData", JSON.stringify(weatherData));

        $('#temp').text(data['main']['temp']);
        $('#humidity').text(data['main']['humidity']);
        $('#speed').text(data['wind']['speed']);
        $('#deg').text(data['wind']['deg']);
        let date = new Date(data['sys']['sunrise']*1000);
        let hours = date.getHours();
        let minutes = "0" + date.getMinutes();
        let seconds = "0" + date.getSeconds();
        let formattedTime = hours + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);
        $('#sunrise').text(formattedTime);
        date = new Date(data['sys']['sunset']*1000);
        hours = date.getHours();
        minutes = "0" + date.getMinutes();
        seconds = "0" + date.getSeconds();
        formattedTime = hours + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);
        
        $('#sunset').text(formattedTime);
    });
}

function loadCountries(){
    fetch('https://localhost:8443/Rest_Service/countries')
        .then(function(response) {
            return response.json()
        })
        .then(function(data) {
            for (const country of data) {
                $('#countryList table').append(
                    "<tr>" +
                        "<td><span>" + country['name'] + "</span></td>" +
                        "<td>" + country['capital'] + "</td>" +
                        "<td>" + country['region'] + "</td>" +
                        "<td>" + country['surface'] + "</td>" +
                        "<td>" + country['population'] + "</td>" +
                        "<td><i class='fas fa-edit' data-toggle='modal' data-target='#exampleModal'></i></td>" +
                        "<td><i class='fas fa-trash-alt' data-toggle='modal' data-target='#exampleModal'></i></td>" +
                    "</tr> + "
                )
            }
        })
        .then(function(){
            addonClick();
        });
}

function addonClick(){
    $('span').each(function(){
        $(this).click(function () {
            let reqExecuted = false;
            let currentElement = $(this).closest('tr');
            let localStorageData = JSON.parse(localStorage.getItem("requestHistory"));
            let requestHistory = localStorageData !== null ? localStorageData : {};
            let requestData = {
                "time": new Date(),
                "requestedCountry": currentElement.find(">:first-child").text(),
            };

            fetch('http://localhost:1337/Rest_Service/countries')
                .then(function(response) {
                    return response.json()
                })
                .then(function(data) {
                    for (const country of data) {
                        if (country['name'] === currentElement.find(">:first-child").text()){
                            let countryData = requestHistory[country['name']];
                            let time10MinutesAgo = new Date();
                            time10MinutesAgo.setMinutes(time10MinutesAgo.getMinutes() - 10);

                            if(countryData == null || Date.parse(countryData['time']) < time10MinutesAgo){
                                showWeather(country['latitude'], country['longitude'], country['name']);

                                requestHistory[requestData['requestedCountry']] = requestData['time'];
                                localStorage.setItem("requestHistory", JSON.stringify(requestHistory));
                            }
                            else {
                                $('#cityHeader').text("Het weer in " + country['name']);

                                let data = JSON.parse(localStorage.getItem('weatherData'))[country['name']];

                                $('#temp').text(data['main']['temp']);
                                $('#humidity').text(data['main']['humidity']);
                                $('#speed').text(data['wind']['speed']);
                                $('#deg').text(data['wind']['deg']);

                                let date = new Date(data['sys']['sunrise']*1000);
                                let hours = date.getHours();
                                let minutes = "0" + date.getMinutes();
                                let seconds = "0" + date.getSeconds();
                                let formattedTime = hours + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);
                                $('#sunrise').text(formattedTime);

                                date = new Date(data['sys']['sunset']*1000);
                                hours = date.getHours();
                                minutes = "0" + date.getMinutes();
                                seconds = "0" + date.getSeconds();
                                formattedTime = hours + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);
                                $('#sunset').text(formattedTime);
                            }
                        }

                    }
                });
        });
    });
}

function addDeleteandUpdateListener(){
    let id = "";
    let countryJSON = {};
    let checkExist = setInterval(function() {
        if ($('.fa-trash-alt').length && $('.fa-edit').length) {
            $('.fa-trash-alt').click(function () {
                let countryName = $(this).closest('tr').find("span").text();
                fetch('http://localhost:1337/Rest_Service/countries')
                    .then(function(response) {
                        return response.json()
                    })
                    .then(function(data) {
                        for (const country of data) {
                            if (country['name'] === countryName) {
                                id = country['code'];
                            }
                        }

                    });
                $('.modal-body').text("Are you sure you want to delete " + countryName + "?");
                $('#actionButton').text("Delete");
            });

            $('.fa-edit').click(function () {
                let countryName = $(this).closest('tr').find("span").text();
                fetch('https://localhost:8443/Rest_Service/countries')
                    .then(function(response) {
                        return response.json()
                    })
                    .then(function(data) {
                        for (const country of data) {
                            if (country['name'] === countryName) {
                                id = country['code'];
                                countryJSON = country;
                                $('.modal-title').text("Update country");
                                let form = createForm("updateForm");
                                $('.modal-body').text("");
                                let label1 = createLabel("name", "Land");
                                let input1 = createInput("text","form-control","name","name",countryJSON['name']);
                                form.appendChild(label1);
                                form.appendChild(input1);
                                let label2 = createLabel("capital", "Hoofdstad");
                                let input2 = createInput("text","form-control","capital","capital",countryJSON['capital']);
                                form.appendChild(label2);
                                form.appendChild(input2);
                                let label3 = createLabel("region", "Regio");
                                let input3 = createInput("text","form-control","region","region",countryJSON['region']);
                                form.appendChild(label3);
                                form.appendChild(input3);
                                let label4 = createLabel("Surface", "Surface");
                                let input4 = createInput("number","form-control","surface","surface",countryJSON['surface']);
                                form.appendChild(label4);
                                form.appendChild(input4);
                                let label5 = createLabel("population", "Population");
                                let input5 = createInput("number","form-control","population", "population" ,countryJSON['population']);
                                form.appendChild(label5);
                                form.appendChild(input5);
                                $('.modal-body').append(form);
                                $('#actionButton').text("Update");
                            }
                        }

                    });

            });
            $('.fa-plus-square').click(function () {
                    $('.modal-title').text("Create new country");
                    let form = createForm("createForm");
                    $('.modal-body').text("");
                    let label0 = createLabel("code", "Code");
                    let input0 = createInput("text","form-control","code","code","");
                    form.appendChild(label0);
                    form.appendChild(input0);
                    let label6 = createLabel("iso3", "ISO3");
                    let input6 = createInput("text","form-control","iso3","iso3","");
                    form.appendChild(label6);
                    form.appendChild(input6);
                    let label1 = createLabel("name", "Land");
                    let input1 = createInput("text","form-control","name","name","");
                    form.appendChild(label1);
                    form.appendChild(input1);
                    let label7 = createLabel("continent", "Continent");
                    let input7 = createInput("text","form-control","continent","continent","");
                    form.appendChild(label7);
                    form.appendChild(input7);
                    let label2 = createLabel("capital", "Hoofdstad");
                    let input2 = createInput("text","form-control","capital","capital","");
                    form.appendChild(label2);
                    form.appendChild(input2);
                    let label9 = createLabel("governmentform", "Government form");
                    let input9 = createInput("text","form-control","governmentform","governmentform","");
                    form.appendChild(label9);
                    form.appendChild(input9);
                    let label3 = createLabel("region", "Regio");
                    let input3 = createInput("text","form-control","region","region","");
                    form.appendChild(label3);
                    form.appendChild(input3);
                    let label4 = createLabel("Surface", "Surface");
                    let input4 = createInput("number","form-control","surface","surface","");
                    form.appendChild(label4);
                    form.appendChild(input4);
                    let label5 = createLabel("population", "Population");
                    let input5 = createInput("number","form-control","population", "population" ,"");
                    form.appendChild(label5);
                    form.appendChild(input5);
                    $('.modal-body').append(form);
                    $('#actionButton').text("Create");

            });
            clearInterval(checkExist);
        }
    }, 100);

    $('#actionButton').click(function () {
        if($(this).text() === "Delete"){
            fetch('http://localhost:1337/Rest_Service/countries/' + id, getFetchOptions('DELETE'))
                .then(function (response) {
                    if(response.ok){
                        console.log("Country deleted!");
                    }
                    else if (response.status == 404){
                        console.log("Country not found!");
                    }
                    else {
                        console.log("Cannot delete country!")
                    }
                });
        }
        else if($(this).text() === "Update"){
            let formData = new FormData(document.querySelector('#updateForm'));
            let encData = new URLSearchParams(formData.entries());

            fetch("Rest_Service/countries/" + id, getFetchOptions('PUT', encData))
                .then(response => response.json())
                .then(function(myJson) { console.log(myJson); })
        }
        else if($(this).text() === "Create"){
            let formData = new FormData(document.querySelector('#createForm'));
            let encData = new URLSearchParams(formData.entries());

            fetch("Rest_Service/countries/", getFetchOptions('POST', encData))
                .then(response => response.json())
                .then(function(myJson) { console.log(myJson); })
        }
        $('#exampleModal').modal('hide');
        window.location.replace("/les5_practicum1.html");

    });
}

function createForm(id){
    let form = document.createElement("form");
    form.setAttribute("id", id);

    return form;
}
function createInput(type, _class, id, name, value){
    let input = document.createElement('input');
    input.setAttribute("type", type);
                                                                                                    input.setAttribute("class", _class);
    input.setAttribute("id", id);
    input.setAttribute("name", name);
    input.setAttribute('value', value);

    return input;
}

function createLabel(_for, _text){
    let label = document.createElement("label");
    label.setAttribute("for", _for);
    label.textContent = _text;

    return label;
}

function getFetchOptions(method, body = null){
    let options = {
        method: method,
        headers : {
            'Authorization': 'Bearer ' +  window.sessionStorage.getItem("sessionToken")
        }
    };
    if(body != null){
        options['body'] = body;
    }
    return options;
}