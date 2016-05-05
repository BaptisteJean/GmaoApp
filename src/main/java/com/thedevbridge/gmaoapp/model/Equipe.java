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
@Table(name = "equipe")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Equipe.findAll", query = "SELECT e FROM Equipe e"),
		@NamedQuery(name = "Equipe.findByIdEquipe", query = "SELECT e FROM Equipe e WHERE e.idEquipe = :idEquipe"),
		@NamedQuery(name = "Equipe.findByChefEquipe", query = "SELECT e FROM Equipe e WHERE e.chefEquipe = :chefEquipe"),
		@NamedQuery(name = "Equipe.findByDescriptionActiviteEquipe", query = "SELECT e FROM Equipe e WHERE e.descriptionActiviteEquipe = :descriptionActiviteEquipe"),
		@NamedQuery(name = "Equipe.findByLibelleEquipe", query = "SELECT e FROM Equipe e WHERE e.libelleEquipe = :libelleEquipe")})
public class Equipe implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_equipe")
	private Long idEquipe;
	@Column(name = "chef_equipe")
	private String chefEquipe;
	@Column(name = "description_activite_equipe")
	private String descriptionActiviteEquipe;
	@Column(name = "libelle_equipe")
	private String libelleEquipe;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idEquipe")
	private List<Rapport> rapportList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idEquipe")
	private List<Technicien> technicienList;
	@JoinColumn(name = "id_departemant", referencedColumnName = "id_departement")
	@ManyToOne(optional = false)
	private Departement idDepartemant;

	public Equipe() {
	}

	public Equipe(Long idEquipe) {
		this.idEquipe = idEquipe;
	}

	public Long getIdEquipe() {
		return idEquipe;
	}

	public void setIdEquipe(Long idEquipe) {
		this.idEquipe = idEquipe;
	}

	public String getChefEquipe() {
		return chefEquipe;
	}

	public void setChefEquipe(String chefEquipe) {
		this.chefEquipe = chefEquipe;
	}

	public String getDescriptionActiviteEquipe() {
		return descriptionActiviteEquipe;
	}

	public void setDescriptionActiviteEquipe(String descriptionActiviteEquipe) {
		this.descriptionActiviteEquipe = descriptionActiviteEquipe;
	}

	public String getLibelleEquipe() {
		return libelleEquipe;
	}

	public void setLibelleEquipe(String libelleEquipe) {
		this.libelleEquipe = libelleEquipe;
	}

	@XmlTransient
	public List<Rapport> getRapportList() {
		return rapportList;
	}

	public void setRapportList(List<Rapport> rapportList) {
		this.rapportList = rapportList;
	}

	@XmlTransient
	public List<Technicien> getTechnicienList() {
		return technicienList;
	}

	public void setTechnicienList(List<Technicien> technicienList) {
		this.technicienList = technicienList;
	}

	public Departement getIdDepartemant() {
		return idDepartemant;
	}

	public void setIdDepartemant(Departement idDepartemant) {
		this.idDepartemant = idDepartemant;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idEquipe != null ? idEquipe.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Equipe)) {
			return false;
		}
		Equipe other = (Equipe) object;
		if ((this.idEquipe == null && other.idEquipe != null)
				|| (this.idEquipe != null && !this.idEquipe
						.equals(other.idEquipe))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.Equipe[ idEquipe=" + idEquipe
				+ " ]";
	}

}
