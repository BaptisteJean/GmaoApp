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

import com.thedevbridge.gmaoapp.model.EmplacementTN;
import com.thedevbridge.gmaoapp.model.Emplacement;

/**
 * Backing bean for EmplacementTN entities.
 * <p/>
 * This class provides CRUD functionality for all EmplacementTN entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class EmplacementTNBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving EmplacementTN entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private EmplacementTN emplacementTN;

	public EmplacementTN getEmplacementTN() {
		return this.emplacementTN;
	}

	public void setEmplacementTN(EmplacementTN emplacementTN) {
		this.emplacementTN = emplacementTN;
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
			this.emplacementTN = this.example;
		} else {
			this.emplacementTN = findById(getId());
		}
	}

	public EmplacementTN findById(Long id) {

		return this.entityManager.find(EmplacementTN.class, id);
	}

	/*
	 * Support updating and deleting EmplacementTN entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.emplacementTN);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.emplacementTN);
				return "view?faces-redirect=true&id="
						+ this.emplacementTN.getIdEmplTN();
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
			EmplacementTN deletableEntity = findById(getId());
			Emplacement idEmplacement = deletableEntity.getIdEmplacement();
			idEmplacement.getEmplacementTNList().remove(deletableEntity);
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
	 * Support searching EmplacementTN entities with pagination
	 */

	private int page;
	private long count;
	private List<EmplacementTN> pageItems;

	private EmplacementTN example = new EmplacementTN();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public EmplacementTN getExample() {
		return this.example;
	}

	public void setExample(EmplacementTN example) {
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
		Root<EmplacementTN> root = countCriteria.from(EmplacementTN.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<EmplacementTN> criteria = builder
				.createQuery(EmplacementTN.class);
		root = criteria.from(EmplacementTN.class);
		TypedQuery<EmplacementTN> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<EmplacementTN> root) {

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
		Emplacement idEmplacement = this.example.getIdEmplacement();
		if (idEmplacement != null) {
			predicatesList.add(builder.equal(root.get("idEmplacement"),
					idEmplacement));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<EmplacementTN> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back EmplacementTN entities (e.g. from inside
	 * an HtmlSelectOneMenu)
	 */

	public List<EmplacementTN> getAll() {

		CriteriaQuery<EmplacementTN> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(EmplacementTN.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(EmplacementTN.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final EmplacementTNBean ejbProxy = this.sessionContext
				.getBusinessObject(EmplacementTNBean.class);

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

				return String.valueOf(((EmplacementTN) value).getIdEmplTN());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private EmplacementTN add = new EmplacementTN();

	public EmplacementTN getAdd() {
		return this.add;
	}

	public EmplacementTN getAdded() {
		EmplacementTN added = this.add;
		this.add = new EmplacementTN();
		return added;
	}
}
