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

import com.thedevbridge.gmaoapp.model.Archivage;
import com.thedevbridge.gmaoapp.model.Client;
import com.thedevbridge.gmaoapp.model.DirecteurTechnique;

/**
 * Backing bean for Archivage entities.
 * <p/>
 * This class provides CRUD functionality for all Archivage entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ArchivageBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Archivage entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Archivage archivage;

	public Archivage getArchivage() {
		return this.archivage;
	}

	public void setArchivage(Archivage archivage) {
		this.archivage = archivage;
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
			this.archivage = this.example;
		} else {
			this.archivage = findById(getId());
		}
	}

	public Archivage findById(Long id) {

		return this.entityManager.find(Archivage.class, id);
	}

	/*
	 * Support updating and deleting Archivage entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.archivage);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.archivage);
				return "view?faces-redirect=true&id="
						+ this.archivage.getIdArchivage();
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
			Archivage deletableEntity = findById(getId());
			Client idClient = deletableEntity.getIdClient();
			idClient.getArchivageList().remove(deletableEntity);
			deletableEntity.setIdClient(null);
			this.entityManager.merge(idClient);
			DirecteurTechnique idDirecteurTechnique = deletableEntity
					.getIdDirecteurTechnique();
			idDirecteurTechnique.getArchivageList().remove(deletableEntity);
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
	 * Support searching Archivage entities with pagination
	 */

	private int page;
	private long count;
	private List<Archivage> pageItems;

	private Archivage example = new Archivage();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Archivage getExample() {
		return this.example;
	}

	public void setExample(Archivage example) {
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
		Root<Archivage> root = countCriteria.from(Archivage.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Archivage> criteria = builder
				.createQuery(Archivage.class);
		root = criteria.from(Archivage.class);
		TypedQuery<Archivage> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Archivage> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		Client idClient = this.example.getIdClient();
		if (idClient != null) {
			predicatesList.add(builder.equal(root.get("idClient"), idClient));
		}
		DirecteurTechnique idDirecteurTechnique = this.example
				.getIdDirecteurTechnique();
		if (idDirecteurTechnique != null) {
			predicatesList.add(builder.equal(root.get("idDirecteurTechnique"),
					idDirecteurTechnique));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Archivage> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Archivage entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Archivage> getAll() {

		CriteriaQuery<Archivage> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Archivage.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Archivage.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ArchivageBean ejbProxy = this.sessionContext
				.getBusinessObject(ArchivageBean.class);

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

				return String.valueOf(((Archivage) value).getIdArchivage());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Archivage add = new Archivage();

	public Archivage getAdd() {
		return this.add;
	}

	public Archivage getAdded() {
		Archivage added = this.add;
		this.add = new Archivage();
		return added;
	}
}
