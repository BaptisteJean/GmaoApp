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
@Table(name = "fournisseur")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Fournisseur.findAll", query = "SELECT f FROM Fournisseur f"),
		@NamedQuery(name = "Fournisseur.findByIdFournisseur", query = "SELECT f FROM Fournisseur f WHERE f.idFournisseur = :idFournisseur"),
		@NamedQuery(name = "Fournisseur.findByRaisonSocial", query = "SELECT f FROM Fournisseur f WHERE f.raisonSocial = :raisonSocial")})
public class Fournisseur implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_fournisseur")
	private Long idFournisseur;
	@Column(name = "raison_social")
	private String raisonSocial;
	@JoinColumn(name = "id_adresse", referencedColumnName = "id_adresse")
	@ManyToOne(optional = false)
	private Adresse idAdresse;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idFournisseur")
	private List<Provenance> provenanceList;

	public Fournisseur() {
	}

	public Fournisseur(Long idFournisseur) {
		this.idFournisseur = idFournisseur;
	}

	public Long getIdFournisseur() {
		return idFournisseur;
	}

	public void setIdFournisseur(Long idFournisseur) {
		this.idFournisseur = idFournisseur;
	}

	public String getRaisonSocial() {
		return raisonSocial;
	}

	public void setRaisonSocial(String raisonSocial) {
		this.raisonSocial = raisonSocial;
	}

	public Adresse getIdAdresse() {
		return idAdresse;
	}

	public void setIdAdresse(Adresse idAdresse) {
		this.idAdresse = idAdresse;
	}

	@XmlTransient
	public List<Provenance> getProvenanceList() {
		return provenanceList;
	}

	public void setProvenanceList(List<Provenance> provenanceList) {
		this.provenanceList = provenanceList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idFournisseur != null ? idFournisseur.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Fournisseur)) {
			return false;
		}
		Fournisseur other = (Fournisseur) object;
		if ((this.idFournisseur == null && other.idFournisseur != null)
				|| (this.idFournisseur != null && !this.idFournisseur
						.equals(other.idFournisseur))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.Fournisseur[ idFournisseur="
				+ idFournisseur + " ]";
	}

}
