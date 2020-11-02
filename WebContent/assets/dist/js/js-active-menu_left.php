<script>
    $(function(){
        var path = window.location.pathname;
        parth = path.split('/');
        filename = parth[3].split('.');
        filename = filename[0];

        $('#accordionSidebar').find('.active').removeClass("active");

        if(filename == "sendsterile" || filename == "sendsterile_except"){
            $('#wash').addClass('active');
        }else if(filename == "recall" || filename == "warn_before_exp" || filename == "warn_exp"){
            $('#restore').addClass('active');
        }

        $('#'+filename).addClass('active');

    });
</script>