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

import com.thedevbridge.gmaoapp.model.Provenance;
import com.thedevbridge.gmaoapp.model.Client;
import com.thedevbridge.gmaoapp.model.Fournisseur;

/**
 * Backing bean for Provenance entities.
 * <p/>
 * This class provides CRUD functionality for all Provenance entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ProvenanceBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Provenance entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Provenance provenance;

	public Provenance getProvenance() {
		return this.provenance;
	}

	public void setProvenance(Provenance provenance) {
		this.provenance = provenance;
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
			this.provenance = this.example;
		} else {
			this.provenance = findById(getId());
		}
	}

	public Provenance findById(Long id) {

		return this.entityManager.find(Provenance.class, id);
	}

	/*
	 * Support updating and deleting Provenance entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.provenance);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.provenance);
				return "view?faces-redirect=true&id="
						+ this.provenance.getIdProvenance();
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
			Provenance deletableEntity = findById(getId());
			Client idClient = deletableEntity.getIdClient();
			idClient.getProvenanceList().remove(deletableEntity);
			deletableEntity.setIdClient(null);
			this.entityManager.merge(idClient);
			Fournisseur idFournisseur = deletableEntity.getIdFournisseur();
			idFournisseur.getProvenanceList().remove(deletableEntity);
			deletableEntity.setIdFournisseur(null);
			this.entityManager.merge(idFournisseur);
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
	 * Support searching Provenance entities with pagination
	 */

	private int page;
	private long count;
	private List<Provenance> pageItems;

	private Provenance example = new Provenance();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Provenance getExample() {
		return this.example;
	}

	public void setExample(Provenance example) {
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
		Root<Provenance> root = countCriteria.from(Provenance.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Provenance> criteria = builder
				.createQuery(Provenance.class);
		root = criteria.from(Provenance.class);
		TypedQuery<Provenance> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Provenance> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String marche = this.example.getMarche();
		if (marche != null && !"".equals(marche)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("marche")),
					'%' + marche.toLowerCase() + '%'));
		}
		Client idClient = this.example.getIdClient();
		if (idClient != null) {
			predicatesList.add(builder.equal(root.get("idClient"), idClient));
		}
		Fournisseur idFournisseur = this.example.getIdFournisseur();
		if (idFournisseur != null) {
			predicatesList.add(builder.equal(root.get("idFournisseur"),
					idFournisseur));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Provenance> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Provenance entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Provenance> getAll() {

		CriteriaQuery<Provenance> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Provenance.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Provenance.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ProvenanceBean ejbProxy = this.sessionContext
				.getBusinessObject(ProvenanceBean.class);

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

				return String.valueOf(((Provenance) value).getIdProvenance());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Provenance add = new Provenance();

	public Provenance getAdd() {
		return this.add;
	}

	public Provenance getAdded() {
		Provenance added = this.add;
		this.add = new Provenance();
		return added;
	}
}
