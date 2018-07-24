<?php
include_once('DBconections/clsPedido.php');

$content = trim(file_get_contents("php://input"));
$decoded = json_decode($content, true);

if($decoded['idpedido1']<>0){
$instancia=new Pedido();
$instancia->setIdpedido($decoded['idpedido1']);
$instancia->modificarEntregado();
}
if($decoded['idpedido2']<>0){
    $instancia=new Pedido();
$instancia->setIdpedido($decoded['idpedido2']);
$instancia->modificarEntregado();
}
if($decoded['idpedido3']<>0){
    $instancia=new Pedido();
$instancia->setIdpedido($decoded['idpedido3']);
$instancia->modificarEntregado();
}
if($decoded['idpedido4']<>0){
    $instancia=new Pedido();
$instancia->setIdpedido($decoded['idpedido4']);
$instancia->modificarEntregado();
}
if($decoded['idpedido5']<>0){
    $instancia=new Pedido();
$instancia->setIdpedido($decoded['idpedido5']);
$instancia->modificarEntregado();
}


?>