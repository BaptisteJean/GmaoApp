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

import com.thedevbridge.gmaoapp.model.DirecteurTechnique;
import com.thedevbridge.gmaoapp.model.Personnel;

/**
 * Backing bean for DirecteurTechnique entities.
 * <p/>
 * This class provides CRUD functionality for all DirecteurTechnique entities.
 * It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class DirecteurTechniqueBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving DirecteurTechnique entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private DirecteurTechnique directeurTechnique;

	public DirecteurTechnique getDirecteurTechnique() {
		return this.directeurTechnique;
	}

	public void setDirecteurTechnique(DirecteurTechnique directeurTechnique) {
		this.directeurTechnique = directeurTechnique;
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
			this.directeurTechnique = this.example;
		} else {
			this.directeurTechnique = findById(getId());
		}
	}

	public DirecteurTechnique findById(Long id) {

		return this.entityManager.find(DirecteurTechnique.class, id);
	}

	/*
	 * Support updating and deleting DirecteurTechnique entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.directeurTechnique);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.directeurTechnique);
				return "view?faces-redirect=true&id="
						+ this.directeurTechnique.getIdDirecteurTech();
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
			DirecteurTechnique deletableEntity = findById(getId());
			Personnel idPersonnel = deletableEntity.getIdPersonnel();
			idPersonnel.getDirecteurTechniqueList().remove(deletableEntity);
			deletableEntity.setIdPersonnel(null);
			this.entityManager.merge(idPersonnel);
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
	 * Support searching DirecteurTechnique entities with pagination
	 */

	private int page;
	private long count;
	private List<DirecteurTechnique> pageItems;

	private DirecteurTechnique example = new DirecteurTechnique();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public DirecteurTechnique getExample() {
		return this.example;
	}

	public void setExample(DirecteurTechnique example) {
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
		Root<DirecteurTechnique> root = countCriteria
				.from(DirecteurTechnique.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<DirecteurTechnique> criteria = builder
				.createQuery(DirecteurTechnique.class);
		root = criteria.from(DirecteurTechnique.class);
		TypedQuery<DirecteurTechnique> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<DirecteurTechnique> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		Personnel idPersonnel = this.example.getIdPersonnel();
		if (idPersonnel != null) {
			predicatesList.add(builder.equal(root.get("idPersonnel"),
					idPersonnel));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<DirecteurTechnique> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back DirecteurTechnique entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<DirecteurTechnique> getAll() {

		CriteriaQuery<DirecteurTechnique> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(DirecteurTechnique.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(DirecteurTechnique.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final DirecteurTechniqueBean ejbProxy = this.sessionContext
				.getBusinessObject(DirecteurTechniqueBean.class);

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

				return String.valueOf(((DirecteurTechnique) value)
						.getIdDirecteurTech());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private DirecteurTechnique add = new DirecteurTechnique();

	public DirecteurTechnique getAdd() {
		return this.add;
	}

	public DirecteurTechnique getAdded() {
		DirecteurTechnique added = this.add;
		this.add = new DirecteurTechnique();
		return added;
	}
}
