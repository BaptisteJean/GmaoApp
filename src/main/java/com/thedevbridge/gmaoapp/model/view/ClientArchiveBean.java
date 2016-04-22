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

import com.thedevbridge.gmaoapp.model.ClientArchive;
import com.thedevbridge.gmaoapp.model.AdresseArchive;

/**
 * Backing bean for ClientArchive entities.
 * <p/>
 * This class provides CRUD functionality for all ClientArchive entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ClientArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving ClientArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private ClientArchive clientArchive;

	public ClientArchive getClientArchive() {
		return this.clientArchive;
	}

	public void setClientArchive(ClientArchive clientArchive) {
		this.clientArchive = clientArchive;
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
			this.clientArchive = this.example;
		} else {
			this.clientArchive = findById(getId());
		}
	}

	public ClientArchive findById(Long id) {

		return this.entityManager.find(ClientArchive.class, id);
	}

	/*
	 * Support updating and deleting ClientArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.clientArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.clientArchive);
				return "view?faces-redirect=true&id="
						+ this.clientArchive.getIdClient();
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
			ClientArchive deletableEntity = findById(getId());
			AdresseArchive idAdresse = deletableEntity.getIdAdresse();
			idAdresse.getClientArchiveList().remove(deletableEntity);
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
	 * Support searching ClientArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<ClientArchive> pageItems;

	private ClientArchive example = new ClientArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public ClientArchive getExample() {
		return this.example;
	}

	public void setExample(ClientArchive example) {
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
		Root<ClientArchive> root = countCriteria.from(ClientArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<ClientArchive> criteria = builder
				.createQuery(ClientArchive.class);
		root = criteria.from(ClientArchive.class);
		TypedQuery<ClientArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<ClientArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String codeClient = this.example.getCodeClient();
		if (codeClient != null && !"".equals(codeClient)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("codeClient")),
					'%' + codeClient.toLowerCase() + '%'));
		}
		String natureActivite = this.example.getNatureActivite();
		if (natureActivite != null && !"".equals(natureActivite)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("natureActivite")),
					'%' + natureActivite.toLowerCase() + '%'));
		}
		String nomClient = this.example.getNomClient();
		if (nomClient != null && !"".equals(nomClient)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("nomClient")),
					'%' + nomClient.toLowerCase() + '%'));
		}
		String prenomClient = this.example.getPrenomClient();
		if (prenomClient != null && !"".equals(prenomClient)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("prenomClient")),
					'%' + prenomClient.toLowerCase() + '%'));
		}
		String typePersonne = this.example.getTypePersonne();
		if (typePersonne != null && !"".equals(typePersonne)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("typePersonne")),
					'%' + typePersonne.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<ClientArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back ClientArchive entities (e.g. from inside
	 * an HtmlSelectOneMenu)
	 */

	public List<ClientArchive> getAll() {

		CriteriaQuery<ClientArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(ClientArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(ClientArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ClientArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(ClientArchiveBean.class);

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

				return String.valueOf(((ClientArchive) value).getIdClient());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private ClientArchive add = new ClientArchive();

	public ClientArchive getAdd() {
		return this.add;
	}

	public ClientArchive getAdded() {
		ClientArchive added = this.add;
		this.add = new ClientArchive();
		return added;
	}
}
