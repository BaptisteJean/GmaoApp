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
@Table(name = "adresse_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "AdresseArchive.findAll", query = "SELECT a FROM AdresseArchive a"),
		@NamedQuery(name = "AdresseArchive.findByIdAdresse", query = "SELECT a FROM AdresseArchive a WHERE a.idAdresse = :idAdresse"),
		@NamedQuery(name = "AdresseArchive.findByBoitePostale", query = "SELECT a FROM AdresseArchive a WHERE a.boitePostale = :boitePostale"),
		@NamedQuery(name = "AdresseArchive.findByCodeAdresse", query = "SELECT a FROM AdresseArchive a WHERE a.codeAdresse = :codeAdresse"),
		@NamedQuery(name = "AdresseArchive.findByEmail", query = "SELECT a FROM AdresseArchive a WHERE a.email = :email"),
		@NamedQuery(name = "AdresseArchive.findByLieuDit", query = "SELECT a FROM AdresseArchive a WHERE a.lieuDit = :lieuDit"),
		@NamedQuery(name = "AdresseArchive.findByNumeroPhone", query = "SELECT a FROM AdresseArchive a WHERE a.numeroPhone = :numeroPhone"),
		@NamedQuery(name = "AdresseArchive.findByRegion", query = "SELECT a FROM AdresseArchive a WHERE a.region = :region"),
		@NamedQuery(name = "AdresseArchive.findByVille", query = "SELECT a FROM AdresseArchive a WHERE a.ville = :ville")})
public class AdresseArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_adresse")
	private Long idAdresse;
	@Column(name = "boite_postale")
	private String boitePostale;
	@Column(name = "code_adresse")
	private String codeAdresse;
	@Column(name = "email")
	private String email;
	@Column(name = "lieu_dit")
	private String lieuDit;
	@Column(name = "numero_phone")
	private String numeroPhone;
	@Column(name = "region")
	private String region;
	@Column(name = "ville")
	private String ville;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idAdresse")
	private List<FournisseurArchive> fournisseurArchiveList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idAdresse")
	private List<ClientArchive> clientArchiveList;

	public AdresseArchive() {
	}

	public AdresseArchive(Long idAdresse) {
		this.idAdresse = idAdresse;
	}

	public Long getIdAdresse() {
		return idAdresse;
	}

	public void setIdAdresse(Long idAdresse) {
		this.idAdresse = idAdresse;
	}

	public String getBoitePostale() {
		return boitePostale;
	}

	public void setBoitePostale(String boitePostale) {
		this.boitePostale = boitePostale;
	}

	public String getCodeAdresse() {
		return codeAdresse;
	}

	public void setCodeAdresse(String codeAdresse) {
		this.codeAdresse = codeAdresse;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLieuDit() {
		return lieuDit;
	}

	public void setLieuDit(String lieuDit) {
		this.lieuDit = lieuDit;
	}

	public String getNumeroPhone() {
		return numeroPhone;
	}

	public void setNumeroPhone(String numeroPhone) {
		this.numeroPhone = numeroPhone;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	@XmlTransient
	public List<FournisseurArchive> getFournisseurArchiveList() {
		return fournisseurArchiveList;
	}

	public void setFournisseurArchiveList(
			List<FournisseurArchive> fournisseurArchiveList) {
		this.fournisseurArchiveList = fournisseurArchiveList;
	}

	@XmlTransient
	public List<ClientArchive> getClientArchiveList() {
		return clientArchiveList;
	}

	public void setClientArchiveList(List<ClientArchive> clientArchiveList) {
		this.clientArchiveList = clientArchiveList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idAdresse != null ? idAdresse.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof AdresseArchive)) {
			return false;
		}
		AdresseArchive other = (AdresseArchive) object;
		if ((this.idAdresse == null && other.idAdresse != null)
				|| (this.idAdresse != null && !this.idAdresse
						.equals(other.idAdresse))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.AdresseArchive[ idAdresse="
				+ idAdresse + " ]";
	}

}
