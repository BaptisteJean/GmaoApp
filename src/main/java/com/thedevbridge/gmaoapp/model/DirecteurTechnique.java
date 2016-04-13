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
@Table(name = "directeur_Technique")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "DirecteurTechnique.findAll", query = "SELECT d FROM DirecteurTechnique d"),
		@NamedQuery(name = "DirecteurTechnique.findByIdDirecteurTech", query = "SELECT d FROM DirecteurTechnique d WHERE d.idDirecteurTech = :idDirecteurTech")})
public class DirecteurTechnique implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_directeur_tech")
	private Long idDirecteurTech;
	@JoinColumn(name = "id_personnel", referencedColumnName = "id_personnel")
	@ManyToOne(optional = false)
	private Personnel idPersonnel;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idDirecteurTechnique")
	private List<Archivage> archivageList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idDirecteurTech")
	private List<Intervention> interventionList;

	public DirecteurTechnique() {
	}

	public DirecteurTechnique(Long idDirecteurTech) {
		this.idDirecteurTech = idDirecteurTech;
	}

	public Long getIdDirecteurTech() {
		return idDirecteurTech;
	}

	public void setIdDirecteurTech(Long idDirecteurTech) {
		this.idDirecteurTech = idDirecteurTech;
	}

	public Personnel getIdPersonnel() {
		return idPersonnel;
	}

	public void setIdPersonnel(Personnel idPersonnel) {
		this.idPersonnel = idPersonnel;
	}

	@XmlTransient
	public List<Archivage> getArchivageList() {
		return archivageList;
	}

	public void setArchivageList(List<Archivage> archivageList) {
		this.archivageList = archivageList;
	}

	@XmlTransient
	public List<Intervention> getInterventionList() {
		return interventionList;
	}

	public void setInterventionList(List<Intervention> interventionList) {
		this.interventionList = interventionList;
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
		if (!(object instanceof DirecteurTechnique)) {
			return false;
		}
		DirecteurTechnique other = (DirecteurTechnique) object;
		if ((this.idDirecteurTech == null && other.idDirecteurTech != null)
				|| (this.idDirecteurTech != null && !this.idDirecteurTech
						.equals(other.idDirecteurTech))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.DirecteurTechnique[ idDirecteurTech="
				+ idDirecteurTech + " ]";
	}

}
