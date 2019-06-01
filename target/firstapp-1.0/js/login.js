$( document ).ready(function() {
    $("#loginForm").submit(function(){
        return false;
    });

    $('input:submit').on('click', function (e) {
        if($('#loginForm p')){
            $('#loginForm p').remove('p');
        }

        let formData = new FormData(document.getElementById('loginForm'));
        let encData = new URLSearchParams(formData.entries());

        fetch("/Rest_Service/authentication", {method: 'POST', body: encData})
            .then(function(response){
                if(response.status === 401){
                    $('#loginForm').append("<p style='color:red'>No user found with that username and password.</p>");

                }
                else if (response.status === 200){
                    $('#loginForm').append("<p style='color:green'>Login successful!</p>");
                    return response.json();
                }
            })
            .then(myJson => window.sessionStorage.setItem("sessionToken", myJson.JWT))
            .then(function () {
                window.location.replace("/");
            })
            .catch(error => console.log(error));
    });
});
