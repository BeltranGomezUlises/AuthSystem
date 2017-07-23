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
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.machineAdmin.entities.cg.admin.postgres.Perfil;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPerfil;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPerfilPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UsuariosPerfilJpaController implements Serializable {

    public UsuariosPerfilJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuariosPerfil usuariosPerfil) throws PreexistingEntityException, Exception {
        if (usuariosPerfil.getUsuariosPerfilPK() == null) {
            usuariosPerfil.setUsuariosPerfilPK(new UsuariosPerfilPK());
        }
        usuariosPerfil.getUsuariosPerfilPK().setUsuario(usuariosPerfil.getUsuario1().getId());
        usuariosPerfil.getUsuariosPerfilPK().setPerfil(usuariosPerfil.getPerfil1().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perfil perfil1 = usuariosPerfil.getPerfil1();
            if (perfil1 != null) {
                perfil1 = em.getReference(perfil1.getClass(), perfil1.getId());
                usuariosPerfil.setPerfil1(perfil1);
            }
            Usuario usuario1 = usuariosPerfil.getUsuario1();
            if (usuario1 != null) {
                usuario1 = em.getReference(usuario1.getClass(), usuario1.getId());
                usuariosPerfil.setUsuario1(usuario1);
            }
            em.persist(usuariosPerfil);
            if (perfil1 != null) {
                perfil1.getUsuariosPerfilList().add(usuariosPerfil);
                perfil1 = em.merge(perfil1);
            }
            if (usuario1 != null) {
                usuario1.getUsuariosPerfilList().add(usuariosPerfil);
                usuario1 = em.merge(usuario1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuariosPerfil(usuariosPerfil.getUsuariosPerfilPK()) != null) {
                throw new PreexistingEntityException("UsuariosPerfil " + usuariosPerfil + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuariosPerfil usuariosPerfil) throws NonexistentEntityException, Exception {
        usuariosPerfil.getUsuariosPerfilPK().setUsuario(usuariosPerfil.getUsuario1().getId());
        usuariosPerfil.getUsuariosPerfilPK().setPerfil(usuariosPerfil.getPerfil1().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuariosPerfil persistentUsuariosPerfil = em.find(UsuariosPerfil.class, usuariosPerfil.getUsuariosPerfilPK());
            Perfil perfil1Old = persistentUsuariosPerfil.getPerfil1();
            Perfil perfil1New = usuariosPerfil.getPerfil1();
            Usuario usuario1Old = persistentUsuariosPerfil.getUsuario1();
            Usuario usuario1New = usuariosPerfil.getUsuario1();
            if (perfil1New != null) {
                perfil1New = em.getReference(perfil1New.getClass(), perfil1New.getId());
                usuariosPerfil.setPerfil1(perfil1New);
            }
            if (usuario1New != null) {
                usuario1New = em.getReference(usuario1New.getClass(), usuario1New.getId());
                usuariosPerfil.setUsuario1(usuario1New);
            }
            usuariosPerfil = em.merge(usuariosPerfil);
            if (perfil1Old != null && !perfil1Old.equals(perfil1New)) {
                perfil1Old.getUsuariosPerfilList().remove(usuariosPerfil);
                perfil1Old = em.merge(perfil1Old);
            }
            if (perfil1New != null && !perfil1New.equals(perfil1Old)) {
                perfil1New.getUsuariosPerfilList().add(usuariosPerfil);
                perfil1New = em.merge(perfil1New);
            }
            if (usuario1Old != null && !usuario1Old.equals(usuario1New)) {
                usuario1Old.getUsuariosPerfilList().remove(usuariosPerfil);
                usuario1Old = em.merge(usuario1Old);
            }
            if (usuario1New != null && !usuario1New.equals(usuario1Old)) {
                usuario1New.getUsuariosPerfilList().add(usuariosPerfil);
                usuario1New = em.merge(usuario1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                UsuariosPerfilPK id = usuariosPerfil.getUsuariosPerfilPK();
                if (findUsuariosPerfil(id) == null) {
                    throw new NonexistentEntityException("The usuariosPerfil with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(UsuariosPerfilPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuariosPerfil usuariosPerfil;
            try {
                usuariosPerfil = em.getReference(UsuariosPerfil.class, id);
                usuariosPerfil.getUsuariosPerfilPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuariosPerfil with id " + id + " no longer exists.", enfe);
            }
            Perfil perfil1 = usuariosPerfil.getPerfil1();
            if (perfil1 != null) {
                perfil1.getUsuariosPerfilList().remove(usuariosPerfil);
                perfil1 = em.merge(perfil1);
            }
            Usuario usuario1 = usuariosPerfil.getUsuario1();
            if (usuario1 != null) {
                usuario1.getUsuariosPerfilList().remove(usuariosPerfil);
                usuario1 = em.merge(usuario1);
            }
            em.remove(usuariosPerfil);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuariosPerfil> findUsuariosPerfilEntities() {
        return findUsuariosPerfilEntities(true, -1, -1);
    }

    public List<UsuariosPerfil> findUsuariosPerfilEntities(int maxResults, int firstResult) {
        return findUsuariosPerfilEntities(false, maxResults, firstResult);
    }

    private List<UsuariosPerfil> findUsuariosPerfilEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuariosPerfil.class));
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

    public UsuariosPerfil findUsuariosPerfil(UsuariosPerfilPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuariosPerfil.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosPerfilCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuariosPerfil> rt = cq.from(UsuariosPerfil.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
