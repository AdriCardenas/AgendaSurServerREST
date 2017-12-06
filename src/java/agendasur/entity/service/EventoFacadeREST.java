/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.entity.service;

import agendasur.entity.Evento;
import agendasur.entity.Tag;
import agendasur.entity.Usuario;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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
@Path("agendasur.entity.evento")
public class EventoFacadeREST extends AbstractFacade<Evento> {

    @PersistenceContext(unitName = "AgendaSurServerRESTPU")
    private EntityManager em;

    public EventoFacadeREST() {
        super(Evento.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void createEventoProxy(EventoProxy entity) {
        //new evento
        //añadir tag
        //super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Evento entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Evento find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Path("listar")
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public List<EventoProxy> obtenerTodosEventos() {
        List<EventoProxy> retorno = new ArrayList<>();
        
        for(Evento evento: super.findAll()){
            retorno.add(new EventoProxy(evento));
        }
        
        return retorno;
    }
    
    @GET
    @Path("bytag/{tag}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Evento> findAllEventosByTag(@PathParam("tag") Tag tag) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        Query q;
        System.out.println(tag.getNombre());
        System.out.println("*************************************");
        q = this.em.createQuery("select e from Evento e where e.validado = true and e.fechafin >= :currentDate");
        q.setParameter("currentDate", formatter.format(currentDate));
        System.out.println(q.getResultList().size());
        Evento e = (Evento)q.getResultList().get(0);
        System.out.print(e.getTagCollection().size());
        return q.getResultList();
    }
   
    
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Evento> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    
    // Clase statica anidada que representa el proxy para evento
    // Sirve para modelar el json que el servidor manda al cliente
    // y del mismo modo el json que el cliente manda al servidor
    
    public static class EventoProxy implements Serializable{
        public Integer id;
        public String nombre, descripcion, fechainicio, fechafin, direccion;
        public boolean validado;
        public float latitud, longitud;
        public String creador;
        public List<String> meGusta;
        public List<String> tags;
        
        public EventoProxy(Evento evento) {
            this.id = evento.getId();
            this.nombre = evento.getNombre();
            this.descripcion = evento.getDescripcion();
            this.fechainicio = evento.getFechainicio();
            this.fechafin = evento.getFechafin();
            this.direccion = evento.getDireccion();
            this.validado = evento.getValidado();
            this.latitud = evento.getLatitud();
            this.longitud = evento.getLongitud();
            this.creador = evento.getCreador().getEmail();
            
            // Completar lista de me gusta con los IDs de los usuarios que le han dado a me gusta
            this.meGusta = evento.getUsuarioCollection().stream().map(e -> e.getEmail()).collect(Collectors.toList());
            
            // Completar lista de me gusta con los tags de un evento
            this.tags = new ArrayList<>();
            
            for(Tag tag : evento.getTagCollection()){
                this.tags.add(tag.getNombre());
            }
        }
             
    }
    
}
