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
@Table(name = "emplacement_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "EmplacementArchive.findAll", query = "SELECT e FROM EmplacementArchive e"),
		@NamedQuery(name = "EmplacementArchive.findByIdEmpl", query = "SELECT e FROM EmplacementArchive e WHERE e.idEmpl = :idEmpl")})
public class EmplacementArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_empl")
	private Long idEmpl;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmplacement")
	private List<EmplacementClientArchive> emplacementClientArchiveList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmplacement")
	private List<ExamplaireMaterielArchive> examplaireMaterielArchiveList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmplacement")
	private List<EmplacementTnArchive> emplacementTnArchiveList;

	public EmplacementArchive() {
	}

	public EmplacementArchive(Long idEmpl) {
		this.idEmpl = idEmpl;
	}

	public Long getIdEmpl() {
		return idEmpl;
	}

	public void setIdEmpl(Long idEmpl) {
		this.idEmpl = idEmpl;
	}

	@XmlTransient
	public List<EmplacementClientArchive> getEmplacementClientArchiveList() {
		return emplacementClientArchiveList;
	}

	public void setEmplacementClientArchiveList(
			List<EmplacementClientArchive> emplacementClientArchiveList) {
		this.emplacementClientArchiveList = emplacementClientArchiveList;
	}

	@XmlTransient
	public List<ExamplaireMaterielArchive> getExamplaireMaterielArchiveList() {
		return examplaireMaterielArchiveList;
	}

	public void setExamplaireMaterielArchiveList(
			List<ExamplaireMaterielArchive> examplaireMaterielArchiveList) {
		this.examplaireMaterielArchiveList = examplaireMaterielArchiveList;
	}

	@XmlTransient
	public List<EmplacementTnArchive> getEmplacementTnArchiveList() {
		return emplacementTnArchiveList;
	}

	public void setEmplacementTnArchiveList(
			List<EmplacementTnArchive> emplacementTnArchiveList) {
		this.emplacementTnArchiveList = emplacementTnArchiveList;
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
		if (!(object instanceof EmplacementArchive)) {
			return false;
		}
		EmplacementArchive other = (EmplacementArchive) object;
		if ((this.idEmpl == null && other.idEmpl != null)
				|| (this.idEmpl != null && !this.idEmpl.equals(other.idEmpl))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.EmplacementArchive[ idEmpl="
				+ idEmpl + " ]";
	}

}
