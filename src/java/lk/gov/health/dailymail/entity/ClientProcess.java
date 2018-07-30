/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.dailymail.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author User
 */
@Entity
public class ClientProcess implements Serializable {

    @OneToMany(mappedBy = "clientProcess")
    private List<ClientProcessStep> clientProcessSteps;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Item process;
    String clientName;

    String mobileNo;
    @ManyToOne
    private Item designation;
    @ManyToOne
    private Institute institute;

    @Temporal(javax.persistence.TemporalType.DATE)
    Date receivedDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date letterDate;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date receivedDateTime;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date letterDateTime;

    Boolean registered;

    @ManyToOne
    Institute sendingInstitute;
    String sendingNumber;
    String topic;

    @ManyToOne
    WebUser executiveOfficer;
    @ManyToOne
    WebUser departmentHead;

    @ManyToOne
    WebUser subjectUser;
    @ManyToOne
    Subject subject;

    @ManyToOne
    WebUser addedUser;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date addedDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date addedTime;

    @ManyToOne
    Institute toInstitute;
    @ManyToOne
    Department toDepartment;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Date getReceivedDateTime() {
        return receivedDateTime;
    }

    public void setReceivedDateTime(Date receivedDateTime) {
        this.receivedDateTime = receivedDateTime;
    }

    public Date getLetterDateTime() {
        return letterDateTime;
    }

    public void setLetterDateTime(Date letterDateTime) {
        this.letterDateTime = letterDateTime;
    }

    public Institute getToInstitute() {
        return toInstitute;
    }

    public void setToInstitute(Institute toInstitute) {
        this.toInstitute = toInstitute;
    }

    public Department getToDepartment() {
        return toDepartment;
    }

    public void setToDepartment(Department toDepartment) {
        this.toDepartment = toDepartment;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Date getLetterDate() {
        return letterDate;
    }

    public void setLetterDate(Date letterDate) {
        this.letterDate = letterDate;
    }

    public Boolean getRegistered() {
        return registered;
    }

    public void setRegistered(Boolean registered) {
        this.registered = registered;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Institute getSendingInstitute() {
        return sendingInstitute;
    }

    public void setSendingInstitute(Institute sendingInstitute) {
        this.sendingInstitute = sendingInstitute;
    }

    public String getSendingNumber() {
        return sendingNumber;
    }

    public void setSendingNumber(String sendingNumber) {
        this.sendingNumber = sendingNumber;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public WebUser getExecutiveOfficer() {
        return executiveOfficer;
    }

    public void setExecutiveOfficer(WebUser executiveOfficer) {
        this.executiveOfficer = executiveOfficer;
    }

    public WebUser getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(WebUser departmentHead) {
        this.departmentHead = departmentHead;
    }

    public WebUser getSubjectUser() {
        return subjectUser;
    }

    public void setSubjectUser(WebUser subjectUser) {
        this.subjectUser = subjectUser;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public WebUser getAddedUser() {
        return addedUser;
    }

    public void setAddedUser(WebUser addedUser) {
        this.addedUser = addedUser;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Date getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(Date addedTime) {
        this.addedTime = addedTime;
    }

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
        if (!(object instanceof ClientProcess)) {
            return false;
        }
        ClientProcess other = (ClientProcess) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.gov.health.dailymail.entity.Mail[ id=" + id + " ]";
    }

    public Item getProcess() {
        return process;
    }

    public void setProcess(Item process) {
        this.process = process;
    }

    public List<ClientProcessStep> getClientProcessSteps() {
        return clientProcessSteps;
    }

    public void setClientProcessSteps(List<ClientProcessStep> clientProcessSteps) {
        this.clientProcessSteps = clientProcessSteps;
    }

    public Item getDesignation() {
        return designation;
    }

    public void setDesignation(Item designation) {
        this.designation = designation;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    
    
}
