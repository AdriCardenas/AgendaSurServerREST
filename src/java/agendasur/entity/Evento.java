/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Adrian
 */
@Entity
@Table(name = "EVENTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Evento.findAll", query = "SELECT e FROM Evento e")
    , @NamedQuery(name = "Evento.findById", query = "SELECT e FROM Evento e WHERE e.id = :id")
    , @NamedQuery(name = "Evento.findByNombre", query = "SELECT e FROM Evento e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "Evento.findByDescripcion", query = "SELECT e FROM Evento e WHERE e.descripcion = :descripcion")
    , @NamedQuery(name = "Evento.findByFechainicio", query = "SELECT e FROM Evento e WHERE e.fechainicio = :fechainicio")
    , @NamedQuery(name = "Evento.findByFechafin", query = "SELECT e FROM Evento e WHERE e.fechafin = :fechafin")
    , @NamedQuery(name = "Evento.findByDireccion", query = "SELECT e FROM Evento e WHERE e.direccion = :direccion")
    , @NamedQuery(name = "Evento.findByValidado", query = "SELECT e FROM Evento e WHERE e.validado = :validado")
    , @NamedQuery(name = "Evento.findByLatitud", query = "SELECT e FROM Evento e WHERE e.latitud = :latitud")
    , @NamedQuery(name = "Evento.findByLongitud", query = "SELECT e FROM Evento e WHERE e.longitud = :longitud")})
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    //@NotNull
    //@Size(min = 1, max = 50)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    //@NotNull
   // @Size(min = 1, max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    //@NotNull
   // @Size(min = 1, max = 80)
    @Column(name = "FECHAINICIO")
    private String fechainicio;
   // @Size(max = 80)
    @Column(name = "FECHAFIN")
    private String fechafin;
    @Basic(optional = false)
   // @NotNull
   // @Size(min = 1, max = 200)
    @Column(name = "DIRECCION")
    private String direccion;
    @Basic(optional = false)
  //  @NotNull
    @Column(name = "VALIDADO")
    private Boolean validado;
    @Basic(optional = false)
   // @NotNull
    @Column(name = "LATITUD")
    private float latitud;
    @Basic(optional = false)
  //  @NotNull
    @Column(name = "LONGITUD")
    private float longitud;
    @JoinTable(name = "MEGUSTA", joinColumns = {
        @JoinColumn(name = "EVENTO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "USUARIO_EMAIL", referencedColumnName = "EMAIL")})
    @ManyToMany
    private Collection<Usuario> usuarioCollection;
    @JoinTable(name = "EVENTOTAG", joinColumns = {
        @JoinColumn(name = "EVENTO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "TAG_NOMBRE", referencedColumnName = "NOMBRE")})
    @ManyToMany
    private Collection<Tag> tagCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evento")
    private Collection<Comentario> comentarioCollection;
    @JoinColumn(name = "CREADOR", referencedColumnName = "EMAIL")
    @ManyToOne(optional = false)
    private Usuario creador;

    public Evento() {
    }

    public Evento(Integer id) {
        this.id = id;
    }

    public Evento(Integer id, String nombre, String descripcion, String fechainicio, String direccion, Boolean validado, float latitud, float longitud) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechainicio = fechainicio;
        this.direccion = direccion;
        this.validado = validado;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(String fechainicio) {
        this.fechainicio = fechainicio;
    }

    public String getFechafin() {
        return fechafin;
    }

    public void setFechafin(String fechafin) {
        this.fechafin = fechafin;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getValidado() {
        return validado;
    }

    public void setValidado(Boolean validado) {
        this.validado = validado;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    @XmlTransient
    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    @XmlTransient
    public Collection<Tag> getTagCollection() {
        return tagCollection;
    }

    public void setTagCollection(Collection<Tag> tagCollection) {
        this.tagCollection = tagCollection;
    }

    @XmlTransient
    public Collection<Comentario> getComentarioCollection() {
        return comentarioCollection;
    }

    public void setComentarioCollection(Collection<Comentario> comentarioCollection) {
        this.comentarioCollection = comentarioCollection;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Evento)) {
            return false;
        }
        Evento other = (Evento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "agendasur.entity.Evento[ id=" + id + " ]";
    }
    
}
