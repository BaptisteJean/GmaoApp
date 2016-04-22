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
@Table(name = "archivage_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ArchivageArchive.findAll", query = "SELECT a FROM ArchivageArchive a"),
		@NamedQuery(name = "ArchivageArchive.findByIdArchivage", query = "SELECT a FROM ArchivageArchive a WHERE a.idArchivage = :idArchivage")})
public class ArchivageArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_archivage")
	private Long idArchivage;
	@JoinColumn(name = "id_client", referencedColumnName = "id_client")
	@ManyToOne(optional = false)
	private ClientArchive idClient;
	@JoinColumn(name = "id_directeur_technique", referencedColumnName = "id_directeur_tech")
	@ManyToOne(optional = false)
	private DirecteurTechniqueArchive idDirecteurTechnique;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idArchivage")
	private List<ArchivageProvoqueArchive> archivageProvoqueArchiveList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idArchivage")
	private List<ArchivageAutomatiqueArchive> archivageAutomatiqueArchiveList;

	public ArchivageArchive() {
	}

	public ArchivageArchive(Long idArchivage) {
		this.idArchivage = idArchivage;
	}

	public Long getIdArchivage() {
		return idArchivage;
	}

	public void setIdArchivage(Long idArchivage) {
		this.idArchivage = idArchivage;
	}

	public ClientArchive getIdClient() {
		return idClient;
	}

	public void setIdClient(ClientArchive idClient) {
		this.idClient = idClient;
	}

	public DirecteurTechniqueArchive getIdDirecteurTechnique() {
		return idDirecteurTechnique;
	}

	public void setIdDirecteurTechnique(
			DirecteurTechniqueArchive idDirecteurTechnique) {
		this.idDirecteurTechnique = idDirecteurTechnique;
	}

	@XmlTransient
	public List<ArchivageProvoqueArchive> getArchivageProvoqueArchiveList() {
		return archivageProvoqueArchiveList;
	}

	public void setArchivageProvoqueArchiveList(
			List<ArchivageProvoqueArchive> archivageProvoqueArchiveList) {
		this.archivageProvoqueArchiveList = archivageProvoqueArchiveList;
	}

	@XmlTransient
	public List<ArchivageAutomatiqueArchive> getArchivageAutomatiqueArchiveList() {
		return archivageAutomatiqueArchiveList;
	}

	public void setArchivageAutomatiqueArchiveList(
			List<ArchivageAutomatiqueArchive> archivageAutomatiqueArchiveList) {
		this.archivageAutomatiqueArchiveList = archivageAutomatiqueArchiveList;
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
		if (!(object instanceof ArchivageArchive)) {
			return false;
		}
		ArchivageArchive other = (ArchivageArchive) object;
		if ((this.idArchivage == null && other.idArchivage != null)
				|| (this.idArchivage != null && !this.idArchivage
						.equals(other.idArchivage))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.ArchivageArchive[ idArchivage="
				+ idArchivage + " ]";
	}

}
