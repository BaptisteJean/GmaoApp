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

import com.thedevbridge.gmaoapp.model.ArchivageAutomatiqueArchive;
import com.thedevbridge.gmaoapp.model.ArchivageArchive;

/**
 * Backing bean for ArchivageAutomatiqueArchive entities.
 * <p/>
 * This class provides CRUD functionality for all ArchivageAutomatiqueArchive
 * entities. It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ArchivageAutomatiqueArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving ArchivageAutomatiqueArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private ArchivageAutomatiqueArchive archivageAutomatiqueArchive;

	public ArchivageAutomatiqueArchive getArchivageAutomatiqueArchive() {
		return this.archivageAutomatiqueArchive;
	}

	public void setArchivageAutomatiqueArchive(
			ArchivageAutomatiqueArchive archivageAutomatiqueArchive) {
		this.archivageAutomatiqueArchive = archivageAutomatiqueArchive;
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
			this.archivageAutomatiqueArchive = this.example;
		} else {
			this.archivageAutomatiqueArchive = findById(getId());
		}
	}

	public ArchivageAutomatiqueArchive findById(Long id) {

		return this.entityManager.find(ArchivageAutomatiqueArchive.class, id);
	}

	/*
	 * Support updating and deleting ArchivageAutomatiqueArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.archivageAutomatiqueArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.archivageAutomatiqueArchive);
				return "view?faces-redirect=true&id="
						+ this.archivageAutomatiqueArchive
								.getIdArchivageAutomatique();
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
			ArchivageAutomatiqueArchive deletableEntity = findById(getId());
			ArchivageArchive idArchivage = deletableEntity.getIdArchivage();
			idArchivage.getArchivageAutomatiqueArchiveList().remove(
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
	 * Support searching ArchivageAutomatiqueArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<ArchivageAutomatiqueArchive> pageItems;

	private ArchivageAutomatiqueArchive example = new ArchivageAutomatiqueArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public ArchivageAutomatiqueArchive getExample() {
		return this.example;
	}

	public void setExample(ArchivageAutomatiqueArchive example) {
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
		Root<ArchivageAutomatiqueArchive> root = countCriteria
				.from(ArchivageAutomatiqueArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<ArchivageAutomatiqueArchive> criteria = builder
				.createQuery(ArchivageAutomatiqueArchive.class);
		root = criteria.from(ArchivageAutomatiqueArchive.class);
		TypedQuery<ArchivageAutomatiqueArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(
			Root<ArchivageAutomatiqueArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		ArchivageArchive idArchivage = this.example.getIdArchivage();
		if (idArchivage != null) {
			predicatesList.add(builder.equal(root.get("idArchivage"),
					idArchivage));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<ArchivageAutomatiqueArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back ArchivageAutomatiqueArchive entities
	 * (e.g. from inside an HtmlSelectOneMenu)
	 */

	public List<ArchivageAutomatiqueArchive> getAll() {

		CriteriaQuery<ArchivageAutomatiqueArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(
						ArchivageAutomatiqueArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria
						.from(ArchivageAutomatiqueArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ArchivageAutomatiqueArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(ArchivageAutomatiqueArchiveBean.class);

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

				return String.valueOf(((ArchivageAutomatiqueArchive) value)
						.getIdArchivageAutomatique());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private ArchivageAutomatiqueArchive add = new ArchivageAutomatiqueArchive();

	public ArchivageAutomatiqueArchive getAdd() {
		return this.add;
	}

	public ArchivageAutomatiqueArchive getAdded() {
		ArchivageAutomatiqueArchive added = this.add;
		this.add = new ArchivageAutomatiqueArchive();
		return added;
	}
}
