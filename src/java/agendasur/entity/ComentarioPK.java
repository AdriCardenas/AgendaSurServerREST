/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Adrian
 */
@Embeddable
public class ComentarioPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "USUARIO_EMAIL")
    private String usuarioEmail;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EVENTO_ID")
    private int eventoId;

    public ComentarioPK() {
    }

    public ComentarioPK(String usuarioEmail, int eventoId) {
        this.usuarioEmail = usuarioEmail;
        this.eventoId = eventoId;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public int getEventoId() {
        return eventoId;
    }

    public void setEventoId(int eventoId) {
        this.eventoId = eventoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioEmail != null ? usuarioEmail.hashCode() : 0);
        hash += (int) eventoId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComentarioPK)) {
            return false;
        }
        ComentarioPK other = (ComentarioPK) object;
        if ((this.usuarioEmail == null && other.usuarioEmail != null) || (this.usuarioEmail != null && !this.usuarioEmail.equals(other.usuarioEmail))) {
            return false;
        }
        if (this.eventoId != other.eventoId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "agendasur.entity.ComentarioPK[ usuarioEmail=" + usuarioEmail + ", eventoId=" + eventoId + " ]";
    }
    
}
