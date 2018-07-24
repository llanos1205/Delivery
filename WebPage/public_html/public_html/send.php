<?php
include_once('DBconections/clsPedido.php');
$aux=new Pedido();
$contador=0;
$registro=$aux->buscarPorEstado();
while($contador<5 && $fila=mysqli_fetch_row($registro) ){
    $iter=new Pedido();
    $iter->setIdpedido($fila[0]);
    $iter->setFecha($fila[1]);
	$iter->setHora($fila[2]);
    $iter->setLat($fila[3]);
	$iter->setLongitud($fila[4]);
    $iter->setNit($fila[5]);
	$iter->setNombrecliente($fila[6]);
	$iter->setProductos($fila[7]);
	$iter->setMontototal($fila[8]);
	$iter->setEstado($fila[9]);
	if($contador==0){
	    $result=array($iter);
	}
	else{
	  $result[]=$iter;
	}
   $contador++;
}
$contador=0;
while($contador<count($result)){
    $result[$contador]->modificar();
    $contador++;
}
$json=json_encode($result);
echo $json;

?>