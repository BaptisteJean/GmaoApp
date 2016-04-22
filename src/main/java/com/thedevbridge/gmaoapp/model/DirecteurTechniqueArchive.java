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
@Table(name = "directeur_technique_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "DirecteurTechniqueArchive.findAll", query = "SELECT d FROM DirecteurTechniqueArchive d"),
		@NamedQuery(name = "DirecteurTechniqueArchive.findByIdDirecteurTech", query = "SELECT d FROM DirecteurTechniqueArchive d WHERE d.idDirecteurTech = :idDirecteurTech")})
public class DirecteurTechniqueArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_directeur_tech")
	private Long idDirecteurTech;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idDirecteurTechnique")
	private List<ArchivageArchive> archivageArchiveList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idDirecteurTech")
	private List<InterventionArchive> interventionArchiveList;
	@JoinColumn(name = "id_personnel", referencedColumnName = "id_personnel")
	@ManyToOne(optional = false)
	private PersonnelArchive idPersonnel;

	public DirecteurTechniqueArchive() {
	}

	public DirecteurTechniqueArchive(Long idDirecteurTech) {
		this.idDirecteurTech = idDirecteurTech;
	}

	public Long getIdDirecteurTech() {
		return idDirecteurTech;
	}

	public void setIdDirecteurTech(Long idDirecteurTech) {
		this.idDirecteurTech = idDirecteurTech;
	}

	@XmlTransient
	public List<ArchivageArchive> getArchivageArchiveList() {
		return archivageArchiveList;
	}

	public void setArchivageArchiveList(
			List<ArchivageArchive> archivageArchiveList) {
		this.archivageArchiveList = archivageArchiveList;
	}

	@XmlTransient
	public List<InterventionArchive> getInterventionArchiveList() {
		return interventionArchiveList;
	}

	public void setInterventionArchiveList(
			List<InterventionArchive> interventionArchiveList) {
		this.interventionArchiveList = interventionArchiveList;
	}

	public PersonnelArchive getIdPersonnel() {
		return idPersonnel;
	}

	public void setIdPersonnel(PersonnelArchive idPersonnel) {
		this.idPersonnel = idPersonnel;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idDirecteurTech != null ? idDirecteurTech.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof DirecteurTechniqueArchive)) {
			return false;
		}
		DirecteurTechniqueArchive other = (DirecteurTechniqueArchive) object;
		if ((this.idDirecteurTech == null && other.idDirecteurTech != null)
				|| (this.idDirecteurTech != null && !this.idDirecteurTech
						.equals(other.idDirecteurTech))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.DirecteurTechniqueArchive[ idDirecteurTech="
				+ idDirecteurTech + " ]";
	}

}
