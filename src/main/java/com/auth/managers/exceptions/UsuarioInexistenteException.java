/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auth.managers.exceptions;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class UsuarioInexistenteException extends Exception {

    public UsuarioInexistenteException(String menssage) {
        super(menssage);
    }

}
