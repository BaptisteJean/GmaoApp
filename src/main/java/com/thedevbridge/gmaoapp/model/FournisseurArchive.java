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
@Table(name = "fournisseur_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "FournisseurArchive.findAll", query = "SELECT f FROM FournisseurArchive f"),
		@NamedQuery(name = "FournisseurArchive.findByIdFournisseur", query = "SELECT f FROM FournisseurArchive f WHERE f.idFournisseur = :idFournisseur"),
		@NamedQuery(name = "FournisseurArchive.findByRaisonSocial", query = "SELECT f FROM FournisseurArchive f WHERE f.raisonSocial = :raisonSocial")})
public class FournisseurArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_fournisseur")
	private Long idFournisseur;
	@Column(name = "raison_social")
	private String raisonSocial;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idFournisseur")
	private List<ProvenanceArchive> provenanceArchiveList;
	@JoinColumn(name = "id_adresse", referencedColumnName = "id_adresse")
	@ManyToOne(optional = false)
	private AdresseArchive idAdresse;

	public FournisseurArchive() {
	}

	public FournisseurArchive(Long idFournisseur) {
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

	@XmlTransient
	public List<ProvenanceArchive> getProvenanceArchiveList() {
		return provenanceArchiveList;
	}

	public void setProvenanceArchiveList(
			List<ProvenanceArchive> provenanceArchiveList) {
		this.provenanceArchiveList = provenanceArchiveList;
	}

	public AdresseArchive getIdAdresse() {
		return idAdresse;
	}

	public void setIdAdresse(AdresseArchive idAdresse) {
		this.idAdresse = idAdresse;
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
		if (!(object instanceof FournisseurArchive)) {
			return false;
		}
		FournisseurArchive other = (FournisseurArchive) object;
		if ((this.idFournisseur == null && other.idFournisseur != null)
				|| (this.idFournisseur != null && !this.idFournisseur
						.equals(other.idFournisseur))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.FournisseurArchive[ idFournisseur="
				+ idFournisseur + " ]";
	}

}
