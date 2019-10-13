<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Pangeum Login</title>
 
        <link rel="shortcut icon" href="images/logo.png">

			<link rel="stylesheet" href="css/bootstrap.min.css">
			<link rel="stylesheet" href="css/style.css">
			<link rel="stylesheet" href="css/hover.css">
			<script src="js/jquery.min.js"></script>
			<script src="js/bootstrap.min.js"></script>
			<script> 
			$.getScript("js/particles.min.js", function(){
			    particlesJS('particles-js',
			    	      {
			    	        "particles": {
			    	          "number": {
			    	            "value": 80,
			    	            "density": {
			    	              "enable": true,
			    	              "value_area": 800
			    	            }
			    	          },
			    	          "color": {
			    	            "value": "#ffffff"
			    	          },
			    	          "shape": {
			    	            "type": "circle",
			    	            "stroke": {
			    	              "width": 0,
			    	              "color": "#000000"
			    	            },
			    	            "polygon": {
			    	              "nb_sides": 5
			    	            },
			    	            "image": {
			    	              "width": 100,
			    	              "height": 100
			    	            }
			    	          },
			    	          "opacity": {
			    	            "value": 0.5,
			    	            "random": false,
			    	            "anim": {
			    	              "enable": false,
			    	              "speed": 1,
			    	              "opacity_min": 0.1,
			    	              "sync": false
			    	            }
			    	          },
			    	          "size": {
			    	            "value": 5,
			    	            "random": true,
			    	            "anim": {
			    	              "enable": false,
			    	              "speed": 40,
			    	              "size_min": 0.1,
			    	              "sync": false
			    	            }
			    	          },
			    	          "line_linked": {
			    	            "enable": true,
			    	            "distance": 150,
			    	            "color": "#ffffff",
			    	            "opacity": 0.4,
			    	            "width": 1
			    	          },
			    	          "move": {
			    	            "enable": true,
			    	            "speed": 6,
			    	            "direction": "none",
			    	            "random": false,
			    	            "straight": false,
			    	            "out_mode": "out",
			    	            "attract": {
			    	              "enable": false,
			    	              "rotateX": 600,
			    	              "rotateY": 1200
			    	            }
			    	          }
			    	        },
			    	        "interactivity": {
			    	          "detect_on": "canvas",
			    	          "events": {
			    	            "onhover": {
			    	              "enable": true,
			    	              "mode": "repulse"
			    	            },
			    	            "onclick": {
			    	              "enable": true,
			    	              "mode": "push"
			    	            },
			    	            "resize": true
			    	          },
			    	          "modes": {
			    	            "grab": {
			    	              "distance": 400,
			    	              "line_linked": {
			    	                "opacity": 1
			    	              }
			    	            },
			    	            "bubble": {
			    	              "distance": 400,
			    	              "size": 40,
			    	              "duration": 2,
			    	              "opacity": 8,
			    	              "speed": 3
			    	            },
			    	            "repulse": {
			    	              "distance": 200
			    	            },
			    	            "push": {
			    	              "particles_nb": 4
			    	            },
			    	            "remove": {
			    	              "particles_nb": 2
			    	            }
			    	          }
			    	        },
			    	        "retina_detect": true,
			    	        "config_demo": {
			    	          "hide_card": false,
			    	          "background_color": "#b61924",
			    	          "background_image": "",
			    	          "background_position": "50% 50%",
			    	          "background_repeat": "no-repeat",
			    	          "background_size": "cover"
			    	        }
			    	      }
			    	    );

			    	});

			myClick();
			function myClick() {
				  setTimeout(
				    function() {
				      document.getElementById('container').style.display='block';
				      //document.getElementById('div2').style.display='none';
				    	 $("#body").attr("style", "background:url('images/background.png') no-repeat;");
				    	// $("#body").attr("style", "background-size:100% 100%;");

				    }, 6000);
				}


</script>
<style>
body{
background:white;
background-size:100% 100% !important;
}
@font-face {
 	font-family: 'font1';
	/*src: url('ALBA____.TTF'); */
	src: 
	url('fonts/ALBA___.TTF') 
		 
}
@font-face {
	font-family: 'font2';
	/*src: url('ALBAM___.TTF'); */
	src: 
	url('fonts/ALBAM___.TTF') 
		 
}
@font-face {
	font-family: 'font3';
	/*src: url('ALBAS___.TTF'); */
	src: 
	url('fonts/ALBAS___.TTF') 
		 
}

