/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.dailymail.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author pdhssp
 */
@Entity
public class Sms implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;
    private String sentTo;
    @ManyToOne
    private WebUser sentBy;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date sentDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date sentDateTime;
    @ManyToOne
    private ClientProcess clientProess;
    @ManyToOne
    private ClientProcessStep clientProcessStep;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Sms)) {
            return false;
        }
        Sms other = (Sms) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.gov.health.dailymail.entity.Sms[ id=" + id + " ]";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public WebUser getSentBy() {
        return sentBy;
    }

    public void setSentBy(WebUser sentBy) {
        this.sentBy = sentBy;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Date getSentDateTime() {
        return sentDateTime;
    }

    public void setSentDateTime(Date sentDateTime) {
        this.sentDateTime = sentDateTime;
    }

    public ClientProcess getClientProess() {
        return clientProess;
    }

    public void setClientProess(ClientProcess clientProess) {
        this.clientProess = clientProess;
    }

    public ClientProcessStep getClientProcessStep() {
        return clientProcessStep;
    }

    public void setClientProcessStep(ClientProcessStep clientProcessStep) {
        this.clientProcessStep = clientProcessStep;
    }
    
}
