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

import com.machineAdmin.daos.cg.admin.postgres.jpaControllers.exceptions.NonexistentEntityException;
import com.machineAdmin.daos.cg.admin.postgres.jpaControllers.exceptions.PreexistingEntityException;
import com.machineAdmin.entities.cg.admin.postgres.GrupoPerfiles;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.machineAdmin.entities.cg.admin.postgres.Perfil;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class GrupoPerfilesJpaController implements Serializable {

    public GrupoPerfilesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GrupoPerfiles grupoPerfiles) throws PreexistingEntityException, Exception {
        if (grupoPerfiles.getPerfilList() == null) {
            grupoPerfiles.setPerfilList(new ArrayList<Perfil>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Perfil> attachedPerfilList = new ArrayList<Perfil>();
            for (Perfil perfilListPerfilToAttach : grupoPerfiles.getPerfilList()) {
                perfilListPerfilToAttach = em.getReference(perfilListPerfilToAttach.getClass(), perfilListPerfilToAttach.getId());
                attachedPerfilList.add(perfilListPerfilToAttach);
            }
            grupoPerfiles.setPerfilList(attachedPerfilList);
            em.persist(grupoPerfiles);
            for (Perfil perfilListPerfil : grupoPerfiles.getPerfilList()) {
                perfilListPerfil.getGrupoPerfilesList().add(grupoPerfiles);
                perfilListPerfil = em.merge(perfilListPerfil);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGrupoPerfiles(grupoPerfiles.getId()) != null) {
                throw new PreexistingEntityException("GrupoPerfiles " + grupoPerfiles + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GrupoPerfiles grupoPerfiles) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GrupoPerfiles persistentGrupoPerfiles = em.find(GrupoPerfiles.class, grupoPerfiles.getId());
            List<Perfil> perfilListOld = persistentGrupoPerfiles.getPerfilList();
            List<Perfil> perfilListNew = grupoPerfiles.getPerfilList();
            List<Perfil> attachedPerfilListNew = new ArrayList<Perfil>();
            for (Perfil perfilListNewPerfilToAttach : perfilListNew) {
                perfilListNewPerfilToAttach = em.getReference(perfilListNewPerfilToAttach.getClass(), perfilListNewPerfilToAttach.getId());
                attachedPerfilListNew.add(perfilListNewPerfilToAttach);
            }
            perfilListNew = attachedPerfilListNew;
            grupoPerfiles.setPerfilList(perfilListNew);
            grupoPerfiles = em.merge(grupoPerfiles);
            for (Perfil perfilListOldPerfil : perfilListOld) {
                if (!perfilListNew.contains(perfilListOldPerfil)) {
                    perfilListOldPerfil.getGrupoPerfilesList().remove(grupoPerfiles);
                    perfilListOldPerfil = em.merge(perfilListOldPerfil);
                }
            }
            for (Perfil perfilListNewPerfil : perfilListNew) {
                if (!perfilListOld.contains(perfilListNewPerfil)) {
                    perfilListNewPerfil.getGrupoPerfilesList().add(grupoPerfiles);
                    perfilListNewPerfil = em.merge(perfilListNewPerfil);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                UUID id = grupoPerfiles.getId();
                if (findGrupoPerfiles(id) == null) {
                    throw new NonexistentEntityException("The grupoPerfiles with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(UUID id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GrupoPerfiles grupoPerfiles;
            try {
                grupoPerfiles = em.getReference(GrupoPerfiles.class, id);
                grupoPerfiles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupoPerfiles with id " + id + " no longer exists.", enfe);
            }
            List<Perfil> perfilList = grupoPerfiles.getPerfilList();
            for (Perfil perfilListPerfil : perfilList) {
                perfilListPerfil.getGrupoPerfilesList().remove(grupoPerfiles);
                perfilListPerfil = em.merge(perfilListPerfil);
            }
            em.remove(grupoPerfiles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GrupoPerfiles> findGrupoPerfilesEntities() {
        return findGrupoPerfilesEntities(true, -1, -1);
    }

    public List<GrupoPerfiles> findGrupoPerfilesEntities(int maxResults, int firstResult) {
        return findGrupoPerfilesEntities(false, maxResults, firstResult);
    }

    private List<GrupoPerfiles> findGrupoPerfilesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GrupoPerfiles.class));
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

    public GrupoPerfiles findGrupoPerfiles(UUID id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GrupoPerfiles.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrupoPerfilesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GrupoPerfiles> rt = cq.from(GrupoPerfiles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
