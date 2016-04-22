/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thedevbridge.gmaoapp.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wabo
 */
@Entity
@Table(name = "technicien_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "TechnicienArchive.findAll", query = "SELECT t FROM TechnicienArchive t"),
		@NamedQuery(name = "TechnicienArchive.findByIdTechnicien", query = "SELECT t FROM TechnicienArchive t WHERE t.idTechnicien = :idTechnicien"),
		@NamedQuery(name = "TechnicienArchive.findByFonctionTechicien", query = "SELECT t FROM TechnicienArchive t WHERE t.fonctionTechicien = :fonctionTechicien")})
public class TechnicienArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_technicien")
	private Long idTechnicien;
	@Column(name = "fonction_techicien")
	private String fonctionTechicien;
	@JoinColumn(name = "id_departement", referencedColumnName = "id_departement")
	@ManyToOne(optional = false)
	private DepartementArchive idDepartement;
	@JoinColumn(name = "id_equipe", referencedColumnName = "id_equipe")
	@ManyToOne(optional = false)
	private EquipeArchive idEquipe;
	@JoinColumn(name = "id_personnel", referencedColumnName = "id_personnel")
	@ManyToOne(optional = false)
	private PersonnelArchive idPersonnel;

	public TechnicienArchive() {
	}

	public TechnicienArchive(Long idTechnicien) {
		this.idTechnicien = idTechnicien;
	}

	public Long getIdTechnicien() {
		return idTechnicien;
	}

	public void setIdTechnicien(Long idTechnicien) {
		this.idTechnicien = idTechnicien;
	}

	public String getFonctionTechicien() {
		return fonctionTechicien;
	}

	public void setFonctionTechicien(String fonctionTechicien) {
		this.fonctionTechicien = fonctionTechicien;
	}

	public DepartementArchive getIdDepartement() {
		return idDepartement;
	}

	public void setIdDepartement(DepartementArchive idDepartement) {
		this.idDepartement = idDepartement;
	}

	public EquipeArchive getIdEquipe() {
		return idEquipe;
	}

	public void setIdEquipe(EquipeArchive idEquipe) {
		this.idEquipe = idEquipe;
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
		hash += (idTechnicien != null ? idTechnicien.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof TechnicienArchive)) {
			return false;
		}
		TechnicienArchive other = (TechnicienArchive) object;
		if ((this.idTechnicien == null && other.idTechnicien != null)
				|| (this.idTechnicien != null && !this.idTechnicien
						.equals(other.idTechnicien))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.TechnicienArchive[ idTechnicien="
				+ idTechnicien + " ]";
	}

}
