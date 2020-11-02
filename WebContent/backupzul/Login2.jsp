
<!DOCTYPE html >
<html lang="en">

    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta charset="utf-8">
        <title>PHC</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">

        <!-- CSS -->
        <link rel='stylesheet' href='http://fonts.googleapis.com/css?family=PT+Sans:400,700'>
        <link rel='stylesheet' href='http://fonts.googleapis.com/css?family=Oleo+Script:400,700'>
        <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/css/style.css">

        <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
            <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->

    </head>

    <body>

        <div class="header">
            <div class="container">
                <div class="row">
                    <div class="logo span4">
                        <h1><a href="">Pose Health Care<span class="red">.</span></a></h1>
                    </div>
                    
                    <div class="links span8">
                        <a class="home" href="" rel="tooltip" data-placement="bottom" data-original-title="Home"></a>
                        <a class="blog" href="" rel="tooltip" data-placement="bottom" data-original-title="Blog"></a>
                    </div>
                </div>
            </div>
        </div>

        <div class="register-container container">
            <div class="row">
                <div class="iphone span5">
                    <img src="assets/img/posebg.png" alt="">
                </div>
                
                <div class="register span6">
                    <form action="Process.jsp" method="post">
                        <h2>Login 
	                        <span class="red">
	                        	<strong> PHC ERP</strong>
	                        </span>
                        </h2>
                        
                        <label for="username">Username</label>
                        <input type="text" id="loginUsername" name="login[Username]" placeholder="username">
                        
                        <label for="password">Password</label>
                        <input type="password" id="loginPassword" name="login[Password]" placeholder="password"><br>
                        
                        <select id="B_ID" name="B_ID">
                        	<option value="0">กรุณาเลือกตึก</option>
                        	<option value="1">ดึกศูนย์การแพทย์</option>
                        	<option value="2">ตึกเหนือ</option>
                        </select><br>
                        <button type="submit">Login</button>
                        
                    </form>
                </div>
            </div>
        </div>

        <!-- Java script -->
        <script src="assets/js/jquery-1.8.2.min.js"></script>
        <script src="assets/bootstrap/js/bootstrap.min.js"></script>
        <script src="assets/js/jquery.backstretch.min.js"></script>
        <script src="assets/js/scripts.js"></script>

    </body>

</html>

