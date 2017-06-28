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
package com.machineAdmin.utils;

import com.machineAdmin.entities.cg.EntityMongo;
import org.mongojack.JacksonDBCollection;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsConfig {
    
    protected static final String COLLECTION_NAME = "config.cg";
    protected static final JacksonDBCollection<CGConfig, String> COLLECTION = JacksonDBCollection.wrap(UtilsDB.getCollection(COLLECTION_NAME), CGConfig.class, String.class);    
                
    public static int getJwtExp(){
        return COLLECTION.findOne().getJwtExp();
    }
    
   
    private static class CGConfig extends EntityMongo{
        
        private int jwtExp;

        public int getJwtExp() {
            return jwtExp;
        }

        public void setJwtExp(int jwtExp) {
            this.jwtExp = jwtExp;
        }
                
    }
}
