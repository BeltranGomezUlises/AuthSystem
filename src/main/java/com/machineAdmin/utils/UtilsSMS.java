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

import com.machineAdmin.entities.cg.admin.mongo.CGConfig;
import com.machineAdmin.models.cg.ModelSMSRecuperacionContra;
import com.sun.jersey.api.client.Client;
import javax.ws.rs.core.MediaType;
/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsSMS {        
    
    public static ModelSMSRecuperacionContra.ModelSMSRescuePassResponse sendSMS(String phoneNumber, String message){                                                
        CGConfig.SMSConfig config = UtilsConfig.getSMSConfig();        
        ModelSMSRecuperacionContra smsRescue = new ModelSMSRecuperacionContra();
        
        smsRescue.setDestino(phoneNumber);
        smsRescue.setMensaje(message);
        smsRescue.setEnvia(config.getDeviceImei());
        smsRescue.setUsuarioId(config.getUsuarioId());
                
        ModelSMSRecuperacionContra.ModelSMSRescuePassResponse res = Client.create()
                .resource(config.getUri())
                .accept(MediaType.APPLICATION_JSON)    
                .type(MediaType.APPLICATION_JSON)
                .post(ModelSMSRecuperacionContra.ModelSMSRescuePassResponse.class, smsRescue);                
        
        return res;
    }
    
    
}
