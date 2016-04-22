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

import com.thedevbridge.gmaoapp.model.EmplacementClientArchive;
import com.thedevbridge.gmaoapp.model.EmplacementArchive;

/**
 * Backing bean for EmplacementClientArchive entities.
 * <p/>
 * This class provides CRUD functionality for all EmplacementClientArchive
 * entities. It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class EmplacementClientArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving EmplacementClientArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private EmplacementClientArchive emplacementClientArchive;

	public EmplacementClientArchive getEmplacementClientArchive() {
		return this.emplacementClientArchive;
	}

	public void setEmplacementClientArchive(
			EmplacementClientArchive emplacementClientArchive) {
		this.emplacementClientArchive = emplacementClientArchive;
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
			this.emplacementClientArchive = this.example;
		} else {
			this.emplacementClientArchive = findById(getId());
		}
	}

	public EmplacementClientArchive findById(Long id) {

		return this.entityManager.find(EmplacementClientArchive.class, id);
	}

	/*
	 * Support updating and deleting EmplacementClientArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.emplacementClientArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.emplacementClientArchive);
				return "view?faces-redirect=true&id="
						+ this.emplacementClientArchive.getIdEmplclient();
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
			EmplacementClientArchive deletableEntity = findById(getId());
			EmplacementArchive idEmplacement = deletableEntity
					.getIdEmplacement();
			idEmplacement.getEmplacementClientArchiveList().remove(
					deletableEntity);
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
	 * Support searching EmplacementClientArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<EmplacementClientArchive> pageItems;

	private EmplacementClientArchive example = new EmplacementClientArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public EmplacementClientArchive getExample() {
		return this.example;
	}

	public void setExample(EmplacementClientArchive example) {
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
		Root<EmplacementClientArchive> root = countCriteria
				.from(EmplacementClientArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<EmplacementClientArchive> criteria = builder
				.createQuery(EmplacementClientArchive.class);
		root = criteria.from(EmplacementClientArchive.class);
		TypedQuery<EmplacementClientArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<EmplacementClientArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String bloc = this.example.getBloc();
		if (bloc != null && !"".equals(bloc)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("bloc")),
					'%' + bloc.toLowerCase() + '%'));
		}
		String dateInstallation = this.example.getDateInstallation();
		if (dateInstallation != null && !"".equals(dateInstallation)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("dateInstallation")),
					'%' + dateInstallation.toLowerCase() + '%'));
		}
		String salle = this.example.getSalle();
		if (salle != null && !"".equals(salle)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("salle")),
					'%' + salle.toLowerCase() + '%'));
		}
		EmplacementArchive idEmplacement = this.example.getIdEmplacement();
		if (idEmplacement != null) {
			predicatesList.add(builder.equal(root.get("idEmplacement"),
					idEmplacement));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<EmplacementClientArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back EmplacementClientArchive entities (e.g.
	 * from inside an HtmlSelectOneMenu)
	 */

	public List<EmplacementClientArchive> getAll() {

		CriteriaQuery<EmplacementClientArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(
						EmplacementClientArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(EmplacementClientArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final EmplacementClientArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(EmplacementClientArchiveBean.class);

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

				return String.valueOf(((EmplacementClientArchive) value)
						.getIdEmplclient());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private EmplacementClientArchive add = new EmplacementClientArchive();

	public EmplacementClientArchive getAdd() {
		return this.add;
	}

	public EmplacementClientArchive getAdded() {
		EmplacementClientArchive added = this.add;
		this.add = new EmplacementClientArchive();
		return added;
	}
}
