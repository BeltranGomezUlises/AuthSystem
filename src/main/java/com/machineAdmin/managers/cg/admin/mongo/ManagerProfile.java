/*
 * Copyright (C) 2017 Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
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
package com.machineAdmin.managers.cg.admin.mongo;

import com.machineAdmin.daos.cg.admin.mongo.DaoProfile;
import com.machineAdmin.entities.cg.admin.mongo.Profile;
import com.machineAdmin.managers.cg.commons.ManagerMongoFacade;
import com.machineAdmin.models.cg.ModelSetPermission;
import com.machineAdmin.models.cg.ModelSetUsuariosToProfile;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerProfile extends ManagerMongoFacade<Profile> {
    
    public ManagerProfile() {
        super(new DaoProfile());
    }

    public void setPermissionsToProfile(ModelSetPermission modelSetPermission) throws Exception {
        Profile profile = this.findOne(modelSetPermission.getId());
        profile.setPermisos(modelSetPermission.getPermissionsAsigned());
        this.update(profile);
    }

    public void setUsersToProfile(ModelSetUsuariosToProfile modelSetUsuariosToProfile) throws Exception {
        Profile profile = this.findOne(modelSetUsuariosToProfile.getId());
        profile.setUsers(modelSetUsuariosToProfile.getUsuarios());
        this.update(profile);
    }
    
}
