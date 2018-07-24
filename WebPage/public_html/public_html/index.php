<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

</head>

<body>
<?php
include_once('DBconections/clsPedido.php');
include_once('DBconections/clsRepartidor.php');
include_once('DBconections/clsSucursal.php');
?>

<b> INDICE  </b>

<button onclick="window.location.href='Tablas.html' ">Registros</button>
<form id="form1" name="form1" method="post" action="index.php">
    <tr>
        <td colspan="2">
            <input type="submit" name="botones"  value="Eliminar" />
            <input type="submit" name="botones"  id="botones" value="Buscar Registros"/>
            <input type="submit" name="botones"  id="botones" value="Buscar Repartidores"/>
            <input type="submit" name="botones"  id="botones" value=   "Buscar Sucursales"/>
            <input type="submit" name="botones"  id="botones" value="+"/>
        </td>
    </tr>
</form>
<?php

function eliminar()
{
    $x=new Pedido();
    $x->Eliminar();
    echo "Eliminado todo";
}
function buscar()
{
    $obj= new Pedido();
    $resultado=$obj->Buscar();
    mostrarRegistros($resultado);
}
function mostrarRep()
{
    $x=new Repartidor();
    $resultado=$x->Buscar();
    mostrarRepartidores($resultado);
}
function buscarSuc()
{
    $x=new Sucursal();
    $r=$x->Buscar();
    mostrarSucursales($r);
}
function  mostrarSucursales($r)
{
    echo "<table border='2 align='left'>";
    echo "<tr><td>IdSucursal</td>";
    echo "<td>Latitud</td>";
    echo "<td>Longitud</td>";
    echo "<td>Latitud2</td>";
    echo "<td>Longitud2</td>";
    while($fila=mysqli_fetch_object($r))
    {
        echo "<tr>";
        echo "<td>$fila->idsucursal</td>";
        echo "<td>$fila->latitud</td>";
        echo "<td>$fila->longitud</td>";
        echo "<td>$fila->latitud2</td>";
        echo "<td>$fila->longitud2</td>";
        echo "</tr>";
    }
    echo "</table>";
}
function mostrarRepartidores($registros)
{
    echo "<table border='2 align='left'>";
    echo "<tr><td>IdRepartidor</td>";
    echo "<td>Nombre</td>";
    echo "<td>IdSucursal</td>";
    while($fila=mysqli_fetch_object($registros))
    {
        echo "<tr>";
        echo "<td>$fila->idrepartidor</td>";
        echo "<td>$fila->nombre</td>";
        echo "<td>$fila->idsucursal</td>";
        echo "</tr>";
    }
    echo "</table>";
}
function mostrarRegistros($registros)
{
    echo "<table border='1' align='left'>";
    echo "<tr><td>IdPedido</td>";
    echo "<td>Fecha</td>";
    echo "<td>Hora</td>";
    echo "<td>Latitud</td>";
    echo "<td>Longitud</td>";
    echo "<td>Nit</td>";
    echo "<td>Nombre Cliente</td>";
    echo "<td>Prouctos</td>";
    echo "<td>Monto</td>";
    echo "<td>Estado</td>";

    while($fila=mysqli_fetch_object($registros))
    {
        echo "<tr>";
        echo "<td>$fila->idpedido</td>";
        echo "<td>$fila->fecha</td>";
        echo "<td>$fila->hora</td>";
        echo "<td>$fila->lat</td>";
        echo "<td>$fila->longitud</td>";
        echo "<td>$fila->nit</td>";
        echo "<td>$fila->nombrecliente</td>";
        echo "<td>$fila->productos</td>";
        echo "<td>$fila->montototal</td>";
        echo "<td>$fila->Estado</td>";

        echo "</tr>";
    }
    echo "</table>";
}
function añadirRep()
{
    $x=new Repartidor();

    $x->setNombre("carlso");
    $x->setIdSucursal(155);
    $x->guardar();
}
function añadirSuc()
{
    $x=new Sucursal();

    $x->setLatitud("1314");
    $x->setLongitud("5899");
    $x->setLatitud2("09909");
    $x->setLongitud2("346676");
    $x->guardar();
}

switch($_POST['botones'])
{
    case "Buscar Registros":
        {
            buscar();break;
        }
    case "Eliminar":
        {
            eliminar();break;
        }
    case "Buscar Repartidores":
        {
            mostrarRep();break;
        }
    case "Buscar Sucursales":
        {
            buscarSuc();break;
        }
    case "+":
        {
            añadirSuc();break;
        }
}


?>
</body>
</html>
