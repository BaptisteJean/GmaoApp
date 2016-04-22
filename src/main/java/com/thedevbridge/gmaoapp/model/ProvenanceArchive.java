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
@Table(name = "provenance_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ProvenanceArchive.findAll", query = "SELECT p FROM ProvenanceArchive p"),
		@NamedQuery(name = "ProvenanceArchive.findByIdProvenance", query = "SELECT p FROM ProvenanceArchive p WHERE p.idProvenance = :idProvenance"),
		@NamedQuery(name = "ProvenanceArchive.findByMarche", query = "SELECT p FROM ProvenanceArchive p WHERE p.marche = :marche")})
public class ProvenanceArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_provenance")
	private Long idProvenance;
	@Column(name = "marche")
	private String marche;
	@JoinColumn(name = "id_client", referencedColumnName = "id_client")
	@ManyToOne(optional = false)
	private ClientArchive idClient;
	@JoinColumn(name = "id_fournisseur", referencedColumnName = "id_fournisseur")
	@ManyToOne(optional = false)
	private FournisseurArchive idFournisseur;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idProvenance")
	private List<ProvientArchive> provientArchiveList;

	public ProvenanceArchive() {
	}

	public ProvenanceArchive(Long idProvenance) {
		this.idProvenance = idProvenance;
	}

	public Long getIdProvenance() {
		return idProvenance;
	}

	public void setIdProvenance(Long idProvenance) {
		this.idProvenance = idProvenance;
	}

	public String getMarche() {
		return marche;
	}

	public void setMarche(String marche) {
		this.marche = marche;
	}

	public ClientArchive getIdClient() {
		return idClient;
	}

	public void setIdClient(ClientArchive idClient) {
		this.idClient = idClient;
	}

	public FournisseurArchive getIdFournisseur() {
		return idFournisseur;
	}

	public void setIdFournisseur(FournisseurArchive idFournisseur) {
		this.idFournisseur = idFournisseur;
	}

	@XmlTransient
	public List<ProvientArchive> getProvientArchiveList() {
		return provientArchiveList;
	}

	public void setProvientArchiveList(List<ProvientArchive> provientArchiveList) {
		this.provientArchiveList = provientArchiveList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idProvenance != null ? idProvenance.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof ProvenanceArchive)) {
			return false;
		}
		ProvenanceArchive other = (ProvenanceArchive) object;
		if ((this.idProvenance == null && other.idProvenance != null)
				|| (this.idProvenance != null && !this.idProvenance
						.equals(other.idProvenance))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.ProvenanceArchive[ idProvenance="
				+ idProvenance + " ]";
	}

}
