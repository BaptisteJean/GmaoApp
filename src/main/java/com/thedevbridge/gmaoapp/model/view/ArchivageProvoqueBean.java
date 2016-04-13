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

import com.thedevbridge.gmaoapp.model.ArchivageProvoque;
import com.thedevbridge.gmaoapp.model.Archivage;

/**
 * Backing bean for ArchivageProvoque entities.
 * <p/>
 * This class provides CRUD functionality for all ArchivageProvoque entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ArchivageProvoqueBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving ArchivageProvoque entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private ArchivageProvoque archivageProvoque;

	public ArchivageProvoque getArchivageProvoque() {
		return this.archivageProvoque;
	}

	public void setArchivageProvoque(ArchivageProvoque archivageProvoque) {
		this.archivageProvoque = archivageProvoque;
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
			this.archivageProvoque = this.example;
		} else {
			this.archivageProvoque = findById(getId());
		}
	}

	public ArchivageProvoque findById(Long id) {

		return this.entityManager.find(ArchivageProvoque.class, id);
	}

	/*
	 * Support updating and deleting ArchivageProvoque entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.archivageProvoque);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.archivageProvoque);
				return "view?faces-redirect=true&id="
						+ this.archivageProvoque.getIdArchivageProvoque();
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
			ArchivageProvoque deletableEntity = findById(getId());
			Archivage idArchivage = deletableEntity.getIdArchivage();
			idArchivage.getArchivageProvoqueList().remove(deletableEntity);
			deletableEntity.setIdArchivage(null);
			this.entityManager.merge(idArchivage);
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
	 * Support searching ArchivageProvoque entities with pagination
	 */

	private int page;
	private long count;
	private List<ArchivageProvoque> pageItems;

	private ArchivageProvoque example = new ArchivageProvoque();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public ArchivageProvoque getExample() {
		return this.example;
	}

	public void setExample(ArchivageProvoque example) {
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
		Root<ArchivageProvoque> root = countCriteria
				.from(ArchivageProvoque.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<ArchivageProvoque> criteria = builder
				.createQuery(ArchivageProvoque.class);
		root = criteria.from(ArchivageProvoque.class);
		TypedQuery<ArchivageProvoque> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<ArchivageProvoque> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		Archivage idArchivage = this.example.getIdArchivage();
		if (idArchivage != null) {
			predicatesList.add(builder.equal(root.get("idArchivage"),
					idArchivage));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<ArchivageProvoque> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back ArchivageProvoque entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<ArchivageProvoque> getAll() {

		CriteriaQuery<ArchivageProvoque> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(ArchivageProvoque.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(ArchivageProvoque.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ArchivageProvoqueBean ejbProxy = this.sessionContext
				.getBusinessObject(ArchivageProvoqueBean.class);

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

				return String.valueOf(((ArchivageProvoque) value)
						.getIdArchivageProvoque());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private ArchivageProvoque add = new ArchivageProvoque();

	public ArchivageProvoque getAdd() {
		return this.add;
	}

	public ArchivageProvoque getAdded() {
		ArchivageProvoque added = this.add;
		this.add = new ArchivageProvoque();
		return added;
	}
}
