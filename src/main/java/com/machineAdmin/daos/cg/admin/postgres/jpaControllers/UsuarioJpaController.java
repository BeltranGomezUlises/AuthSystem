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
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.machineAdmin.entities.cg.admin.postgres.BitacoraContras;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import java.util.ArrayList;
import java.util.List;
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

    public void create(Usuario usuario) {
        if (usuario.getBitacoraContrasList() == null) {
            usuario.setBitacoraContrasList(new ArrayList<BitacoraContras>());
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
            em.getTransaction().commit();
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
            List<String> illegalOrphanMessages = null;
            for (BitacoraContras bitacoraContrasListOldBitacoraContras : bitacoraContrasListOld) {
                if (!bitacoraContrasListNew.contains(bitacoraContrasListOldBitacoraContras)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BitacoraContras " + bitacoraContrasListOldBitacoraContras + " since its usuario1 field is not nullable.");
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
