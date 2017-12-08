/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.entity.service;

import agendasur.entity.Comentario;
import agendasur.entity.ComentarioPK;
import agendasur.entity.Evento;
import agendasur.entity.Usuario;
import java.io.Serializable;
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
import javax.ws.rs.core.PathSegment;

/**
 *
 * @author Adrian
 */
@Stateless
@Path("agendasur.entity.comentario")
public class ComentarioFacadeREST extends AbstractFacade<Comentario> {

    @PersistenceContext(unitName = "AgendaSurServerRESTPU")
    private EntityManager em;

    private ComentarioPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;usuarioEmail=usuarioEmailValue;eventoId=eventoIdValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        agendasur.entity.ComentarioPK key = new agendasur.entity.ComentarioPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> usuarioEmail = map.get("usuarioEmail");
        if (usuarioEmail != null && !usuarioEmail.isEmpty()) {
            key.setUsuarioEmail(usuarioEmail.get(0));
        }
        java.util.List<String> eventoId = map.get("eventoId");
        if (eventoId != null && !eventoId.isEmpty()) {
            key.setEventoId(new java.lang.Integer(eventoId.get(0)));
        }
        return key;
    }

    public ComentarioFacadeREST() {
        super(Comentario.class);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String create(ComentarioProxy comentarioProxy) {
        
        Comentario c = conversorComentarioProxyAComentario(comentarioProxy);
        
        super.create(c);
        
        return "\"status\":\"Comentario enviado. Gracias.\"";
    }
    
    private Comentario conversorComentarioProxyAComentario(ComentarioProxy comentarioProxy){
        Comentario comentario = new Comentario();
        
        comentario.setComentarioPK(comentarioProxy.comentarioPK);
        comentario.setEvento(em.find(Evento.class, comentarioProxy.comentarioPK.getEventoId()));
        comentario.setUsuario(em.find(Usuario.class, comentarioProxy.comentarioPK.getUsuarioEmail()));
        comentario.setComentario(comentarioProxy.comentario);
        comentario.setFecha(comentarioProxy.fecha);
        
        return comentario;
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") PathSegment id, Comentario entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        agendasur.entity.ComentarioPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Comentario find(@PathParam("id") PathSegment id) {
        agendasur.entity.ComentarioPK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<ComentarioProxy> obtenerEventos() {
        List<ComentarioProxy> listComentario = new ArrayList<>();
        for(Comentario c : super.findAll()){
            listComentario.add(new ComentarioProxy(c));
        }
        return listComentario;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Comentario> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("comentario/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ComentarioProxy> getComentarios(@PathParam("id") int id) {
        Query q;
        q = this.em.createQuery("select c from Comentario c where c.comentarioPK.eventoId = :id ");
        q.setParameter("id", id);
        List<ComentarioProxy> listComentario = new ArrayList<>();
        for(Comentario c : (List<Comentario>)q.getResultList()){
            listComentario.add(new ComentarioProxy(c));
        }
        
        return listComentario;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public static class ComentarioProxy implements Serializable {
        public ComentarioPK comentarioPK;
        public String fecha;
        public String comentario;
        public String nombreCreador;
        public String apellidosCreador;
        
        public ComentarioProxy(){
            
        }
        
        public ComentarioProxy(Comentario comentario){
            this.comentarioPK = comentario.getComentarioPK();
            this.fecha = comentario.getFecha();
            this.comentario = comentario.getComentario(); 
            this.nombreCreador = comentario.getUsuario().getNombre();
            this.apellidosCreador = comentario.getUsuario().getApellidos();
        }
    }
    
}
