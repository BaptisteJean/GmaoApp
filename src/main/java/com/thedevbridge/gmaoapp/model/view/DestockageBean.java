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

import com.thedevbridge.gmaoapp.model.Destockage;
import com.thedevbridge.gmaoapp.model.Destination;
import com.thedevbridge.gmaoapp.model.ExamplaireMateriel;

/**
 * Backing bean for Destockage entities.
 * <p/>
 * This class provides CRUD functionality for all Destockage entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class DestockageBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Destockage entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Destockage destockage;

	public Destockage getDestockage() {
		return this.destockage;
	}

	public void setDestockage(Destockage destockage) {
		this.destockage = destockage;
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
			this.destockage = this.example;
		} else {
			this.destockage = findById(getId());
		}
	}

	public Destockage findById(Long id) {

		return this.entityManager.find(Destockage.class, id);
	}

	/*
	 * Support updating and deleting Destockage entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.destockage);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.destockage);
				return "view?faces-redirect=true&id="
						+ this.destockage.getIdDestockage();
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
			Destockage deletableEntity = findById(getId());
			Destination idDestination = deletableEntity.getIdDestination();
			idDestination.getDestockageList().remove(deletableEntity);
			deletableEntity.setIdDestination(null);
			this.entityManager.merge(idDestination);
			ExamplaireMateriel idExemplaire = deletableEntity.getIdExemplaire();
			idExemplaire.getDestockageList().remove(deletableEntity);
			deletableEntity.setIdExemplaire(null);
			this.entityManager.merge(idExemplaire);
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
	 * Support searching Destockage entities with pagination
	 */

	private int page;
	private long count;
	private List<Destockage> pageItems;

	private Destockage example = new Destockage();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Destockage getExample() {
		return this.example;
	}

	public void setExample(Destockage example) {
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
		Root<Destockage> root = countCriteria.from(Destockage.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Destockage> criteria = builder
				.createQuery(Destockage.class);
		root = criteria.from(Destockage.class);
		TypedQuery<Destockage> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Destockage> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String dateRetrait = this.example.getDateRetrait();
		if (dateRetrait != null && !"".equals(dateRetrait)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("dateRetrait")),
					'%' + dateRetrait.toLowerCase() + '%'));
		}
		Integer quantiteRetire = this.example.getQuantiteRetire();
		if (quantiteRetire != null && quantiteRetire.intValue() != 0) {
			predicatesList.add(builder.equal(root.get("quantiteRetire"),
					quantiteRetire));
		}
		Destination idDestination = this.example.getIdDestination();
		if (idDestination != null) {
			predicatesList.add(builder.equal(root.get("idDestination"),
					idDestination));
		}
		ExamplaireMateriel idExemplaire = this.example.getIdExemplaire();
		if (idExemplaire != null) {
			predicatesList.add(builder.equal(root.get("idExemplaire"),
					idExemplaire));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Destockage> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Destockage entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Destockage> getAll() {

		CriteriaQuery<Destockage> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Destockage.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Destockage.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final DestockageBean ejbProxy = this.sessionContext
				.getBusinessObject(DestockageBean.class);

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

				return String.valueOf(((Destockage) value).getIdDestockage());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Destockage add = new Destockage();

	public Destockage getAdd() {
		return this.add;
	}

	public Destockage getAdded() {
		Destockage added = this.add;
		this.add = new Destockage();
		return added;
	}
}
