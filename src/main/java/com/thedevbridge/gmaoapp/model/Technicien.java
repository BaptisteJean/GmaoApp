/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thedevbridge.gmaoapp.model;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wabo
 */
@Entity
@Table(name = "technicien")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Technicien.findAll", query = "SELECT t FROM Technicien t"),
		@NamedQuery(name = "Technicien.findByIdTechnicien", query = "SELECT t FROM Technicien t WHERE t.idTechnicien = :idTechnicien"),
		@NamedQuery(name = "Technicien.findByFonctionTechicien", query = "SELECT t FROM Technicien t WHERE t.fonctionTechicien = :fonctionTechicien")})
public class Technicien implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_technicien")
	private Long idTechnicien;
	@Column(name = "fonction_techicien")
	private String fonctionTechicien;
	@JoinColumn(name = "id_departement", referencedColumnName = "id_departement")
	@ManyToOne(optional = false)
	private Departement idDepartement;
	@JoinColumn(name = "id_equipe", referencedColumnName = "id_equipe")
	@ManyToOne(optional = true)
	private Equipe idEquipe;
	@JoinColumn(name = "id_personnel", referencedColumnName = "id_personnel")
	@ManyToOne(optional = false)
	private Personnel idPersonnel;

	public Technicien() {
            this.idDepartement = new Departement();
            this.idEquipe = null;
            this.idPersonnel = new Personnel();
            
            
	}

	public Technicien(Long idTechnicien) {
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

	public Departement getIdDepartement() {
		return idDepartement;
	}

	public void setIdDepartement(Departement idDepartement) {
		this.idDepartement = idDepartement;
	}

	public Equipe getIdEquipe() {
		return idEquipe;
	}

	public void setIdEquipe(Equipe idEquipe) {
		this.idEquipe = idEquipe;
	}

	public Personnel getIdPersonnel() {
		return idPersonnel;
	}

	public void setIdPersonnel(Personnel idPersonnel) {
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
		if (!(object instanceof Technicien)) {
			return false;
		}
		Technicien other = (Technicien) object;
		if ((this.idTechnicien == null && other.idTechnicien != null)
				|| (this.idTechnicien != null && !this.idTechnicien
						.equals(other.idTechnicien))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.Technicien[ idTechnicien="
				+ idTechnicien + " ]";
	}

}
