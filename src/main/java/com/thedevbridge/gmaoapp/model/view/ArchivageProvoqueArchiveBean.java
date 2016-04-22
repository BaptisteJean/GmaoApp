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

import com.thedevbridge.gmaoapp.model.ArchivageProvoqueArchive;
import com.thedevbridge.gmaoapp.model.ArchivageArchive;

/**
 * Backing bean for ArchivageProvoqueArchive entities.
 * <p/>
 * This class provides CRUD functionality for all ArchivageProvoqueArchive
 * entities. It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ArchivageProvoqueArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving ArchivageProvoqueArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private ArchivageProvoqueArchive archivageProvoqueArchive;

	public ArchivageProvoqueArchive getArchivageProvoqueArchive() {
		return this.archivageProvoqueArchive;
	}

	public void setArchivageProvoqueArchive(
			ArchivageProvoqueArchive archivageProvoqueArchive) {
		this.archivageProvoqueArchive = archivageProvoqueArchive;
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
			this.archivageProvoqueArchive = this.example;
		} else {
			this.archivageProvoqueArchive = findById(getId());
		}
	}

	public ArchivageProvoqueArchive findById(Long id) {

		return this.entityManager.find(ArchivageProvoqueArchive.class, id);
	}

	/*
	 * Support updating and deleting ArchivageProvoqueArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.archivageProvoqueArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.archivageProvoqueArchive);
				return "view?faces-redirect=true&id="
						+ this.archivageProvoqueArchive
								.getIdArchivageProvoque();
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
			ArchivageProvoqueArchive deletableEntity = findById(getId());
			ArchivageArchive idArchivage = deletableEntity.getIdArchivage();
			idArchivage.getArchivageProvoqueArchiveList().remove(
					deletableEntity);
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
	 * Support searching ArchivageProvoqueArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<ArchivageProvoqueArchive> pageItems;

	private ArchivageProvoqueArchive example = new ArchivageProvoqueArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public ArchivageProvoqueArchive getExample() {
		return this.example;
	}

	public void setExample(ArchivageProvoqueArchive example) {
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
		Root<ArchivageProvoqueArchive> root = countCriteria
				.from(ArchivageProvoqueArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<ArchivageProvoqueArchive> criteria = builder
				.createQuery(ArchivageProvoqueArchive.class);
		root = criteria.from(ArchivageProvoqueArchive.class);
		TypedQuery<ArchivageProvoqueArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<ArchivageProvoqueArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		ArchivageArchive idArchivage = this.example.getIdArchivage();
		if (idArchivage != null) {
			predicatesList.add(builder.equal(root.get("idArchivage"),
					idArchivage));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<ArchivageProvoqueArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back ArchivageProvoqueArchive entities (e.g.
	 * from inside an HtmlSelectOneMenu)
	 */

	public List<ArchivageProvoqueArchive> getAll() {

		CriteriaQuery<ArchivageProvoqueArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(
						ArchivageProvoqueArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(ArchivageProvoqueArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ArchivageProvoqueArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(ArchivageProvoqueArchiveBean.class);

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

				return String.valueOf(((ArchivageProvoqueArchive) value)
						.getIdArchivageProvoque());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private ArchivageProvoqueArchive add = new ArchivageProvoqueArchive();

	public ArchivageProvoqueArchive getAdd() {
		return this.add;
	}

	public ArchivageProvoqueArchive getAdded() {
		ArchivageProvoqueArchive added = this.add;
		this.add = new ArchivageProvoqueArchive();
		return added;
	}
}
