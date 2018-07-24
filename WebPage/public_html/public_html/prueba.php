<?php
include_once('DBconections/clsPedido.php');

$content =
$contador=0;
$decoded = json_decode($content, true);
while($contador<count($decoded)){
$instancia=new Pedido();
$instancia->setIdpedido($decoded[$contador]['idpedido']);
$instancia->modificarEntregado();
$contador++;
}
?>