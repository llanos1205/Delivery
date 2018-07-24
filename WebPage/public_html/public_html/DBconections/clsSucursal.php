<?php
/**
 * Created by PhpStorm.
 * User: Diego
 * Date: 18/7/2018
 * Time: 19:40
 */
include_once('clsConexion.php');
class Sucursal extends Conexion
{
    //variables;
    private $idsucursal;
    private $latitud;
    private $longitud;
    private $latitud2;
    private $longitud2;

    /**
     * clsSucursal constructor.
     */
    public function Sucursal()
    {
        parent::Conexion();
        $this->latitud="";
        $this->longitud="";
    }

    /**
     * @return mixed
     */
    public function getLatitud2()
    {
        return $this->latitud2;
    }

    /**
     * @param mixed $latitud2
     */
    public function setLatitud2($latitud2)
    {
        $this->latitud2 = $latitud2;
    }

    /**
     * @return mixed
     */
    public function getLongitud2()
    {
        return $this->longitud2;
    }

    /**
     * @param mixed $longitud2
     */
    public function setLongitud2($longitud2)
    {
        $this->longitud2 = $longitud2;
    }


    /**
     * @return mixed
     */
    public function getIdsucursal()
    {
        return $this->idsucursal;
    }

    /**
     * @param mixed $idsucursal
     */
    public function setIdsucursal($idsucursal)
    {
        $this->idsucursal = $idsucursal;
    }

    /**
     * @return mixed
     */
    public function getLatitud()
    {
        return $this->latitud;
    }

    /**
     * @param mixed $latitud
     */
    public function setLatitud($latitud)
    {
        $this->latitud = $latitud;
    }

    /**
     * @return mixed
     */
    public function getLongitud()
    {
        return $this->longitud;
    }

    /**
     * @param mixed $longitud
     */
    public function setLongitud($longitud)
    {
        $this->longitud = $longitud;
    }
    public function guardar()
    {
        $sql="insert into Sucursal(longitud,latitud,longitud2,latitud2) values ('$this->longitud','$this->latitud','$this->longitud2','$this->latitud2')";
        if(parent::ejecutar($sql))
        {
            return true;
        }
        else{return false;}
    }
    public function buscar()
    {
        $sql="select *from Sucursal";
        return parent::ejecutar($sql);
    }

}
?>