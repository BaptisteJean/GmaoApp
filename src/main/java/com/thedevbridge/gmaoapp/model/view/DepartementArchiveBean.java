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

import com.thedevbridge.gmaoapp.model.DepartementArchive;

/**
 * Backing bean for DepartementArchive entities.
 * <p/>
 * This class provides CRUD functionality for all DepartementArchive entities.
 * It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class DepartementArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving DepartementArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private DepartementArchive departementArchive;

	public DepartementArchive getDepartementArchive() {
		return this.departementArchive;
	}

	public void setDepartementArchive(DepartementArchive departementArchive) {
		this.departementArchive = departementArchive;
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
			this.departementArchive = this.example;
		} else {
			this.departementArchive = findById(getId());
		}
	}

	public DepartementArchive findById(Long id) {

		return this.entityManager.find(DepartementArchive.class, id);
	}

	/*
	 * Support updating and deleting DepartementArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.departementArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.departementArchive);
				return "view?faces-redirect=true&id="
						+ this.departementArchive.getIdDepartement();
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
			DepartementArchive deletableEntity = findById(getId());

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
	 * Support searching DepartementArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<DepartementArchive> pageItems;

	private DepartementArchive example = new DepartementArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public DepartementArchive getExample() {
		return this.example;
	}

	public void setExample(DepartementArchive example) {
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
		Root<DepartementArchive> root = countCriteria
				.from(DepartementArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<DepartementArchive> criteria = builder
				.createQuery(DepartementArchive.class);
		root = criteria.from(DepartementArchive.class);
		TypedQuery<DepartementArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<DepartementArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String chefDepartement = this.example.getChefDepartement();
		if (chefDepartement != null && !"".equals(chefDepartement)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("chefDepartement")),
					'%' + chefDepartement.toLowerCase() + '%'));
		}
		String descriptionActiviteDepartement = this.example
				.getDescriptionActiviteDepartement();
		if (descriptionActiviteDepartement != null
				&& !"".equals(descriptionActiviteDepartement)) {
			predicatesList.add(builder.like(builder.lower(root
					.<String> get("descriptionActiviteDepartement")),
					'%' + descriptionActiviteDepartement.toLowerCase() + '%'));
		}
		String libelleDepartemnt = this.example.getLibelleDepartemnt();
		if (libelleDepartemnt != null && !"".equals(libelleDepartemnt)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("libelleDepartemnt")),
					'%' + libelleDepartemnt.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<DepartementArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back DepartementArchive entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<DepartementArchive> getAll() {

		CriteriaQuery<DepartementArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(DepartementArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(DepartementArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final DepartementArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(DepartementArchiveBean.class);

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

				return String.valueOf(((DepartementArchive) value)
						.getIdDepartement());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private DepartementArchive add = new DepartementArchive();

	public DepartementArchive getAdd() {
		return this.add;
	}

	public DepartementArchive getAdded() {
		DepartementArchive added = this.add;
		this.add = new DepartementArchive();
		return added;
	}
}
