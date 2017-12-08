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
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String createEventoProxy(EventoProxy eventoProxy) {
        //new evento
        //añadir tag
        //super.create(entity);
        Evento evento = new Evento();

        evento.setNombre(eventoProxy.nombre);
        evento.setDescripcion(eventoProxy.descripcion);
        evento.setFechainicio(eventoProxy.fechainicio);
        evento.setFechafin(eventoProxy.fechafin);
        evento.setDireccion(eventoProxy.direccion);
        evento.setValidado(eventoProxy.validado);
        evento.setCreador(em.find(Usuario.class, eventoProxy.creador));
        evento.setLatitud(eventoProxy.latitud);
        evento.setLongitud(eventoProxy.longitud);
        List<String> tagsString = eventoProxy.tags;
        //evento.setTagCollection(eventoProxy.tags.stream().map(Tag::new).collect(Collectors.toList()));
        evento.setTagCollection(eventoProxy.tags.stream().map(nombreTag -> em.find(Tag.class, nombreTag)).collect(Collectors.toList()));

        /*
 for(String t : eventoProxy.tags){
 if(evento.getTagCollection()==null){
 evento.setTagCollection(new ArrayList<Tag>());
 }
 evento.getTagCollection().add(em.find(Tag.class, t));
 }
 evento.setComentarioCollection(new ArrayList<Comentario>());
 evento.setUsuarioCollection(new ArrayList<Usuario>());
         */
        super.create(evento);
        return "\"status\":\"evento creado correctamente\"";
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public String edit(@PathParam("id") Integer id, EventoProxy eventoProxy) {
         Evento evento = super.find( id);

        evento.setNombre(eventoProxy.nombre);
        evento.setDescripcion(eventoProxy.descripcion);
        evento.setFechainicio(eventoProxy.fechainicio);
        evento.setFechafin(eventoProxy.fechafin);
        evento.setDireccion(eventoProxy.direccion);
        evento.setValidado(eventoProxy.validado);
        evento.setCreador(em.find(Usuario.class, eventoProxy.creador));
        evento.setLatitud(eventoProxy.latitud);
        evento.setLongitud(eventoProxy.longitud);
        List<String> tagsString = eventoProxy.tags;
        evento.setTagCollection(eventoProxy.tags.stream().map(nombreTag -> em.find(Tag.class, nombreTag)).collect(Collectors.toList()));
        
        super.edit(evento);
        return "\"status\":\"evento editado correctamente\"";
    }

    @GET
    @Path("eventosByTag/{nombre}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<EventoProxy> findEventosByTag(@PathParam("nombre") String nombre) {
        Tag t = em.find(Tag.class, nombre);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        Query q;
        q = this.em.createQuery("select e from Evento e where :tag MEMBER OF e.tagCollection and e.validado = true and e.fechafin >= :currentDate");
        q.setParameter("tag", t);
        q.setParameter("currentDate", formatter.format(currentDate));

        List<EventoProxy> listEventos = new ArrayList<>();
        for (Evento e : (List<Evento>) q.getResultList()) {
            listEventos.add(new EventoProxy(e));
        }
        return listEventos;
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public EventoProxy find(@PathParam("id") Integer id) {
        return new EventoProxy(super.find(id));
    }

    @GET
    @Path("listar")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<EventoProxy> obtenerTodosEventos() {
        List<EventoProxy> retorno = new ArrayList<>();

        for (Evento evento : super.findAll()) {
            retorno.add(new EventoProxy(evento));
        }

        return retorno;
    }

    @GET
    @Path("eventosNoCaducadosYValidados")
    @Produces({MediaType.APPLICATION_JSON})
    public List<EventoProxy> findEventosNoCaducadosYValidados() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        Query q;
        q = this.em.createQuery("select e from Evento e where e.validado = true and e.fechafin >= :currentDate");
        q.setParameter("currentDate", formatter.format(currentDate));

        List<EventoProxy> listEventos = new ArrayList<>();
        for (Evento e : (List<Evento>) q.getResultList()) {
            listEventos.add(new EventoProxy(e));
        }
        return listEventos;
    }

    @GET
    @Path("eventosNoValidados")
    @Produces({MediaType.APPLICATION_JSON})
    public List<EventoProxy> findEventosNoValidados() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        Query q;
        q = this.em.createQuery("select e from Evento e where e.validado = false and e.fechafin >= :currentDate");
        q.setParameter("currentDate", formatter.format(currentDate));

        List<EventoProxy> listEventos = new ArrayList<>();
        for (Evento e : (List<Evento>) q.getResultList()) {
            listEventos.add(new EventoProxy(e));
        }
        return listEventos;
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

    /*@PUT
    @Path("validar/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void validarEvento(@PathParam("id") int id){
        Evento e = em.find(Evento.class, id);
        e.setValidado(true);
        this.edit(e);
        //this.sendMail(evento);
    }*/
    @GET
    @Path("{evento}/{usuario}")
    @Produces(MediaType.TEXT_PLAIN)
    public boolean existeMegusta(@PathParam("evento") int evento, @PathParam("usuario") String usuario) {
        Query q;
        Usuario u = em.find(Usuario.class, usuario);
        q = this.em.createQuery("select e from Evento e where e.id = :evento and :usuario MEMBER OF e.usuarioCollection");
        q.setParameter("usuario", u);
        q.setParameter("evento", evento);
        return q.getResultList().size() > 0;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    // Clase statica anidada que representa el proxy para evento
    // Sirve para modelar el json que el servidor manda al cliente
    // y del mismo modo el json que el cliente manda al servidor
    public static class EventoProxy implements Serializable {

        public Integer id;
        public String nombre, descripcion, fechainicio, fechafin, direccion;
        public boolean validado;
        public float latitud, longitud;
        public String creador;
        public List<String> meGusta = new ArrayList<>();
        public List<String> tags = new ArrayList<>();
        
        public EventoProxy(){
            
        }

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
            //this.tags = new ArrayList<>();

            for (Tag tag : evento.getTagCollection()) {
                this.tags.add(tag.getNombre());
            }
        }

    }

}
