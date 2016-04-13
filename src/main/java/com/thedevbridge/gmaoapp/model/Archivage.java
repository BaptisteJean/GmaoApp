/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thedevbridge.gmaoapp.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author wabo
 */
@Entity
@Table(name = "archivage")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Archivage.findAll", query = "SELECT a FROM Archivage a"),
		@NamedQuery(name = "Archivage.findByIdArchivage", query = "SELECT a FROM Archivage a WHERE a.idArchivage = :idArchivage")})
public class Archivage implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_archivage")
	private Long idArchivage;
	@JoinColumn(name = "id_client", referencedColumnName = "id_client")
	@ManyToOne(optional = false)
	private Client idClient;
	@JoinColumn(name = "id_directeur_technique", referencedColumnName = "id_directeur_tech")
	@ManyToOne(optional = false)
	private DirecteurTechnique idDirecteurTechnique;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idArchivage")
	private List<ArchivageProvoque> archivageProvoqueList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idArchivage")
	private List<ArchivageAutomatique> archivageAutomatiqueList;

	public Archivage() {
	}

	public Archivage(Long idArchivage) {
		this.idArchivage = idArchivage;
	}

	public Long getIdArchivage() {
		return idArchivage;
	}

	public void setIdArchivage(Long idArchivage) {
		this.idArchivage = idArchivage;
	}

	public Client getIdClient() {
		return idClient;
	}

	public void setIdClient(Client idClient) {
		this.idClient = idClient;
	}

	public DirecteurTechnique getIdDirecteurTechnique() {
		return idDirecteurTechnique;
	}

	public void setIdDirecteurTechnique(DirecteurTechnique idDirecteurTechnique) {
		this.idDirecteurTechnique = idDirecteurTechnique;
	}

	@XmlTransient
	public List<ArchivageProvoque> getArchivageProvoqueList() {
		return archivageProvoqueList;
	}

	public void setArchivageProvoqueList(
			List<ArchivageProvoque> archivageProvoqueList) {
		this.archivageProvoqueList = archivageProvoqueList;
	}

	@XmlTransient
	public List<ArchivageAutomatique> getArchivageAutomatiqueList() {
		return archivageAutomatiqueList;
	}

	public void setArchivageAutomatiqueList(
			List<ArchivageAutomatique> archivageAutomatiqueList) {
		this.archivageAutomatiqueList = archivageAutomatiqueList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idArchivage != null ? idArchivage.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Archivage)) {
			return false;
		}
		Archivage other = (Archivage) object;
		if ((this.idArchivage == null && other.idArchivage != null)
				|| (this.idArchivage != null && !this.idArchivage
						.equals(other.idArchivage))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.Archivage[ idArchivage="
				+ idArchivage + " ]";
	}

}
