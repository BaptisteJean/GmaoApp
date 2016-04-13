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

import com.thedevbridge.gmaoapp.model.Fournisseur;
import com.thedevbridge.gmaoapp.model.Adresse;

/**
 * Backing bean for Fournisseur entities.
 * <p/>
 * This class provides CRUD functionality for all Fournisseur entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class FournisseurBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Fournisseur entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Fournisseur fournisseur;

	public Fournisseur getFournisseur() {
		return this.fournisseur;
	}

	public void setFournisseur(Fournisseur fournisseur) {
		this.fournisseur = fournisseur;
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
			this.fournisseur = this.example;
		} else {
			this.fournisseur = findById(getId());
		}
	}

	public Fournisseur findById(Long id) {

		return this.entityManager.find(Fournisseur.class, id);
	}

	/*
	 * Support updating and deleting Fournisseur entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.fournisseur);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.fournisseur);
				return "view?faces-redirect=true&id="
						+ this.fournisseur.getIdFournisseur();
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
			Fournisseur deletableEntity = findById(getId());
			Adresse idAdresse = deletableEntity.getIdAdresse();
			idAdresse.getFournisseurList().remove(deletableEntity);
			deletableEntity.setIdAdresse(null);
			this.entityManager.merge(idAdresse);
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
	 * Support searching Fournisseur entities with pagination
	 */

	private int page;
	private long count;
	private List<Fournisseur> pageItems;

	private Fournisseur example = new Fournisseur();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Fournisseur getExample() {
		return this.example;
	}

	public void setExample(Fournisseur example) {
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
		Root<Fournisseur> root = countCriteria.from(Fournisseur.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Fournisseur> criteria = builder
				.createQuery(Fournisseur.class);
		root = criteria.from(Fournisseur.class);
		TypedQuery<Fournisseur> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Fournisseur> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String raisonSocial = this.example.getRaisonSocial();
		if (raisonSocial != null && !"".equals(raisonSocial)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("raisonSocial")),
					'%' + raisonSocial.toLowerCase() + '%'));
		}
		Adresse idAdresse = this.example.getIdAdresse();
		if (idAdresse != null) {
			predicatesList.add(builder.equal(root.get("idAdresse"), idAdresse));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Fournisseur> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Fournisseur entities (e.g. from inside
	 * an HtmlSelectOneMenu)
	 */

	public List<Fournisseur> getAll() {

		CriteriaQuery<Fournisseur> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Fournisseur.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Fournisseur.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final FournisseurBean ejbProxy = this.sessionContext
				.getBusinessObject(FournisseurBean.class);

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

				return String.valueOf(((Fournisseur) value).getIdFournisseur());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Fournisseur add = new Fournisseur();

	public Fournisseur getAdd() {
		return this.add;
	}

	public Fournisseur getAdded() {
		Fournisseur added = this.add;
		this.add = new Fournisseur();
		return added;
	}
}
