$(document).ready(function(){
    let operators = {
        '+': (a, b) => a + b,
        '-': (a, b) => a - b,
        '/': (a, b) => a / b,
        '*': (a, b) => a * b,
    };

    let number1 = 0;
    let operator = "";
    let display = $('#display');
    let resultJustShown = false;

    $(".num").on('click', function () {
        if(display.text() !== "0"){
            display.text(display.text() + this.textContent);
        }
        else {
            display.text(this.textContent);
        }
    });

    $(".operator").on('click', function () {
        if(display.text() !== "") {
            if(!['+','-','/','*'].some((v) => display.text().slice(-1).indexOf(v) > -1)){
                operator = this.textContent;
                number1 = Number(display.text());
                display.text(display.text() + this.textContent);
            }
            else {
                operator = this.textContent;
                display.text(display.text().slice(0,-1) + this.textContent);
            }

        }
    });

    $("#btn_clear").on('click', function () {
        operator = "";
        number1 = 0;
        display.text("0");
    });

    $("#btn_eq").on('click', function () {
        if(operator !== "" && display.text() !== ""){
            display.text(operators[operator](number1, Number(display.text().substring(display.text().indexOf(operator) + 1))));
        }
        operator = "";
        number1 = 0;
        resultJustShown = true;
    });

});