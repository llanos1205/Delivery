<?php
include_once('DBconections/clsPedido.php');

$content = trim(file_get_contents("php://input"));
 
//Attempt to decode the incoming RAW post data from JSON.
$decoded = json_decode($content, true);
$instancia=new Pedido();
$instancia->setFecha($decoded['fecha']);
$instancia->setHora($decoded['hora']);
$instancia->setLat($decoded['lat']);
$instancia->setLongitud($decoded['longitud']);
$instancia->setNit($decoded['nit']);
$instancia->setNombrecliente($decoded['nombrecliente']);
$instancia->setProductos($decoded['productos']);
$instancia->setMontototal($decoded['montototal']);
$instancia->setEstado("pendiente");
$instancia->guardar();
?>