/* Main stuff */
.text--transparent {
  fill: transparent; }

.anim-shape {
  -webkit-transform-origin: 0x 150px;
  transform-origin: 0x 150px;
  -webkit-transform: scale(0, 1) translate(0, 0);
  transform: scale(0, 1) translate(0, 0);
  /* -webkit-animation: moving-panel 3s infinite alternate; */
  animation: moving-panel 6s;
  }
  .colortext .anim-shape:nth-child(1) {
    fill: #2a327e; }
  .colortext .anim-shape:nth-child(2) {
    fill: #6bbe5e; }
  .colortext .anim-shape:nth-child(3) {
    fill: #6bbe5e; }
  .colortext .anim-shape:nth-child(4) {
    fill: #2a327e; }
  .colortext .anim-shape:nth-child(5) {
    fill: #2a317e; }

.shadow {
  -webkit-transform: translate(10px, 10px);
  transform: translate(10px, 10px);
  }

.anim-shape--shadow 

{
  fill: #000;
  fill-opacity: .2; 
  }

@-webkit-keyframes moving-panel {
  100% {
    -webkit-transform: scale(1, 1) translate(20px, 0);
    transform: scale(1, 1) translate(20px, 0); } }
@keyframes moving-panel {
  100% {
    -webkit-transform: scale(1, 1) translate(20px, 0);
    transform: scale(1, 1) translate(20px, 0); } }
/* Other stuff */
 

.content {
  font-size: 120px;
  font-family: monospace;
  font-weight: bold;
  text-decoration: underline;
  }

svg {
  width: 90%;
  margin: 0 auto 50px;
  display: block;
  text-transform: uppercase; }

/*# sourceMappingURL=style17.css.map */

</style>
</head>
<body id="body">
<div class="content">
 
			<svg viewbox="0 0 600 300">

				<!-- Symbol with text -->
				<symbol id="s-text">
					<text text-anchor="middle" x="50%" y="50%" dy=".35em" class="text--line">
						pangeum
					</text>
				</symbol>

				<!-- Clippath  with text -->
				<clippath id="cp-text">
					<text text-anchor="middle" x="50%" y="50%" dy=".35em" class="text--line">
						pangeum
					</text>
				</clippath>

				<!-- Group for shadow -->
				<g clip-path="url(#cp-text)" class="shadow">
					<rect width="100%" height="100%" class="anim-shape anim-shape--shadow"></rect>
				</g>

				<!-- Group with clippath for text-->
				<g clip-path="url(#cp-text)" class="colortext">
					<!-- Animated shapes inside text -->
					<rect width="100%" height="100%" class="anim-shape"></rect>
					<rect width="80%" height="100%" class="anim-shape"></rect>
					<rect width="60%" height="100%" class="anim-shape"></rect>
					<rect width="40%" height="100%" class="anim-shape"></rect>
					<rect width="20%" height="100%" class="anim-shape"></rect>
				</g>

				<!-- Transparent copy of text to keep patterned text selectable -->
				<use xlink:href="#s-text" class="text--transparent"></use>

			</svg>
		</div>
 
<div class="container" id="container" style="display:none">
	<div id="login-box">
		<div class="logo">
 			<h1 class="logo-caption">  <img width="182" height="78" src="data:image/png;base64, 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 "></h1>
		</div><!-- /.logo -->
		<div class="controls"> 
		<form action="login.html" method="post">
			<input type="text" name="username" placeholder="Username" class="form-control" />
			<input type="password" name="password" placeholder="Password" class="form-control" />
			<button type="submit" class="btn btn-default btn-block btn-custom">Login</button>
			
		</form>
		</div><!-- /.controls -->
	</div><!-- /#login-box -->
</div>
<!-- /.container 
<form action="LoginController" method="post">
		Enter username :<input type="text" name="username"> <br>
		Enter password :<input type="password" name="password"><br>
		<input type="submit" value="Login">
	</form>-->


		
		<div id="particles-js"></div>

</body>
</html>