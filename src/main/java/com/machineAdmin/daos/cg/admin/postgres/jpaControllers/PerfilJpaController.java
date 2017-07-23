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
import com.machineAdmin.entities.cg.admin.postgres.GrupoPerfiles;
import com.machineAdmin.entities.cg.admin.postgres.Perfil;
import java.util.ArrayList;
import java.util.List;
import com.machineAdmin.entities.cg.admin.postgres.PerfilesPermisos;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPerfil;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class PerfilJpaController implements Serializable {

    public PerfilJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Perfil perfil) throws PreexistingEntityException, Exception {
        if (perfil.getGrupoPerfilesList() == null) {
            perfil.setGrupoPerfilesList(new ArrayList<GrupoPerfiles>());
        }
        if (perfil.getPerfilesPermisosList() == null) {
            perfil.setPerfilesPermisosList(new ArrayList<PerfilesPermisos>());
        }
        if (perfil.getUsuariosPerfilList() == null) {
            perfil.setUsuariosPerfilList(new ArrayList<UsuariosPerfil>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<GrupoPerfiles> attachedGrupoPerfilesList = new ArrayList<GrupoPerfiles>();
            for (GrupoPerfiles grupoPerfilesListGrupoPerfilesToAttach : perfil.getGrupoPerfilesList()) {
                grupoPerfilesListGrupoPerfilesToAttach = em.getReference(grupoPerfilesListGrupoPerfilesToAttach.getClass(), grupoPerfilesListGrupoPerfilesToAttach.getId());
                attachedGrupoPerfilesList.add(grupoPerfilesListGrupoPerfilesToAttach);
            }
            perfil.setGrupoPerfilesList(attachedGrupoPerfilesList);
            List<PerfilesPermisos> attachedPerfilesPermisosList = new ArrayList<PerfilesPermisos>();
            for (PerfilesPermisos perfilesPermisosListPerfilesPermisosToAttach : perfil.getPerfilesPermisosList()) {
                perfilesPermisosListPerfilesPermisosToAttach = em.getReference(perfilesPermisosListPerfilesPermisosToAttach.getClass(), perfilesPermisosListPerfilesPermisosToAttach.getPerfilesPermisosPK());
                attachedPerfilesPermisosList.add(perfilesPermisosListPerfilesPermisosToAttach);
            }
            perfil.setPerfilesPermisosList(attachedPerfilesPermisosList);
            List<UsuariosPerfil> attachedUsuariosPerfilList = new ArrayList<UsuariosPerfil>();
            for (UsuariosPerfil usuariosPerfilListUsuariosPerfilToAttach : perfil.getUsuariosPerfilList()) {
                usuariosPerfilListUsuariosPerfilToAttach = em.getReference(usuariosPerfilListUsuariosPerfilToAttach.getClass(), usuariosPerfilListUsuariosPerfilToAttach.getUsuariosPerfilPK());
                attachedUsuariosPerfilList.add(usuariosPerfilListUsuariosPerfilToAttach);
            }
            perfil.setUsuariosPerfilList(attachedUsuariosPerfilList);
            em.persist(perfil);
            for (GrupoPerfiles grupoPerfilesListGrupoPerfiles : perfil.getGrupoPerfilesList()) {
                grupoPerfilesListGrupoPerfiles.getPerfilList().add(perfil);
                grupoPerfilesListGrupoPerfiles = em.merge(grupoPerfilesListGrupoPerfiles);
            }
            for (PerfilesPermisos perfilesPermisosListPerfilesPermisos : perfil.getPerfilesPermisosList()) {
                Perfil oldPerfil1OfPerfilesPermisosListPerfilesPermisos = perfilesPermisosListPerfilesPermisos.getPerfil1();
                perfilesPermisosListPerfilesPermisos.setPerfil1(perfil);
                perfilesPermisosListPerfilesPermisos = em.merge(perfilesPermisosListPerfilesPermisos);
                if (oldPerfil1OfPerfilesPermisosListPerfilesPermisos != null) {
                    oldPerfil1OfPerfilesPermisosListPerfilesPermisos.getPerfilesPermisosList().remove(perfilesPermisosListPerfilesPermisos);
                    oldPerfil1OfPerfilesPermisosListPerfilesPermisos = em.merge(oldPerfil1OfPerfilesPermisosListPerfilesPermisos);
                }
            }
            for (UsuariosPerfil usuariosPerfilListUsuariosPerfil : perfil.getUsuariosPerfilList()) {
                Perfil oldPerfil1OfUsuariosPerfilListUsuariosPerfil = usuariosPerfilListUsuariosPerfil.getPerfil1();
                usuariosPerfilListUsuariosPerfil.setPerfil1(perfil);
                usuariosPerfilListUsuariosPerfil = em.merge(usuariosPerfilListUsuariosPerfil);
                if (oldPerfil1OfUsuariosPerfilListUsuariosPerfil != null) {
                    oldPerfil1OfUsuariosPerfilListUsuariosPerfil.getUsuariosPerfilList().remove(usuariosPerfilListUsuariosPerfil);
                    oldPerfil1OfUsuariosPerfilListUsuariosPerfil = em.merge(oldPerfil1OfUsuariosPerfilListUsuariosPerfil);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPerfil(perfil.getId()) != null) {
                throw new PreexistingEntityException("Perfil " + perfil + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Perfil perfil) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perfil persistentPerfil = em.find(Perfil.class, perfil.getId());
            List<GrupoPerfiles> grupoPerfilesListOld = persistentPerfil.getGrupoPerfilesList();
            List<GrupoPerfiles> grupoPerfilesListNew = perfil.getGrupoPerfilesList();
            List<PerfilesPermisos> perfilesPermisosListOld = persistentPerfil.getPerfilesPermisosList();
            List<PerfilesPermisos> perfilesPermisosListNew = perfil.getPerfilesPermisosList();
            List<UsuariosPerfil> usuariosPerfilListOld = persistentPerfil.getUsuariosPerfilList();
            List<UsuariosPerfil> usuariosPerfilListNew = perfil.getUsuariosPerfilList();
            List<String> illegalOrphanMessages = null;
            for (PerfilesPermisos perfilesPermisosListOldPerfilesPermisos : perfilesPermisosListOld) {
                if (!perfilesPermisosListNew.contains(perfilesPermisosListOldPerfilesPermisos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PerfilesPermisos " + perfilesPermisosListOldPerfilesPermisos + " since its perfil1 field is not nullable.");
                }
            }
            for (UsuariosPerfil usuariosPerfilListOldUsuariosPerfil : usuariosPerfilListOld) {
                if (!usuariosPerfilListNew.contains(usuariosPerfilListOldUsuariosPerfil)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuariosPerfil " + usuariosPerfilListOldUsuariosPerfil + " since its perfil1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<GrupoPerfiles> attachedGrupoPerfilesListNew = new ArrayList<GrupoPerfiles>();
            for (GrupoPerfiles grupoPerfilesListNewGrupoPerfilesToAttach : grupoPerfilesListNew) {
                grupoPerfilesListNewGrupoPerfilesToAttach = em.getReference(grupoPerfilesListNewGrupoPerfilesToAttach.getClass(), grupoPerfilesListNewGrupoPerfilesToAttach.getId());
                attachedGrupoPerfilesListNew.add(grupoPerfilesListNewGrupoPerfilesToAttach);
            }
            grupoPerfilesListNew = attachedGrupoPerfilesListNew;
            perfil.setGrupoPerfilesList(grupoPerfilesListNew);
            List<PerfilesPermisos> attachedPerfilesPermisosListNew = new ArrayList<PerfilesPermisos>();
            for (PerfilesPermisos perfilesPermisosListNewPerfilesPermisosToAttach : perfilesPermisosListNew) {
                perfilesPermisosListNewPerfilesPermisosToAttach = em.getReference(perfilesPermisosListNewPerfilesPermisosToAttach.getClass(), perfilesPermisosListNewPerfilesPermisosToAttach.getPerfilesPermisosPK());
                attachedPerfilesPermisosListNew.add(perfilesPermisosListNewPerfilesPermisosToAttach);
            }
            perfilesPermisosListNew = attachedPerfilesPermisosListNew;
            perfil.setPerfilesPermisosList(perfilesPermisosListNew);
            List<UsuariosPerfil> attachedUsuariosPerfilListNew = new ArrayList<UsuariosPerfil>();
            for (UsuariosPerfil usuariosPerfilListNewUsuariosPerfilToAttach : usuariosPerfilListNew) {
                usuariosPerfilListNewUsuariosPerfilToAttach = em.getReference(usuariosPerfilListNewUsuariosPerfilToAttach.getClass(), usuariosPerfilListNewUsuariosPerfilToAttach.getUsuariosPerfilPK());
                attachedUsuariosPerfilListNew.add(usuariosPerfilListNewUsuariosPerfilToAttach);
            }
            usuariosPerfilListNew = attachedUsuariosPerfilListNew;
            perfil.setUsuariosPerfilList(usuariosPerfilListNew);
            perfil = em.merge(perfil);
            for (GrupoPerfiles grupoPerfilesListOldGrupoPerfiles : grupoPerfilesListOld) {
                if (!grupoPerfilesListNew.contains(grupoPerfilesListOldGrupoPerfiles)) {
                    grupoPerfilesListOldGrupoPerfiles.getPerfilList().remove(perfil);
                    grupoPerfilesListOldGrupoPerfiles = em.merge(grupoPerfilesListOldGrupoPerfiles);
                }
            }
            for (GrupoPerfiles grupoPerfilesListNewGrupoPerfiles : grupoPerfilesListNew) {
                if (!grupoPerfilesListOld.contains(grupoPerfilesListNewGrupoPerfiles)) {
                    grupoPerfilesListNewGrupoPerfiles.getPerfilList().add(perfil);
                    grupoPerfilesListNewGrupoPerfiles = em.merge(grupoPerfilesListNewGrupoPerfiles);
                }
            }
            for (PerfilesPermisos perfilesPermisosListNewPerfilesPermisos : perfilesPermisosListNew) {
                if (!perfilesPermisosListOld.contains(perfilesPermisosListNewPerfilesPermisos)) {
                    Perfil oldPerfil1OfPerfilesPermisosListNewPerfilesPermisos = perfilesPermisosListNewPerfilesPermisos.getPerfil1();
                    perfilesPermisosListNewPerfilesPermisos.setPerfil1(perfil);
                    perfilesPermisosListNewPerfilesPermisos = em.merge(perfilesPermisosListNewPerfilesPermisos);
                    if (oldPerfil1OfPerfilesPermisosListNewPerfilesPermisos != null && !oldPerfil1OfPerfilesPermisosListNewPerfilesPermisos.equals(perfil)) {
                        oldPerfil1OfPerfilesPermisosListNewPerfilesPermisos.getPerfilesPermisosList().remove(perfilesPermisosListNewPerfilesPermisos);
                        oldPerfil1OfPerfilesPermisosListNewPerfilesPermisos = em.merge(oldPerfil1OfPerfilesPermisosListNewPerfilesPermisos);
                    }
                }
            }
            for (UsuariosPerfil usuariosPerfilListNewUsuariosPerfil : usuariosPerfilListNew) {
                if (!usuariosPerfilListOld.contains(usuariosPerfilListNewUsuariosPerfil)) {
                    Perfil oldPerfil1OfUsuariosPerfilListNewUsuariosPerfil = usuariosPerfilListNewUsuariosPerfil.getPerfil1();
                    usuariosPerfilListNewUsuariosPerfil.setPerfil1(perfil);
                    usuariosPerfilListNewUsuariosPerfil = em.merge(usuariosPerfilListNewUsuariosPerfil);
                    if (oldPerfil1OfUsuariosPerfilListNewUsuariosPerfil != null && !oldPerfil1OfUsuariosPerfilListNewUsuariosPerfil.equals(perfil)) {
                        oldPerfil1OfUsuariosPerfilListNewUsuariosPerfil.getUsuariosPerfilList().remove(usuariosPerfilListNewUsuariosPerfil);
                        oldPerfil1OfUsuariosPerfilListNewUsuariosPerfil = em.merge(oldPerfil1OfUsuariosPerfilListNewUsuariosPerfil);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Object id = perfil.getId();
                if (findPerfil(id) == null) {
                    throw new NonexistentEntityException("The perfil with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Object id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perfil perfil;
            try {
                perfil = em.getReference(Perfil.class, id);
                perfil.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The perfil with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PerfilesPermisos> perfilesPermisosListOrphanCheck = perfil.getPerfilesPermisosList();
            for (PerfilesPermisos perfilesPermisosListOrphanCheckPerfilesPermisos : perfilesPermisosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Perfil (" + perfil + ") cannot be destroyed since the PerfilesPermisos " + perfilesPermisosListOrphanCheckPerfilesPermisos + " in its perfilesPermisosList field has a non-nullable perfil1 field.");
            }
            List<UsuariosPerfil> usuariosPerfilListOrphanCheck = perfil.getUsuariosPerfilList();
            for (UsuariosPerfil usuariosPerfilListOrphanCheckUsuariosPerfil : usuariosPerfilListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Perfil (" + perfil + ") cannot be destroyed since the UsuariosPerfil " + usuariosPerfilListOrphanCheckUsuariosPerfil + " in its usuariosPerfilList field has a non-nullable perfil1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<GrupoPerfiles> grupoPerfilesList = perfil.getGrupoPerfilesList();
            for (GrupoPerfiles grupoPerfilesListGrupoPerfiles : grupoPerfilesList) {
                grupoPerfilesListGrupoPerfiles.getPerfilList().remove(perfil);
                grupoPerfilesListGrupoPerfiles = em.merge(grupoPerfilesListGrupoPerfiles);
            }
            em.remove(perfil);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Perfil> findPerfilEntities() {
        return findPerfilEntities(true, -1, -1);
    }

    public List<Perfil> findPerfilEntities(int maxResults, int firstResult) {
        return findPerfilEntities(false, maxResults, firstResult);
    }

    private List<Perfil> findPerfilEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Perfil.class));
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

    public Perfil findPerfil(Object id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Perfil.class, id);
        } finally {
            em.close();
        }
    }

    public int getPerfilCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Perfil> rt = cq.from(Perfil.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
