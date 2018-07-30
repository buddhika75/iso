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
public class ClientProcessStep implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private ClientProcess clientProcess;
    
    @ManyToOne
    private Item process;
    @ManyToOne
    private Item processStep;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    
    private boolean started;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    
    private boolean completed;
    

    
    
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
        if (!(object instanceof ClientProcessStep)) {
            return false;
        }
        ClientProcessStep other = (ClientProcessStep) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.gov.health.dailymail.entity.ClientProcessStep[ id=" + id + " ]";
    }

    public ClientProcess getClientProcess() {
        return clientProcess;
    }

    public void setClientProcess(ClientProcess clientProcess) {
        this.clientProcess = clientProcess;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Item getProcess() {
        return process;
    }

    public void setProcess(Item process) {
        this.process = process;
    }

    public Item getProcessStep() {
        return processStep;
    }

    public void setProcessStep(Item processStep) {
        this.processStep = processStep;
    }


    
}
