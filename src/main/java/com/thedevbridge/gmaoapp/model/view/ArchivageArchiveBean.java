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

import com.thedevbridge.gmaoapp.model.ArchivageArchive;
import com.thedevbridge.gmaoapp.model.ClientArchive;
import com.thedevbridge.gmaoapp.model.DirecteurTechniqueArchive;

/**
 * Backing bean for ArchivageArchive entities.
 * <p/>
 * This class provides CRUD functionality for all ArchivageArchive entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ArchivageArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving ArchivageArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private ArchivageArchive archivageArchive;

	public ArchivageArchive getArchivageArchive() {
		return this.archivageArchive;
	}

	public void setArchivageArchive(ArchivageArchive archivageArchive) {
		this.archivageArchive = archivageArchive;
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
			this.archivageArchive = this.example;
		} else {
			this.archivageArchive = findById(getId());
		}
	}

	public ArchivageArchive findById(Long id) {

		return this.entityManager.find(ArchivageArchive.class, id);
	}

	/*
	 * Support updating and deleting ArchivageArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.archivageArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.archivageArchive);
				return "view?faces-redirect=true&id="
						+ this.archivageArchive.getIdArchivage();
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
			ArchivageArchive deletableEntity = findById(getId());
			ClientArchive idClient = deletableEntity.getIdClient();
			idClient.getArchivageArchiveList().remove(deletableEntity);
			deletableEntity.setIdClient(null);
			this.entityManager.merge(idClient);
			DirecteurTechniqueArchive idDirecteurTechnique = deletableEntity
					.getIdDirecteurTechnique();
			idDirecteurTechnique.getArchivageArchiveList().remove(
					deletableEntity);
			deletableEntity.setIdDirecteurTechnique(null);
			this.entityManager.merge(idDirecteurTechnique);
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
	 * Support searching ArchivageArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<ArchivageArchive> pageItems;

	private ArchivageArchive example = new ArchivageArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public ArchivageArchive getExample() {
		return this.example;
	}

	public void setExample(ArchivageArchive example) {
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
		Root<ArchivageArchive> root = countCriteria
				.from(ArchivageArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<ArchivageArchive> criteria = builder
				.createQuery(ArchivageArchive.class);
		root = criteria.from(ArchivageArchive.class);
		TypedQuery<ArchivageArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<ArchivageArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		ClientArchive idClient = this.example.getIdClient();
		if (idClient != null) {
			predicatesList.add(builder.equal(root.get("idClient"), idClient));
		}
		DirecteurTechniqueArchive idDirecteurTechnique = this.example
				.getIdDirecteurTechnique();
		if (idDirecteurTechnique != null) {
			predicatesList.add(builder.equal(root.get("idDirecteurTechnique"),
					idDirecteurTechnique));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<ArchivageArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back ArchivageArchive entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<ArchivageArchive> getAll() {

		CriteriaQuery<ArchivageArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(ArchivageArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(ArchivageArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ArchivageArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(ArchivageArchiveBean.class);

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

				return String.valueOf(((ArchivageArchive) value)
						.getIdArchivage());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private ArchivageArchive add = new ArchivageArchive();

	public ArchivageArchive getAdd() {
		return this.add;
	}

	public ArchivageArchive getAdded() {
		ArchivageArchive added = this.add;
		this.add = new ArchivageArchive();
		return added;
	}
}
