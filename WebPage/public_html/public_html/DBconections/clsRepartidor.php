<?php
include_once('clsConexion.php');
class Repartidor extends Conexion
{
    //atributos
    private $idrepartidor;
    private $nombre;
    private $idsucursal;

    /**
     * Repartidor constructor.
     * @param $idrepartidor
     * @param $nombre
     * @param $idsucursal
     */
    public function Repartidor()
    {
        parent::Conexion();
        $this->idrepartidor = 0;
        $this->nombre = "";
        $this->idsucursal = 0;
    }

    /**
     * @return mixed
     */
    public function getIdrepartidor()
    {
        return $this->idrepartidor;
    }

    /**
     * @param mixed $idrepartidor
     */
    public function setIdrepartidor($idrepartidor)
    {
        $this->idrepartidor = $idrepartidor;
    }

    /**
     * @return mixed
     */
    public function getNombre()
    {
        return $this->nombre;
    }

    /**
     * @param mixed $nombre
     */
    public function setNombre($nombre)
    {
        $this->nombre = $nombre;
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
    /**funciones propias de la DB*/
    public function guardar()
    {
        $sql="insert into Repartidor(idrepartidor,nombre,idsucursal) values ('$this->idrepartidor','$this->nombre','$this->idsucursal')";
        if(parent::ejecutar($sql))
        {
            return true;
        }
        else{return false;}
    }
    public function buscar()
    {
        $sql="select *from Repartidor";
        return parent::ejecutar($sql);
    }
}
?>