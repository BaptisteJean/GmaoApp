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

import com.thedevbridge.gmaoapp.model.MaterielArchive;

/**
 * Backing bean for MaterielArchive entities.
 * <p/>
 * This class provides CRUD functionality for all MaterielArchive entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class MaterielArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving MaterielArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private MaterielArchive materielArchive;

	public MaterielArchive getMaterielArchive() {
		return this.materielArchive;
	}

	public void setMaterielArchive(MaterielArchive materielArchive) {
		this.materielArchive = materielArchive;
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
			this.materielArchive = this.example;
		} else {
			this.materielArchive = findById(getId());
		}
	}

	public MaterielArchive findById(Long id) {

		return this.entityManager.find(MaterielArchive.class, id);
	}

	/*
	 * Support updating and deleting MaterielArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.materielArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.materielArchive);
				return "view?faces-redirect=true&id="
						+ this.materielArchive.getIdMateriel();
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
			MaterielArchive deletableEntity = findById(getId());

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
	 * Support searching MaterielArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<MaterielArchive> pageItems;

	private MaterielArchive example = new MaterielArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public MaterielArchive getExample() {
		return this.example;
	}

	public void setExample(MaterielArchive example) {
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
		Root<MaterielArchive> root = countCriteria.from(MaterielArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<MaterielArchive> criteria = builder
				.createQuery(MaterielArchive.class);
		root = criteria.from(MaterielArchive.class);
		TypedQuery<MaterielArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<MaterielArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String designation = this.example.getDesignation();
		if (designation != null && !"".equals(designation)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("designation")),
					'%' + designation.toLowerCase() + '%'));
		}
		String modelMateriel = this.example.getModelMateriel();
		if (modelMateriel != null && !"".equals(modelMateriel)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("modelMateriel")),
					'%' + modelMateriel.toLowerCase() + '%'));
		}
		String typeMateriel = this.example.getTypeMateriel();
		if (typeMateriel != null && !"".equals(typeMateriel)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("typeMateriel")),
					'%' + typeMateriel.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<MaterielArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back MaterielArchive entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<MaterielArchive> getAll() {

		CriteriaQuery<MaterielArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(MaterielArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(MaterielArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final MaterielArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(MaterielArchiveBean.class);

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

				return String
						.valueOf(((MaterielArchive) value).getIdMateriel());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private MaterielArchive add = new MaterielArchive();

	public MaterielArchive getAdd() {
		return this.add;
	}

	public MaterielArchive getAdded() {
		MaterielArchive added = this.add;
		this.add = new MaterielArchive();
		return added;
	}
}
