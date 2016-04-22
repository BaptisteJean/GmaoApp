package com.thedevbridge.gmaoapp.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.thedevbridge.gmaoapp.model.TechnicienArchive;
import com.thedevbridge.gmaoapp.model.DepartementArchive;
import com.thedevbridge.gmaoapp.model.EquipeArchive;
import com.thedevbridge.gmaoapp.model.PersonnelArchive;

/**
 * Backing bean for TechnicienArchive entities.
 * <p/>
 * This class provides CRUD functionality for all TechnicienArchive entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class TechnicienArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving TechnicienArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private TechnicienArchive technicienArchive;

	public TechnicienArchive getTechnicienArchive() {
		return this.technicienArchive;
	}

	public void setTechnicienArchive(TechnicienArchive technicienArchive) {
		this.technicienArchive = technicienArchive;
	}

	@Inject
	private Conversation conversation;

	@PersistenceContext(unitName = "GmaoApp-persistence-unit", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public String create() {

		this.conversation.begin();
		this.conversation.setTimeout(1800000L);
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
			this.conversation.setTimeout(1800000L);
		}

		if (this.id == null) {
			this.technicienArchive = this.example;
		} else {
			this.technicienArchive = findById(getId());
		}
	}

	public TechnicienArchive findById(Long id) {

		return this.entityManager.find(TechnicienArchive.class, id);
	}

	/*
	 * Support updating and deleting TechnicienArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.technicienArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.technicienArchive);
				return "view?faces-redirect=true&id="
						+ this.technicienArchive.getIdTechnicien();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			TechnicienArchive deletableEntity = findById(getId());
			DepartementArchive idDepartement = deletableEntity
					.getIdDepartement();
			idDepartement.getTechnicienArchiveList().remove(deletableEntity);
			deletableEntity.setIdDepartement(null);
			this.entityManager.merge(idDepartement);
			EquipeArchive idEquipe = deletableEntity.getIdEquipe();
			idEquipe.getTechnicienArchiveList().remove(deletableEntity);
			deletableEntity.setIdEquipe(null);
			this.entityManager.merge(idEquipe);
			PersonnelArchive idPersonnel = deletableEntity.getIdPersonnel();
			idPersonnel.getTechnicienArchiveList().remove(deletableEntity);
			deletableEntity.setIdPersonnel(null);
			this.entityManager.merge(idPersonnel);
			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	/*
	 * Support searching TechnicienArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<TechnicienArchive> pageItems;

	private TechnicienArchive example = new TechnicienArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public TechnicienArchive getExample() {
		return this.example;
	}

	public void setExample(TechnicienArchive example) {
		this.example = example;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<TechnicienArchive> root = countCriteria
				.from(TechnicienArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<TechnicienArchive> criteria = builder
				.createQuery(TechnicienArchive.class);
		root = criteria.from(TechnicienArchive.class);
		TypedQuery<TechnicienArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<TechnicienArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String fonctionTechicien = this.example.getFonctionTechicien();
		if (fonctionTechicien != null && !"".equals(fonctionTechicien)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("fonctionTechicien")),
					'%' + fonctionTechicien.toLowerCase() + '%'));
		}
		DepartementArchive idDepartement = this.example.getIdDepartement();
		if (idDepartement != null) {
			predicatesList.add(builder.equal(root.get("idDepartement"),
					idDepartement));
		}
		EquipeArchive idEquipe = this.example.getIdEquipe();
		if (idEquipe != null) {
			predicatesList.add(builder.equal(root.get("idEquipe"), idEquipe));
		}
		PersonnelArchive idPersonnel = this.example.getIdPersonnel();
		if (idPersonnel != null) {
			predicatesList.add(builder.equal(root.get("idPersonnel"),
					idPersonnel));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<TechnicienArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back TechnicienArchive entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<TechnicienArchive> getAll() {

		CriteriaQuery<TechnicienArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(TechnicienArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(TechnicienArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final TechnicienArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(TechnicienArchiveBean.class);

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return ejbProxy.findById(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((TechnicienArchive) value)
						.getIdTechnicien());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private TechnicienArchive add = new TechnicienArchive();

	public TechnicienArchive getAdd() {
		return this.add;
	}

	public TechnicienArchive getAdded() {
		TechnicienArchive added = this.add;
		this.add = new TechnicienArchive();
		return added;
	}
}
