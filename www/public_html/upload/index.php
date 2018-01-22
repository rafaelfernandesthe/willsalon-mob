<html>
	<head>
		<meta charset="UTF-8">
		<!-- Include meta tag to ensure proper rendering and touch zooming -->
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<!-- Include jQuery Mobile stylesheets -->
		<link rel="stylesheet" href="jquery.mobile-1.4.5.min.css">

		<style>
			td.border_bottom {
			  border-bottom:1pt solid black;
			}
		</style>
	</head>

	<body>
		<div data-role="page" id="uploadPage">
		<div data-role="main" class="ui-content">
		<?php

		$action = $_GET['action'];
		$fileName = $_GET['fileName'];
		if($action == 'd'){ 
			if (!unlink("../images/header/$fileName"))
			{
			  echo ("Erro ao deletar $fileName");
			  echo "<meta HTTP-EQUIV='refresh' CONTENT='2;URL=index.php'>";			
			}
			else
			{
			  echo ("<div style='color:green'>Arquivo $fileName apagado com sucesso!</div>");
			  unlink("../content_header.js");
				$fp = fopen("../content_header.js", "w");
				$types = array( 'png', 'jpg', 'jpeg', 'gif' );
				fwrite($fp, "document.write(\"\
\
				<div id='demo' class='wow bounceInRight' data-wow-duration='1s'>\
				<div id='owl-demo' class='owl-carousel'>\
				<!-- APPS SCREEN IMAGES -->\
\
				");

				if ( $handle = opendir('../images/header/') ) {
				while ( $entry = readdir( $handle ) ) {
				$ext = strtolower( pathinfo( $entry, PATHINFO_EXTENSION) );
				if( in_array( $ext, $types ) ) {
					$content = $content."\
						<div class='item'>\
							<a href='images/header/$entry' rel='prettyPhoto[pp_gal]'>\
							<img src='images/header/$entry' width='60' height='60' alt='APPS SCREEN'/>\
							</a>\
						</div>\
					";
				}
				}
				closedir($handle);
				}   

				$content = $content."\
				</div>\
				</div>\
				\");";

				fwrite($fp,$content);
				fclose($fp);	
				chmod("../content_header.js",0755);
			  echo "<meta HTTP-EQUIV='refresh' CONTENT='2;URL=index.php'>";
			}
		}
		
		// Nas versões do PHP anteriores a 4.1.0, $HTTP_POST_FILES deve ser utilizado ao invés de $_FILES.

		$uploaddir = '../images/header/';
		$uploadfile = $uploaddir . basename($_FILES['userfile']['name']);
	
		if (move_uploaded_file($_FILES['userfile']['tmp_name'], $uploadfile)) {
			unlink("../content_header.js");
			$fp = fopen("../content_header.js", "w");
			$types = array( 'png', 'jpg', 'jpeg', 'gif' );
			fwrite($fp, "document.write(\"\
\
			<div id='demo' class='wow bounceInRight' data-wow-duration='1s'>\
			<div id='owl-demo' class='owl-carousel'>\
			<!-- APPS SCREEN IMAGES -->\
\
			");
			$dirFiles = array();
			if ( $handle = opendir('../images/header/') ) {
				while ( $entry = readdir( $handle ) ) {
					$ext = strtolower( pathinfo( $entry, PATHINFO_EXTENSION) );
					if( in_array( $ext, $types ) ) {
						$dirFiles[] = $entry;
					}
				}
			closedir($handle);
			}
			sort($dirFiles);

			foreach($dirFiles as $entry) {
				$content = $content."\
					<div class='item'>\
						<a href='images/header/$entry' rel='prettyPhoto[pp_gal]'>\
						<img src='images/header/$entry' width='60' height='60' alt='APPS SCREEN'/>\
						</a>\
					</div>\
				";
			}

			/**if ( $handle = opendir('../images/header/') ) {
				while ( $entry = readdir( $handle ) ) {
					$ext = strtolower( pathinfo( $entry, PATHINFO_EXTENSION) );
					if( in_array( $ext, $types ) ) {
						$content = $content."\
							<div class='item'>\
								<a href='images/header/$entry' rel='prettyPhoto[pp_gal]'>\
								<img src='images/header/$entry' width='60' height='60' alt='APPS SCREEN'/>\
								</a>\
							</div>\
						";
					}
				}
			closedir($handle);
			}   
			**/

			$content = $content."\
			</div>\
			</div>\
			\");";

			fwrite($fp,$content);
			fclose($fp);	
			
			chmod("../content_header.js",0755);
			echo "<div style='color:green'>Arquivo válido e enviado com sucesso.\n</div>";
			echo "<img src='$uploadfile' width='60' height='60' alt='APPS SCREEN'/></a>";
			echo "<meta HTTP-EQUIV='refresh' CONTENT='2;URL=index.php'>";
		} else {
			//echo "Erro ao realizar Upload!\n";			
		}

		//echo 'Aqui está mais informações de debug:';
		//print_r($_FILES);

		print "</pre>";

		?>
	
		<form id="formMain" enctype="multipart/form-data" data-ajax="false" action="index.php" method="POST">
		    <!-- MAX_FILE_SIZE deve preceder o campo input -->
		    <input type="hidden" name="MAX_FILE_SIZE" value="500000" />
		    <!-- O Nome do elemento input determina o nome da array $_FILES -->
		    <label for="userfile">Selecione o arquivo:</label>
			<input id="userfile" class="ui-btn" name="userfile" type="file" />
		    <input class="ui-btn" type="submit" value="Enviar arquivo" />
		</form>

		<?php

		echo "<table data-role='table' class='ui-responsive' >
			<thead>
			<tr>
				<th>Ação</th>
				<th>Imagem</th> 
			</tr>
			<thead>
			<tbody>";

		$types = array( 'png', 'jpg', 'jpeg', 'gif' );
		if ( $handle = opendir('../images/header/') ) {
		    while ( $entry = readdir( $handle ) ) {
			$ext = strtolower( pathinfo( $entry, PATHINFO_EXTENSION) );
			if( in_array( $ext, $types ) ) {
				echo "<tr>";
				echo "<td><form method='GET' action='index.php?action=d&fileName=$entry'><input type='submit' value='Excluir'/></form></td>";
				echo "<td class='border_bottom'><img src='../images/header/$entry' width='50%' height='20%' alt='APPS SCREEN'/></td>";
				echo "</tr>";
			}
		    }
		    closedir($handle);
		} 

		
		echo "	</tbody>
				</table>";

		?>
	</div>
	</div>
	
		<!-- Include the jQuery library -->
		<script src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
		<!-- Include the jQuery Mobile library -->
		<script src="jquery.mobile-1.4.5.min.js"></script>
		
	</body>
	
</html>
