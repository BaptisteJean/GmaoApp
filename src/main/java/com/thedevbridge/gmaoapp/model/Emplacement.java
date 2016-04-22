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
@Table(name = "emplacement")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Emplacement.findAll", query = "SELECT e FROM Emplacement e"),
		@NamedQuery(name = "Emplacement.findByIdEmpl", query = "SELECT e FROM Emplacement e WHERE e.idEmpl = :idEmpl")})
public class Emplacement implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_empl")
	private Long idEmpl;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmplacement")
	private List<ExamplaireMateriel> examplaireMaterielList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmplacement")
	private List<EmplacementClient> emplacementClientList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmplacement")
	private List<EmplacementTn> emplacementTnList;

	public Emplacement() {
	}

	public Emplacement(Long idEmpl) {
		this.idEmpl = idEmpl;
	}

	public Long getIdEmpl() {
		return idEmpl;
	}

	public void setIdEmpl(Long idEmpl) {
		this.idEmpl = idEmpl;
	}

	@XmlTransient
	public List<ExamplaireMateriel> getExamplaireMaterielList() {
		return examplaireMaterielList;
	}

	public void setExamplaireMaterielList(
			List<ExamplaireMateriel> examplaireMaterielList) {
		this.examplaireMaterielList = examplaireMaterielList;
	}

	@XmlTransient
	public List<EmplacementClient> getEmplacementClientList() {
		return emplacementClientList;
	}

	public void setEmplacementClientList(
			List<EmplacementClient> emplacementClientList) {
		this.emplacementClientList = emplacementClientList;
	}

	@XmlTransient
	public List<EmplacementTn> getEmplacementTnList() {
		return emplacementTnList;
	}

	public void setEmplacementTnList(List<EmplacementTn> emplacementTnList) {
		this.emplacementTnList = emplacementTnList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idEmpl != null ? idEmpl.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Emplacement)) {
			return false;
		}
		Emplacement other = (Emplacement) object;
		if ((this.idEmpl == null && other.idEmpl != null)
				|| (this.idEmpl != null && !this.idEmpl.equals(other.idEmpl))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.Emplacement[ idEmpl=" + idEmpl
				+ " ]";
	}

}
