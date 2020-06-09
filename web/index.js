$(document).ready(function() {
    $('input[type="radio"]').click(function() {
        if($(this).attr('id') == 'fichero') {
            $('#params_fichero').show();
            $('#params_teclado').hide();
        }
        else {
            $('#params_fichero').hide();
            $('#params_teclado').show();
        }
    });
});