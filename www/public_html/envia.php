<html>
<title>Titulo do site</title>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<meta http-equiv="refresh" content=3;url="http://www.willsalon.com">
</head>
<body>
</body>
</html>

<?php
if(!isset($_POST[Submit])) die("N&atilde;o recebi nenhum par&acitc;metro. Por favor volte ao formulario.html antes");
/* Medida preventiva para evitar que outros dom�nios sejam remetente da sua mensagem. */
if (eregi('tempsite.ws$|locaweb.com.br$|hospedagemdesites.ws$|websiteseguro.com$', $_SERVER[HTTP_HOST])) {
        $emailsender='willsalon@willsalon.com';
} else {
        $emailsender = "webmaster@" . $_SERVER[HTTP_HOST];
        //    Na linha acima estamos for�ando que o remetente seja 'webmaster@seudominio',
        // voc� pode alterar para que o remetente seja, por exemplo, 'contato@seudominio'.
}
 
/* Verifica qual � o sistema operacional do servidor para ajustar o cabe�alho de forma correta. N�o alterar */
if(PHP_OS == "Linux") $quebra_linha = "\n"; //Se for Linux
elseif(PHP_OS == "WINNT") $quebra_linha = "\r\n"; // Se for Windows
else die("Este script nao esta preparado para funcionar com o sistema operacional de seu servidor");
 
// Passando os dados obtidos pelo formul�rio para as vari�veis abaixo
$nomeremetente     = $_POST['nomeremetente'];
$emailremetente    = trim($_POST['emailremetente']);
$emaildestinatario = trim($_POST['emaildestinatario']);
$assunto           = $_POST['assunto'];
$mensagem          = $_POST['mensagem'];
 
 
/* Montando a mensagem a ser enviada no corpo do e-mail. */
$mensagemHTML = '<P>Email Enviado pelo site!</P>
<P>Nome do remetende: '. $nomeremetente .'</P>
<P>Email do remetende: '. $emailremetente .'</P>
<P>Assunto: '. $assunto .'</P>
<P>Mensagem:</P>
<p><b><i>'.$mensagem.'</i></b></p>
<hr>';
 
 
/* Montando o cabe�alho da mensagem */
$headers = "MIME-Version: 1.1".$quebra_linha;
$headers .= "Content-type: text/html; charset=iso-8859-1".$quebra_linha;
// Perceba que a linha acima cont�m "text/html", sem essa linha, a mensagem n�o chegar� formatada.
$headers .= "From: ".$emailsender.$quebra_linha;
$headers .= "Return-Path: " . $emailsender . $quebra_linha;
// Esses dois "if's" abaixo s�o porque o Postfix obriga que se um cabe�alho for especificado, dever� haver um valor.
// Se n�o houver um valor, o item n�o dever� ser especificado.
if(strlen($comcopia) > 0) $headers .= "Cc: ".$comcopia.$quebra_linha;
if(strlen($comcopiaoculta) > 0) $headers .= "Bcc: ".$comcopiaoculta.$quebra_linha;
$headers .= "Reply-To: ".$emailremetente.$quebra_linha;
// Note que o e-mail do remetente ser� usado no campo Reply-To (Responder Para)
 
/* Enviando a mensagem */
mail($emaildestinatario, $assunto, $mensagemHTML, $headers, "-r". $emailsender);
 
/* Mostrando na tela as informa��es enviadas por e-mail */
print "Mensagem <b>$assunto</b> enviada com sucesso!<br><br>
De: $nomeremetente<br>
Para: $emaildestinatario<br>
Mensagem: $mensagem<br>
<p><a href='".$_SERVER["HTTP_REFERER"]."'>Voc� voltar� sozinho em 3 segundo</a></p>"
?>
