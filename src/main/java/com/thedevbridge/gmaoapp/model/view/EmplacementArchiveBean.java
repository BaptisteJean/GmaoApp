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

import com.thedevbridge.gmaoapp.model.EmplacementArchive;

/**
 * Backing bean for EmplacementArchive entities.
 * <p/>
 * This class provides CRUD functionality for all EmplacementArchive entities.
 * It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class EmplacementArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving EmplacementArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private EmplacementArchive emplacementArchive;

	public EmplacementArchive getEmplacementArchive() {
		return this.emplacementArchive;
	}

	public void setEmplacementArchive(EmplacementArchive emplacementArchive) {
		this.emplacementArchive = emplacementArchive;
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
			this.emplacementArchive = this.example;
		} else {
			this.emplacementArchive = findById(getId());
		}
	}

	public EmplacementArchive findById(Long id) {

		return this.entityManager.find(EmplacementArchive.class, id);
	}

	/*
	 * Support updating and deleting EmplacementArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.emplacementArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.emplacementArchive);
				return "view?faces-redirect=true&id="
						+ this.emplacementArchive.getIdEmpl();
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
			EmplacementArchive deletableEntity = findById(getId());

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
	 * Support searching EmplacementArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<EmplacementArchive> pageItems;

	private EmplacementArchive example = new EmplacementArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public EmplacementArchive getExample() {
		return this.example;
	}

	public void setExample(EmplacementArchive example) {
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
		Root<EmplacementArchive> root = countCriteria
				.from(EmplacementArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<EmplacementArchive> criteria = builder
				.createQuery(EmplacementArchive.class);
		root = criteria.from(EmplacementArchive.class);
		TypedQuery<EmplacementArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<EmplacementArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<EmplacementArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back EmplacementArchive entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<EmplacementArchive> getAll() {

		CriteriaQuery<EmplacementArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(EmplacementArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(EmplacementArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final EmplacementArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(EmplacementArchiveBean.class);

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

				return String.valueOf(((EmplacementArchive) value).getIdEmpl());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private EmplacementArchive add = new EmplacementArchive();

	public EmplacementArchive getAdd() {
		return this.add;
	}

	public EmplacementArchive getAdded() {
		EmplacementArchive added = this.add;
		this.add = new EmplacementArchive();
		return added;
	}
}
