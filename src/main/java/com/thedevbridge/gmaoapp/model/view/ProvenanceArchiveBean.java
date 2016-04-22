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

import com.thedevbridge.gmaoapp.model.ProvenanceArchive;
import com.thedevbridge.gmaoapp.model.ClientArchive;
import com.thedevbridge.gmaoapp.model.FournisseurArchive;

/**
 * Backing bean for ProvenanceArchive entities.
 * <p/>
 * This class provides CRUD functionality for all ProvenanceArchive entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ProvenanceArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving ProvenanceArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private ProvenanceArchive provenanceArchive;

	public ProvenanceArchive getProvenanceArchive() {
		return this.provenanceArchive;
	}

	public void setProvenanceArchive(ProvenanceArchive provenanceArchive) {
		this.provenanceArchive = provenanceArchive;
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
			this.provenanceArchive = this.example;
		} else {
			this.provenanceArchive = findById(getId());
		}
	}

	public ProvenanceArchive findById(Long id) {

		return this.entityManager.find(ProvenanceArchive.class, id);
	}

	/*
	 * Support updating and deleting ProvenanceArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.provenanceArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.provenanceArchive);
				return "view?faces-redirect=true&id="
						+ this.provenanceArchive.getIdProvenance();
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
			ProvenanceArchive deletableEntity = findById(getId());
			ClientArchive idClient = deletableEntity.getIdClient();
			idClient.getProvenanceArchiveList().remove(deletableEntity);
			deletableEntity.setIdClient(null);
			this.entityManager.merge(idClient);
			FournisseurArchive idFournisseur = deletableEntity
					.getIdFournisseur();
			idFournisseur.getProvenanceArchiveList().remove(deletableEntity);
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
	 * Support searching ProvenanceArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<ProvenanceArchive> pageItems;

	private ProvenanceArchive example = new ProvenanceArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public ProvenanceArchive getExample() {
		return this.example;
	}

	public void setExample(ProvenanceArchive example) {
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
		Root<ProvenanceArchive> root = countCriteria
				.from(ProvenanceArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<ProvenanceArchive> criteria = builder
				.createQuery(ProvenanceArchive.class);
		root = criteria.from(ProvenanceArchive.class);
		TypedQuery<ProvenanceArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<ProvenanceArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String marche = this.example.getMarche();
		if (marche != null && !"".equals(marche)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("marche")),
					'%' + marche.toLowerCase() + '%'));
		}
		ClientArchive idClient = this.example.getIdClient();
		if (idClient != null) {
			predicatesList.add(builder.equal(root.get("idClient"), idClient));
		}
		FournisseurArchive idFournisseur = this.example.getIdFournisseur();
		if (idFournisseur != null) {
			predicatesList.add(builder.equal(root.get("idFournisseur"),
					idFournisseur));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<ProvenanceArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back ProvenanceArchive entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<ProvenanceArchive> getAll() {

		CriteriaQuery<ProvenanceArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(ProvenanceArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(ProvenanceArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ProvenanceArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(ProvenanceArchiveBean.class);

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

				return String.valueOf(((ProvenanceArchive) value)
						.getIdProvenance());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private ProvenanceArchive add = new ProvenanceArchive();

	public ProvenanceArchive getAdd() {
		return this.add;
	}

	public ProvenanceArchive getAdded() {
		ProvenanceArchive added = this.add;
		this.add = new ProvenanceArchive();
		return added;
	}
}
