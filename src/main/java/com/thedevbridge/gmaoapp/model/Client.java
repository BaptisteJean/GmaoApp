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
@Table(name = "client")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c"),
		@NamedQuery(name = "Client.findByIdClient", query = "SELECT c FROM Client c WHERE c.idClient = :idClient"),
		@NamedQuery(name = "Client.findByCodeClient", query = "SELECT c FROM Client c WHERE c.codeClient = :codeClient"),
		@NamedQuery(name = "Client.findByNatureActivite", query = "SELECT c FROM Client c WHERE c.natureActivite = :natureActivite"),
		@NamedQuery(name = "Client.findByNomClient", query = "SELECT c FROM Client c WHERE c.nomClient = :nomClient"),
		@NamedQuery(name = "Client.findByPrenomClient", query = "SELECT c FROM Client c WHERE c.prenomClient = :prenomClient"),
		@NamedQuery(name = "Client.findByTypePersonne", query = "SELECT c FROM Client c WHERE c.typePersonne = :typePersonne")})
public class Client implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	private List<Archivage> archivageList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idClient")
	private List<Destination> destinationList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idClient")
	private List<Provenance> provenanceList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idClient")
	private List<SiteIntervention> siteInterventionList;
	@JoinColumn(name = "id_adresse", referencedColumnName = "id_adresse")
	@ManyToOne(optional = false)
	private Adresse idAdresse;

	public Client() {
            this.idAdresse = new Adresse();
	}

	public Client(Long idClient) {
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
	public List<Archivage> getArchivageList() {
		return archivageList;
	}

	public void setArchivageList(List<Archivage> archivageList) {
		this.archivageList = archivageList;
	}

	@XmlTransient
	public List<Destination> getDestinationList() {
		return destinationList;
	}

	public void setDestinationList(List<Destination> destinationList) {
		this.destinationList = destinationList;
	}

	@XmlTransient
	public List<Provenance> getProvenanceList() {
		return provenanceList;
	}

	public void setProvenanceList(List<Provenance> provenanceList) {
		this.provenanceList = provenanceList;
	}

	@XmlTransient
	public List<SiteIntervention> getSiteInterventionList() {
		return siteInterventionList;
	}

	public void setSiteInterventionList(
			List<SiteIntervention> siteInterventionList) {
		this.siteInterventionList = siteInterventionList;
	}

	public Adresse getIdAdresse() {
		return idAdresse;
	}

	public void setIdAdresse(Adresse idAdresse) {
		this.idAdresse = idAdresse;
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
		if (!(object instanceof Client)) {
			return false;
		}
		Client other = (Client) object;
		if ((this.idClient == null && other.idClient != null)
				|| (this.idClient != null && !this.idClient
						.equals(other.idClient))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.Client[ idClient=" + idClient
				+ " ]";
	}

}
