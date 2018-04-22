/*
 * Copyright (C) 2017 Alonso --- alonso@kriblet.com
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
package com.auth.managers.admin;

import com.auth.daos.admin.DaoBitacoraContra;
import com.auth.daos.admin.DaoSucursal;
import com.auth.daos.admin.DaoUsuario;
import com.auth.daos.admin.DaoUsuariosPermisos;
import com.auth.entities.admin.BitacoraContras;
import com.auth.entities.admin.Usuario;
import com.auth.entities.admin.UsuariosPermisos;
import com.auth.managers.commons.ManagerSQL;
import com.auth.managers.exceptions.ParametroInvalidoException;
import com.auth.managers.exceptions.UserException;
import com.auth.managers.exceptions.UsuarioBlockeadoException;
import com.auth.managers.exceptions.UsuarioInexistenteException;
import com.auth.models.ModelAltaUsuario;
import com.auth.models.ModelAsignarPermisos;
import com.auth.models.ModelCodigoRecuperacionUsuario;
import com.auth.models.ModelLogin;
import com.auth.utils.UtilsConfig;
import com.auth.utils.UtilsDate;
import com.auth.utils.UtilsMail;
import com.auth.utils.UtilsSecurity;
import java.net.MalformedURLException;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import org.apache.commons.mail.EmailException;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class ManagerUsuario extends ManagerSQL<Usuario, Integer> {

    public ManagerUsuario() {
        super(new DaoUsuario());
    }

    @Override
    public Usuario persist(Usuario entity) throws UserException.UsuarioYaExistente, Exception {
        entity.setContra(UtilsSecurity.cifrarMD5(entity.getContra()));
        entity.setCorreo(entity.getCorreo().toLowerCase());
        try {
            Usuario persisted = super.persist(entity);
            //bitacorizar la contraseña
            BitacoraContras bc = new BitacoraContras(persisted.getId(), persisted.getContra());
            bc.setUsuario1(persisted);
            ManagerBitacoraContra managerBitacoraContra = new ManagerBitacoraContra();
            managerBitacoraContra.persist(bc);
            return persisted;
        } catch (Exception e) {
            if (e.toString().contains("duplicate key value violates unique constraint")) {
                throw new UserException.UsuarioYaExistente(getMessageOfUniqueContraint(entity));
            }
            throw e;
        }
    }

    @Override
    public void update(Usuario e) throws Exception {
        Usuario usuario = this.findOne(e.getId());
        if (e.getNombre() != null) {
            if (!e.getNombre().isEmpty()) {
                usuario.setNombre(e.getNombre());
            }
        }
        if (e.getTelefono() != null) {
            if (!e.getTelefono().isEmpty()) {
                usuario.setTelefono(e.getTelefono());
            }
        }
        if (e.getCorreo() != null) {
            if (!e.getCorreo().isEmpty()) {
                usuario.setCorreo(e.getCorreo());
            }
        }
        super.update(usuario);
    }

    @Override
    public void delete(Integer id) throws Exception {
        Usuario u = this.findOne(id);
        u.setInhabilitado(Boolean.TRUE);
        this.dao.update(u);
    }

    public Usuario altaUsuario(ModelAltaUsuario model) throws UserException.UsuarioYaExistente, ParametroInvalidoException, Exception {
        //validar que venga minimo un perfil
        if (model.getPerfiles() == null) {
            throw new ParametroInvalidoException("Debe de asignar por lo menos 1 perfil cuando crea un usuario");
        } else {
            if (model.getPerfiles().isEmpty()) {
                throw new ParametroInvalidoException("Debe de asignar por lo menos 1 perfil cuando crea un usuario");
            }
        }

        //validar contraseña no nula
        if (model.getContra() == null) {
            throw new InvalidParameterException("Debe de asignar una constreña al usuario");
        } else {
            if (model.getContra().isEmpty()) {
                throw new InvalidParameterException("Debe de asignar una constreña al usuario");
            }
        }
        /*Inicializar el nuevo usuario*/
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setContra(UtilsSecurity.cifrarMD5(model.getContra()));
        nuevoUsuario.setCorreo(model.getCorreo());
        nuevoUsuario.setNombre(model.getNombre());
        nuevoUsuario.setTelefono(model.getTelefono());
        nuevoUsuario.setBloqueado(false);
        nuevoUsuario.setBloqueadoHastaFecha(new Date());
        nuevoUsuario.setInhabilitado(false);
        nuevoUsuario.setNumeroIntentosLogin(0);

        DaoUsuario daoUsuario = new DaoUsuario();
        daoUsuario.registrarUsuario(nuevoUsuario, model.getPerfiles());

        return nuevoUsuario;
    }

    /**
     * Metodo de login para autentificar usuarios
     *
     * @param modelLogin -> el usuario puede contener en su atributo user el nombre de usuario, el correo o el telefono como identificador
     * @return loged, usuario logeado
     * @throws UsuarioInexistenteException
     * @throws com.auth.managers.exceptions.UsuarioBlockeadoException
     */
    public Usuario login(ModelLogin modelLogin) throws UsuarioInexistenteException, UsuarioBlockeadoException, Exception {
        Usuario loged;
        try {
            //buscar la sucursal
            if (new DaoSucursal().findOne(modelLogin.getSucursalId()) == null) {
                throw new ParametroInvalidoException("No proporcionó una sucursal válida");
            }

            DaoUsuario daoUsuario = new DaoUsuario();
            String identificador = modelLogin.getLogin();
            String contraseña = modelLogin.getPass();
            switch (getUserIdentifierType(modelLogin.getLogin())) {
                case MAIL:
                    loged = daoUsuario.stream().where(u -> u.getCorreo().equals(identificador.toLowerCase()) && u.getContra().equals(contraseña)).findFirst().get();
                    break;
                case PHONE:
                    loged = daoUsuario.stream().where(u -> u.getTelefono().equals(identificador) && u.getContra().equals(contraseña)).findFirst().get();
                    break;
                default:
                    loged = daoUsuario.stream().where(u -> u.getNombre().equals(identificador) && u.getContra().equals(contraseña)).findFirst().get();
                    break;
            }

            if (loged.getBloqueado() && loged.getBloqueadoHastaFecha().after(new Date())) {
                throw new UsuarioBlockeadoException("Usuario bloqueado hasta " + UtilsDate.format_D_MM_YYYY_HH_MM(loged.getBloqueadoHastaFecha()));
            }
            if (loged.getInhabilitado()) {
                throw new UsuarioInexistenteException("No se encontro un usuario con esa contraseña");
            }

            loged.setNumeroIntentosLogin(0);
            daoUsuario.update(loged);

        } catch (NoSuchElementException e) {
            //verificar si existe el usuario
            this.numberAttemptVerification(modelLogin.getLogin());
            throw new UsuarioInexistenteException("No se encontro un usuario con esa contraseña");
        }
        return loged;
    }

    private void numberAttemptVerification(String identi) throws UsuarioInexistenteException, UsuarioBlockeadoException, Exception {
        try {
            Usuario intentoLogin = this.dao.stream().where(u
                    -> u.getCorreo().equals(identi.toLowerCase())
                    || u.getNombre().equals(identi)
                    || u.getTelefono().equals(identi)).findFirst().get();

            if (intentoLogin.getBloqueado()) {
                throw new UsuarioBlockeadoException("El usuario fue blockeado por el número de intentos fallidos hasta " + UtilsDate.format_D_MM_YYYY_HH_MM(intentoLogin.getBloqueadoHastaFecha()));
            }
            //<editor-fold defaultstate="collapsed" desc="CRITERIOS DE VERIFICACION DE INTENTOS DE LOGIN"> 
//             aumentar numero de intentos para bloqueo temporal si el lapso de tiempo es mayor al configurado 
//             si el numero de intentos realizados es nulo, inicializar y actualizar 
//             si realiza un intento en un ranto menor a los segundos permitidos entre intentos aumentar intentos 
//             si el numero de intentos es mayor al permitido dejar el usuario bloqueado por un timepo configurado 
//             formula = (now() - lastUserAttemptLoginDate) > timeBetweenAttempts             
            //</editor-fold>                
            if (intentoLogin.getNumeroIntentosLogin() == 0) {
                intentoLogin.setNumeroIntentosLogin(1);
                intentoLogin.setFechaUltimoIntentoLogin(new Date());
            } else {
                long intervaloDeIntento = (new Date().getTime() - intentoLogin.getFechaUltimoIntentoLogin().getTime());
                if (intervaloDeIntento < UtilsConfig.getSecondsBetweenLoginAttempt()) { //es un intento fuera del rango permitido de tiempo 
                    intentoLogin.setNumeroIntentosLogin(intentoLogin.getNumeroIntentosLogin() + 1);
                } else { //es un intento dentro del rango permitido de tiempo 
                    intentoLogin.setNumeroIntentosLogin(1);
                    intentoLogin.setFechaUltimoIntentoLogin(new Date());
                }
            }

            //si el numero de intentos excede el permitido, bloquear usuario
            try {
                if (intentoLogin.getNumeroIntentosLogin() > 5) {
                    intentoLogin.setBloqueado(true);
                    intentoLogin.setBloqueadoHastaFecha(UtilsConfig.getDateUtilUserStillBlocked());
                    this.dao.update(intentoLogin);
                    throw new UsuarioBlockeadoException("El usuario fue blockeado por el número de intentos fallidos hasta " + UtilsDate.format_D_MM_YYYY_HH_MM(intentoLogin.getBloqueadoHastaFecha()));
                } else {
                    this.dao.update(intentoLogin);
                }
            } catch (UsuarioBlockeadoException e) {
                throw e;
            } catch (Exception e) {
                throw e;
            }
        } catch (NoSuchElementException e) {
            throw new UsuarioInexistenteException("La contraseña es incorrecta");
        }
    }

    private String getMessageOfUniqueContraint(Usuario entity) {
        //buscar que atributo ya ocupado          
        String mensaje = "ya existen un usuario con el atributo";
        String nombre = entity.getNombre();
        String correo = entity.getCorreo();
        String telefono = entity.getTelefono();
        if (this.dao.stream().where(u -> u.getNombre().equals(nombre)).findFirst().isPresent()) {
            mensaje += " nombre,";
        }
        if (this.dao.stream().where(u -> u.getCorreo().equals(correo.toLowerCase())).findFirst().isPresent()) {
            mensaje += " correo,";
        }
        if (this.dao.stream().where(u -> u.getTelefono().equals(telefono)).findFirst().isPresent()) {
            mensaje += " telefono,";
        }
        mensaje = mensaje.substring(0, mensaje.length() - 1);
        return mensaje;
    }

    private userIdentifierType getUserIdentifierType(String userIdentifier) {
        if (userIdentifier.contains("@")) { //es un correo 
            return userIdentifierType.MAIL;
        } else {
            if (userIdentifier.matches("^[0-9]{10}$")) {
                return userIdentifierType.PHONE;
            } else {
                return userIdentifierType.USER;
            }
        }
    }

    public ModelCodigoRecuperacionUsuario enviarCodigo(final String identifier) throws UsuarioInexistenteException, ParametroInvalidoException, MalformedURLException, EmailException {
        final Usuario usuarioARecuperar = this.dao.stream().where(u -> u.getCorreo().equals(identifier.toLowerCase())).findFirst().get();
        Random r = new Random();
        //generar codigo de 8 digitos aleatorios
        String code = String.valueOf(r.nextInt(99));
        code += String.valueOf(r.nextInt(99));
        code += String.valueOf(r.nextInt(99));
        code += String.valueOf(r.nextInt(99));
        //enviar correo con codigo de recuperacion
        final String finalCode = code;
        new Thread(() -> {
            try {
                UtilsMail.sendRecuperarContraseñaHTMLMail(identifier, usuarioARecuperar.getNombre(), finalCode);
            } catch (EmailException ex) {
                Logger.getLogger(ManagerUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(ManagerUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
        ModelCodigoRecuperacionUsuario model = new ModelCodigoRecuperacionUsuario(code, usuarioARecuperar.getId());
        return model;

    }

    public void resetPassword(Integer userId, String pass) throws Exception {
        pass = UtilsSecurity.cifrarMD5(pass);

        BitacoraContras bitacoraContra = new BitacoraContras(userId, pass);
        DaoBitacoraContra daoBitacora = new DaoBitacoraContra();

        if (daoBitacora.exists(bitacoraContra.getBitacoraContrasPK())) {
            throw new ParametroInvalidoException("La contraseña que esta ingresando ya fué utilizada, intente con otra");
        }

        DaoUsuario daoUsuario = new DaoUsuario();
        Usuario u = dao.findOne(userId);
        u.setContra(pass);
        daoUsuario.update(u);

        List<BitacoraContras> bitacoraContras = daoBitacora.stream()
                .where(b -> b.getBitacoraContrasPK().getUsuario() == (userId))
                .sorted((b1, b2) -> b1.getFechaAsignada().compareTo(b2.getFechaAsignada()))
                .collect(toList());

        //obtener el numero maximo de contraseñas a guardar para impedir repeticion
        int maxNumber = UtilsConfig.getMaxPasswordRecords();
        // lastPassword.size() < maxNumber -> agregar pass actual al registro
        // lastPassword.size() >= maxNumber -> resize de lastPassword con los ultimos maxNumber contraseñas                        
        bitacoraContra.setUsuario1(u);
        if (bitacoraContras.size() < maxNumber) {
            daoBitacora.persist(bitacoraContra); //añadir la bitacora de la contra usada            
        } else {
            daoBitacora.delete(bitacoraContras.get(0).getBitacoraContrasPK()); //remover la ultima contra asignada
            daoBitacora.persist(bitacoraContra);//agregar nueva
        }
    }

    public List<UsuariosPermisos> reemplazarPermisos(ModelAsignarPermisos modelo) throws Exception {
        return new DaoUsuariosPermisos().reemplazarPermisos(modelo.getId(), modelo.getPermisos());
    }

    public String nombreDeUsuario(Integer usuarioId) {
        return dao.stream().where(u -> u.getId().equals(usuarioId)).findFirst().get().getNombre();

    }

    private enum userIdentifierType {
        PHONE, MAIL, USER
    }

}
