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
package com.machineAdmin.daos.cg.admin.postgres.jpaControllers;

import com.machineAdmin.daos.cg.admin.postgres.jpaControllers.exceptions.IllegalOrphanException;
import com.machineAdmin.daos.cg.admin.postgres.jpaControllers.exceptions.NonexistentEntityException;
import com.machineAdmin.daos.cg.admin.postgres.jpaControllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.machineAdmin.entities.cg.admin.postgres.BitacoraContras;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import java.util.ArrayList;
import java.util.List;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPermisos;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPerfil;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getBitacoraContrasList() == null) {
            usuario.setBitacoraContrasList(new ArrayList<BitacoraContras>());
        }
        if (usuario.getUsuariosPermisosList() == null) {
            usuario.setUsuariosPermisosList(new ArrayList<UsuariosPermisos>());
        }
        if (usuario.getUsuariosPerfilList() == null) {
            usuario.setUsuariosPerfilList(new ArrayList<UsuariosPerfil>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<BitacoraContras> attachedBitacoraContrasList = new ArrayList<BitacoraContras>();
            for (BitacoraContras bitacoraContrasListBitacoraContrasToAttach : usuario.getBitacoraContrasList()) {
                bitacoraContrasListBitacoraContrasToAttach = em.getReference(bitacoraContrasListBitacoraContrasToAttach.getClass(), bitacoraContrasListBitacoraContrasToAttach.getBitacoraContrasPK());
                attachedBitacoraContrasList.add(bitacoraContrasListBitacoraContrasToAttach);
            }
            usuario.setBitacoraContrasList(attachedBitacoraContrasList);
            List<UsuariosPermisos> attachedUsuariosPermisosList = new ArrayList<UsuariosPermisos>();
            for (UsuariosPermisos usuariosPermisosListUsuariosPermisosToAttach : usuario.getUsuariosPermisosList()) {
                usuariosPermisosListUsuariosPermisosToAttach = em.getReference(usuariosPermisosListUsuariosPermisosToAttach.getClass(), usuariosPermisosListUsuariosPermisosToAttach.getUsuariosPermisosPK());
                attachedUsuariosPermisosList.add(usuariosPermisosListUsuariosPermisosToAttach);
            }
            usuario.setUsuariosPermisosList(attachedUsuariosPermisosList);
            List<UsuariosPerfil> attachedUsuariosPerfilList = new ArrayList<UsuariosPerfil>();
            for (UsuariosPerfil usuariosPerfilListUsuariosPerfilToAttach : usuario.getUsuariosPerfilList()) {
                usuariosPerfilListUsuariosPerfilToAttach = em.getReference(usuariosPerfilListUsuariosPerfilToAttach.getClass(), usuariosPerfilListUsuariosPerfilToAttach.getUsuariosPerfilPK());
                attachedUsuariosPerfilList.add(usuariosPerfilListUsuariosPerfilToAttach);
            }
            usuario.setUsuariosPerfilList(attachedUsuariosPerfilList);
            em.persist(usuario);
            for (BitacoraContras bitacoraContrasListBitacoraContras : usuario.getBitacoraContrasList()) {
                Usuario oldUsuario1OfBitacoraContrasListBitacoraContras = bitacoraContrasListBitacoraContras.getUsuario1();
                bitacoraContrasListBitacoraContras.setUsuario1(usuario);
                bitacoraContrasListBitacoraContras = em.merge(bitacoraContrasListBitacoraContras);
                if (oldUsuario1OfBitacoraContrasListBitacoraContras != null) {
                    oldUsuario1OfBitacoraContrasListBitacoraContras.getBitacoraContrasList().remove(bitacoraContrasListBitacoraContras);
                    oldUsuario1OfBitacoraContrasListBitacoraContras = em.merge(oldUsuario1OfBitacoraContrasListBitacoraContras);
                }
            }
            for (UsuariosPermisos usuariosPermisosListUsuariosPermisos : usuario.getUsuariosPermisosList()) {
                Usuario oldUsuario1OfUsuariosPermisosListUsuariosPermisos = usuariosPermisosListUsuariosPermisos.getUsuario1();
                usuariosPermisosListUsuariosPermisos.setUsuario1(usuario);
                usuariosPermisosListUsuariosPermisos = em.merge(usuariosPermisosListUsuariosPermisos);
                if (oldUsuario1OfUsuariosPermisosListUsuariosPermisos != null) {
                    oldUsuario1OfUsuariosPermisosListUsuariosPermisos.getUsuariosPermisosList().remove(usuariosPermisosListUsuariosPermisos);
                    oldUsuario1OfUsuariosPermisosListUsuariosPermisos = em.merge(oldUsuario1OfUsuariosPermisosListUsuariosPermisos);
                }
            }
            for (UsuariosPerfil usuariosPerfilListUsuariosPerfil : usuario.getUsuariosPerfilList()) {
                Usuario oldUsuario1OfUsuariosPerfilListUsuariosPerfil = usuariosPerfilListUsuariosPerfil.getUsuario1();
                usuariosPerfilListUsuariosPerfil.setUsuario1(usuario);
                usuariosPerfilListUsuariosPerfil = em.merge(usuariosPerfilListUsuariosPerfil);
                if (oldUsuario1OfUsuariosPerfilListUsuariosPerfil != null) {
                    oldUsuario1OfUsuariosPerfilListUsuariosPerfil.getUsuariosPerfilList().remove(usuariosPerfilListUsuariosPerfil);
                    oldUsuario1OfUsuariosPerfilListUsuariosPerfil = em.merge(oldUsuario1OfUsuariosPerfilListUsuariosPerfil);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getId()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            List<BitacoraContras> bitacoraContrasListOld = persistentUsuario.getBitacoraContrasList();
            List<BitacoraContras> bitacoraContrasListNew = usuario.getBitacoraContrasList();
            List<UsuariosPermisos> usuariosPermisosListOld = persistentUsuario.getUsuariosPermisosList();
            List<UsuariosPermisos> usuariosPermisosListNew = usuario.getUsuariosPermisosList();
            List<UsuariosPerfil> usuariosPerfilListOld = persistentUsuario.getUsuariosPerfilList();
            List<UsuariosPerfil> usuariosPerfilListNew = usuario.getUsuariosPerfilList();
            List<String> illegalOrphanMessages = null;
            for (BitacoraContras bitacoraContrasListOldBitacoraContras : bitacoraContrasListOld) {
                if (!bitacoraContrasListNew.contains(bitacoraContrasListOldBitacoraContras)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BitacoraContras " + bitacoraContrasListOldBitacoraContras + " since its usuario1 field is not nullable.");
                }
            }
            for (UsuariosPermisos usuariosPermisosListOldUsuariosPermisos : usuariosPermisosListOld) {
                if (!usuariosPermisosListNew.contains(usuariosPermisosListOldUsuariosPermisos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuariosPermisos " + usuariosPermisosListOldUsuariosPermisos + " since its usuario1 field is not nullable.");
                }
            }
            for (UsuariosPerfil usuariosPerfilListOldUsuariosPerfil : usuariosPerfilListOld) {
                if (!usuariosPerfilListNew.contains(usuariosPerfilListOldUsuariosPerfil)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuariosPerfil " + usuariosPerfilListOldUsuariosPerfil + " since its usuario1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<BitacoraContras> attachedBitacoraContrasListNew = new ArrayList<BitacoraContras>();
            for (BitacoraContras bitacoraContrasListNewBitacoraContrasToAttach : bitacoraContrasListNew) {
                bitacoraContrasListNewBitacoraContrasToAttach = em.getReference(bitacoraContrasListNewBitacoraContrasToAttach.getClass(), bitacoraContrasListNewBitacoraContrasToAttach.getBitacoraContrasPK());
                attachedBitacoraContrasListNew.add(bitacoraContrasListNewBitacoraContrasToAttach);
            }
            bitacoraContrasListNew = attachedBitacoraContrasListNew;
            usuario.setBitacoraContrasList(bitacoraContrasListNew);
            List<UsuariosPermisos> attachedUsuariosPermisosListNew = new ArrayList<UsuariosPermisos>();
            for (UsuariosPermisos usuariosPermisosListNewUsuariosPermisosToAttach : usuariosPermisosListNew) {
                usuariosPermisosListNewUsuariosPermisosToAttach = em.getReference(usuariosPermisosListNewUsuariosPermisosToAttach.getClass(), usuariosPermisosListNewUsuariosPermisosToAttach.getUsuariosPermisosPK());
                attachedUsuariosPermisosListNew.add(usuariosPermisosListNewUsuariosPermisosToAttach);
            }
            usuariosPermisosListNew = attachedUsuariosPermisosListNew;
            usuario.setUsuariosPermisosList(usuariosPermisosListNew);
            List<UsuariosPerfil> attachedUsuariosPerfilListNew = new ArrayList<UsuariosPerfil>();
            for (UsuariosPerfil usuariosPerfilListNewUsuariosPerfilToAttach : usuariosPerfilListNew) {
                usuariosPerfilListNewUsuariosPerfilToAttach = em.getReference(usuariosPerfilListNewUsuariosPerfilToAttach.getClass(), usuariosPerfilListNewUsuariosPerfilToAttach.getUsuariosPerfilPK());
                attachedUsuariosPerfilListNew.add(usuariosPerfilListNewUsuariosPerfilToAttach);
            }
            usuariosPerfilListNew = attachedUsuariosPerfilListNew;
            usuario.setUsuariosPerfilList(usuariosPerfilListNew);
            usuario = em.merge(usuario);
            for (BitacoraContras bitacoraContrasListNewBitacoraContras : bitacoraContrasListNew) {
                if (!bitacoraContrasListOld.contains(bitacoraContrasListNewBitacoraContras)) {
                    Usuario oldUsuario1OfBitacoraContrasListNewBitacoraContras = bitacoraContrasListNewBitacoraContras.getUsuario1();
                    bitacoraContrasListNewBitacoraContras.setUsuario1(usuario);
                    bitacoraContrasListNewBitacoraContras = em.merge(bitacoraContrasListNewBitacoraContras);
                    if (oldUsuario1OfBitacoraContrasListNewBitacoraContras != null && !oldUsuario1OfBitacoraContrasListNewBitacoraContras.equals(usuario)) {
                        oldUsuario1OfBitacoraContrasListNewBitacoraContras.getBitacoraContrasList().remove(bitacoraContrasListNewBitacoraContras);
                        oldUsuario1OfBitacoraContrasListNewBitacoraContras = em.merge(oldUsuario1OfBitacoraContrasListNewBitacoraContras);
                    }
                }
            }
            for (UsuariosPermisos usuariosPermisosListNewUsuariosPermisos : usuariosPermisosListNew) {
                if (!usuariosPermisosListOld.contains(usuariosPermisosListNewUsuariosPermisos)) {
                    Usuario oldUsuario1OfUsuariosPermisosListNewUsuariosPermisos = usuariosPermisosListNewUsuariosPermisos.getUsuario1();
                    usuariosPermisosListNewUsuariosPermisos.setUsuario1(usuario);
                    usuariosPermisosListNewUsuariosPermisos = em.merge(usuariosPermisosListNewUsuariosPermisos);
                    if (oldUsuario1OfUsuariosPermisosListNewUsuariosPermisos != null && !oldUsuario1OfUsuariosPermisosListNewUsuariosPermisos.equals(usuario)) {
                        oldUsuario1OfUsuariosPermisosListNewUsuariosPermisos.getUsuariosPermisosList().remove(usuariosPermisosListNewUsuariosPermisos);
                        oldUsuario1OfUsuariosPermisosListNewUsuariosPermisos = em.merge(oldUsuario1OfUsuariosPermisosListNewUsuariosPermisos);
                    }
                }
            }
            for (UsuariosPerfil usuariosPerfilListNewUsuariosPerfil : usuariosPerfilListNew) {
                if (!usuariosPerfilListOld.contains(usuariosPerfilListNewUsuariosPerfil)) {
                    Usuario oldUsuario1OfUsuariosPerfilListNewUsuariosPerfil = usuariosPerfilListNewUsuariosPerfil.getUsuario1();
                    usuariosPerfilListNewUsuariosPerfil.setUsuario1(usuario);
                    usuariosPerfilListNewUsuariosPerfil = em.merge(usuariosPerfilListNewUsuariosPerfil);
                    if (oldUsuario1OfUsuariosPerfilListNewUsuariosPerfil != null && !oldUsuario1OfUsuariosPerfilListNewUsuariosPerfil.equals(usuario)) {
                        oldUsuario1OfUsuariosPerfilListNewUsuariosPerfil.getUsuariosPerfilList().remove(usuariosPerfilListNewUsuariosPerfil);
                        oldUsuario1OfUsuariosPerfilListNewUsuariosPerfil = em.merge(oldUsuario1OfUsuariosPerfilListNewUsuariosPerfil);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                UUID id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(UUID id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<BitacoraContras> bitacoraContrasListOrphanCheck = usuario.getBitacoraContrasList();
            for (BitacoraContras bitacoraContrasListOrphanCheckBitacoraContras : bitacoraContrasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the BitacoraContras " + bitacoraContrasListOrphanCheckBitacoraContras + " in its bitacoraContrasList field has a non-nullable usuario1 field.");
            }
            List<UsuariosPermisos> usuariosPermisosListOrphanCheck = usuario.getUsuariosPermisosList();
            for (UsuariosPermisos usuariosPermisosListOrphanCheckUsuariosPermisos : usuariosPermisosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the UsuariosPermisos " + usuariosPermisosListOrphanCheckUsuariosPermisos + " in its usuariosPermisosList field has a non-nullable usuario1 field.");
            }
            List<UsuariosPerfil> usuariosPerfilListOrphanCheck = usuario.getUsuariosPerfilList();
            for (UsuariosPerfil usuariosPerfilListOrphanCheckUsuariosPerfil : usuariosPerfilListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the UsuariosPerfil " + usuariosPerfilListOrphanCheckUsuariosPerfil + " in its usuariosPerfilList field has a non-nullable usuario1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuario findUsuario(UUID id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
