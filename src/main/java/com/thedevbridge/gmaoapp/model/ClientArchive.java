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
@Table(name = "client_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ClientArchive.findAll", query = "SELECT c FROM ClientArchive c"),
		@NamedQuery(name = "ClientArchive.findByIdClient", query = "SELECT c FROM ClientArchive c WHERE c.idClient = :idClient"),
		@NamedQuery(name = "ClientArchive.findByCodeClient", query = "SELECT c FROM ClientArchive c WHERE c.codeClient = :codeClient"),
		@NamedQuery(name = "ClientArchive.findByNatureActivite", query = "SELECT c FROM ClientArchive c WHERE c.natureActivite = :natureActivite"),
		@NamedQuery(name = "ClientArchive.findByNomClient", query = "SELECT c FROM ClientArchive c WHERE c.nomClient = :nomClient"),
		@NamedQuery(name = "ClientArchive.findByPrenomClient", query = "SELECT c FROM ClientArchive c WHERE c.prenomClient = :prenomClient"),
		@NamedQuery(name = "ClientArchive.findByTypePersonne", query = "SELECT c FROM ClientArchive c WHERE c.typePersonne = :typePersonne")})
public class ClientArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_client")
	private Long idClient;
	@Column(name = "code_client")
	private String codeClient;
	@Column(name = "nature_activite")
	private String natureActivite;
	@Column(name = "nom_client")
	private String nomClient;
	@Column(name = "prenom_client")
	private String prenomClient;
	@Column(name = "type_personne")
	private String typePersonne;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idClient")
	private List<ArchivageArchive> archivageArchiveList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idClient")
	private List<SiteInterventionArchive> siteInterventionArchiveList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idClient")
	private List<ProvenanceArchive> provenanceArchiveList;
	@JoinColumn(name = "id_adresse", referencedColumnName = "id_adresse")
	@ManyToOne(optional = false)
	private AdresseArchive idAdresse;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idClient")
	private List<DestinationArchive> destinationArchiveList;

	public ClientArchive() {
	}

	public ClientArchive(Long idClient) {
		this.idClient = idClient;
	}

	public Long getIdClient() {
		return idClient;
	}

	public void setIdClient(Long idClient) {
		this.idClient = idClient;
	}

	public String getCodeClient() {
		return codeClient;
	}

	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	public String getNatureActivite() {
		return natureActivite;
	}

	public void setNatureActivite(String natureActivite) {
		this.natureActivite = natureActivite;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public String getPrenomClient() {
		return prenomClient;
	}

	public void setPrenomClient(String prenomClient) {
		this.prenomClient = prenomClient;
	}

	public String getTypePersonne() {
		return typePersonne;
	}

	public void setTypePersonne(String typePersonne) {
		this.typePersonne = typePersonne;
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
	public List<SiteInterventionArchive> getSiteInterventionArchiveList() {
		return siteInterventionArchiveList;
	}

	public void setSiteInterventionArchiveList(
			List<SiteInterventionArchive> siteInterventionArchiveList) {
		this.siteInterventionArchiveList = siteInterventionArchiveList;
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

	@XmlTransient
	public List<DestinationArchive> getDestinationArchiveList() {
		return destinationArchiveList;
	}

	public void setDestinationArchiveList(
			List<DestinationArchive> destinationArchiveList) {
		this.destinationArchiveList = destinationArchiveList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idClient != null ? idClient.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof ClientArchive)) {
			return false;
		}
		ClientArchive other = (ClientArchive) object;
		if ((this.idClient == null && other.idClient != null)
				|| (this.idClient != null && !this.idClient
						.equals(other.idClient))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.ClientArchive[ idClient="
				+ idClient + " ]";
	}

}
