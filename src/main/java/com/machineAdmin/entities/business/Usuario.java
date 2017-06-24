package com.machineAdmin.entities.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.machineAdmin.entities.cg.Entity;
import java.util.Objects;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class Usuario extends Entity {

    private String usuario;
    private String correo;
    @JsonIgnore
    private String contraseña;
    private Object permisos;

    public Usuario() {
    }

    public Usuario(String usuario, String contraseña) {
        this.usuario = usuario;
        this.contraseña = contraseña;
    }

    public Usuario(String usuario, String correo, String contraseña) {
        this.usuario = usuario;
        this.correo = correo;
        this.contraseña = contraseña;
    }   

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Object getPermisos() {
        return permisos;
    }

    public void setPermisos(Object permisos) {
        this.permisos = permisos;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre=" + usuario + ", correo=" + correo + ", contrase\u00f1a=" + contraseña + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
