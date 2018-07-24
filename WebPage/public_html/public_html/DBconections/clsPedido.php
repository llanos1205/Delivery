<?php
include_once('clsConexion.php');
class Pedido extends Conexion
{
	//atributos
	public $idpedido;
	public $fecha;
	public $hora;
	public $lat;
	public $longitud;
	public $nit;
	public $nombrecliente;
	public $productos;
	public $montototal;
	public $estado;
	//construtor
	public function Pedido()
	{   parent::Conexion();
		$this->idpedido=0;
		$this->fecha="";
		$this->hora="";
		$this->lat="";
		$this->longitud="";
		$this->nit="";
		$this->nombrecliente="";
		$this->productos="";
		$this->montototal="";
		$this->estado="";
	}
	//propiedades de acceso
	public function setEstado($valor)
	{
	    $this->estado=$valor;
	}
	public function getEstado()
	{
	    return $this->estado;
	}
	public function setIdpedido($valor)
	{
		$this->idpedido=$valor;
	}
	public function getIdpedido()
	{
		return $this->idpedido;
	}
	public function setFecha($valor)
	{
		$this->fecha=$valor;
	}
	public function getFecha()
	{
		return $this->fecha;
	}
	public function setHora($valor)
	{
		$this->hora=$valor;
	}
	public function getHora()
	{
		return $this->hora;
	}
	public function setLat($valor)
	{
		$this->lat=$valor;
	}
	public function getLat()
	{
		return $this->lat;
	}
	public function setLongitud($valor)
	{
		$this->longitud=$valor;
	}
	public function getLongitud()
	{
		return $this->longitud;
	}
	public function setNit($valor)
	{
		$this->nit=$valor;
	}
	public function getNit()
	{
		return $this->nit;
	}
	public function setNombrecliente($valor)
	{
		$this->nombrecliente=$valor;
	}
	public function getNombrecliente()
	{
		return $this->nombrecliente;
	}
	public function setProductos($valor)
	{
		$this->productos=$valor;
	}
	public function getProductos()
	{
		return $this->productos;
	}
	public function setMontototal($valor)
	{
		$this->montototal=$valor;
	}
	public function getMontototal()
	{
		return $this->montototal;
	}




	public function guardar()
	{
     $sql="insert into Pedido(idpedido,fecha,hora,lat,longitud,nit,nombrecliente,productos,montototal,estado) 
     values('$this->idpedido','$this->fecha','$this->hora','$this->lat','$this->longitud','$this->nit','$this->nombrecliente',
     '$this->productos','$this->montototal','$this->estado')";
		
		if(parent::ejecutar($sql))
			return true;
		else
			return false;	
	}
	
	public function buscar()
	{
		$sql="select *from Pedido";
		return parent::ejecutar($sql);
	}
	
	public function buscarPorEstado()
	{
		$sql="select *from Pedido where estado='pendiente'";
		return parent::ejecutar($sql);
	}						
	
	public function modificar()	{
	$sql="update Pedido set Estado='repartidor' where idpedido=$this->idpedido";		
		if(parent::ejecutar($sql))
			return true;
		else
			return false;	
	}
	
	public function modificarEntregado()	{
	$sql="update Pedido set Estado='entregado' where idpedido=$this->idpedido";		
		if(parent::ejecutar($sql))
			return true;
		else
			return false;	
	}
	public function eliminar()
	{
	    $sql="DELETE FROM Pedido";
	    if(parent::ejecutar($sql))
	    {
	        return true;
	    }
	    else
	    {return false;}
	}

}    
?>