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

import com.thedevbridge.gmaoapp.model.EmplacementTnArchive;
import com.thedevbridge.gmaoapp.model.EmplacementArchive;

/**
 * Backing bean for EmplacementTnArchive entities.
 * <p/>
 * This class provides CRUD functionality for all EmplacementTnArchive entities.
 * It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class EmplacementTnArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving EmplacementTnArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private EmplacementTnArchive emplacementTnArchive;

	public EmplacementTnArchive getEmplacementTnArchive() {
		return this.emplacementTnArchive;
	}

	public void setEmplacementTnArchive(
			EmplacementTnArchive emplacementTnArchive) {
		this.emplacementTnArchive = emplacementTnArchive;
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
			this.emplacementTnArchive = this.example;
		} else {
			this.emplacementTnArchive = findById(getId());
		}
	}

	public EmplacementTnArchive findById(Long id) {

		return this.entityManager.find(EmplacementTnArchive.class, id);
	}

	/*
	 * Support updating and deleting EmplacementTnArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.emplacementTnArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.emplacementTnArchive);
				return "view?faces-redirect=true&id="
						+ this.emplacementTnArchive.getIdEmpltn();
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
			EmplacementTnArchive deletableEntity = findById(getId());
			EmplacementArchive idEmplacement = deletableEntity
					.getIdEmplacement();
			idEmplacement.getEmplacementTnArchiveList().remove(deletableEntity);
			deletableEntity.setIdEmplacement(null);
			this.entityManager.merge(idEmplacement);
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
	 * Support searching EmplacementTnArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<EmplacementTnArchive> pageItems;

	private EmplacementTnArchive example = new EmplacementTnArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public EmplacementTnArchive getExample() {
		return this.example;
	}

	public void setExample(EmplacementTnArchive example) {
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
		Root<EmplacementTnArchive> root = countCriteria
				.from(EmplacementTnArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<EmplacementTnArchive> criteria = builder
				.createQuery(EmplacementTnArchive.class);
		root = criteria.from(EmplacementTnArchive.class);
		TypedQuery<EmplacementTnArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<EmplacementTnArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String libelleCaisse = this.example.getLibelleCaisse();
		if (libelleCaisse != null && !"".equals(libelleCaisse)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("libelleCaisse")),
					'%' + libelleCaisse.toLowerCase() + '%'));
		}
		String libelleTirroir = this.example.getLibelleTirroir();
		if (libelleTirroir != null && !"".equals(libelleTirroir)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("libelleTirroir")),
					'%' + libelleTirroir.toLowerCase() + '%'));
		}
		EmplacementArchive idEmplacement = this.example.getIdEmplacement();
		if (idEmplacement != null) {
			predicatesList.add(builder.equal(root.get("idEmplacement"),
					idEmplacement));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<EmplacementTnArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back EmplacementTnArchive entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<EmplacementTnArchive> getAll() {

		CriteriaQuery<EmplacementTnArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(EmplacementTnArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(EmplacementTnArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final EmplacementTnArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(EmplacementTnArchiveBean.class);

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

				return String.valueOf(((EmplacementTnArchive) value)
						.getIdEmpltn());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private EmplacementTnArchive add = new EmplacementTnArchive();

	public EmplacementTnArchive getAdd() {
		return this.add;
	}

	public EmplacementTnArchive getAdded() {
		EmplacementTnArchive added = this.add;
		this.add = new EmplacementTnArchive();
		return added;
	}
}
