/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.entity.service;

import agendasur.entity.Tag;
import agendasur.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
@Path("agendasur.entity.usuario")
public class UsuarioFacadeREST extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "AgendaSurServerRESTPU")
    private EntityManager em;

    public UsuarioFacadeREST() {
        super(Usuario.class);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(UsuarioProxy usuarioProxy) {

        if (super.find(usuarioProxy.email) != null) {
            return "{\"status\":\"Usuario ya existe\"}";
        } else {
            Usuario u = new Usuario();
            u.setTipousuario(1);
            u.setApellidos(usuarioProxy.apellidos);
            u.setEmail(usuarioProxy.email);
            u.setNombre(usuarioProxy.nombre);
            u.setPassword(usuarioProxy.nombre);

            super.create(u);
            return "{\"status\":\"Usuario creado correctamente\"}";
        }

    }

    @PUT
    @Path("asignarTag/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String asignarTagsAUsuario(@PathParam("id") String email, List<String> listaTagsString) {
        Usuario u = super.find(email);

        List<Tag> listTag = new ArrayList<>();
        for (String s : listaTagsString) {
            Tag t = em.find(Tag.class, s);
            if (!t.getUsuarioCollection().contains(u)) {
                t.getUsuarioCollection().add(u);

            }
            listTag.add(t);
        }

        for (Tag t : u.getTagCollection()) {
            if (!listTag.contains(t)) {
                t.getUsuarioCollection().remove(u);
                em.merge(t);
            }
        }
        
        u.setTagCollection(listTag);

        super.edit(u);

        return "{\"status\":\"Sus tags han sido actualizados.\"}";
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public String edit(@PathParam("id") String id, UsuarioProxy usuarioProxy) {

        Usuario u = super.find(id);

        if (!u.getNombre().equals(usuarioProxy.nombre)) {
            u.setNombre(usuarioProxy.nombre);
            u.setPassword(usuarioProxy.nombre);
        }

        if (!u.getApellidos().equals(usuarioProxy.apellidos)) {
            u.setApellidos(usuarioProxy.apellidos);
        }

        if (u.getTipousuario() != usuarioProxy.tipoUsuario) {
            u.setTipousuario(usuarioProxy.tipoUsuario);
        }

        List<Tag> listTag = new ArrayList<>();
        for (String s : usuarioProxy.tagsUsuario) {
            Tag t = em.find(Tag.class, s);
            if (!t.getUsuarioCollection().contains(u)) {
                t.getUsuarioCollection().add(u);

            }
            listTag.add(t);
        }

        for (Tag t : u.getTagCollection()) {
            if (!listTag.contains(t)) {
                t.getUsuarioCollection().remove(u);
                em.merge(t);
            }
        }

        u.setTagCollection(listTag);

        super.edit(u);
        return "{\"status\":\"Usuario editado correctamente\"}";
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public UsuarioProxy find(@PathParam("id") String id) {
        return new UsuarioProxy(super.find(id));
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<UsuarioProxy> obtenerUsuarios() {
        List<UsuarioProxy> list = new ArrayList<>();
        for (Usuario u : super.findAll()) {
            list.add(new UsuarioProxy(u));
        }
        return list;
    }

    @GET
    @Path("listar")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UsuarioProxy> listarUsuario() {
        //return super.findAll().stream().map(UsuarioProxy::new).collect(Collectors.toList());
        List<UsuarioProxy> resultado = new ArrayList<>();
        for (Usuario u : super.findAll()) {
            UsuarioProxy p = new UsuarioProxy(u);
            resultado.add(p);
        }
        return resultado;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public static class UsuarioProxy implements Serializable {

        public String email;
        public String nombre;
        public String apellidos;
        public int tipoUsuario;
        public List<String> tagsUsuario = new ArrayList<>();

        public UsuarioProxy() {

        }

        public UsuarioProxy(Usuario usuario) {
            this.email = usuario.getEmail();
            this.nombre = usuario.getNombre();
            this.apellidos = usuario.getApellidos();
            this.tagsUsuario = usuario.getTagCollection().stream().map(tag -> tag.getNombre()).collect(Collectors.toList());
            this.tipoUsuario = usuario.getTipousuario();
        }
    }

}
