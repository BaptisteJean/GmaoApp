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

import com.thedevbridge.gmaoapp.model.ArchivageAutomatique;
import com.thedevbridge.gmaoapp.model.Archivage;

/**
 * Backing bean for ArchivageAutomatique entities.
 * <p/>
 * This class provides CRUD functionality for all ArchivageAutomatique entities.
 * It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ArchivageAutomatiqueBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving ArchivageAutomatique entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private ArchivageAutomatique archivageAutomatique;

	public ArchivageAutomatique getArchivageAutomatique() {
		return this.archivageAutomatique;
	}

	public void setArchivageAutomatique(
			ArchivageAutomatique archivageAutomatique) {
		this.archivageAutomatique = archivageAutomatique;
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
			this.archivageAutomatique = this.example;
		} else {
			this.archivageAutomatique = findById(getId());
		}
	}

	public ArchivageAutomatique findById(Long id) {

		return this.entityManager.find(ArchivageAutomatique.class, id);
	}

	/*
	 * Support updating and deleting ArchivageAutomatique entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.archivageAutomatique);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.archivageAutomatique);
				return "view?faces-redirect=true&id="
						+ this.archivageAutomatique.getIdArchivageAutomatique();
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
			ArchivageAutomatique deletableEntity = findById(getId());
			Archivage idArchivage = deletableEntity.getIdArchivage();
			idArchivage.getArchivageAutomatiqueList().remove(deletableEntity);
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
	 * Support searching ArchivageAutomatique entities with pagination
	 */

	private int page;
	private long count;
	private List<ArchivageAutomatique> pageItems;

	private ArchivageAutomatique example = new ArchivageAutomatique();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public ArchivageAutomatique getExample() {
		return this.example;
	}

	public void setExample(ArchivageAutomatique example) {
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
		Root<ArchivageAutomatique> root = countCriteria
				.from(ArchivageAutomatique.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<ArchivageAutomatique> criteria = builder
				.createQuery(ArchivageAutomatique.class);
		root = criteria.from(ArchivageAutomatique.class);
		TypedQuery<ArchivageAutomatique> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<ArchivageAutomatique> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		Archivage idArchivage = this.example.getIdArchivage();
		if (idArchivage != null) {
			predicatesList.add(builder.equal(root.get("idArchivage"),
					idArchivage));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<ArchivageAutomatique> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back ArchivageAutomatique entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<ArchivageAutomatique> getAll() {

		CriteriaQuery<ArchivageAutomatique> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(ArchivageAutomatique.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(ArchivageAutomatique.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ArchivageAutomatiqueBean ejbProxy = this.sessionContext
				.getBusinessObject(ArchivageAutomatiqueBean.class);

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

				return String.valueOf(((ArchivageAutomatique) value)
						.getIdArchivageAutomatique());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private ArchivageAutomatique add = new ArchivageAutomatique();

	public ArchivageAutomatique getAdd() {
		return this.add;
	}

	public ArchivageAutomatique getAdded() {
		ArchivageAutomatique added = this.add;
		this.add = new ArchivageAutomatique();
		return added;
	}
}
