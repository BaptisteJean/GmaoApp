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
@Table(name = "departement_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "DepartementArchive.findAll", query = "SELECT d FROM DepartementArchive d"),
		@NamedQuery(name = "DepartementArchive.findByIdDepartement", query = "SELECT d FROM DepartementArchive d WHERE d.idDepartement = :idDepartement"),
		@NamedQuery(name = "DepartementArchive.findByChefDepartement", query = "SELECT d FROM DepartementArchive d WHERE d.chefDepartement = :chefDepartement"),
		@NamedQuery(name = "DepartementArchive.findByDescriptionActiviteDepartement", query = "SELECT d FROM DepartementArchive d WHERE d.descriptionActiviteDepartement = :descriptionActiviteDepartement"),
		@NamedQuery(name = "DepartementArchive.findByLibelleDepartemnt", query = "SELECT d FROM DepartementArchive d WHERE d.libelleDepartemnt = :libelleDepartemnt")})
public class DepartementArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_departement")
	private Long idDepartement;
	@Column(name = "chef_departement")
	private String chefDepartement;
	@Column(name = "description_activite_departement")
	private String descriptionActiviteDepartement;
	@Column(name = "libelle_departemnt")
	private String libelleDepartemnt;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idDepartemant")
	private List<EquipeArchive> equipeArchiveList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idDepartement")
	private List<TechnicienArchive> technicienArchiveList;

	public DepartementArchive() {
	}

	public DepartementArchive(Long idDepartement) {
		this.idDepartement = idDepartement;
	}

	public Long getIdDepartement() {
		return idDepartement;
	}

	public void setIdDepartement(Long idDepartement) {
		this.idDepartement = idDepartement;
	}

	public String getChefDepartement() {
		return chefDepartement;
	}

	public void setChefDepartement(String chefDepartement) {
		this.chefDepartement = chefDepartement;
	}

	public String getDescriptionActiviteDepartement() {
		return descriptionActiviteDepartement;
	}

	public void setDescriptionActiviteDepartement(
			String descriptionActiviteDepartement) {
		this.descriptionActiviteDepartement = descriptionActiviteDepartement;
	}

	public String getLibelleDepartemnt() {
		return libelleDepartemnt;
	}

	public void setLibelleDepartemnt(String libelleDepartemnt) {
		this.libelleDepartemnt = libelleDepartemnt;
	}

	@XmlTransient
	public List<EquipeArchive> getEquipeArchiveList() {
		return equipeArchiveList;
	}

	public void setEquipeArchiveList(List<EquipeArchive> equipeArchiveList) {
		this.equipeArchiveList = equipeArchiveList;
	}

	@XmlTransient
	public List<TechnicienArchive> getTechnicienArchiveList() {
		return technicienArchiveList;
	}

	public void setTechnicienArchiveList(
			List<TechnicienArchive> technicienArchiveList) {
		this.technicienArchiveList = technicienArchiveList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idDepartement != null ? idDepartement.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof DepartementArchive)) {
			return false;
		}
		DepartementArchive other = (DepartementArchive) object;
		if ((this.idDepartement == null && other.idDepartement != null)
				|| (this.idDepartement != null && !this.idDepartement
						.equals(other.idDepartement))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.DepartementArchive[ idDepartement="
				+ idDepartement + " ]";
	}

}
