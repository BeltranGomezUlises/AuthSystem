/*
 * Copyright (C) 2018 Alonso --- alonso@kriblet.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.auth.models;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class ModelLogin {

    private String login;
    private String pass;
    private Integer sucursalId;

    public ModelLogin() {
    }

    public ModelLogin(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    public ModelLogin(String login, String pass, Integer sucursalId) {
        this.login = login;
        this.pass = pass;
        this.sucursalId = sucursalId;
    }

    public Integer getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(Integer sucursalId) {
        this.sucursalId = sucursalId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}
