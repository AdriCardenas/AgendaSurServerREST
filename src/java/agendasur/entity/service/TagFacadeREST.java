/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.entity.service;

import agendasur.entity.Evento;
import agendasur.entity.Tag;
import agendasur.entity.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Adrian
 */
@Stateless
@Path("agendasur.entity.tag")
public class TagFacadeREST extends AbstractFacade<Tag> {

    @PersistenceContext(unitName = "AgendaSurServerRESTPU")
    private EntityManager em;

    public TagFacadeREST() {
        super(Tag.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Tag entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, Tag entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Tag find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Tag> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Tag> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("tagsByUser/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TagProxy> findTagsByUser(@PathParam("email") String email) {
        Usuario u = em.find(Usuario.class, email);
        Query q;
        q = this.em.createQuery("select t from Tag t where :usuario MEMBER OF t.usuarioCollection");
        q.setParameter("usuario", u);

        List<TagProxy> listTagProxy = new ArrayList<>();
        for (Tag t : (List<Tag>) q.getResultList()) {
            listTagProxy.add(new TagProxy(t));
        }

        return listTagProxy;
    }

    @GET
    @Path("tagsByEvento/{idEvento}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TagProxy> findTagsByEvento(@PathParam("idEvento") int idEvento) {
        Evento e = em.find(Evento.class, idEvento);
        Query q;
        q = this.em.createQuery("select t from Tag t where :evento MEMBER OF t.eventoCollection");
        q.setParameter("evento", e);

        List<TagProxy> listTagProxy = new ArrayList<>();
        for (Tag t : (List<Tag>) q.getResultList()) {
            listTagProxy.add(new TagProxy(t));
        }

        return listTagProxy;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public static class TagProxy {

        public String nombre;
        public List<String> listUsuario = new ArrayList<>();
        public List<Integer> listEvento = new ArrayList<>();

        public TagProxy() {

        }

        public TagProxy(Tag t) {
            nombre = t.getNombre();

            for (Usuario u : t.getUsuarioCollection()) {
                listUsuario.add(u.getEmail());
            }

            for (Evento e : t.getEventoCollection()) {
                listEvento.add(e.getId());
            }
        }
    }

}
